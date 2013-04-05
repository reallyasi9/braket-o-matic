package net.exclaimindustries.paste.braket.client;

import java.util.Collection;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UserServiceAsync {

	void getUsers(AsyncCallback<Collection<BraketUser>> callback);

	void getRegisteredUsers(AsyncCallback<Collection<BraketUser>> callback);

	void storeUsers(Collection<BraketUser> users, AsyncCallback<Void> callback);

	void storeUser(BraketUser user, AsyncCallback<String> callback);

	void registerUser(BraketUser user, AsyncCallback<Long> callback);

	void unregisterUser(BraketUser user, AsyncCallback<Void> callback);

	void deleteUsers(Collection<BraketUser> users, AsyncCallback<Void> callback);

	void deleteUser(BraketUser user, AsyncCallback<Void> callback);

}
