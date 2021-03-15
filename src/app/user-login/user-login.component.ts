import { Component, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { AuthProvider } from 'ngx-auth-firebaseui';

@Component({
  selector: 'app-user-login',
  templateUrl: './user-login.component.html',
  styleUrls: ['./user-login.component.css'],
})
export class UserLoginComponent implements OnInit {
  providers = AuthProvider;

  constructor(public dialogRef: MatDialogRef<UserLoginComponent>) {}

  ngOnInit(): void {}

  success(): void {
    this.dialogRef.close();
  }
}
