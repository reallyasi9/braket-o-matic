@HtmlImport('admin_list_user.html')
library braket.admin_list_user;

import 'package:polymer/polymer.dart';
import 'package:web_components/web_components.dart' show HtmlImport;

import 'package:polymer_elements/color.dart';
import 'package:polymer_elements/paper_item_body.dart';
import 'package:polymer_elements/paper_toggle_button.dart';
import 'package:polymer_elements/paper_icon_button.dart';
import 'package:polymer_elements/paper_button.dart';
import 'package:polymer_elements/iron_icon.dart';
import 'package:polymer_elements/iron_icons.dart';
import 'package:polymer_elements/iron_flex_layout.dart';
import 'package:polymer_elements/iron_ajax.dart';
import 'package:polymer_elements/iron_request.dart';
import 'package:polymer_elements/iron_collapse.dart';
import 'package:polymer_elements/paper_input.dart';

import 'user_icon.dart';

import '../lib/user.dart';

@PolymerRegister('admin-list-user')
class AdminListUser extends PolymerElement {
  AdminListUser.created() : super.created();

  @Property(notify: true)
  User user;

  bool isFirst = true;

  @Property(notify: true)
  bool isRegistered;

  @Listen('registered.change')
  onChecked(CustomEventWrapper event, _) async {
    IronAjax up;
    if (isRegistered) {
      up = $['register-user'];
    } else {
      up = $['unregister-user'];
    }
    Map<String, String> params = {"userID": user.id};
    up.set('params', params);
    up.generateRequest();
  }

  @reflectable
  delete(CustomEventWrapper event, _) async {
    // Manual unregister
    IronAjax up = $['unregister-user'];
    Map<String, String> params = {"userID": user.id};
    up.set('params', params);
    await up.generateRequest();

    IronAjax ia = $['delete-user'];
    ia.set('params', params);
    ia.generateRequest();
  }

  register([bool registered = true]) {
    this.set("isRegistered", registered); // I hope this doesn't bubble...
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

  @Observe('user.*')
  onTeamChange(Map changeRecord) async {
    if (isFirst) {
      isFirst = false;
      return;
    }
    IronAjax tp = $['user-put'];
    tp.set('body', user.toJson());
    tp.generateRequest();
  }

  @reflectable
  handleError(CustomEventWrapper e, Map detail) async {
    print("Error, yo: $detail");
  }
}
