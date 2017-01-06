package braket

import (
	"encoding/json"
	"errors"
	"net/http"

	"github.com/mjibson/goon"
	"google.golang.org/appengine"
	"google.golang.org/appengine/datastore"
)

// Registration represents a registration for a User to a particular Tournament.
type Registration struct {
	ID         int64          `datastore:"-" goon:"id" json:"-"`
	User       string         `json:"userID"`
	Tournament *datastore.Key `datastore:"-" goon:"parent" json:"tournamentID"`
}

func init() {
	http.HandleFunc("/backend/admin/register-user", registerUser)
	http.HandleFunc("/backend/admin/registrations", registrations)
}

func registrations(w http.ResponseWriter, r *http.Request) {
	// Global goon instance
	ctx := appengine.NewContext(r)
	ds := goon.FromContext(ctx)

	// Need the current tournament
	tournamentKey, _, err := latestTournament(ds)
	if err != nil {
		ReturnError(w, err)
		return
	}

	// Get those registrations!
	q := datastore.NewQuery("Registration").Ancestor(tournamentKey)
	var regs []Registration
	_, err = ds.GetAll(q, &regs)
	if err != nil {
		ReturnError(w, err)
		return
	}

	js, err := json.Marshal(regs)
	if err != nil {
		ReturnError(w, err)
		return
	}
	w.Write(js)
}

func registerUser(w http.ResponseWriter, r *http.Request) {

	// Global goon instance
	ctx := appengine.NewContext(r)
	ds := goon.FromContext(ctx)

	// Need the current tournament
	tournamentKey, _, err := latestTournament(ds)
	if err != nil {
		ReturnError(w, err)
		return
	}

	// Need the target user
	userID := r.FormValue("userID")

	// Need to know if I am registering or unregistering
	doReg := r.FormValue("register")

	if doReg == "true" {
		err = register(ds, tournamentKey, userID)
	} else {
		err = unregister(ds, tournamentKey, userID)
	}
	if err != nil {
		ReturnError(w, err)
		return
	}
}

func register(ds *goon.Goon, tournamentKey *datastore.Key, userID string) error {
	// Get registrations by tournament
	q := datastore.NewQuery("Registration").Ancestor(tournamentKey).Filter("User=", userID)
	var regs []Registration
	_, err := ds.GetAll(q, &regs)
	if err != nil {
		return err
	}

	// Already registered?  Done!
	if len(regs) == 1 {
		return nil
	}

	// Too many users registered?  Oh no...
	if len(regs) > 1 {
		return errors.New("found more than one registered user for user/tournament combination")
	}

	// No Registration for that user?  Make one!
	reg := &Registration{
		User:       userID,
		Tournament: tournamentKey,
	}
	_, err = ds.Put(reg)
	return err
}

func unregister(ds *goon.Goon, tournamentKey *datastore.Key, userID string) error {

	// Get registrations by tournament
	q := datastore.NewQuery("Registration").Ancestor(tournamentKey).Filter("User=", userID)
	var regs []Registration
	keys, err := ds.GetAll(q, &regs)
	if err != nil {
		return err
	}

	// Not registered?  Done!
	if len(keys) == 0 {
		return nil
	}

	// TODO: Too many users registered?  Clear them all?
	// if len(regs) > 1 {
	// return errors.New("found more than one registered user for user/tournament combination")
	// }

	// Clear them all!
	err = ds.DeleteMulti(keys)
	return err

}
