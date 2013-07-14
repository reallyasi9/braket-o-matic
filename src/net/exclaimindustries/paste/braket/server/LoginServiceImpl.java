/**
 * This file is part of braket-o-matic.
 *
 * braket-o-matic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * braket-o-matic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with braket-o-matic.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.exclaimindustries.paste.braket.server;

import java.security.NoSuchAlgorithmException;

import net.exclaimindustries.paste.braket.client.BraketUser;
import net.exclaimindustries.paste.braket.client.LoginService;

import org.apache.commons.codec.digest.DigestUtils;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Key;

public class LoginServiceImpl extends RemoteServiceServlet implements LoginService {

    /**
     * Generated
     */
    private static final long serialVersionUID = 1L;

    @Override
    public BraketUser signIn(String requestUri) throws NoSuchAlgorithmException {
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();

        BraketUser braketUser;
        if (user != null) {
            // Look up the user's information

            // The user's ID should be protected
            String userId = DigestUtils.shaHex(user.getUserId() + "//braket-o-matic");

            // Load from the datastore
            Key<BraketUser> key = Key.create(BraketUser.class, userId);
            braketUser = OfyService.ofy().load().key(key).now();

            // If that returns null, then make a user and write it
            if (braketUser == null) {
                braketUser = new BraketUser(userId);
                braketUser.setEmail(user.getEmail());
                OfyService.ofy().save().entity(braketUser);
            }

            braketUser.setSignOutLink(userService.createLogoutURL(requestUri));
            braketUser.setSignedIn(true);
            braketUser.setAdmin(userService.isUserAdmin());
        } else {
            // Not signed in, so don't do anything with the database.
            braketUser = new BraketUser();
            braketUser.setSignInLink(userService.createLoginURL(requestUri));
        }
        return braketUser;
    }
}
