@HtmlImport('user_button.html')
library braket.user_button;

import 'package:polymer/polymer.dart';
import 'package:web_components/web_components.dart' show HtmlImport;

import 'package:polymer_elements/color.dart';
import 'package:polymer_elements/iron_flex_layout.dart';
import 'package:polymer_elements/paper_button.dart';
import 'package:polymer_elements/iron_image.dart';
import '../lib/user.dart';

@PolymerRegister('user-button')
class UserButton extends PolymerElement {
  UserButton.created() : super.created();

  @Property(notify: true)
  User user;

  @reflectable
  String makeDisplayName(String givenName, String surname, String nickname) {
    if (nickname.isNotEmpty) {
      return '$givenName "$nickname" $surname'.trim();
    }
    String fullName = '$givenName $surname'.trim();
    if (fullName.isEmpty) {
      return 'an unnamed user';
    }
    return fullName;
  }

  @Observe('user.pictureURL')
  updatePictureURL(User newUser) async {
    IronImage ii = $["iron-image"];
    print(newUser.pictureURL);
    ii.setAttribute("src", newUser.pictureURL);
  }
}
