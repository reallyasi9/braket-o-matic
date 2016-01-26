package braket

import (
	"fmt"
)

const nRounds = 6

const nGamesTotal = 63

const nTeams = 64

const nTeamsPerRegion = 16

var nGames = [nRounds]int{
	32, 16, 8, 4, 2, 1,
}

// TODO Make this external
var roundValues = [nRounds]float32{
	1, 2, 3, 5, 7, 13,
}

var teamList = [nTeams]Team{}

var seedsList = [nTeamsPerRegion]int{
	1, 16, 8, 9, 5, 12, 4, 13, 6, 11, 3, 14, 7, 10, 2, 15,
}

func init() {
	for i := 0; i < nTeams; i++ {
		name := fmt.Sprintf("Team%d", i)
		teamList[i] = Team{i, name, seedsList[i%nTeamsPerRegion], 0.}
	}
}
