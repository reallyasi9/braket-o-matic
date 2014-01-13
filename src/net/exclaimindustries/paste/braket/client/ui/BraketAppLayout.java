/**
 * 
 */
package net.exclaimindustries.paste.braket.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author paste
 *
 */
public class BraketAppLayout extends Composite {

    private static BraketAppLayoutUiBinder uiBinder = GWT
            .create(BraketAppLayoutUiBinder.class);

    interface BraketAppLayoutUiBinder extends UiBinder<Widget, BraketAppLayout> {
    }

    /**
     * Because this class has a default constructor, it can
     * be used as a binder template. In other words, it can be used in other
     * *.ui.xml files as follows:
     * <ui:UiBinder xmlns:ui="urn:ui:com.google.gwt.uibinder"
     *   xmlns:g="urn:import:**user's package**">
     *  <g:**UserClassName**>Hello!</g:**UserClassName>
     * </ui:UiBinder>
     * Note that depending on the widget that is used, it may be necessary to
     * implement HasHTML instead of HasText.
     */
    public BraketAppLayout() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @UiField
    BraketHeader header;
    
    @UiField
    BraketMenu menu;
    
    @UiField
    HTMLPanel mainPanel;
    
    public BraketHeader getHeader() {
        return header;
    }
    
    public BraketMenu getMenu() {
        return menu;
    }
    
    public HTMLPanel getMainPanel() {
        return mainPanel;
    }

}
