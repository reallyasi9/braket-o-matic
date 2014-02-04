package net.exclaimindustries.paste.braket.client;

import net.exclaimindustries.paste.braket.client.ui.BraketAppLayout;

/**
 * A class that performs all the UI-based login actions, including
 * handling login/logout clicks, building and displaying the user
 * status, and updating the UI when events that change the user
 * status come down the pipe.
 * @author paste
 *
 */
public class UserLoginHandler {
    
    private BraketAppLayout appLayout;

    public UserLoginHandler(BraketAppLayout braketAppLayout) {
        appLayout = braketAppLayout;
    }
    
    public void login() {
        // TODO Redirect to the login page, handle the response
    }
    
    protected void displayUserStatusPanel() {
        // TODO Make the UserStatus Panel and display it
    }
    
    public void logout() {
        // TODO Do the loging out, handle the response
    }
    
    protected void removeUserStatusPanel() {
        // TODO Detach the UserStatusPanel from the layout and destroy it
    }
    
    // TODO Figure out how to listen for the score and rank updates
}
