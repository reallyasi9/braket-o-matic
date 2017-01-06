@HtmlImport('admin_users_page.html')
library braket.admin_users_page;

import 'package:polymer/polymer.dart';
import 'package:web_components/web_components.dart' show HtmlImport;

import 'package:polymer_elements/color.dart';
import 'package:polymer_elements/iron_ajax.dart';
import 'package:polymer_elements/iron_request.dart';
import 'package:polymer_elements/iron_flex_layout.dart';
import 'package:polymer_elements/paper_listbox.dart';
import 'package:polymer_elements/paper_item.dart';
import 'package:polymer_elements/paper_material.dart';
import 'user_icon.dart';
import 'admin_list_user.dart';

import '../lib/user.dart';

@PolymerRegister('admin-users-page')
class AdminUsersPage extends PolymerElement {
  AdminUsersPage.created() : super.created();

  @Property(notify: true)
  List<User> users = [];

  @reflectable
  handleUsers(CustomEventWrapper e, IronRequest detail) async {
    clear('users');
    List<User> newUsers = detail.response.map((Object o) => new User()..initFromMap(convertToDart(o))).toList();
    addAll('users', newUsers);
  }

  @reflectable
  handleError(CustomEventWrapper e, Map detail) async {
    print("Error, yo: $detail");
  }

}
