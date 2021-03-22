import { Component, OnInit } from '@angular/core';
import { environment } from 'src/environments/environment';
import { Tournament } from '../tournament';
import { TournamentService } from '../tournament.service';

@Component({
  selector: 'app-tournament-editor',
  templateUrl: './tournament-editor.component.html',
  styleUrls: ['./tournament-editor.component.css'],
})
export class TournamentEditorComponent implements OnInit {
  tournaments: Tournament[] = [];
  activeTournament?: Tournament;

  constructor(private ts: TournamentService) {}

  ngOnInit(): void {
    this.ts.getTournaments().subscribe(
      (tournaments) => (this.tournaments = tournaments),
      (error) => console.error(error)
    );
  }

  addTournament(): void {
    const newTournament: Tournament = {
      id: '',
      name: '',
      active: false,
      startDate: new Date(),
      complete: false,
      cartilage: {},
      gridLocation: {},
    };
    this.tournaments.push(newTournament);
  }

  deleteTournament(tournament: Tournament): void {
    this.tournaments = this.tournaments.filter((t) => t != tournament);
  }

  activateTournament(tournament: Tournament): void {
    this.tournaments.map((t) => {
      t.active = false;
    });
    this.activeTournament = tournament;
    tournament.active = true;
  }
}
