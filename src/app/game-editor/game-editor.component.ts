import { Component, Input, OnInit } from '@angular/core';
import { Game } from '../game';
import { Team } from '../team';

@Component({
  selector: 'app-game-editor',
  templateUrl: './game-editor.component.html',
  styleUrls: ['./game-editor.component.css']
})
export class GameEditorComponent implements OnInit {

  @Input() game? : Game;
  @Input() teams : Team[] = [];
  @Input() numRounds : number = 0;

  constructor() { }

  ngOnInit(): void {
  }

}
