@HtmlImport('user_icon.html')
library braket.user_icon;

import 'package:polymer/polymer.dart';
import 'package:web_components/web_components.dart' show HtmlImport;

import 'package:polymer_elements/color.dart';
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
    if (givenName.isNotEmpty && surname.isNotEmpty) {
      return givenName.substring(0,1) + surname.substring(0,1);
    } else if (givenName.isNotEmpty) {
      return givenName.substring(0,1);
    } else if (surname.isNotEmpty) {
      return surname.substring(0,1);
    } else if (nickname.isNotEmpty) {
      return nickname.substring(0,1);
    }
    return "?";
  }

}
