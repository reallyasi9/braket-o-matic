package net.exclaimindustries.paste.braket.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class BraketHeader extends Composite {

    private static BraketHeaderUiBinder uiBinder = GWT
            .create(BraketHeaderUiBinder.class);

    interface BraketHeaderUiBinder extends UiBinder<Widget, BraketHeader> {
    }

    public BraketHeader() {
        initWidget(uiBinder.createAndBindUi(this));
    }

}
