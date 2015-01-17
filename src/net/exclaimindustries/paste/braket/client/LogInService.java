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

package net.exclaimindustries.paste.braket.client;

import java.security.NoSuchAlgorithmException;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client-side stub for the login service
 * 
 * @author paste
 */
@RemoteServiceRelativePath("login")
public interface LogInService extends RemoteService {

    /**
     * Signs a user in using Google's user service.
     * 
     * @param requestUri
     *            The URI whence the request originated, so that a reasonable
     *            login/logout URL can be handed back.
     * @return The user represented by a BraketUser, whether or not the user is
     *         logged in.
     * @throws NoSuchAlgorithmException
     *             If, for whatever reason, you can't access the SHA1 hash
     *             algorithm.
     */
    public User logIn(String requestUri) throws NoSuchAlgorithmException;
}
