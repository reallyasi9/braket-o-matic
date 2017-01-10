@HtmlImport('admin_list_team.html')
library braket.admin_list_team;

import 'package:polymer/polymer.dart';
import 'package:web_components/web_components.dart' show HtmlImport;

import 'package:polymer_elements/color.dart';
import 'package:polymer_elements/paper_item_body.dart';
import 'package:polymer_elements/paper_icon_button.dart';
import 'package:polymer_elements/iron_icons.dart';
import 'package:polymer_elements/iron_flex_layout.dart';
import 'package:polymer_elements/iron_collapse.dart';
import 'package:polymer_elements/paper_input.dart';
import 'package:polymer_elements/iron_ajax.dart';
import 'package:polymer_elements/iron_image.dart';

import '../lib/team.dart';

@PolymerRegister('admin-list-team')
class AdminListTeam extends PolymerElement {
  AdminListTeam.created() : super.created();

  @Property(notify: true)
  Team team;

  bool isFirst = true;

  @reflectable
  clearInput(CustomEventWrapper e, [_]) async {
    PaperIconButton b = e.target;
    PaperInput i = b.parentNode;
    i.updateValueAndPreserveCaret("");
  }

  // TODO: make this universal somewhere
  @reflectable
  toggleCollapse(CustomEventWrapper e, [_]) async {
    PaperIconButton pib = $["toggle-button"];
    pib.disabled = true;

    IronCollapse ic = $["collapse"];
    await ic.toggle();

    bool open = ic.opened;
    if (open) {
      pib.setAttribute("icon", "expand-less");
    } else {
      pib.setAttribute("icon", "expand-more");
    }
    pib.disabled = false;
  }

  @Observe('team.*')
  onTeamChange(Map changeRecord) async {
    if (isFirst) {
      isFirst = false;
      return;
    }
    IronAjax tp = $['team-put'];
    tp.set('body', team.toJson());
    tp.generateRequest();
  }

  @reflectable
  handleError(CustomEventWrapper e, Map detail) async {
    print("Error, yo: $detail");
  }

}
