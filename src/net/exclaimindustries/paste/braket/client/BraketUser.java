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
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Serialize;

/**
 * A class that stores information about a user.
 * 
 * @author paste
 */
@Entity
@Cache
public class BraketUser implements IsSerializable {

    /**
     * The ID of the user as stored in the database. Note that this ID is stored
     * as a <code>String</code> instead of a <code>Long</code> like most other
     * IDs in the Braket. This is to allow use of the ID from Google's
     * <code>UserService</code>. @see <a href=
     * "https://developers.google.com/appengine/docs/java/javadoc/com/google/appengine/api/users/User#getUserId()"
     * >com.google.appengine.api.users.User</code>.
     */
    @Id
    private String id = null;

    /**
     * A set of selections belonging to the user, keyed by tournament key (so
     * that each user can only have one selection per tournament).
     */
    @Serialize
    private Map<Long, Long> selections = new HashMap<Long, Long>();

    /**
     * The full userName of the user.
     */
    private UserName userName = new UserName();

    /**
     * The user's picture, encoded as a JPEG.
     */
    private byte[] picture = null;

    /**
     * The user's email address.
     */
    private String email = null;

    /**
     * The log-in link for the user.
     */
    @Ignore
    private String logInLink = null;

    /**
     * The log-out link for the user.
     */
    @Ignore
    private String logOutLink = null;

    /**
     * Whether or not this is a loged-in user.
     */
    @Ignore
    private boolean isLoggedIn = false;

    /**
     * Whether or not the user is an admin. DO NOT TRUST THIS. This is intended
     * for use only in the UI, not for actually determining if the user is an
     * admin or not!
     */
    @Ignore
    private boolean isAdmin = false;

    /**
     * Default constructor.
     */
    public BraketUser() {
        super();
    }

    /**
     * Constructor from a Google AppEngine <code>User</code>.
     * 
     * @param userId
     *            A unique string identifying the user. It's suggested that the
     *            raw user ID not be used, so a hashed version is best.
     * @throws NoSuchAlgorithmException
     *             If for some reason you don't have access to the SAH1
     *             algorithm.
     */
    public BraketUser(String userId) throws NoSuchAlgorithmException {
        // ID needs to be protected, so it gets sent pre-hashed.
        id = userId;
        userName.setNickname("Unidentified Doofus");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<Long, Long> getSelections() {
        return new HashMap<Long, Long>(selections);
    }

    public void setSelections(Map<Long, Long> selections) {
        this.selections = new HashMap<Long, Long>(selections);
    }

    public void addSelection(Long tournamentId, Long selectionId) {
        selections.put(tournamentId, selectionId);
    }

    public void removeSelection(Long tournamentId) {
        selections.remove(tournamentId);
    }

    public Long getSelection(Long tournamentId) {
        return selections.get(tournamentId);
    }

    public UserName getName() {
        return (UserName) userName.clone();
    }

    public void setName(UserName userName) {
        this.userName = (UserName) userName.clone();
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogInLink() {
        return logInLink;
    }

    public void setLogInLink(String logInLink) {
        this.logInLink = logInLink;
    }

    public String getLogOutLink() {
        return logOutLink;
    }

    public void setLogOutLink(String logOutLink) {
        this.logOutLink = logOutLink;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("BraketUser [id=").append(id).append(", userName=")
                .append(userName).append(", picture=").append(picture)
                .append(", email=").append(email).append(", isLoggedIn=")
                .append(isLoggedIn).append("]");
        return builder.toString();
    }

}
