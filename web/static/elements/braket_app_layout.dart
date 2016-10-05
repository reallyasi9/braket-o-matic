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
import 'package:polymer_elements/iron_ajax.dart';
import 'package:polymer_elements/iron_request.dart';
import 'package:exportable/exportable.dart';

import '../lib/user.dart';
import '../lib/team.dart';

@PolymerRegister('braket-app-layout')
class BraketAppLayout extends PolymerElement {
  BraketAppLayout.created() : super.created();

  @Property(notify: true)
  String page = "braket";

  @Property(notify: true)
  String logoutURL;

  @Property(notify: true)
  User user;

  @Property(notify: true)
  List<Team> teams = [];

  ready() {
    $['user-get'].generateRequest();
  }

  @reflectable
  handleUser(CustomEventWrapper e, IronRequest detail) async {
    // Automatic JSON construction of cusom classes isn't a thing in dart.
    this.set('user', new User()..initFromMap(convertToDart(detail.response)));
    IronAjax e = $['user-put'] as IronAjax;
    e.set('auto', true);
  }

  @Observe('user.*')
  onUserChange(Map changeRecord) {
    user.cleanName();
    IronAjax e = $['user-put'] as IronAjax;
    e.set('body', user.toJson());
  }

  @reflectable
  handleTeams(CustomEventWrapper e, IronRequest detail) async {
    for (Object o in detail.response) {
      Team t = new Team()..initFromMap(convertToDart(o));
      this.add("teams", t);
    }
  }

  @reflectable
  openUserDialog(CustomEventWrapper e, [_]) async {
    UserDialog ud = $["user-dialog"];
    ud.openDialog();
  }

  @reflectable
  handleError(CustomEventWrapper e, IronRequest detail) async {
    print("Error, yo: $detail");
  }

}
