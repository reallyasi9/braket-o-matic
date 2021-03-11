import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Team } from '../team';

@Component({
  selector: 'app-pickem-team',
  templateUrl: './pickem-team.component.html',
  styleUrls: ['./pickem-team.component.css'],
})
export class PickemTeamComponent implements OnInit {
  @Input() team?: Team;
  @Input() picked: boolean = false;
  @Output() pickedChange = new EventEmitter<Team|undefined>();

  constructor() {}

  ngOnInit(): void {}

  togglePick() {
    if (!this.team) {
      console.log(`Cannot pick a null team`);
      this.picked = false;
    } else {
      this.picked = !this.picked;
      console.log(`Pick toggled to ${this.picked}`);
      this.pickedChange.emit(this.picked ? this.team : undefined);
    }
  }
}
