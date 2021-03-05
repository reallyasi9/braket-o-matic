import { Component, Input, OnInit } from '@angular/core';
import { Game } from '../game';
import { Team } from '../team';

@Component({
  selector: 'app-game',
  templateUrl: './game.component.html',
  styleUrls: ['./game.component.css'],
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
}
