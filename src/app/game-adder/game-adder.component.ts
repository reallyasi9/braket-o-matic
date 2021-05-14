import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Team } from '../team-adder/team-adder.component';

export interface Game {
  topTeam: Team | undefined;
  bottomTeam: Team | undefined;
  topPlayInGame: Game | undefined;
  bottomPlayInGame: Game | undefined;
}

@Component({
  selector: 'app-game-adder',
  templateUrl: './game-adder.component.html',
  styleUrls: ['./game-adder.component.css']
})
export class GameAdderComponent implements OnInit {

  @Input() game: Game | undefined;
  @Input() championship: boolean = false;
  @Input() teams: Team[] = [];
  @Output() deleteRequest = new EventEmitter<Game>();
  @Output() addRequest = new EventEmitter<boolean>();

  constructor() { }

  ngOnInit(): void {
  }

  delete() {
    !!this.game && !this.championship && this.deleteRequest.emit(this.game);
  }

  add(bottom: boolean) {
    !!this.game && this.teams.length >= 2 && this.addRequest.emit(bottom);
  }

}
