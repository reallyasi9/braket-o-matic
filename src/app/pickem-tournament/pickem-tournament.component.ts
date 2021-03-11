import { Component, OnInit } from '@angular/core';
import { Game } from '../game';
import { Team } from '../team';
import { Tournament } from '../tournament';
import { TournamentService } from '../tournament.service';

@Component({
  selector: 'app-pickem-tournament',
  templateUrl: './pickem-tournament.component.html',
  styleUrls: ['./pickem-tournament.component.css'],
})
export class PickemTournamentComponent implements OnInit {
  tournament?: Tournament;
  picks: { [gameId: string]: Team | undefined } = {};

  constructor(private tournamentService: TournamentService) {}

  ngOnInit(): void {
    this.tournamentService.getActiveTournament().subscribe(
      (tournament) => (this.tournament = tournament),
      (err) => console.log(`error getting active tournament: ${err}`)
    );
  }

  // Return a style for the game container based on the round number.
  gridLocation(gameId: string) {
    if (!this.tournament || !(gameId in this.tournament.gridLocation)) {
      console.error(`gameID ${gameId} not in tournament grid`);
      return {};
    }
    const col = this.tournament.gridLocation[gameId].col;
    const row = this.tournament.gridLocation[gameId].row;
    return {
      gridColumn: `${col + 1} / span 1`,
      gridRow: `${row + 1} / span 2`,
    };
  }

  updatePick(game: Game, pick?: Team) {
    console.log(`pick for game ${game.id} updated ${pick?.name}`);
    this.picks[game.id] = pick;
    this.propagatePick(game, pick);
  }

  propagatePick(game: Game, pick?: Team) {
    if (!this.tournament) {
      console.error(`cannot propogate winner of game ${game.id} to ${pick?.name} with no tournament defined!`);
      return;
    }
    const gameId = game.id;
    if (!(gameId in this.tournament.cartilage)) {
      console.log(`game ${game.id} does not propagate`);
      return;
    }
    const nextSlot = this.tournament.cartilage[gameId];
    if (!(nextSlot.to in this.tournament.games)) {
      console.error(`game ${game.id} propagates to undefined game ${nextSlot.to}!`);
      return;
    }
    const nextGame = this.tournament.games[nextSlot.to];
    const previousTeam = nextSlot.bottom ? nextGame.bottomTeam : nextGame.topTeam;
    if (nextSlot.bottom) {
      nextGame.bottomTeam = pick;
    } else {
      nextGame.topTeam = pick;
    }
    if (!!previousTeam && this.picks[nextGame.id] == previousTeam) {
      this.updatePick(nextGame, pick);
    }
  }
}
