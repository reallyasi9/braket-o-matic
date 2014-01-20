package net.exclaimindustries.paste.braket.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;

public class BraketHeader extends ResizeComposite {

    private static BraketHeaderUiBinder uiBinder = GWT
            .create(BraketHeaderUiBinder.class);

    interface BraketHeaderUiBinder extends UiBinder<Widget, BraketHeader> {
    }

    public BraketHeader() {
        initWidget(uiBinder.createAndBindUi(this));
    }

//    @UiField
//    HTML svgContainer;
//
//    ResourceCallback<SVGResource> callback = new ResourceCallback<SVGResource>() {
//
//        @Override
//        public void onError(ResourceException e) {
//            // TODO handle errors
//        }
//
//        @Override
//        public void onSuccess(SVGResource resource) {
//            // Insert the SVG root element into the HTML UI
//            Element div = svgContainer.getElement();
//            OMSVGSVGElement svg = resource.getSvg();
//            if (div.hasChildNodes()) {
//                div.replaceChild(svg.getElement(), div.getFirstChild());
//            } else {
//                div.appendChild(svg.getElement());
//            }
//        }
//
//    };

}
