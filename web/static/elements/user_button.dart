@HtmlImport('user_button.html')
library braket.user_button;

import 'package:polymer/polymer.dart';
import 'package:web_components/web_components.dart' show HtmlImport;
import 'package:polymer_elements/paper_button.dart';
import 'package:polymer_elements/iron_image.dart';
import '../lib/user.dart';

@PolymerRegister('user-button')
class UserButton extends PolymerElement {
  UserButton.created() : super.created();

  @Property(observer: 'userObserver')
  User user;

  @reflectable
  String fullDisplayName(User user) {
    if (user.Nickname.isNotEmpty) {
      return '${user.GivenName} "${user.Nickname}" ${user.Surname}';
    }
    String fullName = '${user.GivenName} ${user.Surname}';
    if (fullName.trim().isEmpty) {
      return 'an unnamed bracketeer';
    }
    return fullName;
  }

  @reflectable
  userObserver(User newUser, _) {
    $["iron-image"].setAttribute("src", newUser.PictureURL);
  }

}
