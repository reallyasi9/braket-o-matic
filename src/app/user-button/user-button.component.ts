import { Component, OnInit } from '@angular/core';
import { AngularFireAuth } from '@angular/fire/auth';
import { MatDialog } from '@angular/material/dialog';
import { Observable } from 'rxjs';
import { UserLoginComponent } from '../user-login/user-login.component';

@Component({
  selector: 'app-user-button',
  templateUrl: './user-button.component.html',
  styleUrls: ['./user-button.component.css']
})
export class UserButtonComponent implements OnInit {
  constructor(public afAuth: AngularFireAuth, public loginDialog: MatDialog) { }

  ngOnInit(): void {
  }

  openDialog() {
    const dialogRef = this.loginDialog.open(UserLoginComponent);

    dialogRef.afterClosed().subscribe((result) => {
      console.log(`Dialog: ${result}`);
    });
  }

}
