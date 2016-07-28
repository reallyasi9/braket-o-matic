package braket

import (
	"encoding/base64"
	"encoding/json"
	"io"
	"mime"
	"net/http"
	"strings"
	"time"

	"github.com/mjibson/goon"

	"golang.org/x/crypto/sha3"

	"google.golang.org/appengine"
	"google.golang.org/appengine/datastore"
	"google.golang.org/appengine/file"
	"google.golang.org/appengine/user"
	"google.golang.org/cloud/storage"
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
	http.HandleFunc("/backend/user", dispatch)
	http.HandleFunc("/backend/user-picture", postPicture)
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
	u.GivenName = nu.GivenName
	u.Nickname = nu.Nickname
	u.Surname = nu.Surname
	u.FavoriteTeam = nu.FavoriteTeam

	// Send the update
	ds.Put(u)

	// Done
	return

}

func postPicture(w http.ResponseWriter, r *http.Request) {

	ctx := appengine.NewContext(r)
	ds := goon.FromContext(ctx)

	// Check the datastore for the user
	currentUser := user.Current(ctx)

	u := newUser(currentUser)
	if err := ds.Get(u); err != nil {
		ReturnError(w, err)
		return
	}

	// This should be enough memory...
	r.ParseMultipartForm(32 << 20)

	// Get the file from the request
	f, header, err := r.FormFile("file")
	if err != nil {
		ReturnError(w, err)
		return
	}
	defer f.Close()

	// Get the extension by mime tpye
	ct := header.Header.Get("Content-Type")
	ext, err := mime.ExtensionsByType(ct)
	if err != nil {
		ReturnError(w, err)
		return
	}

	// Make a new filename
	fn := u.ID + ext[0]

	// Get the default bucket name
	bn, err := file.DefaultBucketName(ctx)
	if err != nil {
		ReturnError(w, err)
		return
	}

	// Get the storage client and bucket
	client, err := storage.NewClient(ctx)
	if err != nil {
		ReturnError(w, err)
		return
	}
	defer client.Close()
	bkt := client.Bucket(bn)

	// Write!  Finally!
	wc := bkt.Object(fn).NewWriter(ctx)
	wc.ContentType = ct
	io.Copy(wc, f)
	err = wc.Close()
	if err != nil {
		ReturnError(w, err)
		return
	}

	// Get the new name of the file
	url := wc.Attrs().MediaLink
	u.PictureURL = url
	ds.Put(u)

	// Done.  Return the new file URL
	ret := make(map[string]string)
	js, err := json.Marshal(ret)
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
		PictureURL:      "/images/empty-user.svg",
	}

	return u
}
