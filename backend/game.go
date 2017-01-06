package braket

// Game represents a matchup between two teams in the tournament.
type Game struct {
	Teams           [2]*Team
	Winner          *Team
	WinnerTopBottom int
	Game            int
	Round           int
	GameInRound     int
}

// selectedGames returns a list of Games that are implied by a given selection.
func selectedGames(sel int64) [nGamesTotal]Game {
	teams := make([]Team, len(teamList))
	copy(teams, teamList[:])

	games := [nGamesTotal]Game{}
	i := 0
	for iRound := 0; iRound < nRounds-1; iRound++ {
		for iGame := 0; iGame < nGames[iRound]; iGame++ {
			t1 := &teams[2*iGame]
			t2 := &teams[2*iGame+1]
			wtb := sel & 1
			ta := [2]*Team{t1, t2}
			tw := ta[wtb]
			games[i] = Game{ta, tw, int(wtb), i, iRound, iGame}

			i++
			sel >>= 1
			teams[iGame] = teams[2*iGame+1]
		}
	}

	return games
}

// gamesUpNext determines a mask for the games that have yet to be played, but
// for which the teams have been determined.
func gamesUpNext(played int64) int64 {

	// Anything not played in the first round is easy, and is already done
	next := ^played & roundMasks[0]

	cachedPlayed := played
	totalShift := uint64(nGames[0])
	played >>= totalShift

	// Anything not played in any successive round requires that the two games
	// that feed into it be played.
	for r := 1; r < nRounds; r++ {
		ugames := uint64(nGames[r])
		roundNext := ^played & contractAnd(cachedPlayed, ugames) & roundMasks[r]
		next |= roundNext << totalShift

		cachedPlayed = played
		played >>= ugames
		totalShift += ugames
	}

	return next
}

const beta float64 = 0.003946837612242661

// func topWinProb(g *Game) float64 {
// 	if g.Winner != nil {
// 		return float64(1 - g.WinnerTopBottom)
// 	}
//
// 	if g.Teams[0] == nil || g.Teams[1] == nil {
// 		return 0
// 	}
//
// 	delo := g.Teams[0].Elo - g.Teams[1].Elo
// 	return 1 / (1 + math.Exp(-beta*delo))
// }
