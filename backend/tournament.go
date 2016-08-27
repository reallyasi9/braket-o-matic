package braket

import (
	"time"

	"github.com/mjibson/goon"
	"google.golang.org/appengine/datastore"
)

// Tournament defines a single year's tournament, and is useful almost solely
// for tying together teams and games for easier queries.
type Tournament struct {
	Year int64 `datastore:"-" goon:"id"`
}

func latestTournament(ds *goon.Goon) (*datastore.Key, *Tournament, error) {
	y := int64(time.Now().Year())
	t := &Tournament{Year: y}

	key, err := ds.Put(t)

	if err != nil {
		return nil, nil, err
	}

	return key, t, nil
}
