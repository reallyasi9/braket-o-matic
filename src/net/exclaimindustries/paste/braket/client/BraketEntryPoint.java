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

import java.util.logging.Level;
import java.util.logging.Logger;

import net.exclaimindustries.paste.braket.client.TournamentService.TournamentCollection;
import net.exclaimindustries.paste.braket.client.resources.UiConstants;
import net.exclaimindustries.paste.braket.client.ui.BraketAdminLayout;
import net.exclaimindustries.paste.braket.client.ui.BraketAppLayout;
import net.exclaimindustries.paste.braket.client.ui.BraketMenu;
import net.exclaimindustries.paste.braket.client.ui.LogInPage;
import net.exclaimindustries.paste.braket.client.ui.Toast;
import net.exclaimindustries.paste.braket.client.ui.UserLogInButton;
import net.exclaimindustries.paste.braket.client.ui.UserStatusPanel;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class BraketEntryPoint implements EntryPoint, ValueChangeHandler<String> {

	// Error pages
	private static String ERROR_500 = "500.html";

	// Logger specific to this object
	private Logger logger = Logger.getLogger(this.getClass().getSimpleName());

	// Layout
	private BraketAppLayout layout = new BraketAppLayout();

	// Local cached values
	private BraketUser currentUser;
	private TournamentCollection currentTournamentCollection;

	// Async services

	// Callbacks
	/**
	 * A callback that creates the braket display after a user has logged in.
	 * TODO: Add the UI elements required for the display
	 */
	private RunAsyncCallback braketDisplayCallback = new RunAsyncCallback() {

		@Override
		public void onFailure(Throwable reason) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onSuccess() {
			// TODO Auto-generated method stub
			layout.setCenter(new HTMLPanel("braket goes here"));
		}

	};

	/**
	 * A callback called immediately after the user goes through the log-in
	 * process.
	 */
	private AsyncCallback<BraketUser> userLogInCallback = new AsyncCallback<BraketUser>() {

		/**
		 * Failure means a server error happend (not that the user failed to log
		 * in).
		 */
		@Override
		public void onFailure(Throwable caught) {
			// This is bad. Login should never fail.
			logger.log(Level.SEVERE,
					"login failed: " + caught.getLocalizedMessage());
			Window.Location.assign(GWT.getHostPageBaseURL() + ERROR_500);
		}

		/**
		 * Success means that the user returned from Google's log-in page and a
		 * BraketUser object representing the result has been created.
		 * 
		 * @param result
		 *            An object representing the result of the log-in operation.
		 *            The method <code>result.isLoggedIn()</code> can be queried
		 *            to determine whether or not a recognized user is logged
		 *            in.
		 */
		@Override
		public void onSuccess(BraketUser result) {
			// TODO Auto-generated method stub
			currentUser = result;

			// Fire the history event while everything else is loading
			History.fireCurrentHistoryState();

			if (currentUser.isLoggedIn()) {
				logger.log(Level.INFO, "user [" + currentUser.getId()
						+ "] logged in");

				// Make a status panel
				displayUserStatusPanel();

				// Attempt to make the menu panel
				getCurrentTournament();

			} else {
				// Note that, if not logged in, there is no current
				// tournament!
				logger.log(Level.INFO, "user not logged in");
				displaySignInButton();
			}

		}

	};

	private AsyncCallback<TournamentCollection> currentTournamentCallback = new AsyncCallback<TournamentCollection>() {

		@Override
		public void onFailure(Throwable caught) {
			logger.log(Level.SEVERE, "getting current tournament failed: "
					+ caught.getLocalizedMessage());
			// TODO deal with this?
		}

		@Override
		public void onSuccess(TournamentCollection result) {
			currentTournamentCollection = result;

			// Make the menu, given we now know if the tournament has
			// started or not.
			displayMenu();
		}

	};

	/**
	 * Process the incoming history token and route the user appropriately.
	 * These actions should only result in asynchronous loads. Other things
	 * happen after the call to <code>History.fireCurrentHistoryState()</code>
	 * that, if circumvented with a synchronous load here, might result in bad
	 * behavior of the site.
	 */
	@Override
	public void onValueChange(ValueChangeEvent<String> event) {

		// If you're not logged in, all you get is the log-in page

		if (currentUser == null || !currentUser.isLoggedIn()) {
			// Make the log-in info stuff
			logger.log(Level.INFO, "user not logged in: displaying login info");
			layout.setCenter(new LogInPage());
			return;
		}

		String eventString = event.getValue();
		logger.log(Level.INFO, "received history event string [" + eventString
				+ "]");
		if (eventString.equals(UiConstants.HistoryToken.ABOUT)) {
			// TODO Make an "About" page
			logger.log(Level.INFO, "loading about page");
		} else if (eventString.isEmpty()
				|| eventString
						.equals(UiConstants.HistoryToken.TOURNAMENT_STATUS)) {
			// TODO Make a "Tournament Statue" page
			logger.log(Level.INFO, "loading tournament status page");
		} else if (eventString.equals(UiConstants.HistoryToken.MY_BRACKET)) {
			logger.log(Level.INFO, "loading user bracket page");
			GWT.runAsync(braketDisplayCallback);
		} else if (eventString.equals(UiConstants.HistoryToken.ADMIN)) {
			logger.log(Level.INFO, "loading admin page");
			// Make sure the user is logged in as an admin
			if (!currentUser.isAdmin()) {
				Toast.showErrorToast("You need to be an administrator to use this function");
			} else {
				layout.setCenter(new BraketAdminLayout());
			}
		} else if (eventString.equals(UiConstants.HistoryToken.USER_OPTIONS)) {
			logger.log(Level.INFO, "loading user options page");
			// TODO Make a "User Options" page
		} else if (eventString.equals(UiConstants.HistoryToken.LEADERBOARDS)) {
			logger.log(Level.INFO, "loading leaderboard page");
			// TODO Make a "Leaderboard" page.
		} else if (eventString.equals(UiConstants.HistoryToken.EDIT_USERS)) {
			logger.log(Level.INFO, "loading edit users dialog");
			// TODO Make an "Edit Users" page (perhaps a tab in Admin?)
		} else if (eventString.equals(UiConstants.HistoryToken.EDIT_TEAMS)) {
			logger.log(Level.INFO, "loading edit teams dialog");
			// TODO Make an "Edit Teams" page (perhaps a tab in Admin?)
		} else if (eventString.equals(UiConstants.HistoryToken.EDIT_GAMES)) {
			logger.log(Level.INFO, "loading edit games dialog");
			// TODO Make an "Edit Games" page (perhaps a tab in Admin?)
		} else if (eventString.equals(UiConstants.HistoryToken.EXCITE_O_MATIC)) {
			logger.log(Level.INFO, "loading excite-o-matic");
			// TODO Make an "Excite-o-Matic" page
		} else {
			logger.log(Level.WARNING, "history event [" + eventString
					+ "] not understood");
			// TODO Reconsider whether doing nothing here is the right way to end.
		}
	}

	/**
	 * After getting the current tournament and current user, build the menu.
	 */
	private void displayMenu() {
		// The current tournament might be null.
		layout.addMenu(new BraketMenu(currentTournamentCollection
				.getTournament(), currentUser));
	}

	/**
	 * Asynchronously get the current tournament information and use it to build
	 * the rest of the UI
	 */
	private void getCurrentTournament() {

		// Get the tournament and attempt to build the menu
		TournamentServiceAsync tournamentServiceRPC = GWT
				.create(TournamentService.class);
		tournamentServiceRPC.getCurrentTournament(currentTournamentCallback);

	}

	/**
	 * Make the UserStatusPanel and display it
	 */
	private void displayUserStatusPanel() {

		// Make a status panel
		UserStatusPanel userStatusPanel = new UserStatusPanel(currentUser);

		// Add the panel to the header of the layout
		layout.addToHeader(userStatusPanel);

		// Fade the panel in
		// FadeAnimation animation = new
		// FadeAnimation(userStatusPanel.getElement());
		// animation.fadeIn(500);
	}

	/**
	 * Make a login button and display it.
	 */
	private void displaySignInButton() {

		// Add button where it belongs
		layout.addToHeader(new UserLogInButton(currentUser));

	}

	/**
	 * Initialize the module and get ready for fun!
	 */
	@Override
	public void onModuleLoad() {

		// Register myself as a history listener
		History.addValueChangeHandler(this);

		// Add the application layout to the root panel
		RootLayoutPanel.get().add(layout);

		// Attempt login and get the current user
		LogInServiceAsync logInServiceRPC = GWT.create(LogInService.class);
		logInServiceRPC.logIn(Window.Location.getHref(), userLogInCallback);

		// The history event cannot be fired until after the user is logged in
		// (or not)
	}
}
