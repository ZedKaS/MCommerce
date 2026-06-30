import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Product {
  id: number;
  name: string;
  description: string;
  price: number;
  active: boolean;
  createdDate: string;
  imageUrl?: string;
}

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  private API_URL = '/api/products';

  constructor(private http: HttpClient) {}

  private adminHeaders(): HttpHeaders {
    return new HttpHeaders({
      'X-User-Role': 'ADMIN'
    });
  }

    getProducts(
    keyword: string = '',
    page: number = 0,
    size: number = 5,
    sortBy: string = 'price',
    direction: string = 'asc'
  ): Observable<any> {
    return this.http.get<any>(this.API_URL, {
      headers: this.adminHeaders(),  
      params: {
        keyword,
        page,
        size,
        sortBy,
        direction
      }
    });
  }

  getProductById(id: number): Observable<Product> {
    return this.http.get<Product>(`${this.API_URL}/${id}`);
  }


  createProduct(product: any, image?: File): Observable<any> {
    const formData = new FormData();
    formData.append(
      'product',
      new Blob([JSON.stringify(product)], { type: 'application/json' })
    );

    if (image) {
      formData.append('image', image);
    }

    return this.http.post(this.API_URL, formData, {
      headers: this.adminHeaders()
    });
  }

  updateProduct(id: number, product: any, image?: File): Observable<any> {
    const formData = new FormData();
    formData.append(
      'product',
      new Blob([JSON.stringify(product)], { type: 'application/json' })
    );

    if (image) {
      formData.append('image', image);
    }

    return this.http.patch(`${this.API_URL}/${id}`, formData, {
      headers: this.adminHeaders()
    });
  }

  deleteProduct(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`, {
      headers: this.adminHeaders()
    });
  }
}
