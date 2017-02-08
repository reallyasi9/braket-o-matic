package braket

import (
	"time"

	"github.com/mjibson/goon"
	"google.golang.org/cloud/datastore"
)

// Rules defines the rules that a single Tournament instance will use.
type Rules struct {
	StartTime  time.Time      `json:"startTime"`
	EndTime    time.Time      `json:"endTime"`
	Tournament *datastore.Key `datastore:"-" goon:"parent" json:"tournamentID"`
}

func rules(ds *goon.Goon, t *Tournament) (*Rules, error) {

	tournamentKey, err := ds.KeyError(t)
	if err != nil {
		return nil, err
	}

	q := datastore.NewQuery("Registration").Ancestor(t)
	var ruleses []Rules
	_, err = ds.GetAll(q, &ruleses)
	if err != nil {
		return nil, err
	}

	return ruleses[0]
}
