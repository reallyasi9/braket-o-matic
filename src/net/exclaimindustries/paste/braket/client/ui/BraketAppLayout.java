/**
 * 
 */
package net.exclaimindustries.paste.braket.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
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

    public BraketAppLayout() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    private Widget centerWidget;

    @UiField
    DockLayoutPanel layoutPanel;

    @UiField
    BraketHeader header;

    public DockLayoutPanel getLayoutPanel() {
        return layoutPanel;
    }

    public void addMenu(BraketMenu menu) {
        if (centerWidget != null) {
            layoutPanel.remove(centerWidget);
        }
        layoutPanel.addNorth(menu, 32.);
        if (centerWidget != null) {
            layoutPanel.add(centerWidget);
        }
    }

    public void setCenter(Widget widget) {
        if (centerWidget != null) {
            layoutPanel.remove(centerWidget);
        }
        layoutPanel.add(widget);
        centerWidget = widget;
    }

    public void addToHeader(Widget widget) {
        header.add(widget);
    }

}
