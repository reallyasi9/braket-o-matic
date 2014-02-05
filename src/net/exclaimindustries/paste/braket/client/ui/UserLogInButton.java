package net.exclaimindustries.paste.braket.client.ui;

import net.exclaimindustries.paste.braket.client.BraketUser;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * A Simple button for logging in.
 * @author paste
 *
 */
public class UserLogInButton extends Composite {

    private static UserLogInButtonUiBinder uiBinder = GWT
            .create(UserLogInButtonUiBinder.class);

    interface UserLogInButtonUiBinder extends UiBinder<Widget, UserLogInButton> {
    }

    @UiField
    Anchor logInAnchor;

    public UserLogInButton(BraketUser user) {
        initWidget(uiBinder.createAndBindUi(this));
        logInAnchor.setHref(user.getLogInLink());
    }

}
