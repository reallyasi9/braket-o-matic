@HtmlImport('user_dialog.html')
library braket.user_dialog;

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


@PolymerRegister('user-dialog')
class UserDialog extends PolymerElement {
  UserDialog.created() : super.created();

  @Property(notify: true)
  User user;

  @Property(notify: true)
  List<Team> teams;

  @Property(notify: true)
  String selectedItemLabel;

  Map<int, Team> _teamsById = new Map<int, Team>();

  Map<String, Team> _teamsByName = new Map<String, Team>();

  @reflectable
  openDialog() async {
    PaperDialog pd = $["dialog"];
    // Make sure the favorite team is correctly selected
    PaperListbox pl = $["listbox"];
    int idx = teams.indexOf(_teamsById[user.favoriteTeamID]);
    pl.selectIndex(idx);
    pd.open();
  }

  @reflectable
  clearInput(CustomEventWrapper e, [_]) async {
    PaperIconButton b = e.target;
    PaperInput i = b.parentNode;
    i.updateValueAndPreserveCaret("");
  }

  @Observe("selectedItemLabel")
  handleTeam(String newLabel) async {
    Team t = _teamsByName[newLabel];
    if (t == null) {
      return;
    }
    set("user.favoriteTeamID", t.id);
  }

  @Observe("teams.splices")
  handleTeams(Map changeRecord) async {
    if (changeRecord == null) {
      return;
    }
    changeRecord['indexSplices'].forEach( (Map s) {
      s['removed'].forEach( (Team t) {
        _teamsByName.remove(t.schoolShortName);
        _teamsById.remove(t.id);
      });
      int iStart = s['index'];
      int n = s['addedCount'];
      for (Team t in teams.sublist(iStart).take(n)) {
        _teamsByName[t.schoolShortName] = t;
        _teamsById[t.id] = t;
      }
    });
  }

}
