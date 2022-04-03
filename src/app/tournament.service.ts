import { Injectable } from '@angular/core';
import {
  AngularFirestore,
  AngularFirestoreCollection,
  AngularFirestoreDocument,
  DocumentReference,
} from '@angular/fire/compat/firestore';
import { from, Observable, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { handleError } from './error-handling';
import { Game } from './game';
import { generateGame } from './mock-games';
import { generateTeam } from './mock-teams';
import { generateTournament } from './mock-tournament';
import { Team } from './team';
import { Tournament } from './tournament';

const TOURNAMENT_KEY: string = 'tournaments';
const GAME_KEY: string = 'games';
const TEAM_KEY: string = 'teams';

@Injectable({
  providedIn: 'root',
})
export class TournamentService {
  private tournamentCollection: AngularFirestoreCollection<Tournament>;
  private activeTournament: AngularFirestoreDocument<Tournament>;

  constructor(private firestore: AngularFirestore) {
    this.tournamentCollection = this.firestore.collection<Tournament>(
      TOURNAMENT_KEY
    );
    this.activeTournament = this.tournamentCollection.doc(environment.tournamentId);
  }

  getTournaments(): Observable<Tournament[]> {
    return this.tournamentCollection.valueChanges({ idField: 'id' });
  }

  getActiveTournament(): Observable<Tournament | undefined> {
    return this.activeTournament.valueChanges();
  }

  addTournament(tournament: Tournament): void {
    const id = this.firestore.createId();
    tournament.id = id;
    from(this.tournamentCollection.add(tournament)).pipe(catchError(handleError('addTournament'))).subscribe();
  }

  deleteTournament(tournament: Tournament | string): void {
    const id = typeof tournament == 'string' ? tournament : tournament.id;
    from(this.firestore.doc<Tournament>(`${TOURNAMENT_KEY}/${id}`).delete())
      .pipe(catchError(handleError('deleteTournament')))
      .subscribe();
  }

  activateTournament(tournament: Tournament | string): void {
    const id = typeof tournament == 'string' ? tournament : tournament.id;
    this.activeTournament = this.tournamentCollection.doc(id);
  }
}
