import { Game } from './game';
import { generateGame } from './mock-games';
import { generateTeam } from './mock-teams';
import { Team } from './team';
import { Tournament } from './tournament';

export function generateTournament(id: number): Tournament {
  var games: Game[] = [];
  var teams: Team[] = [];
  for (let i = 0; i < 64; i++) {
    teams.push(generateTeam(i));
  }
  for (let i = 0; i < 32; i++) {
    games.push(generateGame(i, teams[i * 2], teams[i * 2 + 1]));
  }
  for (let i = 33; i < 63; i++) {
    games.push(generateGame(i, null, null));
  }

  return {
    id: id,
    startDate: new Date(),
    complete: false,
    games: games,
  };
}
