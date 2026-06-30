import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';

import { Navbar } from '../../navbar/navbar';
import { Product, ProductService } from '../../../services/product.service';
import { CartService } from '../../../services/cart.service';

@Component({
  selector: 'app-product-detail',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,     // ✅ OBLIGATOIRE POUR routerLink
    Navbar
  ],
  templateUrl: './product-detail.html',
  styleUrl: './product-detail.css',
})
export class ProductDetail implements OnInit {

  product?: Product;
  suggestions: Product[] = [];
  loading = true;

  // 🔁 pagination suggestions
suggestionsPage = 0;
suggestionsSize = 6;
suggestionsTotalPages = 0;


  constructor(
  private route: ActivatedRoute,
  private router: Router,
  private productService: ProductService,
  private cartService: CartService,
  private cdr: ChangeDetectorRef
) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const productId = Number(params.get('id'));

      if (!productId) {
        this.router.navigate(['/products']);
        return;
      }

      this.loadProduct(productId);
      this.loadSuggestions(productId);
    });
  }

  // ================= PRODUCT =================
  loadProduct(productId: number) {
    this.loading = true;

    this.productService.getProductById(productId).subscribe({
      next: (product) => {
        this.product = product;
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.router.navigate(['/products']);
      }
    });
  }

  // ================= SUGGESTIONS =================
  loadSuggestions(currentProductId: number) {
  this.productService
    .getProducts(
      '',
      this.suggestionsPage,
      this.suggestionsSize,
      'price',
      'asc'
    )
    .subscribe({
      next: (res) => {
        this.suggestions = res.content.filter(
          (p: Product) => p.id !== currentProductId
        );
        this.suggestionsTotalPages = res.totalPages;
        this.cdr.detectChanges();
      }
    });
}
  changeSuggestionsPage(newPage: number) {
  if (newPage < 0 || newPage >= this.suggestionsTotalPages) return;

  this.suggestionsPage = newPage;

  if (this.product?.id) {
    this.loadSuggestions(this.product.id);
  }
}


  // ================= IMAGE =================
  getImageUrl(imageUrl?: string): string {
    return imageUrl
      ? imageUrl
      : 'assets/images/default-product.jpg';
  }

  // ================= CART =================
  addToCart() {
  if (!this.product) return;

  this.cartService.addToCart(this.product.id).subscribe();
}


  // ================= NAV =================
  goToDetails(id: number) {
    this.router.navigate(['/products', id]);
  }
}
