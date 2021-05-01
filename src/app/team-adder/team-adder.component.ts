import { Component, Input, OnInit, Output, EventEmitter } from '@angular/core';
import { generateTeam } from '../mock-teams';
import { Team } from '../team';

@Component({
  selector: 'app-team-adder',
  templateUrl: './team-adder.component.html',
  styleUrls: ['./team-adder.component.css']
})
export class TeamAdderComponent implements OnInit {
  @Input() team: Team | undefined;
  // @Output() teamChange = new EventEmitter<Team>();
  @Output() deleteRequest = new EventEmitter<Team>();
  @Output() addRequest = new EventEmitter();

  constructor() { }

  ngOnInit(): void {
  }

  delete() {
    !!this.team && this.deleteRequest.emit(this.team);
  }

  add() {
    !this.team && this.addRequest.emit();
  }

}
