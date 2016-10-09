import 'package:polymer/polymer.dart';
import 'package:exportable/exportable.dart';

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

    if (colors.isEmpty) {
      _backgroundString = "background: #888888";
    } else if (colors.length == 1) {
      _backgroundString = "background: ${colors[0]}";
    } else {
      List<String> cStrings = [];
      cStrings.add(colors[0]);
      cStrings.add("${colors[0]} 50%");
      cStrings.add("${colors[1]} 50%");
      int step = (50 / (colors.length - 1)).floor();
      for (int i = 2; i < colors.length; i++) {
        cStrings.add("${colors[i-1]} ${50+step*(i-1)}%");
        // cStrings.add("black ${step*i}%");
        cStrings.add("${colors[i]} ${50+step*(i-1)}%");
      }
      String c = cStrings.join(",");
      _backgroundString = "background: linear-gradient(100deg,$c)";
    }

    return _backgroundString;
  }
}
