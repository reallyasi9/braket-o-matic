package net.exclaimindustries.paste.braket.client;

import java.util.Collection;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UserServiceAsync {

	void getUsers(AsyncCallback<Collection<User>> callback);

	void getRegisteredUsers(AsyncCallback<Collection<User>> callback);

	void storeUsers(Collection<User> users, AsyncCallback<Void> callback);

	void storeUser(User user, AsyncCallback<String> callback);

	void registerUser(User user, AsyncCallback<Long> callback);

	void unregisterUser(User user, AsyncCallback<Void> callback);

	void deleteUsers(Collection<User> users, AsyncCallback<Void> callback);

	void deleteUser(User user, AsyncCallback<Void> callback);

}
