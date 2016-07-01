import 'dart:html';
import 'package:polymer/polymer.dart';
import 'package:polymer_elements/google_signin.dart';

main() async {
    await initPolymer();
    Element signInButton = new GoogleSignin()
        ..id = 'signin'
        ..theme = 'light'
        ..clientId = '384318016625-he5cnjm31855qknihknudiue9gg41553.apps.googleusercontent.com'
        ..scopes = 'https://www.googleapis.com/auth/userinfo.email';
    document.body.children.add(signInButton);
    signInButton.on["google-signin-success"].listen(handleSignIn);
}

void handleSignIn(e) {
    //var googleUser = context["gapi"]["auth2"].callMethod("getAuthInstance", [])["currentUser"].callMethod("get" []);
    print(e);
}



// document.addEventListener("google-signin-success", function(e) {
// var cu = gapi.auth2.getAuthInstance()['currentUser'].get();
//
// // Authentication
// var it = cu.getAuthResponse().id_token;
// // TODO: post to backend, verify with Google. (https://developers.google.com/identity/sign-in/web/backend-auth)
// var xhr = new XMLHttpRequest();
// // TODO backend is running on a different port.  Hrm...
// xhr.open('POST', 'http://{{.}}/backend/verify-user');
// xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
// xhr.onload = function() {
//   console.log('Signed in as: ' + xhr.responseText);
// };
// xhr.send('id_token=' + it);
//
// // All my data
// var bp = cu.getBasicProfile();
// var name = bp.getName();
// var image = bp.getImageUrl();
// var email = bp.getEmail();
//
// console.log(name);
// console.log(image);
// console.log(email);
// });
