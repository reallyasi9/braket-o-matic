import 'package:polymer/polymer.dart';
import 'package:exportable/exportable.dart';
import 'team.dart';

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

  static RegExp _nickRE = new RegExp(
      r"^(\s+|^[\u0022\u0027\u2018\u2019\u201c\u201d\u0060\u00b4]+)|(\s+|^[\u0022\u0027\u2018\u2019\u201c\u201d\u0060\u00b4]+)$");

  String get displayName {
    String mySurname = surname.trim();
    String myGivenName = givenName.trim();
    String myNickname = nickname.replaceAll(_nickRE, "");

    if (myNickname.isNotEmpty) {
      return '$myGivenName "$myNickname" $mySurname'.trim();
    }
    String fullName = '$myGivenName $mySurname'.trim();
    if (fullName.isEmpty) {
      return 'an unnamed user';
    }
    return fullName;
  }

  void cleanName() {
    surname = surname.trim();
    givenName = givenName.trim();
    nickname = nickname.replaceAll(_nickRE, "");
  }

  String get initials {
    String mySurname = surname.trim().toUpperCase();
    String myGivenName = givenName.trim().toUpperCase();
    String myNickname = nickname.replaceAll(_nickRE, "").toUpperCase();

    if (myGivenName.isNotEmpty && mySurname.isNotEmpty) {
      return myGivenName.substring(0,1) + mySurname.substring(0,1);
    } else if (myGivenName.isNotEmpty) {
      return myGivenName.substring(0,1);
    } else if (mySurname.isNotEmpty) {
      return mySurname.substring(0,1);
    } else if (myNickname.isNotEmpty) {
      return myNickname.substring(0,1);
    }
    return "?";
  }
}
