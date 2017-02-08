package braket

import (
	"time"

	"github.com/mjibson/goon"
	"google.golang.org/appengine/datastore"
)

// Tournament defines a single year's tournament, and is useful
// for tying together teams and games for easier queries.
type Tournament struct {
	Year      int64     `datastore:"-" goon:"id" json:"year"`
	StartTime time.Time `json:"startTime"`
	EndTime   time.Time `json:"endTime"`
}

func latestTournament(ds *goon.Goon) (*datastore.Key, *Tournament, error) {
	y := int64(time.Now().Year())
	t := &Tournament{Year: y}

	key := ds.Key(t)
	err := ds.Get(t)

	if err == datastore.ErrNoSuchEntity {
		// Make one
		t.StartTime = time.Now().AddDate(1, 0, 0)
		t.EndTime = t.StartTime.AddDate(0, 2, 0)
		key, err = ds.Put(t)
	}
	if err != nil {
		return nil, nil, err
	}

	return key, t, nil
}
