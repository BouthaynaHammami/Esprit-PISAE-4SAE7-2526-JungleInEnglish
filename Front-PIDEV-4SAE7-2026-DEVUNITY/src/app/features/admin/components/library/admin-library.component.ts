// src/app/features/admin/components/library/admin-library.component.ts
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { forkJoin } from 'rxjs';
import { switchMap } from 'rxjs/operators';

import { BookService }     from '../../../../core/services/books_clubs/book.service';
import { AuthorService }   from '../../../../core/services/books_clubs/author.service';
import { CategoryService } from '../../../../core/services/books_clubs/category.service';
import { OrderService }    from '../../../../core/services/books_clubs/order.service';
import { RentalService }   from '../../../../core/services/books_clubs/rental.service';
import { StockService }    from '../../../../core/services/books_clubs/stock.service';
import { AuthService }     from '../../../../core/services/auth.service';
import { environment } from '../../../../../environments/environment';

import { Book, Author, Category, Order, Rental, Currency } from '../../../../core/models/book-models';
import { WalletService, Wallet } from '../../../../core/services/books_clubs/wallet.service';

@Component({
  selector: 'app-admin-library',
  templateUrl: './admin-library.component.html',
  styleUrls: ['./admin-library.component.css']
})
export class AdminLibraryComponent implements OnInit {

  // =========================================================
  //  TABS
  // =========================================================
  activeTab: 'books' | 'rentals' | 'orders' | 'stocks' | 'wallets' = 'books';

  // =========================================================
  //  DATA
  // =========================================================
  books:      Book[]     = [];
  authors:    Author[]   = [];
  categories: Category[] = [];
  orders:     Order[]    = [];
  rentals:    Rental[]   = [];
  stocks:     any[]      = [];

  // =========================================================
  //  WALLETS
  // =========================================================
  students:             any[]    = [];
  studentWallets:       { student: any; wallet: Wallet | null }[] = [];
  walletSearch          = '';
  showRechargeModal     = false;
  selectedStudent:      any | null    = null;
  selectedStudentWallet: Wallet | null = null;
  rechargeForm!:        FormGroup;
  walletLoading         = false;

  get filteredStudentWallets() {
    const q = (this.walletSearch || '').toLowerCase();
    if (!q) return this.studentWallets;
    return this.studentWallets.filter(sw =>
      ((sw.student?.firstName ?? '') + ' ' + (sw.student?.lastName ?? '')).toLowerCase().includes(q) ||
      (sw.student?.email || '').toLowerCase().includes(q)
    );
  }

  get totalWalletBalance(): number {
    return this.studentWallets.reduce((sum, sw) => sum + (Number(sw.wallet?.balance) || 0), 0);
  }

  get emptyWalletsCount(): number {
    return this.studentWallets.filter(sw => (Number(sw.wallet?.balance) || 0) === 0).length;
  }

  getWalletBalanceColor(wallet: Wallet | null): string {
    const b = Number(wallet?.balance) || 0;
    if (b === 0) return 'color:#dc2626';
    if (b < 10)  return 'color:#E29578';
    return 'color:#006D77';
  }

  getNewBalance(): number {
    const current = Number(this.selectedStudentWallet?.balance) || 0;
    const amount  = Number(this.rechargeForm.get('amount')?.value) || 0;
    return current + amount;
  }

  refreshWallets(): void {
    this.studentWallets = [];
    this.loadStudentWallets();
  }

  // =========================================================
  //  SEARCH / FILTER
  // =========================================================
  searchQuery  = '';
  filterStatus = '';
  stockSearch  = '';

  get filteredBooks(): Book[] {
    return this.books.filter(b => {
      const q = (this.searchQuery || '').toLowerCase();
      const matchSearch =
        !q ||
        (b.title  || '').toLowerCase().includes(q) ||
        (b.isbn   || '').toLowerCase().includes(q) ||
        ((b.author?.name || '') as string).toLowerCase().includes(q);
      const matchStatus = !this.filterStatus || b.status === this.filterStatus;
      return matchSearch && matchStatus;
    });
  }

