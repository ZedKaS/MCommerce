import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface PaymentHistory {
  transactionId: number;
  userId: number;
  amount: number;
  status: 'SUCCESS' | 'FAILED';
  paymentMethod: string;
  createdDate: string;
}

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
}

@Injectable({ providedIn: 'root' })
export class PaymentHistoryService {

  private API_URL = '/api/payments';

  constructor(private http: HttpClient) {}

  getHistory(page = 0, size = 5): Observable<PageResponse<PaymentHistory>> {
    const params = new HttpParams()
      .set('page', page)
      .set('size', size);

    return this.http.get<PageResponse<PaymentHistory>>(
      `${this.API_URL}/history`,
      { params }
    );
  }
}
