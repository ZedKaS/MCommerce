import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface PaymentHistoryResponse {
  transactionId: number;
  userId: number;
  amount: number;
  status: string;
  paymentMethod: string;
  createdDate: string;
}

@Injectable({
  providedIn: 'root'
})
export class PaymentAdminService {

  private API_URL = '/api/payments/history';

  constructor(private http: HttpClient) {}

  getPayments(page: number = 0, size: number = 5): Observable<any> {
    return this.http.get<any>(this.API_URL, {
      params: {
        page,
        size
      }
    });
  }
}
