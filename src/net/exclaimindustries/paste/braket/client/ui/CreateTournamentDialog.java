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

import net.exclaimindustries.paste.braket.client.BraketTournament;
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
public class CreateTournamentDialog extends DialogBox {

    private static CreateTournamentDialogUiBinder uiBinder = GWT
            .create(CreateTournamentDialogUiBinder.class);

    interface CreateTournamentDialogUiBinder extends
            UiBinder<Widget, CreateTournamentDialog> {
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

    public CreateTournamentDialog() {
        super(false, true);
        setWidget(uiBinder.createAndBindUi(this));
        getCaption().setText("create a tournament");
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
        hide();
    }

    @UiHandler("saveButton")
    void saveMe(ClickEvent event) {

        try {
            saveButton.setEnabled(false);

            final BraketTournament tournament = new BraketTournament();
            // Easy stuff first
            tournament.setName(name.getText());
            tournament.setUpsetValue(Double.valueOf(upset.getText()));
            tournament.setBuyInValue(Double.valueOf(buyIn.getText()));

            // Parse the game mask
            String gmString = gameMask.getText();
            BigInteger gm = BigInteger.ZERO;
            for (int i = gmString.length() - 1; i >= 0; --i) {
                if (gmString.charAt(i) == '1') {
                    gm = gm.setBit(i);
                }
            }
            tournament.setGameMask(gm);

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
            tournament.setPayOutValues(po);

            // Parse the round values
            Iterable<String> rvSplit = splitter.split(roundValues.getText());

            LinkedList<Double> rv = new LinkedList<Double>();
            for (String rvValue : rvSplit) {
                rv.add(Double.valueOf(rvValue));
            }
            tournament.setRoundValues(rv);

            tournament.setStartTime(dateBox.getValue());

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
            // .displayToast("Tournament successfully created.");
            //
            // hide();
            // resetValues();
            // }
            //
            // });
        } catch (Throwable caught) {
            // BraketEntryPoint.displayException(caught);
        }
    }

    // private void resetValues() {
    // dateBox.getTextBox().setText("");
    // name.setText("");
    // gameMask.setText("111111111111111111111111111111111111111111111111111111111111111");
    // buyIn.setText("5");
    // payOut.setText("null, 50, 25, 5");
    // roundValues.setText("1, 2, 3, 5, 7, 13");
    // upset.setValue("1");
    // saveButton.setEnabled(true);
    // }

}
