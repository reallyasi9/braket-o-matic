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
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author paste
 * 
 */
public class ChangeCurrentTournamentDialog extends DialogBox {

    private static ChangeCurrentTournamentDialogUiBinder uiBinder = GWT
            .create(ChangeCurrentTournamentDialogUiBinder.class);

    interface ChangeCurrentTournamentDialogUiBinder extends
            UiBinder<Widget, ChangeCurrentTournamentDialog> {
    }

    // private List<BraketTournament> tournaments;

    private final static Resources res = GWT.create(Resources.class);

    static {
        res.style().ensureInjected();
    }

    @UiField
    Label name;

    @UiField
    ListBox listBox;

    @UiField
    Button saveButton;

    @UiField
    Button cancelButton;

    public ChangeCurrentTournamentDialog() {
        super(false, true);
        setWidget(uiBinder.createAndBindUi(this));
        getCaption().setText("select current tournament");
        setAnimationEnabled(true);
        setGlassEnabled(true);
        addStyleName(res.style().dialogBox());
        setGlassStyleName(res.style().glass());
        name.setText("(no current tournament)");
        saveButton.setEnabled(false);
    }

    public void updateTournamentList() {

        // tournaments = new ArrayList<BraketTournament>();
        listBox.clear();

        // if (BraketEntryPoint.currTournament != null) {
        // name.setText(BraketEntryPoint.currTournament.getName());
        // } else {
        name.setText("(no current tournament)");
        // }

        // BraketEntryPoint.tournaService
        // .getTournaments(new AsyncCallback<Collection<BraketTournament>>() {
        //
        // @Override
        // public void onFailure(Throwable caught) {
        // BraketEntryPoint.displayException(caught);
        // }
        //
        // @Override
        // public void onSuccess(Collection<BraketTournament> result) {
        //
        // int i = 0;
        // for (BraketTournament tournament : result) {
        //
        // tournaments.add(tournament);
        //
        // StringBuilder sb = new StringBuilder();
        // sb.append(tournament.getName());
        // sb.append(" (starts ");
        // sb.append(DateTimeFormat.getFormat(
        // DateTimeFormat.PredefinedFormat.RFC_2822)
        // .format(tournament.getStartTime()));
        // sb.append(")");
        //
        // listBox.addItem(sb.toString());
        //
        // if (BraketEntryPoint.currTournament != null
        // && tournament.getId() == BraketEntryPoint.currTournament
        // .getId()) {
        // listBox.setSelectedIndex(i);
        // }
        //
        // ++i;
        // }
        //
        // saveButton.setEnabled(true);
        // }
        //
        // });
    }

    @UiHandler("saveButton")
    void saveMe(ClickEvent event) {

        // final BraketTournament selected =
        // tournaments.get(listBox.getSelectedIndex());

        // BraketEntryPoint.tournaService.setCurrentTournament(selected,
        // new AsyncCallback<Void>() {
        //
        // @Override
        // public void onFailure(Throwable caught) {
        // BraketEntryPoint.displayException(caught);
        // }
        //
        // @Override
        // public void onSuccess(Void result) {
        // BraketEntryPoint.currTournament = selected;
        // name.setText(BraketEntryPoint.currTournament.getName());
        // hide();
        // BraketEntryPoint
        // .displayToast("New tournament selected.  Please reload page to ensure everything is working correctly.");
        // }
        //
        // });
    }

    @UiHandler("cancelButton")
    void cancelMe(ClickEvent event) {
        hide();
    }

}
