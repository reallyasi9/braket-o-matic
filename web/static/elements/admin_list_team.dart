@HtmlImport('admin_list_team.html')
library braket.admin_list_team;

import 'package:polymer/polymer.dart';
import 'package:web_components/web_components.dart' show HtmlImport;

import 'package:polymer_elements/color.dart';
import 'package:polymer_elements/paper_item_body.dart';
import 'package:polymer_elements/paper_icon_button.dart';
import 'package:polymer_elements/iron_icons.dart';
import 'package:polymer_elements/iron_flex_layout.dart';

import '../lib/team.dart';

@PolymerRegister('admin-list-team')
class AdminListTeam extends PolymerElement {
  AdminListTeam.created() : super.created();

  @Property(notify: true)
  Team team;

  @reflectable
  handleError(CustomEventWrapper e, Map detail) async {
    print("Error, yo: $detail");
  }

}
