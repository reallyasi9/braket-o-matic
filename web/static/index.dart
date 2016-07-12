import 'dart:html';
import 'package:polymer/polymer.dart';
import 'package:polymer_elements/paper_tabs.dart';
import 'package:polymer_elements/iron_pages.dart';
import 'lib/user.dart';

main() async {
    await initPolymer();

    // Bind tab selector
    Element tabs = document.querySelector("paper-tabs");
    tabs.addEventListener("iron-select", onTabSelect);

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

onTabSelect(Event event) async {
    PaperTabs tabs = document.querySelector("paper-tabs");
    IronPages pages = document.querySelector("iron-pages");
    pages.selected = tabs.selected;
}
