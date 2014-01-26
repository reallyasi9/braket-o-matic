/**
 * 
 */
package net.exclaimindustries.paste.braket.client.ui;

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

    /**
     * Because this class has a default constructor, it can be used as a binder
     * template. In other words, it can be used in other *.ui.xml files as
     * follows: <ui:UiBinder xmlns:ui="urn:ui:com.google.gwt.uibinder"
     * xmlns:g="urn:import:**user's package**">
     * <g:**UserClassName**>Hello!</g:**UserClassName> </ui:UiBinder> Note that
     * depending on the widget that is used, it may be necessary to implement
     * HasHTML instead of HasText.
     */
    public BraketMenu() {
        initWidget(uiBinder.createAndBindUi(this));
    }

}