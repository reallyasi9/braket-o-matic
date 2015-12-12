package braket

import (
	"mime"

	//"appengine"
	//"appengine/datastore"
	//"appengine/user"

	"html/template"
	"net/http"
)

func init() {
	// Required to serve SVG from appengine, which treats it as text/xml.  https://github.com/golang/go/issues/6378
	mime.AddExtensionType(".svg", "image/svg+xml")

	http.HandleFunc("/", root)
	http.HandleFunc("/signin", signin)
	http.HandleFunc("/signout", signout)
}

func root(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-type", "text/html; charset=utf-8")
}

func signin(w http.ResponseWriter, r *http.Request) {

}

func signout(w http.ResponseWriter, r *http.Request) {

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

func ReturnError(w http.ResponseWriter, err error) {
	http.Error(w, err.Error(), http.StatusInternalServerError)
}
