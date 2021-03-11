import { formatDate } from '@angular/common';
import { Component, Input, OnInit } from '@angular/core';
import { Game } from '../game';
import { generateGame } from '../mock-games';
import { Team } from '../team';

@Component({
  selector: 'app-game',
  templateUrl: './game.component.html',
  styleUrls: ['./game.component.scss'],
})
export class GameComponent implements OnInit {
  @Input() game: Game;

  constructor() {
    this.game = generateGame(0);
  }

  ngOnInit(): void {}

  // Return CSS style definition for a given team.
  teamStyle(i: number) {
    const team = i == 0 ? this.game.topTeam : this.game.bottomTeam;
    if (!team) {
      return {
        color: '#bbbbbb',
      };
    }
    const winner = this.game.winner;
    if (!winner) {
      return {};
    }
    if (i == winner) {
      return {
        fontWeight: 900,
        backgroundColor: team.primaryColor,
        color: team.accentColor,
      };
    } else {
      return {
        color: '#bbbbbb',
      };
    }
  }

  // Return CSS style definition for a given team.
  scoreStyle(i: number) {
    const team = i == 0 ? this.game.topTeam : this.game.bottomTeam;
    if (!team) {
      return {
        color: '#bbbbbb',
      };
    }
    const winner = this.game.winner;
    if (!winner) {
      return {};
    }
    if (i == winner) {
      return {
        fontWeight: 900,
      };
    } else {
      return {
        color: '#bbbbbb',
      };
    }
  }

  // Return the start time of the game or the game clock.
  gameClock(): string {
    const startDate = this.game.startDate;
    if (!startDate) {
      return this.game.period;
    }
    const now = new Date();
    if (startDate > now) {
      return formatDate(startDate, 'MMM d @ h:mm a', 'en-US');
    } else if (this.game.clockSeconds > 0) {
      var min = Math.floor(this.game.clockSeconds / 60).toString();
      var sec = this.game.clockSeconds % 60;
      return `${min}:${sec.toString().padStart(2, '0')} ${this.game.period}`;
    } else {
      return this.game.period;
    }
  }
}
