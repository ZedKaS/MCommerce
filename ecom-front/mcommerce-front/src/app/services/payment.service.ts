// src/app/services/payment.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PaymentMethod } from '../models/payment-method';
import { AuthService } from './auth.service';

export interface CheckoutResponse {
  transactionId: number;
  userId: number;
  totalAmount: number;
  status: 'SUCCESS' | 'FAILED';
  paymentMethod: PaymentMethod;
  createdDate: string;
}

@Injectable({ providedIn: 'root' })
export class PaymentService {

  private API_URL = '/api/payments';

  constructor(private http: HttpClient) {}

  checkout(method: PaymentMethod): Observable<CheckoutResponse> {
    return this.http.post<CheckoutResponse>(
      `${this.API_URL}/checkout`,
      {
        paymentMethod: method
      }
    );
  }
}


