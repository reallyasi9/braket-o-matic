import { Component, OnInit } from '@angular/core';
import { Tournament } from '../tournament';
import { generateTournament } from '../mock-tournament';

@Component({
  selector: 'app-tournament',
  templateUrl: './tournament.component.html',
  styleUrls: ['./tournament.component.css'],
})
export class TournamentComponent implements OnInit {
  tournament: Tournament = generateTournament(2021);

  constructor() {}

  ngOnInit(): void {
    // this.tournament = generateTournament(2021);
  }
}
