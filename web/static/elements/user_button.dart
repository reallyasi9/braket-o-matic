@HtmlImport('user_button.html')
library braket.user_button;

import 'package:polymer/polymer.dart';
import 'package:web_components/web_components.dart' show HtmlImport;

import 'package:polymer_elements/color.dart';
import 'package:polymer_elements/iron_flex_layout.dart';
import 'package:polymer_elements/paper_button.dart';
import 'user_icon.dart';
import '../lib/user.dart';

@PolymerRegister('user-button')
class UserButton extends PolymerElement {
  UserButton.created() : super.created();

  @Property(notify: true)
  User user;

  @property
  String displayName;

  @Observe('user.*')
  handleUserChange(Map changeRecord){
    set('displayName', user.displayName);
  }

}
