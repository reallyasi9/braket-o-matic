import { Component, OnInit } from '@angular/core';
import { Tournament } from '../tournament';
import { generateTournament } from '../mock-tournament';
import { TournamentService } from '../tournament.service';

@Component({
  selector: 'app-tournament',
  templateUrl: './tournament.component.html',
  styleUrls: ['./tournament.component.css'],
})
export class TournamentComponent implements OnInit {
  tournament?: Tournament;

  constructor(private tournamentService: TournamentService) {}

  ngOnInit(): void {
    this.tournamentService.getActiveTournament().subscribe(
      tournament => this.tournament = tournament,
      err => console.error(`error getting active tournament: ${err}`)
    )
  }

  // Return a style for the game container based on the round number.
  gridLocation(gameId: string) {
    if (!this.tournament || !(gameId in this.tournament.gridLocation)) {
      console.error(`gameId ${gameId} not found in tournament grid`);
      return {};
    }
    const col = this.tournament.gridLocation[gameId].col;
    const row = this.tournament.gridLocation[gameId].row;
    return {
      gridColumn: `${col + 1} / span 1`,
      gridRow: `${row + 1} / span 2`,
    };
  }
}
