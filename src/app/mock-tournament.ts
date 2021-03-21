import { Tournament } from './tournament';

// Generate a compact n-team single-elimination tournament
// that starts now and is not yet complete.
// The tournament is compact in that no play-in rounds are generated.
// This means only a number of games equal to sum(i**2) for i from 0 to some n
// are used in the tournament and all others supplied in gameIds are ignored.
export function generateTournament(id: string, gameIds: string[]): Tournament {
  const gamesPerRound: number[] = [];
  for (
    let nGames = 1, sumGames = 0;
    sumGames < gameIds.length;
    sumGames += nGames, nGames *= 2
  ) {
    gamesPerRound.push(nGames);
  }
  gamesPerRound.reverse();
  const nRounds = gamesPerRound.length;

  const cumulativeGamesPerRound: number[] = [];
  for (let round = 0, sumGames = 0; round < nRounds; round++) {
    sumGames += gamesPerRound[round];
    cumulativeGamesPerRound.push(sumGames);
  }

  const cartilage: { [from: string]: { to: string; bottom: boolean } } = {};
  const gridLocation: { [id: string]: { col: number; row: number } } = {};
  const nCols = nRounds <= 2 ? 1 : 1 + 2 * (nRounds - 2); // number of columns in display, remembering that the middle column contains 2 rounds
  const nRows = gamesPerRound[0]; // number of rows in display, remembering that each game takes up 2 rows

  for (let round = 0, iGame = 0; round < gamesPerRound.length; round++) {
    const nGames = gamesPerRound[round];
    for (let i = 0; i < nGames; i++, iGame++) {
      const nextGame = cumulativeGamesPerRound[round] + Math.floor(i / 2);
      const bottom = i % 2 == 1;
      if (iGame < cumulativeGamesPerRound[cumulativeGamesPerRound.length - 2]) {
        cartilage[gameIds[iGame]] = { to: gameIds[nextGame], bottom: bottom };
      }

      const col =
        round >= nRounds - 2
          ? Math.ceil(nCols / 2)
          : i >= nGames / 2
          ? nCols - round
          : round;
      const rowOffset =
        round >= nRounds - 2 ? 2 ** (round - 1) - 1 : 2 ** round - 1;
      const rowSpan = round >= nRounds - 2 ? 2 ** round : 2 ** (round + 1);
      // const rowCap = round >= 4 ? Infinity : nGames;
      const row = rowOffset + ((i * rowSpan) % gamesPerRound[0]);
      gridLocation[iGame] = { col: col, row: row };
    }
  }

  return {
    id: id.toString(),
    startDate: new Date(),
    complete: false,
    cartilage: cartilage,
    gridLocation: gridLocation,
  };
}