  get filteredStocks(): any[] {
    const q = (this.stockSearch || '').toLowerCase();
    if (!q) return this.stocks;
    return this.stocks.filter(s =>
      (s.book?.title || '').toLowerCase().includes(q) ||
      (s.book?.isbn  || '').toLowerCase().includes(q)
    );
  }

  // =========================================================
  //  STATS
  // =========================================================
  get totalBooks()      { return this.books.length; }
  get availableBooks()  { return this.books.filter(b => b.status === 'AVAILABLE').length; }
  get outOfStockBooks() { return this.books.filter(b => b.status === 'OUT_OF_STOCK').length; }
  get activeRentals()   { return this.rentals.filter(r => r.status === 'ACTIVE').length; }
  get lowStockCount()   { return this.stocks.filter(s => s.quantity > 0 && s.quantity < 5).length; }

  // =========================================================
  //  MODAL FLAGS
  // =========================================================
  showBookModal      = false;
  showDeleteModal    = false;
  showRentalModal    = false;
  showReturnModal    = false;
  showAddItemModal   = false;
  showStockModal     = false;
  showStockEditModal = false;

  isLoading = false;

  // =========================================================
  //  STATE
  // =========================================================
  bookToDelete:         Book | null = null;
  selectedOrderId:      number | null = null;
  returningRentalId:    number | null = null;
  returnDateValue       = '';
  selectedBookForStock: Book | null = null;
  selectedStock:        any  | null = null;

  // =========================================================
  //  IMAGE
  // =========================================================
  bookImagePreview:  string | null = null;
  bookImageFileName: string        = '';

  // =========================================================
  //  FORMS
  // =========================================================
  bookForm!:      FormGroup;
  rentalForm!:    FormGroup;
  addItemForm!:   FormGroup;
  stockForm!:     FormGroup;
  stockEditForm!: FormGroup;

  // =========================================================
  //  EDIT MODE
  // =========================================================
  isEditMode     = false;
  editingBookId: number | null = null;

  // =========================================================
  //  PALETTE
  // =========================================================
  private readonly palette: Array<[string, string]> = [
    ['#006D77', '#0ea896'],
    ['#0f766e', '#14b8a6'],
    ['#2563eb', '#60a5fa'],
    ['#7c3aed', '#a78bfa'],
    ['#f59e0b', '#fbbf24'],
    ['#ef4444', '#f97316'],
    ['#111827', '#4b5563']
  ];

  // =========================================================
  //  FEEDBACK
  // =========================================================
  successMessage = '';
  errorMessage   = '';

  // =========================================================
  //  BASE URL
  // =========================================================
  private readonly BASE = `${environment.apiUrl}/learners/api`;

  constructor(
    private fb:          FormBuilder,
    private bookSvc:     BookService,
    private authorSvc:   AuthorService,
    private categorySvc: CategoryService,
    private orderSvc:    OrderService,
    private rentalSvc:   RentalService,
    private stockSvc:    StockService,
    private authSvc:     AuthService,
    private http:        HttpClient,
    private walletSvc:   WalletService
  ) {}

  ngOnInit(): void {
    this.initForms();
    this.loadAll();
  }

