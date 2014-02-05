package net.exclaimindustries.paste.braket.client.ui;

import net.exclaimindustries.paste.braket.client.BraketUser;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class UserLoginButton extends Composite {

    private static UserLoginButtonUiBinder uiBinder = GWT
            .create(UserLoginButtonUiBinder.class);

    interface UserLoginButtonUiBinder extends UiBinder<Widget, UserLoginButton> {
    }

    @UiField
    Button signInButton;

    private String signInURL;

    public UserLoginButton(BraketUser user) {
        initWidget(uiBinder.createAndBindUi(this));
        signInURL = user.getSignInLink();
    }

    @UiHandler("signInButton")
    void signIn(ClickEvent event) {
        Window.Location.assign(signInURL);
    }

}
