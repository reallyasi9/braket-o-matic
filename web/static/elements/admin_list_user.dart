@HtmlImport('admin_list_user.html')
library braket.admin_list_user;

import 'package:polymer/polymer.dart';
import 'package:web_components/web_components.dart' show HtmlImport;

import 'package:polymer_elements/color.dart';
import 'package:polymer_elements/paper_item_body.dart';
import 'package:polymer_elements/paper_toggle_button.dart';
import 'package:polymer_elements/paper_icon_button.dart';
import 'package:polymer_elements/iron_icons.dart';
import 'package:polymer_elements/iron_flex_layout.dart';
import 'package:polymer_elements/iron_ajax.dart';
import 'package:polymer_elements/iron_request.dart';
import 'user_icon.dart';

import '../lib/user.dart';

@PolymerRegister('admin-list-user')
class AdminListUser extends PolymerElement {
  AdminListUser.created() : super.created();

  @Property(notify: true)
  User user;

  @Property(notify: true)
  bool isRegistered;

  @Listen('registered.change')
  onChecked(CustomEventWrapper event, _) async {
    IronAjax up = $['register-user'];
    Map<String, String> params = {
      "userID": user.id,
      "register": isRegistered
    };
    up.set('params', params);
    up.generateRequest();
  }

  @reflectable
  handleError(CustomEventWrapper e, Map detail) async {
    print("Error, yo: $detail");
  }

}
