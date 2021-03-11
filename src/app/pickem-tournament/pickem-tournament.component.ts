import { Component, OnInit } from '@angular/core';
import { Tournament } from '../tournament';
import { TournamentService } from '../tournament.service';

@Component({
  selector: 'app-pickem-tournament',
  templateUrl: './pickem-tournament.component.html',
  styleUrls: ['./pickem-tournament.component.css']
})
export class PickemTournamentComponent implements OnInit {

  tournament?: Tournament;

  constructor(private tournamentService: TournamentService) {}

  ngOnInit(): void {
    this.tournamentService.getActiveTournament().subscribe(
      tournament => this.tournament = tournament,
      err => console.log(`error getting active tournament: ${err}`),
    )
  }

  // Return a style for the game container based on the round number.
  gridLocation(gameId: string) {
    if (!this.tournament || !(gameId in this.tournament.gridLocation)) {
      console.error(`gameID ${gameId} not in tournament grid`);
      return {};
    }
    const col = this.tournament.gridLocation[gameId].col
    const row = this.tournament.gridLocation[gameId].row
    return {
      gridColumn: `${col + 1} / span 1`,
      gridRow: `${row + 1} / span 2`,
    };
  }

}
