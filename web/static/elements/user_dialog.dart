@HtmlImport('user_dialog.html')
library braket.user_dialog;

import 'dart:html';
import 'dart:async';
import 'dart:convert';

import 'package:polymer/polymer.dart';
import 'package:web_components/web_components.dart' show HtmlImport;

import 'package:polymer_elements/paper_dialog.dart';
import 'package:polymer_elements/paper_dialog_scrollable.dart';
import 'package:polymer_elements/paper_input.dart';
import 'package:polymer_elements/paper_icon_button.dart';
import 'package:polymer_elements/paper_dropdown_menu.dart';
import 'package:polymer_elements/paper_listbox.dart';
import 'package:polymer_elements/paper_item.dart';
import 'package:polymer_elements/iron_flex_layout.dart';
import '../lib/user.dart';
import '../lib/team.dart';

const Duration _TIMEOUT = const Duration(seconds: 1);

@PolymerRegister('user-dialog')
class UserDialog extends PolymerElement {
    UserDialog.created() : super.created();

    @Property(notify: true)
    User user;

    @property
    bool withBackdrop = false;

    @property
    List<Team> favoriteTeams = new List<Team>();

    Timer _debounceTimer = new Timer(_TIMEOUT, () => {});


    void ready() {
        try {
            HttpRequest.getString("/backend/teams").then(_onTeamsLoaded);
        } catch (e) {
            print("Shoot!  Couldn't access the teams URL!");
        }
    }

    _onTeamsLoaded(String jsonMessage) async {
        List<Map<String, dynamic>> teams = JSON.decode(jsonMessage);
        if (teams == null) {
            print("Shoot!  Nothing returned from teams query!");
            return;
        }
        for (Map<String, dynamic> jsonMap in teams) {
            favoriteTeams.add(new Team.fromMap(jsonMap));
        }
        // Now fill the list with these data
        _fillFavorites();
    }

    _fillFavorites() async {
        PaperListbox listbox = $['favorite-list'] as PaperListbox;
        for (Team team in this.favoriteTeams) {
            PaperItem item = new PaperItem();
            item.appendText("${team.schoolName} ${team.nickname}");
            listbox.append(item);
        }
    }

    @reflectable
    openDialog() async {
        PaperDialog pd = $["dialog"];
        pd.open();
    }

    // Modal dialogs that are not direct children of the <body> element are broken.
    @reflectable
    patchOverlay(CustomEventWrapper e, [_]) async {
        PaperDialog d = e.target;
        if (d.withBackdrop) {
            d.parentNode.insertBefore(d.backdropElement, d);
        }
    }


    @reflectable
    clearInput(CustomEventWrapper e, [_]) async {
        PaperIconButton b = e.target;
        PaperInput i = b.parentNode;
        i.updateValueAndPreserveCaret("");
    }


    @reflectable
    clearFavorite(CustomEventWrapper e, [_]) async {
        // TODO
    }


    @Observe('user.givenName, user.surname, user.nickname')
    handleChange(String givenName, String surname, String nickname) async {
        // For now, handle all values
        user.givenName = givenName.trim();
        user.surname = surname.trim();
        user.nickname = nickname.replaceAll(new RegExp(r"\s+|[\u0022\u0027\u2018\u2019\u201c\u201d\u0060\u00b4]+"), "");
        this.notifyPath("user");

        if (_debounceTimer.isActive) {
            this._debounceTimer.cancel();
        }
        _debounceTimer = new Timer(_TIMEOUT, () {
            this._upload();
        });
    }


    _upload() async {
        try {
            HttpRequest.request("/backend/user", method: "PUT", sendData: this.user);
        } catch (err) {
            print("Shoot!  Couldn't write user data!");
        }
    }

}
