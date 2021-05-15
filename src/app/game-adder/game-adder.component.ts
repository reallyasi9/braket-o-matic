import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Team } from '../team-adder/team-adder.component';
import { sortBy } from '../utilities';

export interface Game {
  topTeam: Team | undefined;
  bottomTeam: Team | undefined;
  topPlayInGame: Game | undefined;
  bottomPlayInGame: Game | undefined;
  round: number;
  gameInRound: number;
}

@Component({
  selector: 'app-game-adder',
  templateUrl: './game-adder.component.html',
  styleUrls: ['./game-adder.component.css']
})
export class GameAdderComponent implements OnInit {

  @Input() game: Game;
  @Input() championship: boolean = false;
  @Input() teams: Team[] = [];
  @Output() deleteRequest = new EventEmitter<Game>();
  @Output() addRequest = new EventEmitter<boolean>();

  constructor() {
    this.game = {
      topTeam: undefined,
      bottomTeam: undefined,
      topPlayInGame: undefined,
      bottomPlayInGame: undefined,
      round: 0,
      gameInRound: 0,
    }
  }

  ngOnInit(): void {
  }

  delete() {
    !this.championship && this.deleteRequest.emit(this.game);
  }

  add(bottom: boolean) {
    this.teams.length >= 2 && this.addRequest.emit(bottom);
  }

}
