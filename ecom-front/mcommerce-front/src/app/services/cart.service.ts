import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';

export interface CartItem {
  productId: number;
  productName: string;
  productImageUrl?: string;
  price: number;
  quantity: number;
  totalPrice: number;
}

export interface CartResponse {
  id: number;
  userId: number;
  totalAmount: number;
  items: CartItem[];
}

@Injectable({
  providedIn: 'root'
})
export class CartService {

  private API_URL = '/api/carts';

  private cartCountSubject = new BehaviorSubject<number>(0);
  cartCount$ = this.cartCountSubject.asObservable();

  constructor(private http: HttpClient) {}

  private userHeaders(): HttpHeaders {
    return new HttpHeaders({
      'X-User-Id': localStorage.getItem('userId') || '1'
    });
  }

  getMyCart(): Observable<CartResponse> {
    return this.http
      .get<CartResponse>(`${this.API_URL}/me`, {
        headers: this.userHeaders()
      })
      .pipe(tap(cart => this.updateCartCount(cart)));
  }

  addToCart(productId: number): Observable<CartResponse> {
    return this.http
      .post<CartResponse>(
        `${this.API_URL}/me/items/${productId}`,
        {},
        { headers: this.userHeaders() }
      )
      .pipe(tap(cart => this.updateCartCount(cart)));
  }

  updateQuantity(productId: number, quantity: number): Observable<CartResponse> {
    return this.http
      .put<CartResponse>(
        `${this.API_URL}/me/items/${productId}`,
        {},
        {
          headers: this.userHeaders(),
          params: { quantity }
        }
      )
      .pipe(tap(cart => this.updateCartCount(cart)));
  }

  removeItem(productId: number): Observable<CartResponse> {
    return this.http
      .delete<CartResponse>(
        `${this.API_URL}/me/items/${productId}`,
        { headers: this.userHeaders() }
      )
      .pipe(tap(cart => this.updateCartCount(cart)));
  }

  clearCart(): Observable<void> {
    return this.http
      .delete<void>(`${this.API_URL}/me`, {
        headers: this.userHeaders()
      })
      .pipe(
        tap(() => this.cartCountSubject.next(0))
      );
  }

  private updateCartCount(cart: CartResponse) {
    const count = cart.items.reduce((sum, item) => sum + item.quantity, 0);
    this.cartCountSubject.next(count);
  }
}
