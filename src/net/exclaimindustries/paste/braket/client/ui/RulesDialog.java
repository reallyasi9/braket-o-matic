/**
 * This file is part of braket-o-matic.
 *
 * braket-o-matic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * braket-o-matic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with braket-o-matic.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.exclaimindustries.paste.braket.client.ui;

import net.exclaimindustries.paste.braket.client.resources.Resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author paste
 * 
 */
public class RulesDialog extends DialogBox {

    private static RulesDialogUiBinder uiBinder = GWT
            .create(RulesDialogUiBinder.class);

    interface RulesDialogUiBinder extends UiBinder<Widget, RulesDialog> {
    }

    private static Resources res = GWT.create(Resources.class);

    static {
        res.style().ensureInjected();
    }

    @UiField
    Button closeButton;

    @UiField
    FlowPanel rules;

    public RulesDialog() {
        super(false, true);
        setWidget(uiBinder.createAndBindUi(this));
        getCaption().setText("rules");
        setAnimationEnabled(true);
        setGlassEnabled(true);
        addStyleName(res.style().dialogBox());
        setGlassStyleName(res.style().glass());
        rules.setWidth("600px");
    }

    public void reset() {
        rules.clear();
//        if (BraketEntryPoint.currTournament != null) {
//            rules.add(new HTMLPanel(BraketEntryPoint.currTournament.getRules()));
//        }
    }

    @UiHandler("closeButton")
    void closeMe(ClickEvent event) {
        hide();
    }

}
