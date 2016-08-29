@HtmlImport('braket_page_layout.html')
library braket.braket_page_layout;

import 'package:polymer/polymer.dart';
import 'package:web_components/web_components.dart' show HtmlImport;

import 'package:polymer_elements/iron_pages.dart';
import 'braket_page.dart';
import 'standings_page.dart';
import 'predict_page.dart';
import 'excite_page.dart';

@PolymerRegister('braket-page-layout')
class BraketPageLayout extends PolymerElement {
  BraketPageLayout.created() : super.created();

  @property
  String page;
}