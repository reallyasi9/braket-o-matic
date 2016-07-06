@JS("gapi.auth2")
library gapi.auth2;

import "package:js/js.dart";

@JS()
@anonymous
class CurrentUser {
    external GoogleUser get();
}

@JS()
class GoogleAuth {
    external CurrentUser get currentUser;
}

@JS()
class BasicProfile {
    external String getId();
    external String getName();
    external String getGivenName();
    external String getFamilyName();
    external String getImageUrl();
    external String getEmail();
}

@JS()
class AuthResponse {
    external String get access_token;
    external String get id_token;
    external String get login_hint;
    external String get scope;
    external String get expires_in;
    external String get first_issued_at;
    external String get expires_at;
}

@JS()
class GoogleUser {
    external String getId();
    external BasicProfile getBasicProfile();
    external AuthResponse getAuthResponse();
}

@JS()
external GoogleAuth getAuthInstance();
