import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

import { Navbar } from '../navbar/navbar';
import { CartService, CartItem } from '../../services/cart.service';
import { PaymentService } from '../../services/payment.service';
import { PaymentMethod } from '../../models/payment-method';

@Component({
  selector: 'app-payment',
  standalone: true,
  imports: [CommonModule, FormsModule, Navbar],
  templateUrl: './payment.html',
  styleUrl: './payment.css'
})
export class Payment implements OnInit {

  paymentMethod: PaymentMethod = 'CARD';
  loading = false;

  cartItems: CartItem[] = [];
  total = 0;

  // carte
  cardName = '';
  cardNumber = '';
  cardExpiry = '';
  cardCvv = '';

  constructor(
    private paymentService: PaymentService,
    private cartService: CartService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadCart();
  }

  loadCart() {
    this.cartService.getMyCart().subscribe({
      next: (cart) => {
        this.cartItems = cart.items;
        this.total = cart.totalAmount;
        this.cdr.detectChanges();
      },
      error: () => {
        console.error('Erreur chargement panier');
      }
    });
  }

  pay() {
  if (this.cartItems.length === 0) {
    alert('Votre panier est vide');
    return;
  }

  this.loading = true;

  this.paymentService.checkout(this.paymentMethod).subscribe({
    next: (response) => {
      console.log('PAIEMENT OK', response);

      // 🔥 le backend a déjà vidé le panier
      this.loading = false;
      this.router.navigate(['/profile']);
      this.cdr.detectChanges();
    },
    error: (err) => {
      console.error(err);
      alert('Erreur lors du paiement');
      this.loading = false;
      this.cdr.detectChanges();
    }
  });
}


}
