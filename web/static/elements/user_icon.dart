@HtmlImport('user_icon.html')
library braket.user_icon;

import 'package:polymer/polymer.dart';
import 'package:web_components/web_components.dart' show HtmlImport;

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

  @Observe("size")
  handleSize(int newSize) {
    $['icon'].style
      ..width = "${newSize}px"
      ..height = "${newSize}px"
      ..borderRadius = "${newSize * 0.5}px"
      ..lineHeight = "${newSize}px"
      ..fontSize = "${newSize * 0.5}px";
  }

  @Observe("user.surname,user.givenName,user.nickname")
  makeInitials(String surname, String givenName, String nickname) async {
    set('initials', user.initials);
  }

  @Observe("user")
  handleUser(User newUser) async {
    print("Hey, ${user.backgroundString}");
    $['icon'].style
      ..background = user.backgroundString;
  }

  @Observe("user.favoriteColors.splices")
  handleColors(Map changeRecord) async {
    if (changeRecord == null) {
      return;
    }
    $['icon'].style
      ..background = user.backgroundString;
  }


}
