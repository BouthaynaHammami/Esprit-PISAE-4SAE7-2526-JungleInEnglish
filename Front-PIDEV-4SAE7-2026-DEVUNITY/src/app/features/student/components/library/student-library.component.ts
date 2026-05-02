// src/app/features/student/components/library/student-library.component.ts
import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { BookService }   from '../../../../core/services/books_clubs/book.service';
import { RentalService } from '../../../../core/services/books_clubs/rental.service';
import { OrderService }  from '../../../../core/services/books_clubs/order.service';
import { WalletService, Wallet } from '../../../../core/services/books_clubs/wallet.service';
import { StockService } from '../../../../core/services/books_clubs/stock.service'; // ✅ FIX
import { Book, Currency } from '../../../../core/models/book-models';
import { environment } from '../../../../../environments/environment';

type StockPatchMode = 'RENTAL' | 'ORDER';
type PaymentMode    = 'CAISSE' | 'WALLET';

@Component({
  selector: 'app-student-library',
  templateUrl: './student-library.component.html',
  styleUrls: ['./student-library.component.css']
})
export class StudentLibraryComponent implements OnInit {

  books:         Book[] = [];
  filteredBooks: Book[] = [];
  searchQuery          = '';

  showRentModal    = false;
  showOrderModal   = false;
  showWalletModal  = false;   // ← NOUVEAU : modal portefeuille

  // Modal félicitation réduction
  showDiscountModal = false;
  discountContext   = '';
  discountTitle     = '';

  selectedBook: Book | null = null;
  isLoading = false;

  readonly today = new Date().toISOString().split('T')[0];

  rentForm!:    FormGroup;
  orderForm!:   FormGroup;
  rechargeForm!: FormGroup;

  successMessage = '';
  errorMessage   = '';

  // ── WALLET ──────────────────────────────────────────────
  wallet: Wallet | null = null;
  walletLoading          = false;
  walletError            = '';
  walletTransactions: any[] = [];
  walletTab: 'overview' | 'history' = 'overview';
  insufficientFunds      = false;

  private readonly BASE = `${environment.apiUrl}/learners/api`;

  private readonly palette: Array<[string, string]> = [
    ['#006D77', '#0ea896'],
    ['#0f766e', '#14b8a6'],
    ['#2563eb', '#60a5fa'],
    ['#7c3aed', '#a78bfa'],
    ['#f59e0b', '#fbbf24'],
    ['#ef4444', '#f97316'],
    ['#111827', '#4b5563']
  ];

  constructor(
    private fb:         FormBuilder,
    private bookSvc:    BookService,
    private rentalSvc:  RentalService,
    private orderSvc:   OrderService,
    private walletSvc:  WalletService,
    private stockSvc:   StockService, // ✅ FIX
    private cdr:        ChangeDetectorRef,
    private http:       HttpClient
  ) {}

  ngOnInit(): void {
    this.initForms();
    this.loadBooks();
    this.loadWallet();
  }