  // =========================================================
  //  ✅ FIX — Retourne les HttpHeaders avec le JWT Bearer token
  //  Utilise AuthService.getToken() → clé 'jwt_token' (et non 'token')
  // =========================================================
  private getAuthHeaders(): { headers: HttpHeaders } {
    const token = this.authSvc.getToken() ?? '';
    return {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'Authorization': token ? `Bearer ${token}` : ''
      })
    };
  }

  // =========================================================
  //  ✅ FIX — Retourne les HttpHeaders pour les réponses blob (PDF)
  // =========================================================
  private getAuthHeadersBlob(): { headers: HttpHeaders; responseType: 'blob' } {
    const token = this.authSvc.getToken() ?? '';
    return {
      headers: new HttpHeaders({
        'Authorization': token ? `Bearer ${token}` : ''
      }),
      responseType: 'blob'
    };
  }

  // =========================================================
  //  ✅ FIX — Délègue à AuthService (lit la bonne clé 'jwt_token')
  // =========================================================
  private getCurrentUserId(): number | null {
    return this.authSvc.getUserId();
  }

  private isTokenValid(): boolean {
    return this.authSvc.isLoggedIn();
  }

  // =========================================================
  //  FORMS INIT
  // =========================================================
  initForms(): void {
    this.bookForm = this.fb.group({
      title:           ['', Validators.required],
      isbn:            ['', Validators.required],
      status:          ['AVAILABLE'],
      salePrice:       [0,  [Validators.required, Validators.min(0)]],
      currency:        ['TND'],
      publicationYear: ['', Validators.required],
      authorId:        ['', Validators.required],
      categoryId:      ['', Validators.required],
      qte:             [1,  [Validators.required, Validators.min(1)]],
      image:           [null]
    });

    this.rentalForm = this.fb.group({
      bookId:     ['', Validators.required],
      startDate:  ['', Validators.required],
      dueDate:    ['', Validators.required],
      dailyPrice: [0,  [Validators.required, Validators.min(0.1)]],
      currency:   ['TND']
    });

    this.addItemForm = this.fb.group({
      bookId: ['', Validators.required],
      qty:    [1,  [Validators.required, Validators.min(1)]]
    });

    this.stockForm = this.fb.group({
      quantity: [0, [Validators.required, Validators.min(0)]]
    });

    this.stockEditForm = this.fb.group({
      quantity: [0, [Validators.required, Validators.min(0)]]
    });

    this.rechargeForm = this.fb.group({
      amount: ['', [Validators.required, Validators.min(1)]]
    });
  }

  // =========================================================
  //  LOAD ALL
  // =========================================================
  loadAll(): void {
    // ✅ FIX — Vérifie le token avant tout chargement
    if (!this.isTokenValid()) {
      this.showError('Session expirée. Veuillez vous reconnecter.');
      return;
    }

    forkJoin({
      books:      this.bookSvc.getAll(),
      authors:    this.authorSvc.getAll(),
      categories: this.categorySvc.getAll(),
      orders:     this.orderSvc.getAll(),
      rentals:    this.rentalSvc.getAll()
    }).subscribe({
      next: ({ books, authors, categories, orders, rentals }) => {
        this.books      = books      || [];
        this.authors    = authors    || [];
        this.categories = categories || [];
        this.orders     = orders     || [];
        this.rentals    = rentals    || [];

        this.stocks = this.books
          .filter(b => b.stock)
          .map(b => ({
            ...b.stock,
            book: { bookId: b.bookId, title: b.title, isbn: b.isbn, status: b.status }
          }));
      },
      error: (err) => {
        console.error('loadAll error', err);
        if (err.status === 403 || err.status === 401) {
          this.showError('Accès refusé (403). Vérifiez vos droits ou reconnectez-vous.');
        } else {
          this.showError('Échec du chargement des données. Veuillez rafraîchir.');
        }
      }
    });
  }

  // =========================================================
  //  IMAGE
  // =========================================================
  onImageSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (!input.files?.length) return;

    const file = input.files[0];

    if (file.size > 2 * 1024 * 1024) {
      this.showError('Image trop grande — max 2 Mo.');
      return;
    }

    this.bookImageFileName = file.name;

    const reader = new FileReader();
    reader.onload = () => {
      const base64 = reader.result as string;
      this.bookImagePreview = base64;
      this.bookForm.patchValue({ image: base64 });
    };
    reader.readAsDataURL(file);
  }

  clearImage(): void {
    this.bookImagePreview  = null;
    this.bookImageFileName = '';
    this.bookForm.patchValue({ image: null });
  }

  getBookImageSrc(b: Book): string | null {
    const img = b?.image;
    if (!img) return null;
    if (img.startsWith('data:')) return img;
    return `data:image/jpeg;base64,${img}`;
  }

  // =========================================================
  //  BOOKS – CREATE
  // =========================================================
  openBookModal(): void {
    this.isEditMode    = false;
    this.editingBookId = null;
    this.clearMessages();
    this.bookForm.reset({ status: 'AVAILABLE', currency: 'TND', qte: 1, image: null });
    this.bookForm.get('qte')?.enable();
    this.bookImagePreview  = null;
    this.bookImageFileName = '';
    this.showBookModal = true;
  }

  openEditBookModal(b: Book): void {
    this.isEditMode    = true;
    this.editingBookId = b.bookId;
    this.clearMessages();

    this.bookForm.patchValue({
      title:           b.title,
      isbn:            b.isbn,
      status:          b.status,
      salePrice:       b.salePrice,
      currency:        b.currency,
      publicationYear: (b.publicationYear as any)?.toString?.().slice(0, 10) ?? (b.publicationYear as any),
      authorId:        (b.author   as any)?.authorId   ?? '',
      categoryId:      (b.category as any)?.categoryId ?? '',
      qte:             b.stock?.quantity ?? 1,
      image:           b.image ?? null
    });

    this.bookForm.get('qte')?.disable();

    if (b.image) {
      this.bookImagePreview  = b.image.startsWith('data:')
        ? b.image
        : `data:image/jpeg;base64,${b.image}`;
      this.bookImageFileName = '(image actuelle)';
    } else {
      this.bookImagePreview  = null;
      this.bookImageFileName = '';
    }

    this.showBookModal = true;
  }

  closeBookModal(): void {
    this.showBookModal = false;
    this.isEditMode    = false;
    this.editingBookId = null;
    this.bookForm.get('qte')?.enable();
    this.bookImagePreview  = null;
    this.bookImageFileName = '';
    this.clearMessages();
  }

  submitBook(): void {
    if (this.bookForm.invalid) return;

    this.isLoading = true;
    this.clearMessages();

    const v          = this.bookForm.getRawValue();
    const authorId   = Number(v.authorId);
    const categoryId = Number(v.categoryId);
    const qte        = Number(v.qte);

    const body = {
      title:           v.title,
      isbn:            v.isbn,
      status:          v.status,
      salePrice:       Number(v.salePrice),
      currency:        v.currency,
      publicationYear: v.publicationYear,
      image:           v.image ?? null
    };

    if (this.isEditMode && this.editingBookId) {
      this.bookSvc.updateBook(this.editingBookId, body, authorId, categoryId).subscribe({
        next: () => {
          this.isLoading = false;
          this.closeBookModal();
          this.loadAll();
          this.showSuccess('Livre mis à jour avec succès.');
        },
        error: (err) => {
          console.error('updateBook error', err);
          this.isLoading = false;
          this.showError(err.status === 403 ? 'Accès refusé (403) — droits insuffisants.' : 'Échec de la mise à jour.');
        }
      });
      return;
    }

    this.bookSvc.addBook(body, authorId, categoryId, qte).subscribe({
      next: () => {
        this.isLoading = false;
        this.closeBookModal();
        this.loadAll();
        this.showSuccess('Livre ajouté avec succès.');
      },
      error: (err) => {
        console.error('addBook error', err);
        this.isLoading = false;
        this.showError(err.status === 403 ? 'Accès refusé (403) — droits insuffisants.' : 'Échec de l\'ajout.');
      }
    });
  }

  // =========================================================
  //  BOOKS – DELETE
  // =========================================================
  confirmDelete(b: Book): void {
    this.bookToDelete    = b;
    this.showDeleteModal = true;
  }

  deleteBook(): void {
    if (!this.bookToDelete) return;
    this.bookSvc.delete(this.bookToDelete.bookId).subscribe({
      next: () => {
        this.showDeleteModal = false;
        this.bookToDelete    = null;
        this.loadAll();
        this.showSuccess('Livre supprimé avec succès.');
      },
      error: (err) => {
        console.error('deleteBook error', err);
        this.showError(err.status === 403 ? 'Accès refusé (403).' : 'Échec de la suppression.');
      }
    });
  }

  // =========================================================
  //  STOCK
  // =========================================================
  openStockModal(b: Book): void {
    this.selectedBookForStock = b;
    this.stockForm.patchValue({ quantity: b.stock?.quantity ?? 0 });
    this.showStockModal = true;
  }

  updateStock(): void {
    if (!this.selectedBookForStock) return;

    const quantity = Number(this.stockForm.value.quantity);

    this.stockSvc.update(this.selectedBookForStock.bookId, quantity).subscribe({
      next: () => {
        this.showStockModal = false;
        this.loadAll();
        this.showSuccess('Stock mis à jour avec succès.');
      },
      error: (err) => {
        console.error('updateStock error', err);
        // ✅ FIX — Message d'erreur clair selon le code HTTP
        if (err.status === 403) {
          this.showError('Accès refusé (403). Vérifiez que votre token JWT est valide et que vous avez le rôle ADMIN.');
        } else if (err.status === 401) {
          this.showError('Non authentifié (401). Veuillez vous reconnecter.');
        } else {
          this.showError('Échec de la mise à jour du stock.');
        }
      }
    });
  }

  addQty(b: Book): void {
    this.stockSvc.add(b.bookId, 1).subscribe({
      next: () => this.loadAll(),
      error: (err) => {
        console.error('addQty error', err);
        if (err.status === 403) this.showError('Accès refusé (403) pour modifier le stock.');
      }
    });
  }

  removeQty(b: Book): void {
    if ((b.stock?.quantity ?? 0) <= 0) return;
    this.stockSvc.remove(b.bookId, 1).subscribe({
      next: () => this.loadAll(),
      error: (err) => {
        console.error('removeQty error', err);
        if (err.status === 403) this.showError('Accès refusé (403) pour modifier le stock.');
      }
    });
  }

  openStockEditModal(s: any): void {
    this.selectedStock = s;
    this.stockEditForm.patchValue({ quantity: s.quantity });
    this.showStockEditModal = true;
  }

  closeStockEditModal(): void {
    this.showStockEditModal = false;
    this.selectedStock      = null;
  }

  submitStockEdit(): void {
    if (!this.selectedStock || this.stockEditForm.invalid) return;
    const quantity = Number(this.stockEditForm.value.quantity);
    this.stockSvc.update(this.selectedStock.book.bookId, quantity).subscribe({
      next: () => { this.closeStockEditModal(); this.loadAll(); this.showSuccess('Stock mis à jour.'); },
      error: (err) => { console.error('submitStockEdit error', err); this.showError('Échec de la mise à jour du stock.'); }
    });
  }

  addStockQty(s: any, qty: number): void {
    this.stockSvc.add(s.book.bookId, qty).subscribe({
      next: () => { this.loadAll(); this.showSuccess(`+${qty} ajouté au stock.`); },
      error: (err) => { console.error('addStockQty error', err); this.showError('Échec de l\'ajout de quantité.'); }
    });
  }

  removeStockQty(s: any, qty: number): void {
    this.stockSvc.remove(s.book.bookId, qty).subscribe({
      next: () => { this.loadAll(); this.showSuccess(`-${qty} retiré du stock.`); },
      error: (err) => { console.error('removeStockQty error', err); this.showError('Échec du retrait de quantité.'); }
    });
  }

  getStockStatusClass(quantity: number): string {
    if (quantity === 0) return 'badge-err';
    if (quantity < 5)   return 'badge-warn';
    return 'badge-ok';
  }

  getStockStatusLabel(quantity: number): string {
    if (quantity === 0) return 'Out of Stock';
    if (quantity < 5)   return 'Low Stock';
    return 'In Stock';
  }

  // =========================================================
  //  ORDERS
  // =========================================================
  createOrder(currency: Currency): void {
    const userId = this.getCurrentUserId();
    this.orderSvc.create(currency, userId).subscribe({
      next: () => { this.loadAll(); this.showSuccess(`Commande en ${currency} créée.`); },
      error: (err) => { console.error('createOrder error', err); this.showError('Échec de la création de la commande.'); }
    });
  }

  openAddItemModal(o: Order): void {
    this.selectedOrderId = o.orderId;
    this.addItemForm.reset({ qty: 1 });
    this.showAddItemModal = true;
  }

  submitAddItem(): void {
    if (!this.selectedOrderId || this.addItemForm.invalid) return;
    const v      = this.addItemForm.value;
    const bookId = Number(v.bookId);
    const qty    = Number(v.qty);
    this.orderSvc.addItem(this.selectedOrderId, bookId, qty).subscribe({
      next: () => { this.showAddItemModal = false; this.loadAll(); this.showSuccess('Article ajouté à la commande.'); },
      error: (err) => { console.error('submitAddItem error', err); this.showError('Échec de l\'ajout à la commande.'); }
    });
  }

  // =========================================================
  //  PAY ORDER → vérif réduction → paiement → facture PDF
  //  ✅ FIX — Ajout des headers JWT sur tous les appels http
  // =========================================================
  payOrder(id: number): void {
    const order  = this.orders.find(o => o.orderId === id);
    const userId = (order as any)?.userId;

    const doPay = () => {
      // ✅ FIX — headers auth sur le POST /pay
      this.http.post(`${this.BASE}/orders/${id}/pay`, {}, this.getAuthHeaders()).pipe(
        // ✅ FIX — headers auth sur le GET /invoice (blob)
        switchMap(() =>
          this.http.get(`${this.BASE}/invoice`, {
            params: { orderId: id.toString() },
            ...this.getAuthHeadersBlob()
          })
        )
      ).subscribe({
        next: (blob: any) => {
          this.downloadBlob(blob, `facture-order-${id}.pdf`);
          this.loadAll();
          this.showSuccess('✅ Commande payée — facture téléchargée.');
        },
        error: (err) => {
          console.error('payOrder error', err);
          if (err.status === 403) {
            this.showError('Accès refusé (403). Token JWT invalide ou expiré.');
          } else {
            this.showError('Échec du paiement de la commande.');
          }
        }
      });
    };

    if (userId) {
      // ✅ FIX — headers auth sur le GET /discount/eligible
      this.http.get<boolean>(
        `${this.BASE}/discount/eligible?userId=${userId}`,
        this.getAuthHeaders()
      ).subscribe({
        next: (eligible) => {
          const msg = eligible
            ? '🎉 Ce client bénéficie de -20% sur cette commande !\nConfirmer le paiement ?'
            : 'Confirmer le paiement de cette commande ?';
          if (confirm(msg)) doPay();
        },
        error: () => {
          if (confirm('Confirmer le paiement ?')) doPay();
        }
      });
    } else {
      if (confirm('Confirmer le paiement de cette commande ?')) doPay();
    }
  }

  cancelOrder(id: number): void {
    this.orderSvc.cancel(id).subscribe({
      next: () => { this.loadAll(); this.showSuccess('Commande annulée.'); },
      error: (err) => { console.error('cancelOrder error', err); this.showError('Échec de l\'annulation.'); }
    });
  }

  deleteOrder(id: number): void {
    this.orderSvc.delete(id).subscribe({
      next: () => { this.loadAll(); this.showSuccess('Commande supprimée.'); },
      error: (err) => { console.error('deleteOrder error', err); this.showError('Échec de la suppression.'); }
    });
  }

  // =========================================================
  //  RENTALS
  // =========================================================
  openRentalModal(): void {
    this.rentalForm.reset({ currency: 'TND' });
    this.showRentalModal = true;
  }

  submitRental(): void {
    if (this.rentalForm.invalid) return;
    const v      = this.rentalForm.value;
    const userId = this.getCurrentUserId();
    this.rentalSvc.create(
      Number(v.bookId),
      v.startDate,
      v.dueDate,
      Number(v.dailyPrice),
      v.currency,
      userId
    ).subscribe({
      next: () => { this.showRentalModal = false; this.loadAll(); this.showSuccess('Location créée avec succès.'); },
      error: (err) => { console.error('submitRental error', err); this.showError('Échec de la création de la location.'); }
    });
  }

  // =========================================================
  //  PAY RENTAL → vérif réduction → paiement → facture PDF
  //  ✅ FIX — Ajout des headers JWT sur tous les appels http
  // =========================================================
  payRental(id: number): void {
    const rental = this.rentals.find(r => r.rentalId === id);
    const userId = (rental as any)?.userId;

    const doPay = () => {
      // ✅ FIX — headers auth sur le POST /pay
      this.http.post(`${this.BASE}/rentals/${id}/pay`, {}, this.getAuthHeaders()).pipe(
        // ✅ FIX — headers auth sur le GET /invoice (blob)
        switchMap(() =>
          this.http.get(`${this.BASE}/invoice`, {
            params: { rentalId: id.toString() },
            ...this.getAuthHeadersBlob()
          })
        )
      ).subscribe({
        next: (blob: any) => {
          this.downloadBlob(blob, `facture-rental-${id}.pdf`);
          this.loadAll();
          this.showSuccess('✅ Location payée — facture téléchargée.');
        },
        error: (err) => {
          console.error('payRental error', err);
          if (err.status === 403) {
            this.showError('Accès refusé (403). Token JWT invalide ou expiré.');
          } else {
            this.showError('Échec du paiement de la location.');
          }
        }
      });
    };

    if (userId) {
      // ✅ FIX — headers auth sur le GET /discount/eligible
      this.http.get<boolean>(
        `${this.BASE}/discount/eligible?userId=${userId}`,
        this.getAuthHeaders()
      ).subscribe({
        next: (eligible) => {
          const msg = eligible
            ? '🎉 Ce client bénéficie de -20% sur cette location !\nConfirmer le paiement ?'
            : 'Confirmer le paiement de cette location ?';
          if (confirm(msg)) doPay();
        },
        error: () => {
          if (confirm('Confirmer le paiement ?')) doPay();
        }
      });
    } else {
      if (confirm('Confirmer le paiement de cette location ?')) doPay();
    }
  }

  openReturnModal(id: number): void {
    this.returningRentalId = id;
    this.returnDateValue   = new Date().toISOString().split('T')[0];
    this.showReturnModal   = true;
  }

  submitReturn(): void {
    if (!this.returningRentalId) return;
    this.rentalSvc.return(this.returningRentalId, this.returnDateValue).subscribe({
      next: () => {
        this.showReturnModal   = false;
        this.returningRentalId = null;
        this.loadAll();
        this.showSuccess('Retour enregistré avec succès.');
      },
      error: (err) => { console.error('submitReturn error', err); this.showError('Échec de l\'enregistrement du retour.'); }
    });
  }

  deleteRental(id: number): void {
    this.rentalSvc.delete(id).subscribe({
      next: () => { this.loadAll(); this.showSuccess('Location supprimée.'); },
      error: (err) => { console.error('deleteRental error', err); this.showError('Échec de la suppression.'); }
    });
  }

  // =========================================================
  //  HELPER — télécharger un Blob comme PDF
  // =========================================================
  private downloadBlob(blob: Blob, filename: string): void {
    const url = URL.createObjectURL(blob);
    const a   = document.createElement('a');
    a.href     = url;
    a.download = filename;
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
    URL.revokeObjectURL(url);
  }

  // =========================================================
  //  UI HELPERS
  // =========================================================
  setTab(t: 'books' | 'rentals' | 'orders' | 'stocks' | 'wallets'): void {
    this.activeTab = t;
  }

  getStatusClass(s: string): string {
    return ({ AVAILABLE: 'badge-ok', OUT_OF_STOCK: 'badge-warn', DISCONTINUED: 'badge-err' } as Record<string, string>)[s] || '';
  }

  getRentalStatusClass(s: string): string {
    return ({ ACTIVE: 'badge-ok', RETURNED: 'badge-info', LATE: 'badge-warn', CANCELLED: 'badge-err' } as Record<string, string>)[s] || '';
  }

  getOrderStatusClass(s: string): string {
    return ({ CREATED: 'badge-info', PAID: 'badge-ok', CANCELLED: 'badge-err' } as Record<string, string>)[s] || '';
  }

  fmt(d: string | null | undefined): string {
    return d ? new Date(d).toLocaleDateString('fr-FR') : '—';
  }

  getSpineStyle(i: number): string {
    const [a, b] = this.palette[i % this.palette.length];
    return `background: linear-gradient(180deg, ${a}, ${b});`;
  }

  getCoverStyle(i: number): string {
    const [a, b] = this.palette[(i + 2) % this.palette.length];
    return `background: linear-gradient(135deg, ${a}, ${b});`;
  }

  isOverdue(r: Rental): boolean {
    if (!r?.dueDate)           return false;
    if ((r as any).returnDate) return false;
    const due   = new Date(r.dueDate as any); due.setHours(0, 0, 0, 0);
    const today = new Date();                 today.setHours(0, 0, 0, 0);
    return due.getTime() < today.getTime();
  }

  // =========================================================
  //  WALLETS ADMIN
  // =========================================================
  loadStudentWallets(): void {
    if (this.studentWallets.length > 0) { this.walletLoading = false; return; }
    this.walletLoading = true;
    const headers = this.getAuthHeaders().headers;

    this.http.get<any[]>(`${this.BASE}/users`, { headers }).subscribe({
      next: (students) => {
        this.students = (students || []).filter((s: any) => (s?.role || '').toString().toUpperCase() === 'STUDENT');
        students = this.students;
        this.studentWallets = students.map((s: any) => ({ student: s, wallet: null }));
        let loaded = 0;
        if (students.length === 0) { this.walletLoading = false; return; }

        students.forEach((s: any, i: number) => {
          const userId = s.userId ?? s.id;
          this.walletSvc.getWallet(userId).subscribe({
            next: (w: Wallet) => {
              this.studentWallets[i].wallet = w;
              if (++loaded === students.length) this.walletLoading = false;
            },
            error: () => {
              this.walletSvc.createWallet(userId).subscribe({
                next: (w: Wallet) => { this.studentWallets[i].wallet = w; },
                error: () => {}
              });
              if (++loaded === students.length) this.walletLoading = false;
            }
          });
        });
      },
      error: () => { this.walletLoading = false; this.showError('Impossible de charger les students.'); }
    });
  }

  openRechargeModal(sw: { student: any; wallet: Wallet | null }): void {
    this.selectedStudent        = sw.student;
    this.selectedStudentWallet  = sw.wallet;
    this.rechargeForm.reset({ amount: '' });
    this.showRechargeModal = true;
  }

  closeRechargeModal(): void {
    this.showRechargeModal      = false;
    this.selectedStudent        = null;
    this.selectedStudentWallet  = null;
  }

  submitAdminRecharge(): void {
    if (this.rechargeForm.invalid || !this.selectedStudent) return;
    const userId = this.selectedStudent.userId ?? this.selectedStudent.id;
    const amount = Number(this.rechargeForm.get('amount')?.value);
    const name   = `${this.selectedStudent.firstName ?? ''} ${this.selectedStudent.lastName ?? ''}`.trim();

    this.walletSvc.recharge(userId, amount).subscribe({
      next: (w: Wallet) => {
        const idx = this.studentWallets.findIndex(
          sw => (sw.student.userId ?? sw.student.id) === userId
        );
        if (idx !== -1) this.studentWallets[idx].wallet = w;
        this.closeRechargeModal();
        this.showSuccess(`✅ Wallet de ${name} rechargé de ${amount} TND — nouveau solde : ${w.balance} TND`);
      },
      error: () => this.showError('Échec de la recharge du wallet.')
    });
  }

  // =========================================================
  //  FEEDBACK
  // =========================================================
  private showSuccess(msg: string): void {
    this.successMessage = msg;
    this.errorMessage   = '';
    setTimeout(() => (this.successMessage = ''), 4000);
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