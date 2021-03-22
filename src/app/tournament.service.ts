import { Injectable } from '@angular/core';
import {
  AngularFirestore,
  AngularFirestoreCollection,
  AngularFirestoreDocument,
  DocumentReference,
} from '@angular/fire/firestore';
import { from, Observable, of } from 'rxjs';
import { environment } from 'src/environments/environment';
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
  // tournaments: Tournament[] = [];
  // private tournamentDoc: AngularFirestoreDocument<Tournament>;
  // tournament: Observable<Tournament|undefined>;
  // private gamesCollection: AngularFirestoreCollection<Game>;

  constructor(private firestore: AngularFirestore) {
    this.tournamentCollection = this.firestore.collection<Tournament>(
      TOURNAMENT_KEY
    );
    // const id = environment.tournamentId;
    // this.tournamentDoc = firestore.doc<Tournament>(
    //   `${TOURNAMENT_KEY}/${id}`
    // );
    // this.tournamentDoc.ref.get().then(
    //   (snapshot) => {
    //     if (!snapshot.exists) {
    //       const randomTournament = generateTournament(id, this.generateGames());
    //       this.tournamentDoc.set(randomTournament);
    //     }
    //   }
    // );
    // this.gamesCollection = this.tournamentDoc.collection<Game>(GAME_KEY);
    // this.tournament = this.tournamentDoc.valueChanges();
  }

  getTournaments(): Observable<Tournament[]> {
    return this.tournamentCollection.valueChanges({ idField: 'id' });
  }

  // private generateGames(): string[] {
  //   const gameIds: string[] = [];
  //   for (let i = 0, team = 0; i < environment.nGames; i++) {
  //     var topTeam: DocumentReference<Team> | undefined = undefined;
  //     if (team < environment.nTeams) {
  //       const top = generateTeam(team.toString());
  //       const doc = this.firestore.doc<Team>(`${TOURNAMENT_KEY}/${environment.tournamentId}/${TEAM_KEY}/${team}`);
  //       doc.set(top);
  //       topTeam = doc.ref;
  //       team++;
  //     }
  //     var bottomTeam: DocumentReference<Team> | undefined = undefined;
  //     if (team < environment.nTeams) {
  //       const bottom = generateTeam(team.toString());
  //       const doc = this.firestore.doc<Team>(`${TOURNAMENT_KEY}/${environment.tournamentId}/${TEAM_KEY}/${team}`);
  //       doc.set(bottom);
  //       bottomTeam = doc.ref;
  //       team++;
  //     }
  //     const game = generateGame(i.toString(), topTeam, bottomTeam);
  //     const gameDoc = this.firestore.doc<Game>(`${TOURNAMENT_KEY}/${environment.tournamentId}/${GAME_KEY}/${game.id}`);
  //     gameDoc.set(game);
  //     gameIds.push(game.id);
  //   }
  //   return gameIds;
  // }
}
