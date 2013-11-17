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

package net.exclaimindustries.paste.braket.client;

import net.exclaimindustries.paste.braket.client.ui.BraketHeader;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class BraketEntryPoint implements EntryPoint, ValueChangeHandler<String> {

    // History tokens
    private static class HistoryToken {
        public static final String ABOUT = "about";
        public static final String BRAKET = "braket";
        public static final String ADMIN = "admin";
        public static final String USER_OPTIONS = "user-options";
        public static final String LEADERBOARD = "leaderboard";
        public static final String EDIT_USERS = "edit-users";
        public static final String EDIT_TEAMS = "edit-teams";
        public static final String EDIT_GAMES = "edit-games";
        public static final String EXCITE_O_MATIC = "excite-o-matic";
    }
    
    // Fixed sizes
    private static class Dimensions {
        public static final Unit UNITS = Unit.PT;
        public static final double HEADER_HEIGHT = 50;
        public static final double FOOTER_HEIGHT = 15;
    }

    // Panels
    private BraketHeader braketHeader = new BraketHeader();
    private FlowPanel braketMain = new FlowPanel();
    private FlowPanel braketFooter = new FlowPanel();

    // RPC services
    private LoginServiceAsync loginServiceRPC = GWT.create(LoginService.class);

    // Callbacks
    private RunAsyncCallback signUpDisplayCallback = new RunAsyncCallback() {

        @Override
        public void onFailure(Throwable reason) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onSuccess() {
            // TODO Auto-generated method stub
            braketMain.clear();
            braketMain.add(new Label("please log in"));
        }

    };

    private RunAsyncCallback braketDisplayCallback = new RunAsyncCallback() {

        @Override
        public void onFailure(Throwable reason) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onSuccess() {
            // TODO Auto-generated method stub
            braketMain.clear();
            braketMain.add(new HTMLPanel("braket goes here"));
        }

    };

    @Override
    public void onValueChange(ValueChangeEvent<String> event) {
        String eventString = event.getValue();
        if (eventString.equals(HistoryToken.ABOUT)) {
            // TODO
        } else if (eventString.equals(HistoryToken.BRAKET)) {
            GWT.runAsync(braketDisplayCallback);
        } else if (eventString.equals(HistoryToken.ADMIN)) {
            // TODO
        } else if (eventString.equals(HistoryToken.USER_OPTIONS)) {
            // TODO
        } else if (eventString.equals(HistoryToken.LEADERBOARD)) {
            // TODO
        } else if (eventString.equals(HistoryToken.EDIT_USERS)) {
            // TODO
        } else if (eventString.equals(HistoryToken.EDIT_TEAMS)) {
            // TODO
        } else if (eventString.equals(HistoryToken.EDIT_GAMES)) {
            // TODO
        } else if (eventString.equals(HistoryToken.EXCITE_O_MATIC)) {
            // TODO
        } else {
            // TODO
        }
    }

    @Override
    public void onModuleLoad() {

        History.addValueChangeHandler(this);

        // Everything has a header and a footer
        DockLayoutPanel dlp = new DockLayoutPanel(Dimensions.UNITS);
        dlp.addNorth(braketHeader, Dimensions.HEADER_HEIGHT);
        dlp.addSouth(braketFooter, Dimensions.FOOTER_HEIGHT);
        dlp.add(braketMain);

        RootLayoutPanel.get().add(dlp);
        
        braketMain.add(new Label("logging in..."));
        braketFooter.add(new Label("footer"));

        // TODO Determine whether or not you are logged in.
        loginServiceRPC.signIn(GWT.getHostPageBaseURL(),
                new AsyncCallback<BraketUser>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onSuccess(BraketUser result) {
                        if (result.isSignedIn()) {
                            // TODO handle the history state
                            // Handle history token
                            History.fireCurrentHistoryState();
                        } else {
                            // TODO display the sign-in/sign-up screen
                            GWT.runAsync(signUpDisplayCallback);
                        }
                    }

                });

    }

}
