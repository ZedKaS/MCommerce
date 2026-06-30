import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SidebarAdmin } from '../sidebar-admin/sidebar-admin';
import {
  PaymentAdminService,
  PaymentHistoryResponse
} from '../../../services/payment-admin.service';

@Component({
  selector: 'app-payments-admin',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './payments-admin.html',
  styleUrl: './payments-admin.css'
})
export class PaymentsAdmin implements OnInit {

  payments: PaymentHistoryResponse[] = [];
  filteredPayments: PaymentHistoryResponse[] = [];

  loading = true;
  error = '';

  // Pagination
  page = 0;
  size = 5;
  totalPages = 0;

  // Filters
  searchTerm = '';
  statusFilter = '';
  methodFilter = '';

  constructor(
    private paymentService: PaymentAdminService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadPayments();
  }

  loadPayments(): void {
    this.loading = true;

    this.paymentService.getPayments(this.page, this.size).subscribe({
      next: (res) => {
        this.payments = res.content;
        this.filteredPayments = [...this.payments];
        this.totalPages = res.totalPages;
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error(err);
        this.error = 'Erreur chargement paiements';
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  applyFilters(): void {
    this.filteredPayments = this.payments.filter(p => {
      const matchSearch =
        !this.searchTerm ||
        p.transactionId.toString().includes(this.searchTerm) ||
        p.userId.toString().includes(this.searchTerm);

      const matchStatus =
        !this.statusFilter || p.status === this.statusFilter;

      const matchMethod =
        !this.methodFilter || p.paymentMethod === this.methodFilter;

      return matchSearch && matchStatus && matchMethod;
    });
  }

  resetFilters(): void {
    this.searchTerm = '';
    this.statusFilter = '';
    this.methodFilter = '';
    this.filteredPayments = [...this.payments];
  }

  // Pagination
  changePage(newPage: number): void {
    if (newPage < 0 || newPage >= this.totalPages) return;
    this.page = newPage;
    this.loadPayments();
  }
}
