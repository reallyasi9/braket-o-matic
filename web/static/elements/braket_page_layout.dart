@HtmlImport('braket_page_layout.html')
library braket.braket_page_layout;

import 'package:polymer/polymer.dart';
import 'package:web_components/web_components.dart' show HtmlImport;
import 'braket_page.dart'; // Someone has to load this somewhere...

@PolymerRegister('braket-page-layout')
class BraketPageLayout extends PolymerElement {
  BraketPageLayout.created() : super.created();

  @Property(observer: 'pageChanged')
  String page;

  @reflectable
  void pageChanged(newValue, _) {
      print("Firing $newValue");
      $['pages'].selected = newValue;
  }
}
