package braket

import (
	"encoding/json"
	"io/ioutil"
	"math"
	"net/http"
	"strings"

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
	Tournament      *datastore.Key `datastore:"-" goon:"parent"`
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
	http.HandleFunc("/backend/search-teams", searchTeams)
}

func buildTeams(w http.ResponseWriter, r *http.Request) {

	// Global goon instance
	ctx := appengine.NewContext(r)
	ds := goon.FromContext(ctx)
	tournamentKey, _, err := latestTournament(ds)
	if err != nil {
		ReturnError(w, err)
		return
	}

	q := datastore.NewQuery("Team").Ancestor(tournamentKey)
	keys, _ := ds.GetAll(q, nil)
	// Type might not exist at all, in which case this would be an error
	// But I guess I don't care...

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

	// Add the ancestor
	for _, team := range teams {
		team.Tournament = tournamentKey
	}

	// Send the update
	_, err = ds.PutMulti(teams)
	if err != nil {
		ReturnError(w, err)
		return
	}

	// Done!
	return

}

func getTeams(w http.ResponseWriter, r *http.Request) {

	// Global goon instance
	ctx := appengine.NewContext(r)
	ds := goon.FromContext(ctx)

	tournamentKey, _, err := latestTournament(ds)
	if err != nil {
		ReturnError(w, err)
		return
	}

	// Get them all!
	q := datastore.NewQuery("Team").Ancestor(tournamentKey)
	var teams []Team
	_, err = ds.GetAll(q, &teams)

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

func searchTeams(w http.ResponseWriter, r *http.Request) {

	// Just in case, make sure the search term is reasonable
	s := r.FormValue("s")
	if len(s) < 3 {
		var fakeTeams []Team
		js, err := json.Marshal(fakeTeams) // Empty
		if err != nil {
			ReturnError(w, err)
			return
		}
		w.Write(js)
		return
	}

	// Global goon instance
	ctx := appengine.NewContext(r)
	ds := goon.FromContext(ctx)

	tournamentKey, _, err := latestTournament(ds)
	if err != nil {
		ReturnError(w, err)
		return
	}

	// Get them all!
	q := datastore.NewQuery("Team").Ancestor(tournamentKey)
	var teams []Team
	_, err = ds.GetAll(q, &teams)
	if err != nil {
		ReturnError(w, err)
		return
	}

	err = ds.GetMulti(teams)
	if err != nil {
		ReturnError(w, err)
		return
	}

	// Filter based on the search term into different priorities
	var schoolBegTeams []Team
	var shortBegTeams []Team
	var nickBegTeams []Team
	var schoolAnyTeams []Team
	var shortAnyTeams []Team
	var nickAnyTeams []Team

	found := 0
	for _, team := range teams {

		// First up:  match beginning of school name
		if strings.HasPrefix(team.SchoolName, s) {
			schoolBegTeams = append(schoolBegTeams, team)
			found++
		} else if strings.Contains(team.SchoolName, s) {
			schoolAnyTeams = append(schoolAnyTeams, team)
			found++
		}

		// Match beginning of short school name
		if strings.HasPrefix(team.SchoolShortName, s) {
			shortBegTeams = append(shortBegTeams, team)
			found++
		} else if strings.Contains(team.SchoolShortName, s) {
			schoolAnyTeams = append(shortAnyTeams, team)
			found++
		}

		// Match beginning of team nickname
		if strings.HasPrefix(team.Nickname, s) {
			nickBegTeams = append(nickBegTeams, team)
			found++
		} else if strings.Contains(team.Nickname, s) {
			schoolAnyTeams = append(nickAnyTeams, team)
			found++
		}

		// Finally, only return 5 (for brevity)
		if found >= 5 {
			break
		}
	}

	// Join in order
	returnTeams := append(schoolBegTeams, shortBegTeams...)
	returnTeams = append(returnTeams, nickBegTeams...)
	returnTeams = append(returnTeams, schoolAnyTeams...)
	returnTeams = append(returnTeams, shortAnyTeams...)
	returnTeams = append(returnTeams, nickAnyTeams...)

	js, err := json.Marshal(returnTeams)
	if err != nil {
		ReturnError(w, err)
		return
	}
	w.Write(js)
}
