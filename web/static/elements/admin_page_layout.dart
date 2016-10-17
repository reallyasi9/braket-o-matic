@HtmlImport('admin_page_layout.html')
library braket.admin_page_layout;

import 'package:polymer/polymer.dart';
import 'package:web_components/web_components.dart' show HtmlImport;

import 'package:polymer_elements/iron_pages.dart';
import 'admin_users_page.dart';
import 'admin_teams_page.dart';
import 'admin_games_page.dart';
import 'admin_tournament_page.dart';

@PolymerRegister('admin-page-layout')
class AdminPageLayout extends PolymerElement {
  AdminPageLayout.created() : super.created();

  @property
  String page;
}
