import 'package:polymer/polymer.dart';
import 'package:exportable/exportable.dart';

@export
class User extends JsProxy with Exportable {
  @export
  @reflectable
  String surname = "";

  @export
  @reflectable
  String givenName = "";

  @export
  @reflectable
  String nickname = "";

  @export
  @reflectable
  int favoriteTeamID = 170; // go Blue

}
