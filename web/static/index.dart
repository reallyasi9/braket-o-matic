import 'dart:html';
import 'package:polymer/polymer.dart';
import 'package:polymer_elements/paper_header_panel.dart';
import 'lib/user.dart';

main() async {
    await initPolymer();
    try {
        HttpRequest.getString("/backend/get-user").then(onUserLoaded);
    } catch (e) {
        print("Shoot!  Couldn't access the login URL!");
    }
}

onUserLoaded(String jsonUser) async {
    // I seriously can't believe this just works.
    User user = new UserImpl.fromJsonString(jsonUser);
    print(user.AppEngineUser.Email);
}
