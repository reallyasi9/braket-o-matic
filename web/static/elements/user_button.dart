@HtmlImport('user_button.html')
library braket.user_button;

import 'package:polymer/polymer.dart';
import 'package:web_components/web_components.dart' show HtmlImport;
import 'package:polymer_elements/paper_button.dart';

@PolymerRegister('user-button')
class UserButton extends PolymerElement {
  UserButton.created() : super.created();

  @Property(observer: 'userChanged')
  String userName;

  @reflectable
  void userChanged(newValue, _) {
      print("Hey, got $newValue");
  }
}
