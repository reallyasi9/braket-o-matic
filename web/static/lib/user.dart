import 'package:polymer/polymer.dart';
import 'package:exportable/exportable.dart';
import 'team.dart';

@export
class User extends JsProxy with Exportable{

	@export
	String surname;

	@export
	String givenName;

	@export
	String nickname;

	@export
	Team favoriteTeam;

	String get displayName => (nickname.isNotEmpty) ? '${givenName} "${nickname}" ${surname}'.trim() : '${givenName} ${surname}'.trim();

	String get initial => (givenName.isNotEmpty) ? givenName.substring(0, 1) : (surname.isNotEmpty) ? surname.substring(0, 1) : (nickname.isNotEmpty) ? nickname.substring(0, 1) : "?";

	User(String this.surname, String this.givenName, String this.nickname, String this.favoriteTeam);

	User.fromMap(Map<String, dynamic> obj) {
		this.surname = obj["Surname"];
		this.givenName = obj["GivenName"];
		this.nickname = obj["Nickname"];
		this.favoriteTeam = obj["FavoriteTeam"];
	}
}
