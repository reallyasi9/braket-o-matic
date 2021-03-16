import { Injectable } from '@angular/core';
import { AngularFirestore, AngularFirestoreCollection } from '@angular/fire/firestore';
import { Observable, of } from 'rxjs';
import { generateTeam } from './mock-teams';
import { Team } from './team';

@Injectable({
  providedIn: 'root'
})
export class TeamService {

  private teamCollection?: AngularFirestoreCollection<Team>;
  private tournamentId?: string;

  constructor(private firestore: AngularFirestore) { }

  private cacheTeams(tournamentId: string) {
    this.tournamentId = tournamentId;
    this.teamCollection = this.firestore.collection<Team>(`teams/${tournamentId}`);
  }

  getTeams(tournamentId: string) : Observable<Team[]> {
    if (!this.tournamentId || this.tournamentId != tournamentId || !this.teamCollection) {
      this.cacheTeams(tournamentId);
    }
    if (!this.teamCollection) {
      console.error(`teams for tournamentId ${tournamentId} not found`);
      const teams : Team[] = [];
      return of(teams);
    }
    return this.teamCollection.valueChanges({idField: "id"});
  }

  updateTeam(team : Team) {
    if (!this.tournamentId || !this.teamCollection) {
      console.error(`team cannot be updated without a collection cached`);
      return;
    }
    const teamDoc = this.teamCollection.doc(team.id);
    teamDoc.update(team);
  }

  generateTeam(id?: string) {
    if (!this.tournamentId || !this.teamCollection) {
      console.error(`team cannot be generated without a collection cached`);
      return;
    }
    const team = generateTeam(id);
    this.teamCollection.add(team);
  }
}
