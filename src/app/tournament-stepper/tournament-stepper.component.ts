import { Component, OnInit } from '@angular/core';
import { Game } from '../game';
import { generateTeam } from '../mock-teams';
import { Team } from '../team';

@Component({
  selector: 'app-tournament-stepper',
  templateUrl: './tournament-stepper.component.html',
  styleUrls: ['./tournament-stepper.component.css'],
})
export class TournamentStepperComponent implements OnInit {
  roundValues: number[] = [];
  teams: Team[] = [];
  games: Game[] = [];
  cartilage: { [from: string]: { to: string; bottom: boolean } } = {};

  constructor() {}

  ngOnInit(): void {}

  addRound(): void {
    const value = 2 ** this.roundValues.length;
    this.roundValues.push(value);
  }

  deleteRound(): void {
    if (this.roundValues.length > 0) {
      this.roundValues.pop();
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
      this.roundValues.length == 0 ||
      this.teams.length >= 2 ** this.roundValues.length
    );
  }
}
