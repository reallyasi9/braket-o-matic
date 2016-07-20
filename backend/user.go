package braket

import (
	"encoding/base64"
	"encoding/json"
	"net/http"
	"strings"
	"time"

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
	PictureURL      string
}

type returnMessage struct {
	User      *User
	LogoutURL string
}

func init() {
	http.HandleFunc("/backend/get-user", get)
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

func newUser(in *user.User) *User {

	id := []byte(in.ID + salt)
	sum := sha3.Sum512(id)
	hash := base64.StdEncoding.EncodeToString(sum[:])

	u := &User{
		ID:              hash,
		Email:           in.Email,
		Nickname:        strings.Split(in.Email, "@")[0],
		FirstAccessDate: time.Now(),
		PictureURL:      "/images/empty-user.png",
	}

	return u
}
