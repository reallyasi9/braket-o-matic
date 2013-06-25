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
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author paste
 * 
 */
public class EditRulesDialog extends DialogBox {

    private static EditRulesDialogUiBinder uiBinder = GWT
            .create(EditRulesDialogUiBinder.class);

    interface EditRulesDialogUiBinder extends UiBinder<Widget, EditRulesDialog> {
    }

    private static Resources res = GWT.create(Resources.class);

    static {
        res.style().ensureInjected();
    }

    private RichTextArea rta;

    @UiField
    Button saveButton;

    @UiField
    Button cancelButton;

    @UiField
    FlowPanel rulesHere;

    public EditRulesDialog() {
        super(false, true);
        setWidget(uiBinder.createAndBindUi(this));
        getCaption().setText("edit rules");
        setAnimationEnabled(true);
        setGlassEnabled(true);
        addStyleName(res.style().dialogBox());
        setGlassStyleName(res.style().glass());
        saveButton.setEnabled(false);

        // Create the text area and toolbar
        rta = new RichTextArea();
        rta.setSize("600px", "14em");
        RichTextToolbar toolbar = new RichTextToolbar(rta);
        toolbar.setWidth("100%");

        // Add the components to a panel
        Grid grid = new Grid(2, 1);
        grid.setStyleName("cw-RichText");
        grid.setWidget(0, 0, toolbar);
        grid.setWidget(1, 0, rta);

        rulesHere.add(grid);
    }

    /**
     * 
     */
    // public void reset() {
    // if (BraketEntryPoint.currTournament == null) {
    // rta.setHTML("");
    // } else {
    // rta.setHTML(BraketEntryPoint.currTournament.getRules());
    // }
    // saveButton.setEnabled(true);
    // }

    @UiHandler("saveButton")
    void saveMe(ClickEvent event) {

        // if (BraketEntryPoint.currTournament == null) {
        // BraketEntryPoint.displayToast("Current tournament is not set");
        // return;
        // }

        saveButton.setEnabled(false);

        // final String rules = rta.getHTML();

        // BraketEntryPoint.tournaService.setRules(rules,
        // new AsyncCallback<Void>() {
        //
        // @Override
        // public void onFailure(Throwable caught) {
        // BraketEntryPoint.displayException(caught);
        // }
        //
        // @Override
        // public void onSuccess(Void result) {
        // BraketEntryPoint.currTournament.setRules(rules);
        // hide();
        // BraketEntryPoint.displayToast("Rules updated.");
        // saveButton.setEnabled(true);
        // }
        //
        // });
    }

    @UiHandler("cancelButton")
    void cancelMe(ClickEvent event) {
        hide();
    }

}
