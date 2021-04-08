import { Component, OnInit } from '@angular/core';
import { Game } from '../game';
import { generateGame } from '../mock-games';
import { generateTeam } from '../mock-teams';
import { Team } from '../team';
import { Tournament } from '../tournament';

@Component({
  selector: 'app-tournament-stepper',
  templateUrl: './tournament-stepper.component.html',
  styleUrls: ['./tournament-stepper.component.css'],
})
export class TournamentStepperComponent implements OnInit {
  tournament: Tournament;
  teams: Team[] = [];
  games: Game[] = [];
  cartilage: { [from: string]: { to: string; bottom: boolean } } = {};

  constructor() {
    this.tournament = {
      id: "",
      name: "",
      startDate: new Date(),
      complete: false,
      roundValues: [1],
      payouts: [-1],
      cartilage: {},
      gridLocation: {},
    }
  }

  ngOnInit(): void {
    const topTeam = generateTeam("1");
    const bottomTeam = generateTeam("2");
    this.teams = [
      topTeam,
      bottomTeam,
    ]; // two teams
    const game : Game = {
      id: "1",
      round: 0,
      clockSeconds: 20*60,
      period: "Pregame",
      topScore: 0,
      bottomScore: 0,
    }
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
    const id = this.teams.reduce(
      (max, team) => (parseInt(team.id) > max ? parseInt(team.id) : max),
      -1
    ) + 1;
    const team = generateTeam(id.toString());
    this.teams.push(team);
  }

  deleteTeam(team: Team): void {
    this.teams = this.teams.filter((t) => t !== team);
  }

  noMoreTeams(): boolean {
    return (
      this.tournament.roundValues.length == 0 ||
      this.teams.length >= 2 ** this.tournament.roundValues.length
    );
  }

  addGame(): void {
    const id = this.games.reduce(
      (max, game) => (parseInt(game.id) > max ? parseInt(game.id) : max),
      -1
    ) + 1;
    const game = generateGame(id.toString());
    this.games.push(game);
  }

  deleteGame(game: Game): void {
    this.connectGames("none", game, false);
    delete this.cartilage[game.id];
    this.games = this.games.filter((g) => g !== game);
  }

  noMoreGames(): boolean {
    return (
      this.tournament.roundValues.length == 0 ||
      this.teams.length == 0 ||
      this.games.length >= this.teams.length - 1
    );
  }

  makeFirestoreID(id: string) : string {
    const firestoreID = id.replace(/\W+/, "-");
    return firestoreID;
  }

  connectGames(from: Game|string, to: Game|string, bottom: boolean) {
    console.log(to);
    if (typeof(from) != "string") {
      from = from.id;
    }
    if (typeof(to) != "string") {
      to = to.id;
    }
    if (from == "none") {
      Object.keys(this.cartilage).some(key => {
        if (this.cartilage[key].to === to) {
          delete this.cartilage[key];
          return true;
        }
        return false;
      })
    } else {
      this.cartilage[from] = {to: to, bottom: bottom};
    }
  }

}
