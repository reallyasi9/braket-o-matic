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
import 'package:polymer_elements/paper_ripple.dart';
import 'package:polymer_elements/iron_flex_layout.dart';
import 'package:polymer_elements/iron_icons.dart';
import '../lib/user.dart';
import '../lib/team.dart';
import 'favorite_team.dart';

const Duration _TIMEOUT = const Duration(seconds: 1);

@PolymerRegister('user-dialog')
class UserDialog extends PolymerElement {
    UserDialog.created() : super.created();


    /**
     * Factory constructor to prevent user upload from being triggered on
     * creation.
     */
    factory UserDialog(User user) {
        UserDialog ud = document.createElement('user-dialog');
        ud.user = user;
        return ud;
    }


    @Property(notify: true)
    User user;

    @Property(notify: true)
    List<Team> teams = new List<Team>();

    Timer _debounceTimer = new Timer(_TIMEOUT, () => {});


    void ready() {
        try {
            HttpRequest.getString("/backend/teams?sorted=1").then(_updateTeams);
        } catch (e) {
            print("Shoot!  Couldn't access teams! $e");
        }
    }


    @reflectable
    openDialog() async {
        PaperDialog pd = $["dialog"];
        pd.open();
    }


    @reflectable
    clearInput(CustomEventWrapper e, [_]) async {
        PaperIconButton b = e.target;
        PaperInput i = b.parentNode;
        i.updateValueAndPreserveCaret("");
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


    _updateTeams(String jsonMessage) async {
        List<Team> jsonTeams = JSON.decode(jsonMessage);
        this.teams.clear();
        this.teams.addAll(jsonTeams);
        this.notifyPath('teams', jsonTeams);

        // Now we determine which item is selected
        for (Team team in this.teams) {
            if (team.id == user.favoriteTeam.id) {
                // TODO
            }
        }
    }

}
