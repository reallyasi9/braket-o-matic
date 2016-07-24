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

  @property
  String userName = "calculating name...";

  @Property(notify: true, observer: "changePicture")
  String picture;

  @reflectable
  changePicture(String newPictureURL, [_]) async {
    IronImage ii = $["iron-image"];
    print(newPictureURL);
    ii.setAttribute("src", newPictureURL);
  }
}
