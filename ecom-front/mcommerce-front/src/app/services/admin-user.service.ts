import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface UserResponse {
  id: number;
  username: string;
  role: 'ADMIN' | 'USER';
  enabled: boolean;
  createdDate: string;
  updatedDate: string;
}

export interface CreateUserRequest {
  username: string;
  password: string;
  role: 'ADMIN' | 'USER';
}

export interface UpdateUserRequest {
  username: string;
  role: 'ADMIN' | 'USER';
  enabled: boolean;
}

@Injectable({ providedIn: 'root' })
export class AdminUserService {

  private API_URL = '/api/admin/users';

  constructor(private http: HttpClient) {}

  getAllUsers(): Observable<UserResponse[]> {
    return this.http.get<UserResponse[]>(this.API_URL);
  }

  createUser(data: CreateUserRequest): Observable<UserResponse> {
    return this.http.post<UserResponse>(this.API_URL, data);
  }

  updateUser(id: number, data: UpdateUserRequest): Observable<UserResponse> {
    return this.http.put<UserResponse>(`${this.API_URL}/${id}`, data);
  }

  deleteUser(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`);
  }
}
