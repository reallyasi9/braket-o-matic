import 'package:json_object/json_object.dart';

abstract class AppEngineUser {
    String Email;
    String AuthDomain;
    bool Admin;
    String ID;
    String ClientID;
    String FederatedIdentity;
    String FederatedProvider;
}

abstract class User {
	bool LoggedIn;
	String LoginURL;
	String LogoutURL;
	AppEngineUser AppEngineUser;
}

class AppEngineUserImpl extends JsonObject implements AppEngineUser {
    AppEngineUserImpl();

    factory AppEngineUserImpl.fromJsonString(String string) {
        return new JsonObject.fromJsonString(string, new AppEngineUserImpl());
    }
}

class UserImpl extends JsonObject implements User {
    UserImpl();

    factory UserImpl.fromJsonString(String string) {
        return new JsonObject.fromJsonString(string, new UserImpl());
    }
}
