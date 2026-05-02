export type Currency = 'TND' | 'EUR' | 'USD';
export type BookStatus = 'AVAILABLE' | 'OUT_OF_STOCK' | 'DISCONTINUED';
export type OrderStatus = 'CREATED' | 'PAID' | 'CANCELLED';
export type RentalStatus = 'ACTIVE' | 'RETURNED' | 'LATE' | 'CANCELLED';

export interface Author {
  authorId: number;
  name: string;
}

export interface Category {
  categoryId: number;
  name: string;
}

export interface Stock {
  stockId: number;
  quantity: number;
  reservedQty: number;
  lastUpdate: string;
}

export interface Book {
  bookId: number;
  title: string;
  isbn: string;
  status: BookStatus;
  salePrice: number;
  currency: Currency;
  publicationYear: string;
  image: string;          // ✅ AJOUT IMPORTANT
  stock: Stock | null;
  author: Author | null;
  category: Category | null;
}

export interface Order {
  orderId: number;
  orderDate: string;
  totalAmount: number;
  currency: Currency;
  status: OrderStatus;
  paid: boolean;
  discountApplied?: boolean;   // ← AJOUTER

  paymentDate: string | null;
}

export interface Rental {
  rentalId: number;
  startDate: string;
  dueDate: string;
  returnDate: string | null;
  status: RentalStatus;
  dailyRentalPrice: number;
  totalRentalPrice: number;
  currency: Currency;
  paid: boolean;
  discountApplied?: boolean;   // ← AJOUTER

  paymentDate: string | null;
  book: Book | null;
}