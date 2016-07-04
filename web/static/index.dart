import 'dart:html';
//import 'dart:convert';
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

handleSignIn(e) async {
    // I seriously can't believe this just works.
    var googleUser = auth2.getAuthInstance().currentUser.get();
    var loginUrl = '/backend/verify-user';
    try {
        await HttpRequest.postFormData(loginUrl, {'id_token': googleUser.getAuthResponse().id_token});
    } catch (e) {
        print("Shoot!  Couldn't access $loginUrl!");
    }
    // var profile = googleUser.getBasicProfile();
    // print(profile.getName());
    // print(profile.getImageUrl());
    // print(profile.getEmail());
}
