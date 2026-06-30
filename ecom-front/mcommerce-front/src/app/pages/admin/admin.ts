import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { SidebarAdmin } from './sidebar-admin/sidebar-admin';

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [
    RouterOutlet,
    SidebarAdmin
  ],
  templateUrl: './admin.html',
  styleUrl: './admin.css'
})
export class Admin {}
