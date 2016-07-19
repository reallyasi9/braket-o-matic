import 'dart:html';
import 'package:polymer/polymer.dart';
import 'package:polymer_elements/paper_tabs.dart';
import 'elements/braket_page_layout.dart';
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

onUserLoaded(String jsonMessage) async {
    // I seriously can't believe this just works.
    UserReturnMessage userReturn = new UserReturnMessageImpl.fromJsonString(jsonMessage);
    //User user = userReturn.User; // TODO update button, etc.
    print(userReturn.LogoutURL);
}

onTabSelect(Event event) async {
    PaperTabs tabs = document.querySelector("paper-tabs");
    BraketPageLayout pages = document.querySelector("braket-page-layout");
    pages.setAttribute("page", tabs.selected);
}
