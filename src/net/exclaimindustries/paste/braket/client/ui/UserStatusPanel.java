package net.exclaimindustries.paste.braket.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class UserStatusPanel extends Composite {

    private static UserStatusPanelUiBinder uiBinder = GWT
            .create(UserStatusPanelUiBinder.class);

    interface UserStatusPanelUiBinder extends UiBinder<Widget, UserStatusPanel> {
    }

    public UserStatusPanel() {
        initWidget(uiBinder.createAndBindUi(this));
    }

}
