@HtmlImport('braket_admin_layout.html')
library braket.braket_admin_layout;

import 'dart:html';
import 'dart:convert';

import 'package:polymer/polymer.dart';
import 'package:web_components/web_components.dart' show HtmlImport;

import 'package:polymer_elements/color.dart';
import 'package:polymer_elements/iron_flex_layout.dart';
import 'package:polymer_elements/app_layout/app_header/app_header.dart';
import 'package:polymer_elements/app_layout/app_toolbar/app_toolbar.dart';
import 'package:polymer_elements/app_layout/app_scroll_effects/effects/waterfall.dart';
import 'package:polymer_elements/paper_tabs.dart';
import 'package:polymer_elements/paper_tab.dart';
import 'package:polymer_elements/iron_ajax.dart';
import 'package:polymer_elements/iron_request.dart';

import 'admin_page_layout.dart';

import '../lib/user.dart';
import '../lib/team.dart';

@PolymerRegister('braket-admin-layout')
class BraketAdminLayout extends PolymerElement {
  BraketAdminLayout.created() : super.created();

  @Property(notify: true)
  String page = "users";
  //
  // @Property(notify: true)
  // String logoutURL;
  //
  // @Property(notify: true)
  // User user;
  //
  // @Property(notify: true)
  // List<Team> teams = [];
  //
  ready() {
    // $['user-get'].generateRequest();
  }
  //
  // @reflectable
  // handleUser(CustomEventWrapper e, IronRequest detail) async {
  //   // Automatic JSON construction of cusom classes isn't a thing in dart.
  //   set('user', new User()..initFromMap(convertToDart(detail.response)));
  //   IronAjax up = $['user-put'];
  //   up.set('auto', true);
  // }
  //
  // @Observe('user.*')
  // onUserChange(Map changeRecord) async {
  //   user.cleanName();
  //   IronAjax up = $['user-put'];
  //   up.set('body', user.toJson());
  // }
  //
  // @Observe('teams.*,user.favoriteTeamID')
  // onFavoriteTeamChange(_, int newID) async {
  //   for (Team favTeam in teams.where((Team t) => t.id == newID)) {
  //     set('user.favoriteTeam', favTeam);
  //     break;
  //   }
  // }
  //
  // @reflectable
  // handleTeams(CustomEventWrapper e, IronRequest detail) async {
  //   List<Team> newTeams = detail.response.map((Object o) => new Team()..initFromMap(convertToDart(o)));
  //   clear('teams');
  //   addAll('teams', newTeams);
  // }
  //
  // @reflectable
  // openUserDialog(CustomEventWrapper e, [_]) async {
  //   UserDialog ud = $["user-dialog"];
  //   ud.openDialog();
  // }
  //
  // @reflectable
  // handleError(CustomEventWrapper e, IronRequest detail) async {
  //   print("Error, yo: $detail");
  // }

}
