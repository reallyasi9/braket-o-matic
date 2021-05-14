import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Game } from '../game-adder/game-adder.component';
import { randomColors, randomTeamName } from '../mock-teams';
import { Team } from '../team-adder/team-adder.component';
import { Tournament } from '../tournament';

interface GridLocation {
  game: Game;
  row: number;
  col: number;
}

function generateTeam(): Team {
  const colors = randomColors();
  return {
    name: randomTeamName(),
    seed: undefined,
    primaryColor: colors[0],
    accentColor: colors[1],
  };
}
@Component({
  selector: 'app-tournament-stepper',
  templateUrl: './tournament-stepper.component.html',
  styleUrls: ['./tournament-stepper.component.scss'],
})
export class TournamentStepperComponent implements OnInit {
  tournament: Tournament = {
    id: '',
    name: '',
    startDate: new Date(),
    complete: false,
    roundValues: [1],
    payouts: [-1],
    cartilage: {},
    gridLocation: {},
  };
  teams: Team[] = [];
  teamsRemaining: Team[] = [];
  games: Game[] = [];
  positions: GridLocation[] = [];

  constructor(private _snackBar: MatSnackBar) {}

  ngOnInit(): void {
    const topTeam = generateTeam();
    const bottomTeam = generateTeam();
    this.teams = [topTeam, bottomTeam]; // two teams
    this.teamsRemaining = [topTeam, bottomTeam];
    const game: Game = {
      topTeam: undefined,
      bottomTeam: undefined,
      topPlayInGame: undefined,
      bottomPlayInGame: undefined,
    };
    this.games = [game];
  }

  addTeam(): void {
    const team = generateTeam();
    this.teams.push(team);
    this.teamsRemaining.push(team);
  }

  deleteTeam(team: Team): void {
    if (this.teams.length <= 2) {
      this._snackBar.open('Tournaments must have at least two teams.', 'Close');
      return;
    }
    this.teams = this.teams.filter((t) => t !== team);
    this.teamsRemaining = this.teamsRemaining.filter((t) => t !== team);
  }

  addGame(from: Game, bottom: boolean): void {
    if (this.noMoreGames()) {
      this._snackBar.open('Add more teams before adding a play-in game.', 'Close');
      return;
    }
    var insertIndex = this.games.indexOf(from);
    if (insertIndex < 0) {
      this._snackBar.open('Error finding play-in target.', 'Close')
      return;
    }
    const game: Game = {
      topTeam: undefined,
      bottomTeam: undefined,
      topPlayInGame: undefined,
      bottomPlayInGame: undefined,
    };
    if (bottom) {
      from.bottomPlayInGame = game;
    } else {
      from.topPlayInGame = game;
    }
    if (!bottom) {
      insertIndex -= 1;
    }
    this.games.splice(insertIndex, 0, game);
  }

  // selectTeam(team: Team): void {
  //   this.teamsRemaining = this.teamsRemaining.filter((t) => t !== team);
  // }

  deleteGame(game: Game): void {
    this.games = this.games.filter((g) => g !== game);
  }

  noMoreGames(): boolean {
    return this.games.length >= this.teams.length - 1;
  }

  saveAndActivate() {
    console.log(this.tournament);
    console.log(this.teams);
    console.log(this.games);
    console.log(this.positions);
  }
}
