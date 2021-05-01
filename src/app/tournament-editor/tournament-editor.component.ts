import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Tournament } from '../tournament';
import { TournamentService } from '../tournament.service';

@Component({
  selector: 'app-tournament-editor',
  templateUrl: './tournament-editor.component.html',
  styleUrls: ['./tournament-editor.component.css'],
})
export class TournamentEditorComponent implements OnInit {
  @Input() tournament?: Tournament;
  @Output() tournamentChange = new EventEmitter<Tournament>();

  constructor(private ts: TournamentService) {}

  ngOnInit(): void {
  }

  addRound(): void {
    if (!this.tournament) {
      return;
    }
    const nRounds = this.tournament.roundValues.length;
    this.tournament.roundValues.push(2**nRounds);
  }

  deleteRound(): void {
    if (!this.tournament || this.tournament.roundValues.length <= 1) {
      return;
    }
    this.tournament.roundValues.pop();
  }

  addPayout(): void {
    if (!this.tournament) {
      return;
    }
    this.tournament.payouts.push(-1);
  }

  deletePayout(): void {
    if (!this.tournament || this.tournament.payouts.length <= 1) {
      return;
    }
    this.tournament.payouts.pop();
  }

  updateDateTime(x: any) {
    console.log(x);
  }

}
