package braket

import (
	"mime"

	"appengine"
	//"appengine/urlfetch"
	"appengine/user"
	//"google.golang.org/api/oauth2/v2"

	//"encoding/json"
	"fmt"
	//"html/template"
	"net/http"
	//"net/url"
	//"strconv"
	//"time"
)

// TODO Deal with OAuth later.
const oauthScope = "https://www.googleapis.com/auth/userinfo.email"

func init() {
	// Required to serve SVG from appengine, which treats it as text/xml.
	// https://github.com/golang/go/issues/6378
	mime.AddExtensionType(".svg", "image/svg+xml")

	http.HandleFunc("/backend/verify-user", signin)
}

func signin(w http.ResponseWriter, r *http.Request) {

	w.Header().Set("Content-type", "text/html; charset=utf-8")

	ctx := appengine.NewContext(r)
	u := user.Current(ctx)
	if u == nil {
		url, _ := user.LoginURL(ctx, "/")
		fmt.Fprintf(w, `<a href="%s">Sign in or register</a>`, url)
		return
	}
	url, _ := user.LogoutURL(ctx, "/")
	fmt.Fprintf(w, `Welcome, %s! (<a href="%s">sign out</a>)`, u, url)

	// TODO: Deal with OAuth later.
	_, err := user.CurrentOAuth(ctx, oauthScope)
	if err != nil {
		returnError(w, err)
		return
	}
	//if allowed := allowedClients[oau.ClientID]; !allowed {
	//	returnError(w, fmt.Errorf("client %s not allowed", oau.ClientID))
	//	return
	//}
	key, _ := user.OAuthConsumerKey(ctx)
	fmt.Fprintf(w, "Hello, user %++v key %s", *u, key)
}

func returnError(w http.ResponseWriter, err error) {
	http.Error(w, err.Error(), http.StatusInternalServerError)
}
