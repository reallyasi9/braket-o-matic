package braket

import (
	"mime"

	"appengine"
	"appengine/urlfetch"
	//"appengine/datastore"
	"appengine/user"

	"encoding/json"
	"fmt"
	"html/template"
	"net/http"
	"net/url"
	"strconv"
	"time"
)

const oauthScope = "https://www.googleapis.com/auth/userinfo.email"

var allowedClients = map[string]bool{
	"955696707006-d65hn137t184mbjcohuosff7os1vumjp.apps.googleusercontent.com": true,
	"123456789.apps.googleusercontent.com":                                     true, // test client
}

func init() {
	// Required to serve SVG from appengine, which treats it as text/xml.  https://github.com/golang/go/issues/6378
	mime.AddExtensionType(".svg", "image/svg+xml")

	http.HandleFunc("/backend/verify-user", verifyuser)
	http.HandleFunc("/backend/signin", signin)
}

func verifyuser(w http.ResponseWriter, r *http.Request) {
	token := r.PostFormValue("id_token")

	// No need to error check here.
	u, _ := url.Parse("https://www.googleapis.com/oauth2/v3/tokeninfo?id_token=" + token)

	// AppEngine uses a specialized version of http.Get
	ctx := appengine.NewContext(r)
	client := urlfetch.Client(ctx)

	resp, err := client.Get(u.String())
	if err != nil {
		returnError(w, err)
		return
	}
	defer resp.Body.Close()

	// TODO move all this to some other function?
	if resp.StatusCode != http.StatusOK {
		returnError(w, fmt.Errorf("Status code returned from auth server %d", resp.StatusCode))
		return
	}
	/*
		body, err := ioutil.ReadAll(resp.Body)
		if err != nil {
			returnError(w, err)
			return
		}

		fmt.Fprintf(w, "%s", body)
	*/
	decoder := json.NewDecoder(resp.Body)
	var message interface{}
	err = decoder.Decode(&message)
	if err != nil {
		returnError(w, err)
		return
	}
	m := message.(map[string]interface{})

	if m["iss"] != "https://accounts.google.com" && m["iss"] != "accounts.google.com" {
		returnError(w, fmt.Errorf("Invalid iss '%s'", m["iss"]))
		return
	}

	exp, err := strconv.ParseInt(m["exp"].(string), 10, 64)
	if err != nil {
		returnError(w, err)
		return
	}

	if exp < time.Now().Unix() {
		returnError(w, fmt.Errorf("Authorization has expired %d", exp))
		return
	}

	if m["aud"] != "955696707006-d65hn137t184mbjcohuosff7os1vumjp.apps.googleusercontent.com" {
		returnError(w, fmt.Errorf("Authorization not from correct app '%s'", m["aud"]))
		return
	}

	w.Header().Set("Content-type", "text/html; charset=utf-8")
	fmt.Fprint(w, m["sub"])
}

func signin(w http.ResponseWriter, r *http.Request) {
	ctx := appengine.NewContext(r)
	u := user.Current(ctx)
	if u == nil {
		url, _ := user.LoginURL(ctx, "/")
		fmt.Fprintf(w, `<a href="%s">Sign in or register</a>`, url)
		return
	}
	url, _ := user.LogoutURL(ctx, "/")
	fmt.Fprintf(w, `Welcome, %s! (<a href="%s">sign out</a>)`, u, url)

	oau, err := user.CurrentOAuth(ctx, oauthScope)
	if err != nil {
		returnError(w, err)
		return
	}
	if allowed := allowedClients[oau.ClientID]; !allowed {
		returnError(w, fmt.Errorf("client %s now allowed", oau.ClientID))
		return
	}
	key, _ := user.OAuthConsumerKey(ctx)
	fmt.Fprintf(w, "Hello, %+v", key)

}

const guestbookForm = `
<html>
  <body>
		<title>
    <form action="/sign" method="post">
      <div><textarea name="content" rows="3" cols="60"></textarea></div>
      <div><input type="submit" value="Sign Guestbook"></div>
    </form>
  </body>
</html>
`

func sign(w http.ResponseWriter, r *http.Request) {
	err := signTemplate.Execute(w, r.FormValue("content"))
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
	}
}

var signTemplate = template.Must(template.New("sign").Parse(signTemplateHTML))

const signTemplateHTML = `
<html>
  <body>
    <p>You wrote:</p>
    <pre>{{.}}</pre>
  </body>
</html>
`

func returnError(w http.ResponseWriter, err error) {
	http.Error(w, err.Error(), http.StatusInternalServerError)
}
