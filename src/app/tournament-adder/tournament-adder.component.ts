import { Component, Input, OnInit } from '@angular/core';
import { MatLegacySnackBar as MatSnackBar } from '@angular/material/legacy-snack-bar';

@Component({
  selector: 'app-tournament-adder',
  templateUrl: './tournament-adder.component.html',
  styleUrls: ['./tournament-adder.component.css']
})
export class TournamentAdderComponent implements OnInit {

  @Input() roundValues: number[] = [];
  name: string = "";
  startDate: Date = new Date();
  hasPayout: boolean = false;
  registrationFee: number = 0;
  payouts: number[] = [];

  constructor(private _snackBar: MatSnackBar) { }

  ngOnInit(): void {
  }

  addPayout(): void {
    this.payouts.push(-1);
  }

  deletePayout(): void {
    if (this.payouts.length <= 1) {
      this._snackBar.open("At least one payout position required", "Close")
    }
    this.payouts.pop();
  }

}