  // =========================================================
  //  HELPER — extrait userId depuis le token JWT
  // =========================================================
  private getCurrentUserId(): number | null {
    // Utilise la meme cle que AuthService : 'jwt_token'
    const storedId = localStorage.getItem('user_id');
    if (storedId && Number(storedId) > 0) return Number(storedId);
    const token = localStorage.getItem('jwt_token');
    if (!token) return null;
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload.userId ?? payload.id ?? payload.sub ?? null;
    } catch {
      return null;
    }
  }

  // =========================================================
  //  WALLET — chargement
  // =========================================================
  loadWallet(): void {
    const userId = this.getCurrentUserId();
    if (!userId) return;

    this.walletLoading = true;
    this.walletSvc.getWallet(userId).subscribe({
      next: w => {
        this.wallet        = w;
        this.walletLoading = false;
        this.cdr.detectChanges();
      },
      error: () => {
        // Wallet n'existe pas encore → on le crée automatiquement (toute erreur)
        this.walletSvc.createWallet(userId).subscribe({
          next: w => {
            this.wallet        = w;
            this.walletLoading = false;
            this.cdr.detectChanges();
          },
          error: () => { this.walletLoading = false; }
        });
      }
    });
  }

  loadWalletTransactions(): void {
  const userId = this.getCurrentUserId();
  if (!userId) return;

  this.walletSvc.getTransactions(userId).subscribe({
    next: t => {
      this.walletTransactions = Array.isArray(t)
        ? t.sort((a, b) => new Date(b.date).getTime() - new Date(a.date).getTime())
        : [];
      this.cdr.detectChanges();
    },
    error: () => {
      this.walletTransactions = [];
      this.cdr.detectChanges();
    }
  });
}

  // =========================================================
  //  WALLET MODAL
  // =========================================================
  openWalletModal(): void {
    this.showWalletModal = true;
    this.walletTab       = 'overview';
    this.walletError     = '';
    this.rechargeForm.reset({ amount: '' });
    this.loadWalletTransactions();
  }

  closeWalletModal(): void {
    this.showWalletModal = false;
  }

  setWalletTab(tab: 'overview' | 'history'): void {
    this.walletTab = tab;
    if (tab === 'history') this.loadWalletTransactions();
  }

  // Recharge désactivée côté student — géré par l'admin

  // =========================================================
  //  HELPER — solde suffisant ?
  // =========================================================
  hasEnoughBalance(amount: number): boolean {
    return (this.wallet?.balance ?? 0) >= amount;
  }

  get rentTotal(): number {
    const currency = this.rentForm.get('currency')?.value ?? 'TND';
    const price    = this.getRentalPriceConverted(this.selectedBook, currency);
    return price * this.getDuration();
  }

  get rentTotalTND(): number {
    const currency = this.rentForm.get('currency')?.value ?? 'TND';
    return this.convertPrice(this.rentTotal, currency, 'TND');
  }

  get orderTotal(): number {
    const currency = this.orderForm.get('currency')?.value ?? 'TND';
    const qty      = Number(this.orderForm.get('qty')?.value ?? 1);
    return this.getSalePriceConverted(this.selectedBook, currency) * qty;
  }

  get orderTotalTND(): number {
    const currency = this.orderForm.get('currency')?.value ?? 'TND';
    return this.convertPrice(this.orderTotal, currency, 'TND');
  }

  // =========================================================
  //  HELPER — vérifie éligibilité réduction et affiche modal
  // =========================================================
  private checkDiscountAfter(title: string, context: 'rental' | 'order'): void {
    const userId = this.getCurrentUserId();
    if (!userId) return;

    this.http.get<boolean>(`${this.BASE}/discount/eligible?userId=${userId}`)
      .subscribe({
        next: (eligible) => {
          if (eligible) {
            this.discountTitle    = title;
            this.discountContext  = context;
            this.showDiscountModal = true;
          }
        },
        error: () => { /* silencieux */ }
      });
  }

  closeDiscountModal(): void {
    this.showDiscountModal = false;
  }

  // =========================================================
  //  CURRENCY
  // =========================================================
  private readonly rates: Record<string, number> = { TND: 1.0, EUR: 3.38, USD: 3.10 };

  convertPrice(amount: number, from: string, to: string): number {
    if (!amount || !from || !to) return amount;
    if (from === to) return amount;
    const inTND = amount * (this.rates[from] ?? 1);
    return Math.round((inTND / (this.rates[to] ?? 1)) * 1000) / 1000;
  }

  getRentalPriceConverted(b: Book | null, targetCurrency: string): number {
    if (!b) return 0;
    const base = this.getRentalPrice(b);
    return this.convertPrice(base, b.currency ?? 'TND', targetCurrency);
  }

  getSalePriceConverted(b: Book | null, targetCurrency: string): number {
    if (!b) return 0;
    return this.convertPrice(b.salePrice ?? 0, b.currency ?? 'TND', targetCurrency);
  }

  initForms(): void {
    this.rentForm = this.fb.group({
      startDate:   ['', Validators.required],
      dueDate:     ['', Validators.required],
      currency:    ['TND', Validators.required],
      paymentMode: ['CAISSE', Validators.required]   // ← NOUVEAU
    });
    this.orderForm = this.fb.group({
      qty:         [1, [Validators.required, Validators.min(1)]],
      currency:    ['TND', Validators.required],
      paymentMode: ['CAISSE', Validators.required]   // ← NOUVEAU
    });
    this.rechargeForm = this.fb.group({
      amount: ['', [Validators.required, Validators.min(1)]]
    });
  }

  // =========================================================
  //  STOCK DISPONIBLE
  // =========================================================
  getAvailableStock(b: Book): number {
    const q = (b as any)?.stock?.quantity    ?? 0;
    const r = (b as any)?.stock?.reservedQty ?? 0;
    return Math.max(0, q - r);
  }

  isBookUnavailable(b: Book): boolean {
    return b.status === 'OUT_OF_STOCK' || this.getAvailableStock(b) === 0;
  }

  // =========================================================
  //  IMAGE
  // =========================================================
  getBookImageSrc(b: Book): string | null {
    const img = (b as any).image as string | null | undefined;
    if (!img) return null;
    if (img.startsWith('data:')) return img;
    return `data:image/jpeg;base64,${img}`;
  }

  // =========================================================
  //  LOAD
  // =========================================================
  loadBooks(): void {
    this.bookSvc.getAll().subscribe({
      next: books => {
        this.books = books || [];
        this.filteredBooks = [...this.books];
        this.cdr.detectChanges();
      },
      error: err => {
        console.error('loadBooks', err);
        this.showError('Impossible de charger les livres.');
      }
    });
  }

  // =========================================================
  //  SEARCH
  // =========================================================
  onSearch(): void {
    const q = (this.searchQuery || '').toLowerCase();
    if (!q) { this.filteredBooks = [...this.books]; return; }
    this.filteredBooks = this.books.filter(b =>
      (b.title          || '').toLowerCase().includes(q) ||
      (b.isbn           || '').toLowerCase().includes(q) ||
      (b.author?.name   || '').toLowerCase().includes(q) ||
      (b.category?.name || '').toLowerCase().includes(q)
    );
  }

  // =========================================================
  //  ALLOUER (RENTAL)
  // =========================================================
  openRentModal(b: Book): void {
    if (this.isBookUnavailable(b)) return;
    this.selectedBook   = b;
    this.insufficientFunds = false;
    const today    = new Date().toISOString().split('T')[0];
    const nextWeek = new Date(Date.now() + 7 * 86_400_000).toISOString().split('T')[0];
    this.rentForm.reset({ startDate: today, dueDate: nextWeek, currency: b.currency || 'TND', paymentMode: 'CAISSE' });
    this.showRentModal = true;
    this.clearMessages();
  }

  closeRentModal(): void {
    this.showRentModal = false;
    this.selectedBook  = null;
    this.insufficientFunds = false;
  }

  submitRent(): void {
    if (!this.selectedBook || this.rentForm.invalid) return;

    const v           = this.rentForm.getRawValue();
    const paymentMode = v.paymentMode as PaymentMode;
    const title       = this.selectedBook.title;
    const bookId      = this.selectedBook.bookId;
    const currency: Currency = (v.currency as Currency) ?? 'TND';
    const dailyPrice  = this.getRentalPriceConverted(this.selectedBook, currency);
    const userId      = this.getCurrentUserId();

    // ── Vérification solde si paiement portefeuille ─────────
    if (paymentMode === 'WALLET') {
      const totalTND = this.rentTotalTND;
      if (!this.hasEnoughBalance(totalTND)) {
        this.insufficientFunds = true;
        return;
      }
    }

    this.isLoading = true;
    this.insufficientFunds = false;

    this.rentalSvc.create(bookId, v.startDate, v.dueDate, dailyPrice, currency, userId).subscribe({
      next: () => {
        // Si paiement wallet → débiter immédiatement
        if (paymentMode === 'WALLET' && userId) {
          this.walletSvc.payFromWallet(userId, this.rentTotalTND).subscribe({
            next: () => this.loadWallet(),
            error: () => console.warn('Wallet debit failed after rent')
          });
        }

        this.isLoading = false;
        this.closeRentModal();
        const payLabel = paymentMode === 'WALLET' ? '💳 Payé via portefeuille' : '💵 Paiement à la caisse';
        this.showSuccess(`"${title}" alloué avec succès ! ${payLabel}.`);
        // ✅ FIX — décrémenter le stock réel en BD
        this.stockSvc.remove(bookId, 1).subscribe({
          next:  () => this.loadBooks(),
          error: () => this.loadBooks()
        });
        this.checkDiscountAfter(title, 'rental');
      },
      error: err => {
        console.error('submitRent', err);
        this.isLoading = false;
        this.showError("Échec de l'allocation. Veuillez réessayer.");
      }
    });
  }

  // =========================================================
  //  COMMANDER (ORDER)
  // =========================================================
  openOrderModal(b: Book): void {
    if (this.isBookUnavailable(b)) return;
    this.selectedBook      = b;
    this.insufficientFunds = false;
    this.orderForm.reset({ qty: 1, currency: b.currency || 'TND', paymentMode: 'CAISSE' });
    this.showOrderModal = true;
    this.clearMessages();
  }

  closeOrderModal(): void {
    this.showOrderModal    = false;
    this.selectedBook      = null;
    this.insufficientFunds = false;
  }

  submitOrder(): void {
    if (!this.selectedBook || this.orderForm.invalid) return;

    const v           = this.orderForm.getRawValue();
    const paymentMode = v.paymentMode as PaymentMode;
    const bookId      = this.selectedBook.bookId;
    const qty         = Number(v.qty);
    const title       = this.selectedBook.title;
    const userId      = this.getCurrentUserId();

    // ── Vérification solde si paiement portefeuille ─────────
    if (paymentMode === 'WALLET') {
      const totalTND = this.orderTotalTND;
      if (!this.hasEnoughBalance(totalTND)) {
        this.insufficientFunds = true;
        return;
      }
    }

    this.isLoading = true;
    this.insufficientFunds = false;

    this.orderSvc.create(v.currency as Currency, userId).subscribe({
      next: order => {
        this.orderSvc.addItem(order.orderId, bookId, qty).subscribe({
          next: () => {
            // Si paiement wallet → débiter immédiatement
            if (paymentMode === 'WALLET' && userId) {
              this.walletSvc.payFromWallet(userId, this.orderTotalTND).subscribe({
                next: () => this.loadWallet(),
                error: () => console.warn('Wallet debit failed after order')
              });
            }

            this.isLoading = false;
            this.closeOrderModal();
            const payLabel = paymentMode === 'WALLET' ? '💳 Payé via portefeuille' : '💵 Paiement à la caisse';
            this.showSuccess(`"${title}" commandé (×${qty}) ! ${payLabel}.`);
            // ✅ FIX — décrémenter le stock réel en BD
            this.stockSvc.remove(bookId, qty).subscribe({
              next:  () => this.loadBooks(),
              error: () => this.loadBooks()
            });
            this.checkDiscountAfter(title, 'order');
          },
          error: err => {
            console.error('addItem', err);
            this.isLoading = false;
            this.showError("Commande créée mais l'ajout du livre a échoué.");
          }
        });
      },
      error: err => {
        console.error('createOrder', err);
        this.isLoading = false;
        this.showError('Échec de la commande. Veuillez réessayer.');
      }
    });
  }

  // =========================================================
  //  STOCK — patch local immédiat
  // =========================================================
  private patchStockLocally(bookId: number, qty: number, mode: StockPatchMode): void {

    const patchOne = (b: Book): Book => {
      if (b.bookId !== bookId) return b;

      const stock: any = (b as any).stock ?? {};
      const oldQ = stock.quantity    ?? 0;
      const oldR = stock.reservedQty ?? 0;

      const newQ = mode === 'RENTAL' ? Math.max(0, oldQ - qty) : oldQ;
      const newR = mode === 'ORDER'  ? Math.max(0, oldR + qty) : oldR;

      const available = Math.max(0, newQ - newR);
      const newStatus = available > 0 ? 'AVAILABLE' : 'OUT_OF_STOCK';

      return {
        ...b,
        status: newStatus,
        stock: { ...stock, quantity: newQ, reservedQty: newR }
      } as Book;
    };

    this.books         = this.books.map(patchOne);
    this.filteredBooks = this.filteredBooks.map(patchOne);
    this.cdr.detectChanges();
  }

  // =========================================================
  //  UI HELPERS
  // =========================================================
  getCoverStyle(i: number): string {
    const [a, b] = this.palette[(i + 2) % this.palette.length];
    return `background: linear-gradient(135deg, ${a}, ${b});`;
  }

  getSpineStyle(i: number): string {
    const [a, b] = this.palette[i % this.palette.length];
    return `background: linear-gradient(180deg, ${a}, ${b});`;
  }

  fmt(d: string | null | undefined): string {
    return d ? new Date(d).toLocaleDateString('fr-FR') : '—';
  }

  fmtDateTime(d: string | null | undefined): string {
    return d ? new Date(d).toLocaleString('fr-FR') : '—';
  }

  getRentalPrice(b: Book): number {
    return b?.salePrice ? Math.round(b.salePrice * 0.25 * 100) / 100 : 0;
  }

  getDuration(): number {
    const start = this.rentForm.get('startDate')?.value;
    const end   = this.rentForm.get('dueDate')?.value;
    if (!start || !end) return 0;
    const diff = new Date(end).getTime() - new Date(start).getTime();
    return Math.max(0, Math.ceil(diff / 86_400_000));
  }

  // =========================================================
  //  FEEDBACK
  // =========================================================
  private showSuccess(msg: string): void {
    this.successMessage = msg;
    this.errorMessage   = '';
    setTimeout(() => (this.successMessage = ''), 5000);
  }

  private showError(msg: string): void {
    this.errorMessage   = msg;
    this.successMessage = '';
    setTimeout(() => (this.errorMessage = ''), 6000);
  }

  clearMessages(): void {
    this.successMessage = '';
    this.errorMessage   = '';
  }
} 