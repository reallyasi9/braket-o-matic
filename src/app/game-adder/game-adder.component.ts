import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Team } from '../team-adder/team-adder.component';

export class Game {
  topTeam: Team | undefined;
  bottomTeam: Team | undefined;
  topPlayInGame: Game | undefined;
  bottomPlayInGame: Game | undefined;
  round: number;
  gameInRound: number;
  nextGame: Game | undefined;
  nextBottom: boolean;

  constructor() {
    this.round = 0;
    this.gameInRound = 0;
    this.nextBottom = false;
  }

  name(): string {
    if (!this.nextGame) {
      return "Championship";
    }
    return "Round " + this.round.toString() + " Game " + this.gameInRound.toString();
  }

  nextName(): string {
    if (!this.nextGame) {
      return "Final game";
    }
    const slot = this.nextBottom ? "1" : "0";
    return "Play-in for " + this.nextGame.name() + " Slot " + slot;
  }
}

@Component({
  selector: 'app-game-adder',
  templateUrl: './game-adder.component.html',
  styleUrls: ['./game-adder.component.css']
})
export class GameAdderComponent implements OnInit {

  @Input() game: Game = new Game();
  @Input() teams: Team[] = [];
  @Output() deleteRequest = new EventEmitter<Game>();
  @Output() addRequest = new EventEmitter<boolean>();

  constructor() {
  }

  ngOnInit(): void {
  }

  add(bottom: boolean): void {
    if ((bottom && !!this.game.bottomPlayInGame) || (!bottom && !!this.game.topPlayInGame)) {
      return;
    }
    this.teams.length >= 2 && this.addRequest.emit(bottom);
  }

  update(bottom: boolean): boolean {
    const deleteGames: Game[] = [];
    const game = bottom ? this.game.bottomPlayInGame : this.game.topPlayInGame;
    if (!!game) {
      const checkGames: Game[] = [game];
      var check;
      while (check = checkGames.pop()) {
        if (check.topPlayInGame) {
          checkGames.push(check.topPlayInGame);
        }
        if (check.bottomPlayInGame) {
          checkGames.push(check.bottomPlayInGame);
        }
        deleteGames.push(check);
      }
      deleteGames.forEach((g: Game) => this.deleteRequest.emit(g));
    }
    if (bottom && !!this.game.bottomPlayInGame) {
      this.game.bottomPlayInGame = undefined;
    } else if (!bottom && !!this.game.topPlayInGame) {
      this.game.topPlayInGame = undefined;
    }
    return true;
  }

}
