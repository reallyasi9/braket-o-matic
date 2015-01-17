package net.exclaimindustries.paste.braket.client;

import java.util.Collection;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("user")
public interface UserService extends RemoteService {

    /**
     * Get all the users in the datastore.
     * 
     * @return A collection of all the users in the datastore.
     * 
     * @throws SecurityException
     *             If the current user is not logged in as an administrator.
     */
    public Collection<User> getUsers();

    /**
     * Get all the registered users for a given tournament.
     * 
     * @return A collection of users registered to the current tournament. If
     *         the currently logged in user is not an administrator, the email
     *         addresses will be stripped from the users in the returned
     *         collection for privacy reasons.
     */
    public Collection<User> getRegisteredUsers();

    /**
     * Update a collection of users all at one time.
     * 
     * @param users
     *            The collection of users to update or create.
     * @return A map of users keyed by their (possibly new) datastore keys.
     * @throws SecurityException
     *             If the current user is not logged in as an administrator.
     */
    public void storeUsers(Collection<User> users);

    /**
     * Update or create a user in the datastore.
     * 
     * @param user
     *            The user to update or create.
     * @return The (possibly new) Id of the stored user.
     * 
     * @throws SecurityException
     *             If the current user is not logged in as an administrator and
     *             is attempting to update a user other than himself.
     */
    public String storeUser(User user);

    /**
     * Register a user for the current tournament.
     * 
     * @param user
     *            The user to register.
     * @return The ID of the selection that was registered.
     * @throws SecurityException
     *             If the current user is not logged in as an administrator.
     */
    public Long registerUser(User user);

    /**
     * Unregister a user from the current tournament.
     * 
     * @param user
     *            The user to register.
     * @throws SecurityException
     *             If the current user is not logged in as an administrator.
     */
    public void unregisterUser(User user);

    /**
     * Delete a collection of users from the datastore.
     * 
     * @param users
     *            The users to delete.
     * 
     * @throws SecurityException
     *             If the current user is not logged in as an administrator.
     */
    public void deleteUsers(Collection<User> users);

    /**
     * Delete a user from the datastore.
     * 
     * @param user
     *            The user to delete.
     * 
     * @throws SecurityException
     *             If the current user is not logged in as an administrator.
     */
    public void deleteUser(User user);
}
