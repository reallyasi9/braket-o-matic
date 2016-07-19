package braket

import (
	"encoding/base64"
	"encoding/json"
	"net/http"
	"strings"
	"time"

	"golang.org/x/crypto/sha3"

	"google.golang.org/appengine"
	"google.golang.org/appengine/datastore"
	"google.golang.org/appengine/user"
)

const salt = "<braket|o|matic>"

// User represents a user, yo.
type User struct {
	Surname         string
	GivenName       string
	Nickname        string
	Email           string
	FirstAccessDate time.Time
	LastAccessDate  time.Time
	FavoriteTeam    *datastore.Key
}

type returnMessage struct {
	User      *User
	LogoutURL string
}

func newUser(in *user.User) *User {
	u := new(User)
	u.Email = in.Email
	u.Nickname = strings.Split(in.Email, "@")[0]
	u.FirstAccessDate = time.Now()
	u.LastAccessDate = u.FirstAccessDate

	return u
}

func init() {
	http.HandleFunc("/backend/get-user", get)
}

func get(w http.ResponseWriter, r *http.Request) {

	ctx := appengine.NewContext(r)

	w.Header().Set("Content-Type", "application/json")

	// Allow cross-site origin to the frontend and backend
	hn, err := appengine.ModuleHostname(ctx, "frontend", "", "")
	if err != nil {
		returnError(w, err)
		return
	}
	w.Header().Add("Access-Control-Allow-Origin", "http://"+hn)

	hn, err = appengine.ModuleHostname(ctx, "default", "", "")
	if err != nil {
		returnError(w, err)
		return
	}
	w.Header().Add("Access-Control-Allow-Origin", "http://"+hn)

	// Check the datastore for the user
	currentUser := user.Current(ctx)
	id := []byte(currentUser.ID + salt)
	sum := sha3.Sum512(id)
	hash := base64.StdEncoding.EncodeToString(sum[:])
	key := datastore.NewKey(ctx, "User", hash, 0, nil)
	braketUser := new(User)
	err = datastore.Get(ctx, key, braketUser)

	switch err {
	case datastore.ErrNoSuchEntity:
		// Create a new, default user
		braketUser = newUser(currentUser)
		_, err2 := datastore.Put(ctx, key, braketUser)
		if err2 != nil {
			returnError(w, err2)
			return
		}
	case nil:
		braketUser.LastAccessDate = time.Now()
	default:
		returnError(w, err)
		return
	}

	var rm returnMessage
	rm.User = braketUser
	lou, _ := user.LogoutURL(ctx, "/")
	rm.LogoutURL = lou
	js, err := json.Marshal(rm)
	if err != nil {
		returnError(w, err)
		return
	}
	w.Write(js)
}

func returnError(w http.ResponseWriter, err error) {
	http.Error(w, err.Error(), http.StatusInternalServerError)
}
