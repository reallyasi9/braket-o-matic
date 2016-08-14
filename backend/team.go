package braket

import (
	"encoding/json"
	"io/ioutil"
	"math"
	"net/http"

	"github.com/mjibson/goon"
	"google.golang.org/appengine"
	"google.golang.org/appengine/datastore"
)

// Team represents a single team in the tournament.
type Team struct {
	ID              int64 `datastore:"-" goon:"id"`
	Seed            int64
	Elo             float64
	SchoolName      string
	SchoolShortName string
	Nickname        string
	Colors          []string
	ImageName       string
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

func init() {
	http.HandleFunc("/backend/admin/build_teams", buildTeams)
	http.HandleFunc("/backend/teams", getTeams)
}

func buildTeams(w http.ResponseWriter, r *http.Request) {

	// Global goon instance
	ctx := appengine.NewContext(r)
	ds := goon.FromContext(ctx)

	// TODO
	//err := ds.RunInTransaction(deleteAndBuildTeams, &datastore.TransactionOptions{XG: true})
	q := datastore.NewQuery("Team")
	keys, err := ds.GetAll(q, nil)

	// Wipe them all!
	err = ds.DeleteMulti(keys)
	if err != nil {
		ReturnError(w, err)
		return
	}

	// Now read from json and add them back!
	teamf, err := ioutil.ReadFile("teams.json")
	if err != nil {
		ReturnError(w, err)
		return
	}

	var teams []Team
	json.Unmarshal(teamf, &teams)

	// Send the update
	_, err = ds.PutMulti(teams)
	if err != nil {
		ReturnError(w, err)
		return
	}

	// Done!
	return

}

// func deleteAndBuildTeams(ds *goon.Goon) error {
// 	// Get them all!
// 	q := datastore.NewQuery("Team")
// 	keys, err := ds.GetAll(q, nil)
//
// 	// Wipe them all!
// 	err = ds.DeleteMulti(keys)
// 	if err != nil {
// 		return err
// 	}
//
// 	// Now read from json and add them back!
// 	teamf, err := ioutil.ReadFile("teams.json")
// 	if err != nil {
// 		return err
// 	}
//
// 	var teams []Team
// 	json.Unmarshal(teamf, &teams)
//
// 	// Send the update
// 	_, err = ds.PutMulti(teams)
// 	if err != nil {
// 		return err
// 	}
//
// 	return nil
// }

func getTeams(w http.ResponseWriter, r *http.Request) {

	// Global goon instance
	ctx := appengine.NewContext(r)
	ds := goon.FromContext(ctx)

	// Get them all!
	q := datastore.NewQuery("Team")
	var teams []Team
	_, err := ds.GetAll(q, &teams)

	if err != nil {
		ReturnError(w, err)
		return
	}

	err = ds.GetMulti(teams)
	if err != nil {
		ReturnError(w, err)
		return
	}
	js, err := json.Marshal(teams)
	if err != nil {
		ReturnError(w, err)
		return
	}
	w.Write(js)
}
