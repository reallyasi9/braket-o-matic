package net.exclaimindustries.paste.braket.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import net.exclaimindustries.paste.braket.client.ui.BraketAppLayout;
import net.exclaimindustries.paste.braket.client.ui.BraketMenu;
import net.exclaimindustries.paste.braket.client.ui.FadeAnimation;
import net.exclaimindustries.paste.braket.client.ui.LogInPage;
import net.exclaimindustries.paste.braket.client.ui.UserLogInButton;
import net.exclaimindustries.paste.braket.client.ui.UserStatusPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * A class that performs all the UI-based logIn actions, including handling
 * logIn/logOut clicks, building and displaying the user status, and updating
 * the UI when events that change the user status come down the pipe.
 * 
 * @author paste
 * 
 */
public class UserLogInWrangler {

    private BraketAppLayout appLayout;

    private BraketTournament tournament;

    private LogInServiceAsync logInServiceRPC = GWT.create(LogInService.class);

    private Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    private AsyncCallback<BraketUser> logInAsyncCallback = new AsyncCallback<BraketUser>() {

        @Override
        public void onFailure(Throwable caught) {
            handleFailure(caught);
        }

        @Override
        public void onSuccess(BraketUser result) {
            if (result.isLoggedIn()) {
                logger.log(Level.INFO, "user [" + result.getId()
                        + "] logged in");
                displayUserStatusPanel(result);
                displayMenu(result);
                // Take the user wherever he or she was going
                History.fireCurrentHistoryState();
            } else {
                logger.log(Level.INFO, "user not logged in");
                displaySignInButton(result);
            }
        }

    };

    public UserLogInWrangler(BraketAppLayout braketAppLayout,
            BraketTournament tournament) {
        this.appLayout = braketAppLayout;
        this.tournament = tournament;
    }

    protected void displayMenu(BraketUser currentUser) {
        appLayout.addMenu(new BraketMenu(tournament, currentUser));
    }

    /**
     * Attempt to log a user in.
     */
    public void logIn() {
        logInServiceRPC.logIn(Window.Location.getHref(), logInAsyncCallback);
    }

    protected void displaySignInButton(BraketUser currentUser) {

        // Add button where it belongs
        appLayout.addToHeader(new UserLogInButton(currentUser));

        // Make the log-in info stuff
        appLayout.setCenter(new LogInPage());
    }

    /**
     * Make the UserStatusPanel and display it
     */
    protected void displayUserStatusPanel(BraketUser currentUser) {

        // Make a status panel
        UserStatusPanel userStatusPanel = new UserStatusPanel(currentUser);

        // Add the panel to the header of the layout
        appLayout.addToHeader(userStatusPanel);

        // Fade the panel in
        FadeAnimation animation = new FadeAnimation(
                userStatusPanel.getElement());
        animation.fadeIn(500);
    }

    protected void handleFailure(Throwable caught) {
        logger.log(Level.SEVERE,
                "failure logging in: " + caught.getLocalizedMessage());
        // TODO Do something else?
    }

}
