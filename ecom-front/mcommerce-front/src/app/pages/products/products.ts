import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

import { Navbar } from '../navbar/navbar';
import { ProductService, Product } from '../../services/product.service';

@Component({
  selector: 'app-products',
  standalone: true,
  imports: [CommonModule, FormsModule, Navbar, RouterModule],
  templateUrl: './products.html',
  styleUrl: './products.css',
})
export class Products implements OnInit {

  products: Product[] = [];

  // 🔍 filtres
  keyword = '';
  priceOrder: 'asc' | 'desc' = 'asc';

  // 📄 pagination
  page = 0;
  size = 8;
  totalPages = 0;

  constructor(
    private productService: ProductService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    setTimeout(() => {
      this.loadProducts();
    }, 0);
  }

  // ================= LOAD =================
  loadProducts() {
    this.productService
      .getProducts(this.keyword, this.page, this.size, 'price', this.priceOrder)
      .subscribe({
        next: (res) => {
          this.products = res.content;
          this.totalPages = res.totalPages;
          this.cdr.detectChanges();
        },
        error: (err) => {
          console.error('Erreur chargement produits', err);
          this.cdr.detectChanges();
        }
      });
  }

  // ================= SEARCH =================
  onSearch() {
    this.page = 0;
    this.loadProducts();
  }

  // ================= SORT =================
  onSortChange() {
    this.page = 0;
    this.loadProducts();
  }

  // ================= PAGINATION =================
  changePage(newPage: number) {
    if (newPage < 0 || newPage >= this.totalPages) return;
    this.page = newPage;
    this.loadProducts();
  }

  // ================= IMAGE =================
  getImageUrl(imageUrl?: string): string {
    return imageUrl
      ? imageUrl
      : 'assets/images/default-product.jpg';
  }
}
