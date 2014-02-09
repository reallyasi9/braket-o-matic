package net.exclaimindustries.paste.braket.server;

import net.exclaimindustries.paste.braket.client.BraketUser;
import net.exclaimindustries.paste.braket.shared.UserNotAdminException;
import net.exclaimindustries.paste.braket.shared.UserNotLoggedInException;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/**
 * A set of helper functions wrapping Google's UserService.
 * 
 * @author paste
 * 
 */
public final class LogInServiceHelper {

    static public BraketUser assertLoggedIn() throws UserNotLoggedInException {
        UserService us = UserServiceFactory.getUserService();
        if (!us.isUserLoggedIn()) {
            throw new UserNotLoggedInException(
                    "you need to be logged in to use this feature");
        }
        return new BraketUser();
    }

    static public void assertAdmin() throws UserNotLoggedInException,
            UserNotAdminException {
        UserService us = UserServiceFactory.getUserService();
        if (!us.isUserLoggedIn()) {
            throw new UserNotLoggedInException(
                    "you need to be logged in to use this feature");
        }
        if (!us.isUserAdmin()) {
            throw new UserNotAdminException(
                    "you need to be an administrator to use this feature");
        }
    }
}
