import { Component, OnInit } from '@angular/core';
import { Tournament } from '../tournament';
import { Game } from '../game';
import { Team } from '../team';

const team1: Team = {
  id: 1,
  name: 'Michigan',
  primaryColor: '#00274C',
  accentColor: '#FFCB05',
};

const team2: Team = {
  id: 2,
  name: 'Ohio State',
  primaryColor: '#CE0F3D',
  accentColor: '#B0B7BC',
};

const games: Game[] = [
  {
    id: 1,
    startDate: new Date(2001, 2, 3, 4, 5, 6),
    teams: [team1, team2],
    scores: [],
    winner: -1,
    clockSeconds: 0,
    period: "Pregame",
  },
  {
    id: 2,
    startDate: new Date(2002, 3, 4, 5, 6, 7),
    teams: [team2, team1],
    scores: [6, 14],
    winner: -1,
    clockSeconds: 712,
    period: "2nd",
  },
  {
    id: 3,
    startDate: new Date(2003, 4, 5, 6, 7, 8),
    teams: [team1, team2],
    scores: [100, 0],
    winner: 0,
    clockSeconds: 0,
    period: "Final",
  },
];

const tournament: Tournament = {
  id: 2021,
  startDate: new Date(2021, 2, 3, 4, 5, 6),
  complete: false,
  games: games,
};

@Component({
  selector: 'app-tournament',
  templateUrl: './tournament.component.html',
  styleUrls: ['./tournament.component.css'],
})
export class TournamentComponent implements OnInit {
  tournament: Tournament = tournament;

  constructor() {}

  ngOnInit(): void {}
}
