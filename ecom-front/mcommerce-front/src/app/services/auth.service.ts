import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface LoginRequest {
  username: string;
  password: string;
}

export interface AuthResponse {
  message: string;
  token: string;
}


@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private API_URL = '/api/auth';

  constructor(private http: HttpClient) {}

  login(data: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.API_URL}/login`, data);
  }

  register(data: any): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.API_URL}/register`, data);
  }

  saveAuth(auth: AuthResponse) {
    localStorage.setItem('token', auth.token);
  }

  getToken(): string | null {
  return localStorage.getItem('token');
}

getRoleFromToken(): 'USER' | 'ADMIN' | null {
  const token = this.getToken();
  if (!token) return null;

  const payload = JSON.parse(atob(token.split('.')[1]));
  return payload.role || null;
}


  getUserIdFromToken(): number | null {
  const token = this.getToken();
  if (!token) return null;

  const payload = JSON.parse(atob(token.split('.')[1]));
  return payload.userId || null;
}


  logout() {
    localStorage.clear();
  }
}
