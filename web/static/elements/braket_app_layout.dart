@HtmlImport('braket_app_layout.html')
library braket.braket_app_layout;

import 'dart:html';
import 'dart:convert';

import 'package:polymer/polymer.dart';
import 'package:web_components/web_components.dart' show HtmlImport;

import 'package:polymer_elements/color.dart';
import 'package:polymer_elements/iron_flex_layout.dart';
import 'user_dialog.dart';
import 'package:polymer_elements/app_layout/app_header/app_header.dart';
import 'package:polymer_elements/app_layout/app_toolbar/app_toolbar.dart';
import 'package:polymer_elements/app_layout/app_scroll_effects/effects/waterfall.dart';
import 'user_button.dart';
import 'package:polymer_elements/paper_tabs.dart';
import 'package:polymer_elements/paper_tab.dart';
import 'braket_page_layout.dart';

import '../lib/user.dart';

@PolymerRegister('braket-app-layout')
class BraketAppLayout extends PolymerElement {
    BraketAppLayout.created() : super.created();

    @Property(notify: true)
    String page = "braket";

    @Property(notify: true)
    User user;

    void ready() {
        try {
            HttpRequest.getString("/backend/user").then(_onUserLoaded);
        } catch (e) {
            print("Shoot!  Couldn't access the login URL!");
        }
    }

    _onUserLoaded(String jsonMessage) async {
        // I seriously can't believe this just works.
        Map<String, dynamic> userReturn = JSON.decode(jsonMessage);
        this.set("user", new User.fromMap(userReturn["User"]));
        // notification isn't smart enough to do this yet...
         this.notifyPath('user.surname', user.surname);
         this.notifyPath('user.givenName', user.givenName);
         this.notifyPath('user.nickname', user.nickname);
         this.notifyPath('user.pictureURL', user.pictureURL);
    }

    @reflectable
    openUserDialog(CustomEventWrapper e, [_]) async {
        UserDialog ud = $["user-dialog"];
        ud.openDialog();
    }

}
