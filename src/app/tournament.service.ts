import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { generateTournament } from './mock-tournament';
import { Tournament } from './tournament';

@Injectable({
  providedIn: 'root',
})
export class TournamentService {
  constructor() {}

  getActiveTournament(): Observable<Tournament> {
    const year = new Date().getFullYear();
    const tournament = of(generateTournament(year.toString()));
    return tournament;
  }

  getTournament(id: string): Observable<Tournament> {
    const tournament = of(generateTournament(id));
    return tournament;
  }
}
