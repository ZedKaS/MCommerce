import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { Navbar } from '../navbar/navbar';
import { CartService, CartItem } from '../../services/cart.service';
import { Router } from '@angular/router';
import { PaymentService } from '../../services/payment.service';
import { PaymentMethod } from '../../models/payment-method';
@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [CommonModule, FormsModule, Navbar],
  templateUrl: './cart.html',
  styleUrl: './cart.css'
})
export class Cart implements OnInit {

  cartItems: CartItem[] = [];
  total = 0;

  constructor(
  private cartService: CartService,
  private paymentService: PaymentService,
  private router: Router,
  private cdr: ChangeDetectorRef
) {}

  ngOnInit(): void {
    // même pattern que products / admin
    setTimeout(() => {
      this.loadCart();
    }, 0);
  }

  // ================= LOAD =================
  loadCart() {
    this.cartService.getMyCart().subscribe({
      next: (cart) => {
        this.cartItems = cart.items;
        this.total = cart.totalAmount;
        this.cdr.detectChanges();     // ✅ IMPORTANT
      },
      error: (err) => {
        console.error('Erreur chargement panier', err);
        this.cdr.detectChanges();
      }
    });
  }

  // ================= ACTIONS =================
  updateQuantity(item: CartItem) {
    if (item.quantity < 1) {
      item.quantity = 1;
    }

    this.cartService
      .updateQuantity(item.productId, item.quantity)
      .subscribe({
        next: (cart) => {
          this.cartItems = cart.items;
          this.total = cart.totalAmount;
          this.cdr.detectChanges();   // ✅ IMPORTANT
        },
        error: (err) => {
          console.error('Erreur mise à jour quantité', err);
          this.cdr.detectChanges();
        }
      });
  }

  removeItem(productId: number) {
    this.cartService.removeItem(productId).subscribe({
      next: (cart) => {
        this.cartItems = cart.items;
        this.total = cart.totalAmount;
        this.cdr.detectChanges();     // ✅ IMPORTANT
      },
      error: (err) => {
        console.error('Erreur suppression produit', err);
        this.cdr.detectChanges();
      }
    });
  }

  checkout() {
    this.router.navigate(['/payment']); // ✅ SEULE ACTION
  }


  // ================= IMAGE =================
  getImageUrl(imageUrl?: string): string {
    return imageUrl
      ? imageUrl
      : 'assets/images/default-product.jpg';
  }
}
