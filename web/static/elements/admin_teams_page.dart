@HtmlImport('admin_teams_page.html')
library braket.admin_teams_page;

import 'package:polymer/polymer.dart';
import 'package:web_components/web_components.dart' show HtmlImport;

import 'package:polymer_elements/color.dart';
import 'package:polymer_elements/iron_ajax.dart';
import 'package:polymer_elements/iron_request.dart';
import 'package:polymer_elements/iron_flex_layout.dart';
import 'package:polymer_elements/paper_material.dart';
import 'package:polymer_elements/paper_button.dart';
import 'admin_list_team.dart';

import '../lib/team.dart';

@PolymerRegister('admin-teams-page')
class AdminTeamsPage extends PolymerElement {
  AdminTeamsPage.created() : super.created();

  @Property(notify: true)
  List<Team> teams = [];

  @reflectable
  teamsBuilt(CustomEventWrapper e, IronRequest detail) async {
    ($['teams-get'] as IronAjax).generateRequest();
    // TODO: toast?
  }

  @reflectable
  handleTeams(CustomEventWrapper e, IronRequest detail) async {
    clear('teams');
    List<Team> newTeams = detail.response.map((Object o) => new Team()..initFromMap(convertToDart(o))).toList();

    if (newTeams.isEmpty) {
      ($['build-teams'] as IronAjax).generateRequest();
    } else {
      addAll('teams', newTeams);
    }
  }

  @reflectable
  handleError(CustomEventWrapper e, Map detail) async {
    print("Error, yo: $detail");
  }
}
