import 'dart:html';
import 'package:polymer/polymer.dart';
import 'package:polymer_elements/google_signin.dart';
import 'auth2.dart' as auth2;

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
    // I seriously can't believe this just works.
    var googleUser = auth2.getAuthInstance().currentUser.get();
    print(googleUser.getAuthResponse().id_token); // use this to authenticate on the backend.
    var profile = googleUser.getBasicProfile();
    print(profile.getName());
    print(profile.getImageUrl());
    print(profile.getEmail());
}
