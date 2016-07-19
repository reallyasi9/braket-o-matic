import 'package:json_object/json_object.dart';

abstract class User {
	String Surname;
    String GivenName;
    String Nickname;
    String Email;
    DateTime FirstAccessDate;
    DateTime LastAccessDate;
    FavoriteTeam String; // TODO make this a new object.
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
