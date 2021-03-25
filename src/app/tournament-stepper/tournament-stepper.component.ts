import { Component, OnInit } from '@angular/core';
import { generateTeam } from '../mock-teams';
import { Team } from '../team';

@Component({
  selector: 'app-tournament-stepper',
  templateUrl: './tournament-stepper.component.html',
  styleUrls: ['./tournament-stepper.component.css']
})
export class TournamentStepperComponent implements OnInit {

  roundValues : number[] = [];
  teams : Team[] = [];

  constructor() { }

  ngOnInit(): void {
  }

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
    if (this.noMoreTeams()) {
      console.error(`A ${this.roundValues.length}-round tournament can only have up to ${2**this.roundValues.length} teams`);
      return;
    }
    const team = generateTeam(this.teams.length.toString());
    this.teams.push(team);
  }

  deleteTeam(team : Team): void {
    this.teams = this.teams.filter(t => t !== team);
  }

  noMoreTeams(): boolean {
    return this.roundValues.length == 0 || this.teams.length >= 2**this.roundValues.length;
  }

}
