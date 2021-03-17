import { Injectable } from '@angular/core';
import {
  AngularFirestore,
  AngularFirestoreDocument,
  DocumentReference,
} from '@angular/fire/firestore';
import { from, Observable, of } from 'rxjs';
import { environment } from 'src/environments/environment';
import { generateTournament } from './mock-tournament';
import { Tournament } from './tournament';

const TOURNAMENT_KEY: string = 'tournaments';

@Injectable({
  providedIn: 'root',
})
export class TournamentService {
  private tournamentDoc: AngularFirestoreDocument<Tournament>;
  tournament: Observable<Tournament|undefined>;

  constructor(private firestore: AngularFirestore) {
    const id = environment.tournamentId;
    this.tournamentDoc = firestore.doc<Tournament>(
      `${TOURNAMENT_KEY}/${id}`
    );
    this.tournamentDoc.ref.get().then(
      (snapshot) => {
        if (!snapshot.exists) {
          const randomTournament = generateTournament(id);
          this.tournamentDoc.set(randomTournament);
        }
      }
    )
    this.tournament = this.tournamentDoc.valueChanges();
  }
}
