package braket

import (
	"net/http"

	"github.com/mjibson/goon"
	"google.golang.org/appengine"
	"google.golang.org/appengine/datastore"
)

// Game represents a matchup between two teams in the tournament.
type Game struct {
	ID              int64          `datastore:"-" goon:"id" json:"id"`
	Tournament      *datastore.Key `datastore:"-" goon:"parent" json:"tournamentID"`
	Teams           []int64        `json:"teams"`
	Winner          int64          `json:"winner"`
	WinnerTopBottom int            `json:"winnerTopBottom"`
	Game            int            `json:"game"`
	Round           int            `json:"round"`
	GameInRound     int            `json:"gameInRound"`
}

func init() {
	http.HandleFunc("/backend/admin/build-games", buildGames)
}

func buildGames(w http.ResponseWriter, r *http.Request) {

	// Global goon instance
	ctx := appengine.NewContext(r)
	ds := goon.FromContext(ctx)

	q := datastore.NewQuery("Game")
	keys, _ := ds.GetAll(q, nil)
	// Type might not exist at all, in which case this would be an error
	// But I guess I don't care...

	// Wipe them all?
	err := ds.DeleteMulti(keys)
	if err != nil {
		ReturnError(w, err)
		return
	}

	// Now build them from scratch.
	tk, _, err := latestTournament(ds)
	if err != nil {
		ReturnError(w, err)
		return
	}

	games := make([]Game, nGamesTotal)
	g := 0
	for iR := 0; iR < nRounds; iR++ {
		for iG := 0; iG < nGames[iR]; iG++ {
			games[g] = Game{
				Tournament:  tk,
				Game:        g,
				Round:       iR,
				GameInRound: iG,
			}

			g++
		}
	}
	_, err = ds.PutMulti(games)
	if err != nil {
		ReturnError(w, err)
		return
	}

	return

}

// selectedGames returns a list of Games that are implied by a given selection.
func selectedGames(sel int64) [nGamesTotal]Game {
	//TODO: make teamList a parameter?
	teams := make([]Team, len(teamList))
	copy(teams, teamList[:])

	games := [nGamesTotal]Game{}
	i := 0
	for iRound := 0; iRound < nRounds-1; iRound++ {
		for iGame := 0; iGame < nGames[iRound]; iGame++ {
			t1 := &teams[2*iGame]
			t2 := &teams[2*iGame+1]
			wtb := sel & 1
			// ta := make([]*Team, 2
			ta := []int64{t1.ID, t2.ID}
			tw := ta[wtb]
			games[i] = Game{
				Teams:           ta,
				Winner:          tw,
				WinnerTopBottom: int(wtb),
				Game:            i,
				Round:           iRound,
				GameInRound:     iGame}

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
