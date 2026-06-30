import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Navbar } from '../navbar/navbar';
import { AuthService } from '../../services/auth.service';
import { PaymentHistoryService, PaymentHistory } from '../../services/payment-history.service';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, Navbar],
  templateUrl: './profile.html',
  styleUrl: './profile.css'
})
export class Profile implements OnInit {

  username = '';
  role = '';
  status = 'Actif';

  payments: PaymentHistory[] = [];
  loading = false;

  constructor(
    private authService: AuthService,
    private paymentHistoryService: PaymentHistoryService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadUserInfo();
    this.loadPaymentHistory();
  }

  loadUserInfo() {
    const token = this.authService.getToken();
    if (!token) return;

    const payload = JSON.parse(atob(token.split('.')[1]));
    this.username = payload.sub;
    this.role = payload.role;

    // 🔄 Forcer la détection des changements
    this.cdr.detectChanges();
  }

  loadPaymentHistory() {
    this.loading = true;
    this.cdr.detectChanges();

    this.paymentHistoryService.getHistory().subscribe({
      next: (page) => {
        this.payments = page.content;
        this.loading = false;

        // 🔄 Forcer la détection des changements après chargement
        this.cdr.detectChanges();
      },
      error: () => {
        console.error('Erreur chargement historique');
        this.loading = false;

        // 🔄 Synchroniser l’UI même en cas d’erreur
        this.cdr.detectChanges();
      }
    });
  }
}
