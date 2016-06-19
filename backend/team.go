package braket

import (
	"math"
)

// Team represents a single team in the tournament.
type Team struct {
	ID   int
	Name string
	Seed int
	Elo  float64
}

// eloK is the maximum amount Elo can change per game.
const eloK float64 = 65

// eloS the number of "points" available per matchup.  This might be fun to play
// with later if I want to look at each posession as a matchup and predict off
// of that, but for now, this is just the number of games played per game (1).
const eloS float64 = 1

// updateElo uses the Elo system to update two team's Elo scores after a
// head-to-head matchup.
func updateElo(ra float64, rb float64) (float64, float64) {
	qa := math.Pow(10, ra/400.)
	qb := math.Pow(10, rb/400.)
	ea := qa / (qa + qb)
	return ra + eloK*(eloS-ea), rb + eloK*(ea-eloS)
}
