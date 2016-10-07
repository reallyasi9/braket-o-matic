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

  @property
  Object chosenTeam;

  @reflectable
  openDialog() async {
    PaperDialog pd = $["dialog"] as PaperDialog;
    pd.open();
  }

  @reflectable
  clearInput(CustomEventWrapper e, [_]) async {
    PaperIconButton b = e.target;
    PaperInput i = b.parentNode;
    i.updateValueAndPreserveCaret("");
  }

  @Observe('chosenTeam')
  handleTeam(Object newTeam) async {
    // user.favoriteTeamID = newTeam;
    print(newTeam);
  }

  // _updateTeams(String jsonMessage) async {
  //   List<Map> mapTeams = JSON.decode(jsonMessage);
  //   List<Team> jsonTeams = mapTeams.map((Map m) => new Team.fromMap(m));
  //   this.teams.clear();
  //   this.teams.addAll(jsonTeams);
  //   this.notifyPath('teams', jsonTeams); // Not working?  I'm not sure here...
  //
  //   // Now we determine which item is selected
  //   for (Team team in this.teams) {
  //     if (team.id == user.favoriteTeamID) {
  //       // TODO
  //     }
  //   }
  // }
}
