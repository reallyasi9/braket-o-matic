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

import net.exclaimindustries.paste.braket.client.resources.UiConstants;
import net.exclaimindustries.paste.braket.client.ui.BraketAppLayout;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class BraketEntryPoint implements EntryPoint, ValueChangeHandler<String> {

    // Panels
    private BraketAppLayout layout = new BraketAppLayout();

    // Handlers
    private UserLoginHandler userLoginHandler = new UserLoginHandler(layout);

    // Callbacks
    private RunAsyncCallback signUpDisplayCallback = new RunAsyncCallback() {

        @Override
        public void onFailure(Throwable reason) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onSuccess() {
            // TODO Auto-generated method stub
            layout.getMainPanel().clear();
            layout.getMainPanel().add(new Label("please log in"));
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
            layout.getMainPanel().clear();
            layout.getMainPanel().add(new HTMLPanel("braket goes here"));
        }

    };

    @Override
    public void onValueChange(ValueChangeEvent<String> event) {
        String eventString = event.getValue();
        if (eventString.equals(UiConstants.HistoryToken.ABOUT)) {
            // TODO
        } else if (eventString.equals(UiConstants.HistoryToken.TOURNAMENT_STATUS)) {
            // TODO
            userLoginHandler.signIn();
        } else if (eventString.equals(UiConstants.HistoryToken.MY_BRACKET)) {
            GWT.runAsync(braketDisplayCallback);
        } else if (eventString.equals(UiConstants.HistoryToken.ADMIN)) {
            // TODO
        } else if (eventString.equals(UiConstants.HistoryToken.USER_OPTIONS)) {
            // TODO
        } else if (eventString.equals(UiConstants.HistoryToken.LEADERBOARDS)) {
            // TODO
        } else if (eventString.equals(UiConstants.HistoryToken.EDIT_USERS)) {
            // TODO
        } else if (eventString.equals(UiConstants.HistoryToken.EDIT_TEAMS)) {
            // TODO
        } else if (eventString.equals(UiConstants.HistoryToken.EDIT_GAMES)) {
            // TODO
        } else if (eventString.equals(UiConstants.HistoryToken.EXCITE_O_MATIC)) {
            // TODO
        } else {
            // TODO
        }
    }

    @Override
    public void onModuleLoad() {

        History.addValueChangeHandler(this);

        RootLayoutPanel.get().add(layout);

        layout.getMainPanel().add(new Label("logging in..."));

        // TODO Determine whether or not you are logged in.
        // loginServiceRPC.signIn(GWT.getHostPageBaseURL(),
        // new AsyncCallback<BraketUser>() {
        //
        // @Override
        // public void onFailure(Throwable caught) {
        // // TODO Auto-generated method stub
        //
        // }
        //
        // @Override
        // public void onSuccess(BraketUser result) {
        // if (result.isSignedIn()) {
        // // TODO handle the history state
        // // Handle history token
        // History.fireCurrentHistoryState();
        // } else {
        // // TODO display the sign-in/sign-up screen
        // GWT.runAsync(signUpDisplayCallback);
        // }
        // }
        //
        // });

    }

}
