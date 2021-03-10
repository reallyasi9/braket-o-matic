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
  @Input() column: number = 0;
  @Input() row: number = 0;
  @Input() round: number = 0;
  @Input() number: number = 0;

  constructor() {
    this.game = generateGame(0, null, null);
  }

  ngOnInit(): void {}

  // Return CSS style definition for a given team.
  teamStyle(i: number) {
    var team = i == 0 ? this.game.topTeam : this.game.bottomTeam;
    if (!team) {
      return {
        color: '#bbbbbb',
      };
    }
    if (i == this.game.winner) {
      return {
        fontWeight: 900,
        backgroundColor: team.primaryColor,
        color: team.accentColor,
      };
    } else if (this.game.winner >= 0) {
      return {
        color: '#bbbbbb',
      };
    } else {
      return {};
    }
  }

  // Return CSS style definition for a given team.
  scoreStyle(i: number) {
    var team = i == 0 ? this.game.topTeam : this.game.bottomTeam;
    if (!team) {
      return {
        color: '#bbbbbb',
      };
    }
    if (i == this.game.winner) {
      return {
        fontWeight: 900,
      };
    } else if (0 <= this.game.winner) {
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
      return formatDate(this.game.startDate, 'MMM d @ h:mm a', 'en-US');
    } else if (this.game.clockSeconds > 0) {
      var min = Math.floor(this.game.clockSeconds / 60).toString();
      var sec = this.game.clockSeconds % 60;
      return `${min}:${sec.toString().padStart(2, '0')} ${this.game.period}`;
    } else {
      return this.game.period;
    }
  }

}
