@HtmlImport('user_icon.html')
library braket.user_icon;

import 'package:polymer/polymer.dart';
import 'package:web_components/web_components.dart' show HtmlImport;

import 'package:polymer_elements/color.dart';
import '../lib/user.dart';

@PolymerRegister('user-icon')
class UserIcon extends PolymerElement {
  UserIcon.created() : super.created();

  @Property(notify: true)
  User user;

  @property
  String initials;

  @property
  int size = 24;

  @Observe("user.*")
  makeInitials(Map changeRecord) {
    this.set('initials', user.initials);
  }

}
