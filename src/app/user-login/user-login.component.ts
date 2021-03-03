import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthProvider } from 'ngx-auth-firebaseui';

@Component({
  selector: 'app-user-login',
  templateUrl: './user-login.component.html',
  styleUrls: ['./user-login.component.css'],
})
export class UserLoginComponent implements OnInit {
  providers = AuthProvider;

  constructor(private router: Router) {}

  ngOnInit(): void {}

  register(): void {
    this.router.navigate(['/register']);
  }

  success(): void {
    this.router.navigate(['/']);
  }
}
