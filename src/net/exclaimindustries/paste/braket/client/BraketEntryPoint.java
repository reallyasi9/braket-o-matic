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

import java.util.logging.Level;
import java.util.logging.Logger;

import net.exclaimindustries.paste.braket.client.resources.UiConstants;
import net.exclaimindustries.paste.braket.client.ui.BraketAppLayout;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class BraketEntryPoint implements EntryPoint, ValueChangeHandler<String> {

    // Logger specific to this object
    private Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    // Layout
    private BraketAppLayout layout = new BraketAppLayout();

    // Handlers
    private UserLogInHandler userLogInHandler = new UserLogInHandler(layout);

    // Callbacks?
    // TODO Put these into the handlers
    private RunAsyncCallback braketDisplayCallback = new RunAsyncCallback() {

        @Override
        public void onFailure(Throwable reason) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onSuccess() {
            // TODO Auto-generated method stub
            layout.setCenter(new HTMLPanel("braket goes here"));
        }

    };

    @Override
    public void onValueChange(ValueChangeEvent<String> event) {
        String eventString = event.getValue();
        if (eventString.equals(UiConstants.HistoryToken.ABOUT)) {
            // TODO
            logger.log(Level.INFO, "loading about page");
        } else if (eventString.equals(UiConstants.HistoryToken.TOURNAMENT_STATUS)) {
            // TODO
            logger.log(Level.INFO, "loading tournament status page");
        } else if (eventString.equals(UiConstants.HistoryToken.MY_BRACKET)) {
            logger.log(Level.INFO, "loading user bracket page");
            GWT.runAsync(braketDisplayCallback);
        } else if (eventString.equals(UiConstants.HistoryToken.ADMIN)) {
            logger.log(Level.INFO, "loading admin page");
            // TODO
        } else if (eventString.equals(UiConstants.HistoryToken.USER_OPTIONS)) {
            logger.log(Level.INFO, "loading user options page");
            // TODO
        } else if (eventString.equals(UiConstants.HistoryToken.LEADERBOARDS)) {
            logger.log(Level.INFO, "loading leaderboard page");
            // TODO
        } else if (eventString.equals(UiConstants.HistoryToken.EDIT_USERS)) {
            logger.log(Level.INFO, "loading edit users dialog");
            // TODO
        } else if (eventString.equals(UiConstants.HistoryToken.EDIT_TEAMS)) {
            logger.log(Level.INFO, "loading edit teams dialog");
            // TODO
        } else if (eventString.equals(UiConstants.HistoryToken.EDIT_GAMES)) {
            logger.log(Level.INFO, "loading edit games dialog");
            // TODO
        } else if (eventString.equals(UiConstants.HistoryToken.EXCITE_O_MATIC)) {
            logger.log(Level.INFO, "loading excite-o-matic");
            // TODO
        } else {
            logger.log(Level.WARNING, "history event [" + eventString
                    + "] not understood");
            // TODO
        }
    }

    @Override
    public void onModuleLoad() {

        History.addValueChangeHandler(this);

        RootLayoutPanel.get().add(layout);

        // Attempt to log the user in and deal with the result.
        // Either the user will not be logged in, in which case an "About" page
        // will be displayed,
        // or the user will be logged in, and the history token in the URL will
        // be followed.
        userLogInHandler.logIn();

    }

}
