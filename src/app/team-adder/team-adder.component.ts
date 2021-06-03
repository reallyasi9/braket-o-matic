import { Component, Input, OnInit, Output, EventEmitter } from '@angular/core';
import { generateTeam, randomColors } from '../mock-teams';

export class Team {
  name: string;
  seed: number | undefined;
  primaryColor: string;
  accentColor: string;

  constructor() {
    const colors = randomColors();
    this.name = 'unnamed team';
    this.primaryColor = colors[0];
    this.accentColor = colors[1];
  }

  nameWithSeed(): string {
    var n = this.name;
    if (!!this.seed) {
      n = '#' + this.seed.toString() + ' ' + n;
    }
    return n;
  }
}

@Component({
  selector: 'app-team-adder',
  templateUrl: './team-adder.component.html',
  styleUrls: ['./team-adder.component.css'],
})
export class TeamAdderComponent implements OnInit {
  @Input() team: Team | undefined;
  @Input() deletable: boolean = false;
  // @Output() teamChange = new EventEmitter<Team>();
  @Output() deleteRequest = new EventEmitter<Team>();
  @Output() addRequest = new EventEmitter();

  constructor() {}

  ngOnInit(): void {}

  delete() {
    !!this.team && this.deletable && this.deleteRequest.emit(this.team);
  }

  add() {
    !this.team && this.addRequest.emit();
  }
}
