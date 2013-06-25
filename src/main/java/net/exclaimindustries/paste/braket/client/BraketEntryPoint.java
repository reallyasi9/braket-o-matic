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

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class BraketEntryPoint implements EntryPoint, ValueChangeHandler<String> {

	// History tokens
	public static class HistoryToken {
		public static final String ABOUT = "about";
		public static final String BRAKET = "braket";
		public static final String ADMIN = "admin";
		public static final String USER_OPTIONS = "user-options";
		public static final String LEADERBOARD = "leaderboard";
		public static final String EDIT_USERS = "edit-users";
		public static final String EDIT_TEAMS = "edit-teams";
		public static final String EDIT_GAMES = "edit-games";
		public static final String EXCITE_O_MATIC = "excite-o-matic";
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		String eventString = event.getValue();
		if (eventString.equals(HistoryToken.ABOUT)) {
			// TODO
		} else if (eventString.equals(HistoryToken.BRAKET)) {
			// TODO
		} else if (eventString.equals(HistoryToken.ADMIN)) {
			// TODO
		} else if (eventString.equals(HistoryToken.USER_OPTIONS)) {
			// TODO
		} else if (eventString.equals(HistoryToken.LEADERBOARD)) {
			// TODO
		} else if (eventString.equals(HistoryToken.EDIT_USERS)) {
			// TODO
		} else if (eventString.equals(HistoryToken.EDIT_TEAMS)) {
			// TODO
		} else if (eventString.equals(HistoryToken.EDIT_GAMES)) {
			// TODO
		} else if (eventString.equals(HistoryToken.EXCITE_O_MATIC)) {
			// TODO
		} else {
			// TODO
		}
	}

	@Override
	public void onModuleLoad() {

		// TODO Check for initial configuration conditions
		
		
		// Check the history token
		String token = History.getToken();
		if (token.length() == 0) {
			// Default token
			token = HistoryToken.ABOUT;
		}

		// TODO Determine if the user is logged in and update content as needed
		LoginServiceAsync loginService = GWT.create(LoginService.class);
		loginService.signIn(GWT.getModuleBaseURL(),
				new AsyncCallback<BraketUser>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(BraketUser result) {
						// TODO Auto-generated method stub

					}

				});

		// Add self as a listener
		History.addValueChangeHandler(this);

		// Fire the history event
		History.fireCurrentHistoryState();
	}

}
