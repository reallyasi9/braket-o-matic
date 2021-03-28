import { Component, Input, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { handleError } from '../error-handling';
import { Tournament } from '../tournament';
import { TournamentService } from '../tournament.service';

@Component({
  selector: 'app-tournament-editor',
  templateUrl: './tournament-editor.component.html',
  styleUrls: ['./tournament-editor.component.css'],
})
export class TournamentEditorComponent implements OnInit {
  @Input() tournament?: Tournament;

  constructor(private ts: TournamentService) {}

  ngOnInit(): void {
  }

}
