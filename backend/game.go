package braket

// Game represents a matchup between two teams in the tournament.
type Game struct {
	Teams           [2]Team
	Winner          Team
	WinnerTopBottom int
	Game            int
	Round           int
	GameInRound     int
}

func selectedGames(sel int64) [nGamesTotal]Game {
	teams := make([]Team, len(teamList))
	copy(teams, teamList[:])

	games := [nGamesTotal]Game{}
	i := 0
	for iRound := 0; iRound < nRounds-1; iRound++ {
		for iGame := 0; iGame < nGames[iRound]; iGame++ {
			t1 := teams[2*iGame]
			t2 := teams[2*iGame+1]
			wtb := sel & 1
			ta := [2]Team{t1, t2}
			tw := ta[wtb]
			games[i] = Game{ta, tw, int(wtb), i, iRound, iGame}

			i++
			sel >>= 1
			teams[iGame] = teams[2*iGame+1]
		}
	}

	return games
}
