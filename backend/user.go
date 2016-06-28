package braket

import (

	//"appengine/urlfetch"

	//"google.golang.org/api/oauth2/v2"
	"encoding/json"
	"net/http"

	"appengine"

	"golang.org/x/oauth2"
	"golang.org/x/oauth2/google"
	newappengine "google.golang.org/appengine"
	newurlfetch "google.golang.org/appengine/urlfetch"
)

// TODO Deal with OAuth later.
const oauthScope = "https://www.googleapis.com/auth/userinfo.email"

// User contains information about a logged-in braket user.
// Only non-identifying information is used.
type User struct {
	FullName string
	Picture  string
}

func init() {
	http.HandleFunc("/backend/verify-user", signin)
}

func signin(w http.ResponseWriter, r *http.Request) {

	//f := r.PostFormValue("id_token")
	ctx := newappengine.NewContext(r)
	client := &http.Client{
		Transport: &oauth2.Transport{
			Source: google.AppEngineTokenSource(ctx, "scope"),
			Base:   &newurlfetch.Transport{Context: ctx},
		},
	}
	_, err := client.Get("https://www.googleapis.com/oauth2/v2/userinfo")
	if err != nil {
		returnError(w, err)
		return
	}
	//resp.Write(w)

	u := User{FullName: "Test", Picture: "pictures/test.jpg"}
	js, err := json.Marshal(u)
	if err != nil {
		returnError(w, err)
		return
	}
	// TODO Secure me!
	c := appengine.NewContext(r)
	hn, err := appengine.ModuleHostname(c, "frontend", "", "")
	if err != nil {
		returnError(w, err)
		return
	}
	// Enable CORS
	w.Header().Add("Access-Control-Allow-Origin", "http://"+hn)
	w.Header().Set("Content-Type", "application/json")
	w.Write(js)
	// ctx := appengine.NewContext(r)
	// u := user.Current(ctx)
	// if u == nil {
	// 	url, _ := user.LoginURL(ctx, "/")
	// 	fmt.Fprintf(w, `<a href="%s">Sign in or register</a>`, url)
	// 	return
	// }
	// url, _ := user.LogoutURL(ctx, "/")
	// fmt.Fprintf(w, `Welcome, %s! (<a href="%s">sign out</a>)`, u, url)
	//
	// // TODO: Deal with OAuth later.
	// _, err := user.CurrentOAuth(ctx, oauthScope)
	// if err != nil {
	// 	returnError(w, err)
	// 	return
	// }
	// //if allowed := allowedClients[oau.ClientID]; !allowed {
	// //	returnError(w, fmt.Errorf("client %s not allowed", oau.ClientID))
	// //	return
	// //}
	// key, _ := user.OAuthConsumerKey(ctx)
	// fmt.Fprintf(w, "Hello, user %++v key %s", *u, key)
}

func returnError(w http.ResponseWriter, err error) {
	http.Error(w, err.Error(), http.StatusInternalServerError)
}
