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
    const game = generateGame("1");
    this.games = [generateGame("1")];
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
    this.games = this.games.filter((g) => g !== game);
  }

  noMoreGames(): boolean {
    return (
      this.tournament.roundValues.length == 0 ||
      this.teams.length == 0 ||
      this.games.length >= this.teams.length - 1
    );
  }
}
