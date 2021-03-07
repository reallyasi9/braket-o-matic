import { Injectable } from '@angular/core';
import { Game } from './game';
import { generateTeam } from './mock-teams';

@Injectable({
  providedIn: 'root'
})
export class GameService {

  constructor() { }

  getGames(): Game[] {
    var games: Game[] = [];
    return games;
  }
}
