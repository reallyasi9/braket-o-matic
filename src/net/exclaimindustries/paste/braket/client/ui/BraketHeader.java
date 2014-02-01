package net.exclaimindustries.paste.braket.client.ui;

import net.exclaimindustries.paste.braket.client.BraketUser;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

public class BraketHeader extends Composite {

    private static BraketHeaderUiBinder uiBinder = GWT
            .create(BraketHeaderUiBinder.class);

    @UiField
    FlowPanel panel;

    UserStatusPanel userStatusPanel;

    interface BraketHeaderUiBinder extends UiBinder<Widget, BraketHeader> {
    }

    public BraketHeader() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public void setUser(BraketUser user) {
        // Remove the previous user
        removeUser();
        userStatusPanel = new UserStatusPanel(user);
        panel.add(userStatusPanel);
        // Fade the panel in
        FadeAnimation animation = new FadeAnimation(userStatusPanel.getElement());
        animation.fadeIn(500);
    }

    public void removeUser() {
        if (userStatusPanel != null) {
            FadeAnimation animation =
                    new FadeAnimation(userStatusPanel.getElement()) {
                        @Override
                        protected void onComplete() {
                            // Clear the panel on completion
                            panel.remove(userStatusPanel);
                            userStatusPanel = null;
                        }
                    };
            animation.fadeOut(500);
        }
    }

    public UserStatusPanel getUserStatusPanel() {
        return userStatusPanel;
    }

    // @UiField
    // HTML svgContainer;
    //
    // ResourceCallback<SVGResource> callback = new
    // ResourceCallback<SVGResource>() {
    //
    // @Override
    // public void onError(ResourceException e) {
    // // TODO handle errors
    // }
    //
    // @Override
    // public void onSuccess(SVGResource resource) {
    // // Insert the SVG root element into the HTML UI
    // Element div = svgContainer.getElement();
    // OMSVGSVGElement svg = resource.getSvg();
    // if (div.hasChildNodes()) {
    // div.replaceChild(svg.getElement(), div.getFirstChild());
    // } else {
    // div.appendChild(svg.getElement());
    // }
    // }
    //
    // };

}
