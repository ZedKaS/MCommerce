import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AdminUserService, UserResponse, CreateUserRequest, UpdateUserRequest } from '../../../services/admin-user.service';

@Component({
  selector: 'app-users-admin',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './users-admin.html',
  styleUrl: './users-admin.css'
})
export class UsersAdmin implements OnInit {

  users: UserResponse[] = [];
  loading = true;
  error = '';

  // 🔍 SEARCH
searchTerm = '';
allUsers: UserResponse[] = [];


  // ===== MODALS =====
  showAddModal = false;
  showEditModal = false;

  // ===== ADD FORM =====
  newUser: CreateUserRequest = {
    username: '',
    password: '',
    role: 'USER'
  };

  // ===== EDIT FORM =====
  editUser: UpdateUserRequest & { id?: number } = {
    username: '',
    role: 'USER',
    enabled: true
  };

  constructor(
    private adminUserService: AdminUserService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadUsers();
  }

  // ================= LOAD =================
  loadUsers(): void {
  this.loading = true;
  this.error = '';

  this.adminUserService.getAllUsers().subscribe({
    next: (users) => {
      this.allUsers = users;     // 🔥 source complète
      this.users = users;        // 🔥 affichage
      this.loading = false;
      this.cdr.detectChanges();
    },
    error: (err) => {
      console.error(err);
      this.error = 'Erreur lors du chargement des utilisateurs';
      this.loading = false;
      this.cdr.detectChanges();
    }
  });
}

onSearch(): void {
  const term = this.searchTerm.trim().toLowerCase();

  if (!term) {
    // Champ vide → tout afficher
    this.users = this.allUsers;
  } else {
    this.users = this.allUsers.filter(user =>
      user.username.toLowerCase().includes(term)
    );
  }

  this.cdr.detectChanges();
}


  // ================= MODALS =================
  openAddModal() {
    this.resetAddForm();
    this.showAddModal = true;
  }

  closeAddModal() {
    this.showAddModal = false;
  }

  openEditModal(user: UserResponse) {
    this.editUser = {
      id: user.id,
      username: user.username,
      role: user.role,
      enabled: user.enabled
    };
    this.showEditModal = true;
  }

  closeEditModal() {
    this.showEditModal = false;
  }

  // ================= CRUD =================
  createUser() {
    this.adminUserService.createUser(this.newUser).subscribe({
      next: () => {
        this.closeAddModal();
        this.loadUsers();
      },
      error: (err) => {
        console.error(err);
        alert('Erreur création utilisateur');
      }
    });
  }

  updateUser() {
    if (!this.editUser.id) return;

    this.adminUserService.updateUser(this.editUser.id, this.editUser).subscribe({
      next: () => {
        this.closeEditModal();
        this.loadUsers();
      },
      error: (err) => {
        console.error(err);
        alert('Erreur modification utilisateur');
      }
    });
  }

  deleteUser(id: number) {
    if (!confirm('Supprimer cet utilisateur ?')) return;

    this.adminUserService.deleteUser(id).subscribe({
      next: () => this.loadUsers(),
      error: (err) => {
        console.error(err);
        alert('Erreur suppression utilisateur');
      }
    });
  }

  // ================= RESET =================
  resetAddForm() {
    this.newUser = {
      username: '',
      password: '',
      role: 'USER'
    };
  }
}
