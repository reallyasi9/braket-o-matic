package net.exclaimindustries.paste.braket.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * A simple HTMLPanel that contains the welcome/login page that the user sees
 * before logging in.
 * @author paste
 *
 */
public class LogInPage extends Composite {

    private static LogInPageUiBinder uiBinder = GWT.create(LogInPageUiBinder.class);

    interface LogInPageUiBinder extends UiBinder<Widget, LogInPage> {
    }

    public LogInPage() {
        initWidget(uiBinder.createAndBindUi(this));
    }

}
