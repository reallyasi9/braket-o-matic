package braket

import (
	"encoding/base64"
	"encoding/json"
	"net/http"
	"strings"
	"time"
	"unicode"

	"github.com/mjibson/goon"

	"golang.org/x/crypto/sha3"

	"google.golang.org/appengine"
	"google.golang.org/appengine/datastore"
	"google.golang.org/appengine/user"
)

const salt = "<braket|o|matic>"

const defaultTeam = int64(170)

// User represents a user, yo.
type User struct {
	ID              string    `datastore:"-" goon:"id" json:"id"`
	Surname         string    `json:"surname"`
	GivenName       string    `json:"givenName"`
	Nickname        string    `json:"nickname"`
	Email           string    `json:"email"`
	FirstAccessDate time.Time `json:"-"`
	FavoriteTeamID  int64     `json:"favoriteTeamID"`
}

func init() {
	http.HandleFunc("/backend/user", dispatchUser)
	http.HandleFunc("/backend/admin/users", getUsers)
	http.HandleFunc("/backend/user-logout-url", getLogoutURL)
}

func dispatchUser(w http.ResponseWriter, r *http.Request) {
	switch r.Method {
	default:
		ReturnError(w, http.ErrNotSupported)
	case http.MethodGet:
		getUser(w, r)
	case http.MethodPut:
		putUser(w, r)
	}
}

func getUser(w http.ResponseWriter, r *http.Request) {

	// Global goon instance
	ctx := appengine.NewContext(r)
	ds := goon.FromContext(ctx)

	// Check the datastore for the user
	currentUser := user.Current(ctx)

	u := newUser(currentUser)
	if err := ds.Get(u); err != nil {
		if err != datastore.ErrNoSuchEntity {
			ReturnError(w, err)
			return
		}
		_, err := ds.Put(u)
		if err != nil {
			ReturnError(w, err)
			return
		}
	}

	js, err := json.Marshal(u)
	if err != nil {
		ReturnError(w, err)
		return
	}

	w.Write(js)
}

func getUsers(w http.ResponseWriter, r *http.Request) {

	// Global goon instance
	ctx := appengine.NewContext(r)
	ds := goon.FromContext(ctx)

	// Get them all!
	q := datastore.NewQuery("User")
	var users []User
	_, err := ds.GetAll(q, &users)

	if err != nil {
		ReturnError(w, err)
		return
	}

	err = ds.GetMulti(users)
	if err != nil {
		ReturnError(w, err)
		return
	}

	js, err := json.Marshal(users)
	if err != nil {
		ReturnError(w, err)
		return
	}
	w.Write(js)
}

func getLogoutURL(w http.ResponseWriter, r *http.Request) {
	ctx := appengine.NewContext(r)
	lou, _ := user.LogoutURL(ctx, "/")
	js, err := json.Marshal(lou)
	if err != nil {
		ReturnError(w, err)
		return
	}
	w.Write(js)
}

func putUser(w http.ResponseWriter, r *http.Request) {

	// Global goon instance
	ctx := appengine.NewContext(r)
	ds := goon.FromContext(ctx)

	// Check the datastore for the user
	currentUser := user.Current(ctx)

	u := newUser(currentUser)
	if err := ds.Get(u); err != nil {
		ReturnError(w, err)
		return
	}

	// Dig json out of the sent data
	nu := &User{}
	decoder := json.NewDecoder(r.Body)
	if err := decoder.Decode(nu); err != nil {
		ReturnError(w, err)
		return
	}

	// Update the fields that are updatable
	u.GivenName = strings.TrimSpace(nu.GivenName)
	// manually trim spaces and quotes from the nickname
	u.Nickname = strings.TrimFunc(nu.Nickname, func(r rune) bool {
		return unicode.IsSpace(r) || unicode.In(r, unicode.Properties["Quotation_Mark"])
	})
	u.Surname = strings.TrimSpace(nu.Surname)
	u.FavoriteTeamID = nu.FavoriteTeamID

	// Send the update
	ds.Put(u)

	// Done
	return

}

func newUser(in *user.User) *User {

	id := []byte(in.ID + salt)
	sum := sha3.Sum512(id)
	hash := base64.StdEncoding.EncodeToString(sum[:])

	u := &User{
		ID:              hash,
		Email:           in.Email,
		Nickname:        strings.Split(in.Email, "@")[0],
		FirstAccessDate: time.Now(),
		FavoriteTeamID:  defaultTeam,
	}

	return u
}
