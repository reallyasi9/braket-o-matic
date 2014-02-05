package net.exclaimindustries.paste.braket.client.ui;

import net.exclaimindustries.paste.braket.client.BraketUser;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class UserSignInButton extends Composite {

    private static UserSignInButtonUiBinder uiBinder = GWT
            .create(UserSignInButtonUiBinder.class);

    interface UserSignInButtonUiBinder extends UiBinder<Widget, UserSignInButton> {
    }

    @UiField
    Anchor signInAnchor;

    public UserSignInButton(BraketUser user) {
        initWidget(uiBinder.createAndBindUi(this));
        signInAnchor.setHref(user.getSignInLink());
    }

}
