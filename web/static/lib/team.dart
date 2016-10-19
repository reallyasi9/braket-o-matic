import 'package:polymer/polymer.dart';
import 'package:exportable/exportable.dart';
import 'colors.dart';

@export
class Team extends JsProxy with Exportable {
  @reflectable
  @export
  int id;

  @reflectable
  @export
  int seed;

  @reflectable
  @export
  double elo;

  @reflectable
  @export
  String schoolName;

  @reflectable
  @export
  String schoolShortName;

  @reflectable
  @export
  String nickname;

  @reflectable
  @export
  List<String> colors;

  @reflectable
  @export
  String imageName;

  String _backgroundString = "";

  String get backgroundString {
    if (!_backgroundString.isEmpty) {
      return _backgroundString;
    }
    _backgroundString = generateBackground(colors);
    return _backgroundString;
  }
}
