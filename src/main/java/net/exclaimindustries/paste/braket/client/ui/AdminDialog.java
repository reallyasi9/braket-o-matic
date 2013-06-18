/**
 * This file is part of braket-o-matic.
 *
 *  braket-o-matic is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  braket-o-matic is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with braket-o-matic.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.exclaimindustries.paste.braket.client.ui;

import net.exclaimindustries.paste.braket.client.BraketEntryPoint;
import net.exclaimindustries.paste.braket.client.ExpectedValueService;
import net.exclaimindustries.paste.braket.client.ExpectedValueServiceAsync;
import net.exclaimindustries.paste.braket.client.resources.Resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author phil
 * 
 */
public class AdminDialog extends DialogBox {

    private static AdminDialogUiBinder uiBinder = GWT
            .create(AdminDialogUiBinder.class);

    private static ExpectedValueServiceAsync EXPECTO_SERVICE = GWT
            .create(ExpectedValueService.class);

    interface AdminDialogUiBinder extends UiBinder<Widget, AdminDialog> {
    }

    private static Resources res = GWT.create(Resources.class);

    static {
        res.style().ensureInjected();
    }

    @UiField
    Button createTournamentButton;

    @UiField
    Button changeTournamentButton;

    @UiField
    Button modifyTournamentButton;

    @UiField
    Button rulesButton;

    @UiField
    Button downloadTeamsButton;

    @UiField
    Button modifyTeamsButton;

    @UiField
    Button modifyGamesButton;

    @UiField
    Button modifyUsersButton;

    @UiField
    Button expectoButton;

    public AdminDialog() {
        super(true, true);
        setWidget(uiBinder.createAndBindUi(this));
        getCaption().setText("administration functions");
        setAnimationEnabled(true);
        setGlassEnabled(true);
        addStyleName(res.style().dialogBox());
        setGlassStyleName(res.style().glass());

        // changeTournamentButton.setEnabled(false);
    }

    @Override
    protected void beginDragging(MouseDownEvent e) {
        e.preventDefault();
    }

    @UiHandler("createTournamentButton")
    void createTournament(ClickEvent e) {
        hide();
        //BraketEntryPoint.doNewTournamentPopup();
    }

    @UiHandler("changeTournamentButton")
    void changeTournament(ClickEvent e) {
        hide();
        //BraketEntryPoint.doChangeTournamentPopup();
    }

    @UiHandler("modifyTournamentButton")
    void modifyTournament(ClickEvent e) {
        hide();
        //BraketEntryPoint.doEditTournamentPopup();
    }

    @UiHandler("rulesButton")
    void rules(ClickEvent e) {
        hide();
        //BraketEntryPoint.doEditRulesPopup();
    }

    @UiHandler("downloadTeamsButton")
    void downloadTeams(ClickEvent e) {
        //BraketEntryPoint.doDownloadTeamsPopup();
        hide();
    }

    @UiHandler("modifyTeamsButton")
    void modifyTeams(ClickEvent e) {
        hide();
        History.newItem(BraketEntryPoint.HistoryToken.EDIT_TEAMS);
        History.fireCurrentHistoryState();
    }

    @UiHandler("modifyGamesButton")
    void modifyGames(ClickEvent e) {
        hide();
        History.newItem(BraketEntryPoint.HistoryToken.EDIT_GAMES);
        History.fireCurrentHistoryState();
    }

    @UiHandler("modifyUsersButton")
    void modifyUsers(ClickEvent e) {
        hide();
        History.newItem(BraketEntryPoint.HistoryToken.EDIT_USERS);
        History.fireCurrentHistoryState();
    }

    @UiHandler("expectoButton")
    void expecto(ClickEvent e) {
        hide();
        EXPECTO_SERVICE.startExpectOMatic(new AsyncCallback<Void>() {

            @Override
            public void onFailure(Throwable caught) {
                //BraketEntryPoint.displayException(caught);
            }

            @Override
            public void onSuccess(Void result) {
                //BraketEntryPoint.displayToast("Expect-o-Matic signal sent!");
            }

        });

    }

}
