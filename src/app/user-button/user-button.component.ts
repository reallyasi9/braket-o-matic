import { Location } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { AngularFireAuth } from '@angular/fire/compat/auth';
import { MatLegacyDialog as MatDialog } from '@angular/material/legacy-dialog';
import { UserLoginComponent } from '../user-login/user-login.component';

@Component({
  selector: 'app-user-button',
  templateUrl: './user-button.component.html',
  styleUrls: ['./user-button.component.css'],
})
export class UserButtonComponent implements OnInit {
  dialogOpen: boolean = false;

  constructor(
    public afAuth: AngularFireAuth,
    public loginDialog: MatDialog,
    private location: Location,
  ) {}

  ngOnInit(): void {}

  openDialog() {
    const dialogRef = this.loginDialog.open(UserLoginComponent);
    dialogRef.afterOpened().subscribe(() => {
      this.dialogOpen = true;
    });

    dialogRef.afterClosed().subscribe((result) => {
      console.log(`Dialog: ${result}`);
      this.dialogOpen = false;
      location.reload();
    });
  }

  logOut() {
    this.dialogOpen = true;
    this.afAuth.signOut().then(
      () => {
        console.log('Logged out');
        this.dialogOpen = false;
        location.reload();
      },
      () => {
        console.log('Logging out failed!');
        this.dialogOpen = false;
      }
    );
  }
}
