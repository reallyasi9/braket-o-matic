import { Component, Input, OnInit } from '@angular/core';
import { Team } from '../team';

@Component({
  selector: 'app-team-editor',
  templateUrl: './team-editor.component.html',
  styleUrls: ['./team-editor.component.css'],
})
export class TeamEditorComponent implements OnInit {
  @Input() team? : Team;

  constructor() {}

  ngOnInit(): void {
  }

}
