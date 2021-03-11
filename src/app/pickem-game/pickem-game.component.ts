import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Game } from '../game';
import { Team } from '../team';

@Component({
  selector: 'app-pickem-game',
  templateUrl: './pickem-game.component.html',
  styleUrls: ['./pickem-game.component.css']
})
export class PickemGameComponent implements OnInit {

  @Input() topTeam?: Team;
  @Input() bottomTeam?: Team;
  @Input() pick?: Team;
  @Output() pickChange = new EventEmitter<Team|undefined>();

  constructor() { }

  ngOnInit(): void {
  }

  togglePick(team?: Team) {
    console.log(`Game received team pick ${team}`);
    this.pick = team;
    console.log(`Game emitting team pick ${team}`);
    this.pickChange.emit(this.pick);
  }

}
