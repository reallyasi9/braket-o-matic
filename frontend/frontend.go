package braket

import (
	"html/template"
	"mime"
	"net/http"

	"appengine"
)

func init() {
	// Required to serve SVG from appengine, which treats it as text/xml.
	// https://github.com/golang/go/issues/6378
	mime.AddExtensionType(".svg", "image/svg+xml")

	http.HandleFunc("/", root)
}

func root(w http.ResponseWriter, r *http.Request) {
	c := appengine.NewContext(r)
	hn, err := appengine.ModuleHostname(c, "default", "", "")
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
	if err := rootTemplate.Execute(w, hn); err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
}

var rootTemplate = template.Must(template.New("root").Parse(`
<!doctype html>
<html>
<head>
  <title>braket-o-matic</title>
  <meta name="viewport"
  content="width=device-width, minimum-scale=1.0, initial-scale=1.0, user-scalable=yes">

  <link rel="stylesheet" type="text/css"
  href="https://fonts.googleapis.com/css?family=Cantarell:400,700">
  <link rel="import"
  href="bower_components/google-signin/google-signin.html">

  <style>
html,body {
  height: 100%;
  margin: 0;
  background-color: #E5E5E5;
  font-family: 'RobotoDraft', sans-serif;
}
  </style>

</head>

<body unresolved>

  <google-signin client-id="384318016625-he5cnjm31855qknihknudiue9gg41553.apps.googleusercontent.com" scopes="https://www.googleapis.com/auth/userinfo.email"></google-signin>

  <script type="text/javascript">
  document.addEventListener("google-signin-success", function(e) {
    var cu = gapi.auth2.getAuthInstance()['currentUser'].get();

    // Authentication
    var it = cu.getAuthResponse().id_token;
    // TODO: post to backend, verify with Google. (https://developers.google.com/identity/sign-in/web/backend-auth)
    var xhr = new XMLHttpRequest();
    // TODO backend is running on a different port.  Hrm...
    xhr.open('POST', 'http://{{.}}/backend/verify-user');
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.onload = function() {
      console.log('Signed in as: ' + xhr.responseText);
    };
    xhr.send('id_token=' + it);

    // All my data
    var bp = cu.getBasicProfile();
    var name = bp.getName();
    var image = bp.getImageUrl();
    var email = bp.getEmail();

    console.log(name);
    console.log(image);
    console.log(email);
  });
  </script>
</body>

</html>
`))
