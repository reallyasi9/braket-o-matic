import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-user-register',
  templateUrl: './user-register.component.html',
  styleUrls: ['./user-register.component.css'],
})
export class UserRegisterComponent implements OnInit {
  constructor(private router: Router) {}

  ngOnInit(): void {}

  login(): void {
    this.router.navigate(['/login']);
  }

  success(): void {
    this.router.navigate(['/']);
  }
}