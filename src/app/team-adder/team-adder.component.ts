import { Component, Input, OnInit, Output, EventEmitter } from '@angular/core';
import { generateTeam } from '../mock-teams';

export interface Team {
  name: string;
  seed: number | undefined;
  primaryColor: string;
  accentColor: string;
}
@Component({
  selector: 'app-team-adder',
  templateUrl: './team-adder.component.html',
  styleUrls: ['./team-adder.component.css']
})
export class TeamAdderComponent implements OnInit {
  @Input() team: Team | undefined;
  @Input() deletable: boolean = false;
  // @Output() teamChange = new EventEmitter<Team>();
  @Output() deleteRequest = new EventEmitter<Team>();
  @Output() addRequest = new EventEmitter();

  constructor() { }

  ngOnInit(): void {
  }

  delete() {
    !!this.team && this.deletable && this.deleteRequest.emit(this.team);
  }

  add() {
    !this.team && this.addRequest.emit();
  }

}
