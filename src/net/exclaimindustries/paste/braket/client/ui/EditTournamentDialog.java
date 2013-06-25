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

import java.math.BigInteger;
import java.util.LinkedList;

import net.exclaimindustries.paste.braket.client.resources.Resources;

import com.google.common.base.Splitter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

/**
 * @author paste
 * 
 */
public class EditTournamentDialog extends DialogBox {

    private static EditTournamentDialogUiBinder uiBinder = GWT
            .create(EditTournamentDialogUiBinder.class);

    interface EditTournamentDialogUiBinder extends
            UiBinder<Widget, EditTournamentDialog> {
    }

    private final static Resources res = GWT.create(Resources.class);

    static {
        res.style().ensureInjected();
    }

    private DateBox dateBox = new DateBox();

    @UiField
    TextBox name;

    @UiField
    TextBox gameMask;

    @UiField
    FlowPanel dateBoxHere;

    @UiField
    TextBox buyIn;

    @UiField
    TextBox payOut;

    @UiField
    TextBox roundValues;

    @UiField
    TextBox upset;

    @UiField
    Button saveButton;

    @UiField
    Button cancelButton;

    public EditTournamentDialog() {
        super(false, true);
        setWidget(uiBinder.createAndBindUi(this));
        getCaption().setText("edit a tournament");
        setAnimationEnabled(true);
        setGlassEnabled(true);
        addStyleName(res.style().dialogBox());
        setGlassStyleName(res.style().glass());

        dateBox.setFormat(new DateBox.DefaultFormat(DateTimeFormat
                .getFormat(DateTimeFormat.PredefinedFormat.RFC_2822)));
        dateBox.addStyleName(res.style().formField());
        dateBoxHere.add(dateBox);
    }

    @Override
    protected void beginDragging(MouseDownEvent e) {
        e.preventDefault();
    }

    @UiHandler("cancelButton")
    void cancelMe(ClickEvent event) {
        saveButton.setEnabled(false);
        hide();
    }

    @UiHandler("deleteButton")
    void deleteMe(ClickEvent event) {
        // if (BraketEntryPoint.currTournament == null) {
        // BraketEntryPoint.displayToast("No tournament to delete.");
        // return;
        // }

        // if (Window
        // .confirm("Woah.  WOAH!\n\nAre you sure you want to delete the current tournament?  Bad things might happen!"))
        // {
        // // All right. It's your tournament. I tried to warn you.
        // BraketEntryPoint.tournaService.deleteTournament(
        // BraketEntryPoint.currTournament, new AsyncCallback<Void>() {
        //
        // @Override
        // public void onFailure(Throwable caught) {
        // BraketEntryPoint.displayException(caught);
        // }
        //
        // @Override
        // public void onSuccess(Void result) {
        //
        // BraketEntryPoint
        // .displayToast("Tournament successfully deleted.");
        //
        // BraketEntryPoint.currTournament = null;
        // BraketEntryPoint.bracketContainer.clearContainer();
        // hide();
        // resetValues();
        // //BraketEntryPoint.doBraketDisplay();
        // }
        //
        // });
        // }
    }

    @UiHandler("saveButton")
    void saveMe(ClickEvent event) {

        try {
            saveButton.setEnabled(false);

            // final BraketTournament tournament =
            // BraketEntryPoint.currTournament;
            // Easy stuff first
            // tournament.setName(name.getText());
            // tournament.setUpsetValue(Double.valueOf(upset.getText()));
            // tournament.setBuyInValue(Double.valueOf(buyIn.getText()));

            // Parse the game mask
            String gmString = gameMask.getText();
            BigInteger gm = BigInteger.ZERO;
            for (int i = gmString.length() - 1; i >= 0; --i) {
                if (gmString.charAt(i) == '1') {
                    gm = gm.setBit(i);
                }
            }
            // tournament.setGameMask(gm);

            // Parse the payouts
            Splitter splitter = Splitter.on(',').trimResults().omitEmptyStrings();
            Iterable<String> poSplit = splitter.split(payOut.getText());

            LinkedList<Double> po = new LinkedList<Double>();
            for (String poValue : poSplit) {
                if (poValue.toLowerCase().equals("null")) {
                    po.add(null);
                } else {
                    po.add(Double.valueOf(poValue));
                }
            }
            // tournament.setPayOutValues(po);

            // Parse the round values
            Iterable<String> rvSplit = splitter.split(roundValues.getText());

            LinkedList<Double> rv = new LinkedList<Double>();
            for (String rvValue : rvSplit) {
                rv.add(Double.valueOf(rvValue));
            }
            // tournament.setRoundValues(rv);

            // tournament.setStartTime(dateBox.getValue());

            // BraketEntryPoint.tournaService.storeTournament(tournament,
            // new AsyncCallback<Long>() {
            //
            // @Override
            // public void onFailure(Throwable caught) {
            // BraketEntryPoint.displayException(caught);
            // }
            //
            // @Override
            // public void onSuccess(Long result) {
            //
            // BraketEntryPoint
            // .displayToast("Tournament successfully modified.");
            //
            // hide();
            // resetValues();
            // //BraketEntryPoint.doBraketDisplay();
            // }
            //
            // });
        } catch (Throwable caught) {
            // BraketEntryPoint.displayException(caught);
        }
    }

    public void resetValues() {
        saveButton.setEnabled(true);
        // if (BraketEntryPoint.currTournament == null) {
        // dateBox.getTextBox().setText("");
        // name.setText("");
        // gameMask.setText("");
        // buyIn.setText("");
        // payOut.setText("");
        // roundValues.setText("");
        // upset.setValue("");
        // } else {
        // dateBox.getTextBox().setText(
        // DateTimeFormat.getFormat(
        // DateTimeFormat.PredefinedFormat.RFC_2822).format(
        // BraketEntryPoint.currTournament.getStartTime()));
        // name.setText(BraketEntryPoint.currTournament.getName());
        // gameMask.setText(BraketEntryPoint.currTournament.getGameMask()
        // .toString(2));
        // buyIn.setText(BraketEntryPoint.currTournament.getBuyInValue()
        // .toString());
        // Joiner joiner = Joiner.on(", ").useForNull("null");
        // payOut.setText(joiner.join(BraketEntryPoint.currTournament
        // .getPayOutValues()));
        // roundValues.setText(joiner.join(BraketEntryPoint.currTournament
        // .getRoundValues()));
        // upset.setValue(Double.toString(BraketEntryPoint.currTournament
        // .getUpsetValue()));
        // saveButton.setEnabled(true);
        // }

    }

}
