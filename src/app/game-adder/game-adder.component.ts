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

  delete() {
    !!this.game.nextGame && this.deleteRequest.emit(this.game);
  }

  add(bottom: boolean) {
    this.teams.length >= 2 && this.addRequest.emit(bottom);
  }

}
