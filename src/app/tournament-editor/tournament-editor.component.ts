import { Component, OnInit } from '@angular/core';
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
  tournaments?: Observable<Tournament[]>;
  activeTournament: Observable<Tournament | undefined> = new Observable<undefined>();

  constructor(private ts: TournamentService) {}

  ngOnInit(): void {
    this.tournaments = this.ts
      .getTournaments()
      .pipe(catchError(handleError<Tournament[]>('init', [])));
    this.activeTournament = this.ts
      .getActiveTournament()
      .pipe(catchError(handleError<undefined>('init active')));
  }

  addTournament(): void {
    const newTournament: Tournament = {
      id: '',
      name: '',
      startDate: new Date(),
      complete: false,
      cartilage: {},
      gridLocation: {},
    };
    this.ts.addTournament(newTournament);
  }

  deleteTournament(tournament: Tournament): void {
    this.ts.deleteTournament(tournament);
  }

  activateTournament(tournament: Tournament): void {
    this.ts.activateTournament(tournament);
  }
}
