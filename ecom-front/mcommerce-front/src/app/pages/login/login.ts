import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [RouterModule, FormsModule, CommonModule],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class Login {

  username: string = '';
  password: string = '';
  showPassword: boolean = false;
  errorMessage: string = '';

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  togglePassword(): void {
    this.showPassword = !this.showPassword;
  }

  onSubmit(): void {
    this.errorMessage = '';

    this.authService.login({
      username: this.username,
      password: this.password
    }).subscribe({
      next: (res) => {
        // Sauvegarde du token
        this.authService.saveAuth(res);

        // Extraction du rôle depuis le JWT
        const role = this.authService.getRoleFromToken();

        // Redirection selon le rôle
        if (role === 'ADMIN') {
          this.router.navigate(['/admin/products']);
        } else {
          this.router.navigate(['/products']);
        }

      },
      error: () => {
        this.errorMessage = 'Invalid username or password';
      }
    });
  }
}
