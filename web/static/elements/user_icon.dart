@HtmlImport('user_icon.html')
library braket.user_icon;

import 'package:polymer/polymer.dart';
import 'package:web_components/web_components.dart' show HtmlImport;

import '../lib/user.dart';
import '../lib/team.dart';

@PolymerRegister('user-icon')
class UserIcon extends PolymerElement {
  UserIcon.created() : super.created();

  @Property(notify: true)
  User user;

  @property
  String initials;

  @property
  int size = 24;

  @property
  String iconStyle;

  ready() {
    set('iconStyle', "background: linear-gradient(45deg, rgb(30, 30, 30), rgb(200, 200, 200)); width: ${size}px; height: ${size}px; border-radius: ${size * 0.5}px; line-height: ${size}px; font-size: ${size * 0.5}px;");
  }

  @Observe("user.surname,user.givenName,user.nickname")
  makeInitials(String surname, String givenName, String nickname) {
    set('initials', user.initials);
  }

  @Observe("user.favoriteTeam")
  updateColors(Team newTeam) {
    if (newTeam == null) {
      return;
    }
    set('iconStyle', "background: linear-gradient(45deg, rgb(30, 30, 30), rgb(200, 200, 200)); width: ${size}px; height: ${size}px; border-radius: ${size * 0.5}px; line-height: ${size}px; font-size: ${size * 0.5}px; ${newTeam.backgroundString}");
  }

}
