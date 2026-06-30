import { Component, OnInit, ChangeDetectorRef  } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ProductService, Product } from '../../../services/product.service';
import { Router, NavigationEnd } from '@angular/router';
import { filter } from 'rxjs/operators';


@Component({
  selector: 'app-products-admin',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './products-admin.html',
  styleUrl: './products-admin.css'
})
export class ProductsAdmin implements OnInit {

  page = 0;      
  size = 5;
  totalPages = 0;


  products: Product[] = [];

  showAddModal = false;
  showEditModal = false;

  keyword = '';
  onSearch() {
  this.page = 0;
  this.loadProducts();
}


  newProduct = {
    name: '',
    description: '',
    price: 0,
    active: true
  };
  newImage?: File;


  priceOrder: 'asc' | 'desc' = 'asc';

  onSortChange() {
  this.page = 0;
  this.loadProducts();
}


  editProduct: any = {};
  editImage?: File;

  constructor(
  private productService: ProductService,
  private router: Router,
  private cdr: ChangeDetectorRef
) {
}



  ngOnInit(): void {
    setTimeout(() => {
      this.loadProducts();
    }, 0);
  }


  loadProducts() {
  this.productService
    .getProducts(
      this.keyword,
      this.page,
      this.size,
      'price',
      this.priceOrder
    )
    .subscribe({
      next: (res) => {
        this.products = res.content;
        this.totalPages = res.totalPages;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Erreur chargement produits', err);
      }
    });
}




changePage(newPage: number) {
  if (newPage < 0 || newPage >= this.totalPages) return;
  this.page = newPage;
  this.loadProducts();
}


  getImageUrl(imageUrl?: string): string {
    if (!imageUrl) {
      return '/assets/guitare.jpg';
    }
    return imageUrl;
  }

  // ================= MODALS =================
  openAddModal() {
    this.showAddModal = true;
  }

  closeAddModal() {
    this.showAddModal = false;
    this.resetAddForm();
  }

  openEditModal(product: Product) {
    this.editProduct = { ...product };
    this.showEditModal = true;
  }

  closeEditModal() {
    this.showEditModal = false;
    this.editImage = undefined;
  }

  // ================= FILE HANDLING =================
  onFileSelected(event: any) {
    this.newImage = event.target.files[0];
  }

  onEditFileSelected(event: any) {
    this.editImage = event.target.files[0];
  }

  // ================= CRUD =================
  createProduct() {
  this.productService
    .createProduct(this.newProduct, this.newImage)
    .subscribe({
      next: () => {
        this.loadProducts();
        this.closeAddModal(); // ✅ fermeture auto
      },
      error: () => {
        alert('Erreur lors de la création du produit');
      }
    });
}


  updateProduct() {
    this.productService
      .updateProduct(this.editProduct.id, this.editProduct, this.editImage)
      .subscribe(() => {
        this.loadProducts();
        this.closeEditModal();
      });
  }

  deleteProduct(id: number) {
    if (!confirm('Supprimer ce produit ?')) return;

    this.productService.deleteProduct(id).subscribe(() => {
      this.loadProducts();
    });
  }

  // ================= RESET =================
  resetAddForm() {
    this.newProduct = {
      name: '',
      description: '',
      price: 0,
      active: true
    };
    this.newImage = undefined;
  }
}
