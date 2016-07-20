import 'package:json_object/json_object.dart';

abstract class User {
	String Surname;
    String GivenName;
    String Nickname;
    String Email;
    DateTime FirstAccessDate;
    String FavoriteTeam; // TODO make this a new object.
	String PictureURL;
}

abstract class UserReturnMessage {
    User User;
    String LogoutURL;
}

class UserReturnMessageImpl extends JsonObject implements UserReturnMessage {
    UserReturnMessageImpl();

    factory UserReturnMessageImpl.fromJsonString(String string) {
        return new JsonObject.fromJsonString(string, new UserReturnMessageImpl());
    }
}

class UserImpl extends JsonObject implements User {
    UserImpl();

    factory UserImpl.fromJsonString(String string) {
        return new JsonObject.fromJsonString(string, new UserImpl());
    }
}
