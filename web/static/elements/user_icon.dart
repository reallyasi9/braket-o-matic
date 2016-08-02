@HtmlImport('user_icon.html')
library braket.user_icon;

import 'package:polymer/polymer.dart';
import 'package:web_components/web_components.dart' show HtmlImport;

import 'package:polymer_elements/color.dart';
import 'package:polymer_elements/iron_icon.dart';
import 'braket_icon_set.dart';
import '../lib/user.dart';

@PolymerRegister('user-icon')
class UserIcon extends PolymerElement {
  UserIcon.created() : super.created();

  @Property(notify: true)
  User user;

  @property
  int size = 24;

  @reflectable
  String makeInitial(String givenName, String surname, String nickname) {
    return (givenName.isNotEmpty) ? givenName.substring(0, 1) : (surname.isNotEmpty) ? surname.substring(0, 1) : (nickname.isNotEmpty) ? nickname.substring(0, 1) : "?";
  }

}
