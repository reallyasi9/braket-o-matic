import { formatDate } from '@angular/common';
import { Component, Input, OnInit } from '@angular/core';
import { Game } from '../game';
import { Team } from '../team';

@Component({
  selector: 'app-game',
  templateUrl: './game.component.html',
  styleUrls: ['./game.component.scss'],
})
export class GameComponent implements OnInit {
  @Input() game: Game;

  constructor() {
    this.game = {
      id: -1,
      teams: [
        {
          id: -1,
          name: 'team1',
          primaryColor: 'black',
          accentColor: 'white',
        },
        {
          id: -2,
          name: 'team2',
          primaryColor: 'blue',
          accentColor: 'yellow',
        },
      ],
      startDate: new Date(),
      winner: -1,
      scores: [0, 0],
      clockSeconds: 0,
      period: "Pregame",
    };
  }

  ngOnInit(): void {}

  // Return CSS style definition for a given team.
  teamStyle(i: number) {
    var team: Team = this.game.teams[i];
    if (i == this.game.winner) {
      return {
        fontWeight: 900,
        backgroundColor: team?.primaryColor,
        color: team?.accentColor,
      };
    } else if (this.game?.winner >= 0) {
      return {
        color: '#bbbbbb',
      };
    } else {
      return {};
    }
  }

  // Return CSS style definition for a given team.
  scoreStyle(i: number) {
    var team: Team = this.game.teams[i];
    if (i == this.game.winner) {
      return {
        fontWeight: 900,
      };
    } else if (0 <= this.game?.winner) {
      return {
        color: '#bbbbbb',
      };
    } else {
      return {};
    }
  }

  // Return the start time of the game or the game clock.
  gameClock(): string {
    var now = new Date();
    if (this.game.startDate > now) {
      return formatDate(this.game.startDate, "MMM d @ h:mm a", "en-US");
    } else if (this.game.clockSeconds > 0) {
      var min = Math.floor(this.game.clockSeconds / 60).toString();
      var sec = this.game.clockSeconds % 60;
      return `${min}:${sec.toString().padStart(2, '0')} ${this.game.period}`;
    } else {
      return this.game.period;
    }
  }
}
