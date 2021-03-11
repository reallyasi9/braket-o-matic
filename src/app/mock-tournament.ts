import { Game } from './game';
import { generateGame } from './mock-games';
import { generateTeam } from './mock-teams';
import { Team } from './team';
import { Tournament } from './tournament';

// Generate a fake 64-team, 6-round, single-elimination tournament
// that starts now and is not yet complete.
export function generateTournament(id: string): Tournament {
  var games: { [id: string]: Game } = {};
  var cartilage: { [from: string]: { to: string; bottom: boolean } } = {};
  var gridLocation: { [id: string]: { col: number; row: number } } = {};
  const gamesPerRound = [32, 16, 8, 4, 2, 1];
  var iGame = 0;
  for (let round = 0; round < gamesPerRound.length; round++) {
    const nGames = gamesPerRound[round];
    for (let i = 0; i < nGames; i++) {
      if (round == 0) {
        games[iGame.toString()] = generateGame(
          iGame.toString(),
          generateTeam(iGame * 2),
          generateTeam(iGame * 2 + 1)
        );
      } else {
        games[iGame] = generateGame(iGame.toString());
      }

      const nextGame = nGames + Math.floor(i / 2);
      const bottom = i % 2 == 1;
      cartilage[iGame] = { to: nextGame.toString(), bottom: bottom };

      const col = round >= 4 ? 4 : i >= nGames / 2 ? 8 - round : round;
      const rowOffset = round >= 4 ? 2 ** (round - 1) - 1 : 2 ** round - 1;
      const rowSpan = round >= 4 ? 2 ** round : 2 ** (round + 1);
      // const rowCap = round >= 4 ? Infinity : nGames;
      const row = rowOffset + ((i * rowSpan) % gamesPerRound[0]);
      gridLocation[iGame] = { col: col, row: row };

      iGame++;
    }
  }

  return {
    id: id.toString(),
    startDate: new Date(),
    complete: false,
    games: games,
    cartilage: cartilage,
    gridLocation: gridLocation,
  };
}
