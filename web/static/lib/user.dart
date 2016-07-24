import 'package:polymer/polymer.dart';

class User extends JsProxy {
	String surname;
	String givenName;
	String nickname;
	String favoriteTeam; // TODO make this a new object.
	String pictureURL;

	User(Map<String, dynamic> obj) {
		this.surname = obj["Surname"];
		this.givenName = obj["GivenName"];
		this.nickname = obj["Nickname"];
		this.favoriteTeam = obj["FavoriteTeam"];
		this.pictureURL = obj["PictureURL"];
	}
}
