/**
 * 
 */
package net.exclaimindustries.paste.braket.client.ui;

import net.exclaimindustries.paste.braket.client.BraketUser;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author paste
 * 
 */
public class BraketMenu extends Composite {

    private static BraketMenuUiBinder uiBinder = GWT
            .create(BraketMenuUiBinder.class);

    interface BraketMenuUiBinder extends UiBinder<Widget, BraketMenu> {
    }

    public BraketMenu(BraketUser user) {
        initWidget(uiBinder.createAndBindUi(this));
        // TODO Change the menu based on the user
    }

}
