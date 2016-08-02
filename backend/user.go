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

// User represents a user, yo.
type User struct {
	ID              string `datastore:"-" goon:"id"`
	Surname         string
	GivenName       string
	Nickname        string
	Email           string
	FirstAccessDate time.Time
	FavoriteTeam    *datastore.Key
}

type returnMessage struct {
	User      *User
	LogoutURL string
}

func init() {
	http.HandleFunc("/backend/user", dispatch)
}

func dispatch(w http.ResponseWriter, r *http.Request) {
	switch r.Method {
	default:
		ReturnError(w, http.ErrNotSupported)
	case http.MethodGet:
		get(w, r)
	case http.MethodPut:
		put(w, r)
	}
}

func get(w http.ResponseWriter, r *http.Request) {

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

	lou, _ := user.LogoutURL(ctx, "/")
	rm := returnMessage{User: u, LogoutURL: lou}
	js, err := json.Marshal(rm)
	if err != nil {
		ReturnError(w, err)
		return
	}

	w.Write(js)
}

func put(w http.ResponseWriter, r *http.Request) {

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
	u.FavoriteTeam = nu.FavoriteTeam

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
	}

	return u
}
