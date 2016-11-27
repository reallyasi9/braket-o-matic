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
	ID              int64          `datastore:"-" goon:"id" json:"id"`
	Seed            int64          `json:"seed"`
	Elo             float64        `json:"elo"`
	SchoolName      string         `json:"schoolName"`
	SchoolShortName string         `json:"schoolShortName"`
	Nickname        string         `json:"nickname"`
	Colors          []string       `json:"colors"`
	ImageName       string         `json:"imageName"`
	Tournament      *datastore.Key `datastore:"-" goon:"parent" json:"-"`
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
	http.HandleFunc("/backend/admin/build-teams", buildTeams)
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
	// Note: range makes a copy, so have to use the index.
	for i := range teams {
		teams[i].Tournament = tournamentKey
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

	sorted := r.FormValue("sorted")

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
	if len(sorted) > 0 {
		q = q.Order("SchoolShortName")
	}
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
	s := strings.ToLower(r.FormValue("s"))
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

		// Only return 5 (for brevity)
		if found >= 5 {
			break
		}

		// First up:  match beginning of school name
		check := strings.ToLower(team.SchoolName)
		if strings.HasPrefix(check, s) {
			schoolBegTeams = append(schoolBegTeams, team)
			found++
			continue
		} else if strings.Contains(check, s) {
			schoolAnyTeams = append(schoolAnyTeams, team)
			found++
			continue
		}

		// Match beginning of short school name
		check = strings.ToLower(team.SchoolShortName)
		if strings.HasPrefix(check, s) {
			shortBegTeams = append(shortBegTeams, team)
			found++
			continue
		} else if strings.Contains(check, s) {
			shortAnyTeams = append(shortAnyTeams, team)
			found++
			continue
		}

		// Match beginning of team nickname
		check = strings.ToLower(team.Nickname)
		if strings.HasPrefix(check, s) {
			nickBegTeams = append(nickBegTeams, team)
			found++
			continue
		} else if strings.Contains(check, s) {
			nickAnyTeams = append(nickAnyTeams, team)
			found++
			continue
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
