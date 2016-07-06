package braket

import (
	"encoding/json"
	"net/http"

	//"google.golang.org/appengine"
	//"google.golang.org/appengine/user"
	"appengine"
	"appengine/user"
)

// TODO Deal with OAuth later.
const oauthScope = "https://www.googleapis.com/auth/userinfo.email"

// User represents a user, yo.
// TODO Add OAuth here
type User struct {
	LoggedIn      bool
	LoginURL      string
	LogoutURL     string
	AppEngineUser *user.User
}

func init() {
	http.HandleFunc("/backend/get-user", get)
}

func get(w http.ResponseWriter, r *http.Request) {

	ctx := appengine.NewContext(r)

	w.Header().Set("Content-Type", "application/json")

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

	liu, _ := user.LoginURL(ctx, "/")
	lou, _ := user.LogoutURL(ctx, "/")
	loggedIn := true
	u := user.Current(ctx)
	if u == nil {
		loggedIn = false
	}

	data := User{loggedIn, liu, lou, u}
	js, err := json.Marshal(data)
	if err != nil {
		returnError(w, err)
		return
	}
	w.Write(js) // Why doesn't this work?
}

func returnError(w http.ResponseWriter, err error) {
	http.Error(w, err.Error(), http.StatusInternalServerError)
}
