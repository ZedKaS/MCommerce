import { Routes } from '@angular/router';
import { Admin } from './pages/admin/admin';
import { ProductsAdmin } from './pages/admin/products-admin/products-admin';
import { Login } from './pages/login/login';
import { Register } from './pages/register/register';
import { Products } from './pages/products/products';
import { Cart } from './pages/cart/cart';
import { Profile } from './pages/profile/profile';
import { Payment } from './pages/payment/payment';
import { UsersAdmin } from './pages/admin/users-admin/users-admin';
import { PaymentsAdmin } from './pages/admin/payments-admin/payments-admin';
import { ProductDetail } from './pages/products/product-detail/product-detail';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },

  { path: 'login', component: Login },
  { path: 'register', component: Register },

  { path: 'products/:id', component: ProductDetail },
  { path: 'products', component: Products },

  { path: 'cart', component: Cart },
  { path: 'profile', component: Profile },

  {
    path: 'admin',
    component: Admin,
    children: [
      {
        path: 'products',
        component: ProductsAdmin,
        runGuardsAndResolvers: 'always'
      },
      {
        path: 'users',
        component: UsersAdmin,
        runGuardsAndResolvers: 'always'
      },
      {
        path: 'payments',
        component: PaymentsAdmin,
        runGuardsAndResolvers: 'always'
      },
      { path: '', redirectTo: 'products', pathMatch: 'full' }
    ]
  },

  { path: 'payment', component: Payment },

  { path: '**', redirectTo: 'login' }
];
