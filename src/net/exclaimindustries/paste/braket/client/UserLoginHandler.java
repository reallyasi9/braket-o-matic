package net.exclaimindustries.paste.braket.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import net.exclaimindustries.paste.braket.client.ui.BraketAppLayout;
import net.exclaimindustries.paste.braket.client.ui.FadeAnimation;
import net.exclaimindustries.paste.braket.client.ui.UserLoginButton;
import net.exclaimindustries.paste.braket.client.ui.UserStatusPanel;

/**
 * A class that performs all the UI-based login actions, including handling
 * login/logout clicks, building and displaying the user status, and updating
 * the UI when events that change the user status come down the pipe.
 * 
 * @author paste
 * 
 */
public class UserLoginHandler {

    private BraketAppLayout appLayout;

    private UserStatusPanel userStatusPanel;

    private UserLoginButton userLoginButton;

    private BraketUser currentUser;

    private LoginServiceAsync loginServiceRPC = GWT.create(LoginService.class);

    private Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    public UserLoginHandler(BraketAppLayout braketAppLayout) {
        appLayout = braketAppLayout;
        currentUser = new BraketUser();
    }

    /**
     * Attempt to sign a user in.
     * 
     * @throws IllegalStateException
     *             if a user is already signed in.
     */
    public void signIn() {

        // Someone is already logged in!
        if (currentUser.isSignedIn()) {
            throw new IllegalStateException("user already signed in");
        }

        loginServiceRPC.signIn(GWT.getHostPageBaseURL(),
                new AsyncCallback<BraketUser>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        handleFailure(caught);
                    }

                    @Override
                    public void onSuccess(BraketUser result) {
                        currentUser = result;
                        if (result.isSignedIn()) {
                            logger.log(Level.INFO, "user [" + result.getId()
                                    + "] logged in");
                            displayUserStatusPanel();
                        } else {
                            logger.log(Level.INFO, "user not logged in");
                            displaySignInButton();
                        }
                    }

                });

    }

    protected void displaySignInButton() {
        // TODO Auto-generated method stub
        // Make a sign in button
        userLoginButton = new UserLoginButton(currentUser);
        
        // Add it where it belongs
        appLayout.getHeader().getPanel().add(userLoginButton);
    }

    /**
     * Make the UserStatusPanel and display it
     */
    protected void displayUserStatusPanel() {

        // Lock all ability to log in or out
        lockControls();

        // Make a status panel
        userStatusPanel = new UserStatusPanel(currentUser);

        // Add the panel to the header of the layout
        appLayout.getHeader().getPanel().add(userStatusPanel);

        // Fade the panel in
        FadeAnimation animation = new FadeAnimation(userStatusPanel.getElement()) {
            @Override
            protected void onComplete() {
                unlockControls();
            }
        };
        animation.fadeIn(500);
    }

    public void signOut() {

        // No one is logged in!
        if (!currentUser.isSignedIn()) {
            throw new IllegalStateException("no user currently signed in");
        }

        // Remove the status panel
        removeUserStatusPanel();

        // TODO Do the logging out, handle the response

    }

    protected void removeUserStatusPanel() {
        // Lock all ability to log in or out
        lockControls();

        // Add the panel to the header of the layout
        appLayout.getHeader().getPanel().add(userStatusPanel);

        // Fade the panel out
        FadeAnimation animation = new FadeAnimation(userStatusPanel.getElement()) {
            @Override
            protected void onComplete() {
                appLayout.getHeader().getPanel().remove(userStatusPanel);
                unlockControls();
            }
        };
        animation.fadeOut(500);
    }

    protected void lockControls() {

    }

    protected void unlockControls() {

    }

    protected void handleFailure(Throwable caught) {
        logger.log(Level.SEVERE,
                "failure logging in: " + caught.getLocalizedMessage());
        // TODO Do something else?
    }

    // TODO Figure out how to listen for the score and rank updates
}
