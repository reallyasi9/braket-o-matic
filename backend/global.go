package braket

import (
	"fmt"
	"net/http"

	"google.golang.org/appengine"
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

var roundMasks = [nRounds]int64{
	0xffffffff,
	0xffff,
	0xff,
	0xf,
	0x3,
	0x1,
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

func EnableCORS(w http.ResponseWriter, r *http.Request) {
	ctx := appengine.NewContext(r)

	w.Header().Set("Content-Type", "application/json")

	// Allow cross-site origin to the frontend and backend
	hn, err := appengine.ModuleHostname(ctx, "frontend", "", "")
	if err != nil {
		ReturnError(w, err)
		return
	}
	w.Header().Add("Access-Control-Allow-Origin", "http://"+hn)

	hn, err = appengine.ModuleHostname(ctx, "default", "", "")
	if err != nil {
		ReturnError(w, err)
		return
	}
	w.Header().Add("Access-Control-Allow-Origin", "http://"+hn)
}

func ReturnError(w http.ResponseWriter, err error) {
	http.Error(w, err.Error(), http.StatusInternalServerError)
}
