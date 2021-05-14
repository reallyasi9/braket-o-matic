import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Game } from '../game';
import { generateGame } from '../mock-games';
import { randomColors, randomTeamName } from '../mock-teams';
import { Team } from '../team-adder/team-adder.component';
import { Tournament } from '../tournament';

interface TournamentStepperCartilage {
  from: string;
  to: string;
  bottom: boolean;
}

interface GridLocation {
  game: string;
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
  cartilage: TournamentStepperCartilage[] = [];
  posisionts: GridLocation[] = [];

  constructor(private _snackBar: MatSnackBar) {}

  ngOnInit(): void {
    const topTeam = generateTeam();
    const bottomTeam = generateTeam();
    this.teams = [topTeam, bottomTeam]; // two teams
    this.teamsRemaining = [topTeam, bottomTeam];
    const game: Game = {
      id: '1',
      round: 0,
      clockSeconds: 20 * 60,
      period: 'Pregame',
      topScore: 0,
      bottomScore: 0,
    };
    this.games = [game];
  }

  addRound(): void {
    const value = 2 ** this.tournament.roundValues.length;
    this.tournament.roundValues.push(value);
  }

  deleteRound(): void {
    if (this.tournament.roundValues.length > 1) {
      this.tournament.roundValues.pop();
    }
  }

  addTeam(): void {
    const team = generateTeam();
    this.teams.push(team);
    this.teamsRemaining.push(team);
  }

  deleteTeam(team: Team): void {
    if (this.teams.length <= 2) {
      this._snackBar.open("Tournaments must have at least two teams.", "Close");
      return;
    }
    this.teams = this.teams.filter((t) => t !== team);
    this.teamsRemaining = this.teamsRemaining.filter((t) => t !== team);
  }

  noMoreTeams(): boolean {
    return (
      this.tournament.roundValues.length == 0 ||
      this.teams.length >= 2 ** this.tournament.roundValues.length
    );
  }

  addGame(): void {
    const id =
      this.games.reduce(
        (max, game) => (parseInt(game.id) > max ? parseInt(game.id) : max),
        -1
      ) + 1;
    const game = {
      id: id.toString(),
      round: 0,
      clockSeconds: 1200,
      period: 'Pregame',
      topScore: 0,
      bottomScore: 0,
    };
    this.games.push(game);
  }

  deleteGame(game: Game): void {
    this.cartilage = this.cartilage.filter(
      (c) => c.from !== game.id && c.to !== game.id
    );
    this.games = this.games.filter((g) => g !== game);
  }

  noMoreGames(): boolean {
    return (
      this.tournament.roundValues.length == 0 ||
      this.teams.length == 0 ||
      this.games.length >= this.teams.length - 1
    );
  }

  makeFirestoreID(id: string): string {
    const firestoreID = id.replace(/\W+/, '-');
    return firestoreID;
  }

  connectGames(from: Game | string, to: Game | string, bottom: boolean) {
    if (typeof from != 'string') {
      from = from.id;
    }
    if (typeof to != 'string') {
      to = to.id;
    }
    if (from == 'none') {
      this.cartilage = this.cartilage.filter((c) => c.to !== to);
    } else {
      this.cartilage.push({ from: from, to: to, bottom: bottom });
    }
  }

  getPlayInGame(to: Game, bottom: boolean): Game | string {
    const result = this.cartilage.find(
      (c) => c.to == to.id && c.bottom == bottom
    );
    if (!result) {
      return 'none';
    }
    const game = this.games.find((g) => g.id == result.from);
    if (!game) {
      return 'none';
    }
    return game;
  }

  saveAndActivate() {
    console.log(this.tournament);
    console.log(this.teams);
    console.log(this.games);
    console.log(this.cartilage);
    console.log(this.posisionts);
  }
}
