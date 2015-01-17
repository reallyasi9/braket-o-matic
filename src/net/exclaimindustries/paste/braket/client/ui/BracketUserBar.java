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

import net.exclaimindustries.paste.braket.client.User;
import net.exclaimindustries.paste.braket.client.resources.Resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author paste
 * 
 */
public class BracketUserBar extends Composite {

    interface MyStyle extends CssResource {
        String signInButton();

        String signOutButton();

        String bumpDown();
    }

    interface BracketUserBarUiBinder extends UiBinder<Widget, BracketUserBar> {
    }

    private static BracketUserBarUiBinder uiBinder = GWT
            .create(BracketUserBarUiBinder.class);

    private User thisUser;

    @UiField
    MyStyle style;

    @UiField
    Button loginLink;

    @UiField
    Button userLink;

    @UiField
    Button leaderboardLink;

    @UiField
    Button exciteoLink;

    @UiField
    Button adminLink;

    @UiField
    Button rulesLink;

    @UiField
    Button helpLink;

    @UiField
    Button doneEditing;

    private static Resources res = GWT.create(Resources.class);

    static {
        res.style().ensureInjected();
    }

    public BracketUserBar() {
        initWidget(uiBinder.createAndBindUi(this));
        setUser(null);
    }

    public void setUser(User user) {
        thisUser = user;
        // Can access @UiField after calling createAndBindUi
        if (thisUser == null || !thisUser.isLoggedIn()) {
            loginLink.setHTML("log in");
            loginLink.setStyleName(res.style().redButton());
            loginLink.addStyleName(style.signInButton());
            loginLink.addStyleName(style.bumpDown());
            loginLink.getOffsetHeight();
            userLink.setVisible(false);
            leaderboardLink.setVisible(false);
            exciteoLink.setVisible(false);
            helpLink.setVisible(false);
            adminLink.setVisible(false);
        } else {
            loginLink.setHTML("log out");
            loginLink.setStyleName(res.style().blueButton());
            loginLink.addStyleName(style.signOutButton());
            loginLink.addStyleName(style.bumpDown());
            userLink.setVisible(true);
            leaderboardLink.setVisible(true);
            exciteoLink.setVisible(true);
            // if (BraketEntryPoint.currTournament == null
            // || BraketEntryPoint.currTournament.isScheduled()) {
            // helpLink.setVisible(true);
            // }
            if (user.isAdmin()) {
                adminLink.setVisible(true);
            }
        }

        loginLink.setVisible(true);
    }

    public void startEditing() {
        doneEditing.setVisible(true);
        // BraketEntryPoint
        // .displayToast("Click the 'done editing' button when you are finished");
    }

    @UiHandler("loginLink")
    void loginClick(ClickEvent event) {
        if (thisUser == null) {
            // do nothing
            return;
        }
        if (!thisUser.isLoggedIn()) {
            Window.Location.assign(thisUser.getLogInLink());
        } else {
            if (Window
                    .confirm("Choosing to log out will log you out out of all of Google's services (including, for example, Gmail).")) {
                Window.Location.assign(thisUser.getLogOutLink());
            }
        }
    }

    @UiHandler("userLink")
    void userClick(ClickEvent event) {
        // BraketEntryPoint.doUserPopup();
    }

    @UiHandler("leaderboardLink")
    void leaderboardClick(ClickEvent event) {
        //History.newItem(BraketEntryPoint.HistoryToken.LEADERBOARD);
        History.fireCurrentHistoryState();
    }

    @UiHandler("exciteoLink")
    void exciteoClick(ClickEvent event) {
        //History.newItem(BraketEntryPoint.HistoryToken.EXCITE_O_MATIC);
        History.fireCurrentHistoryState();
    }

    @UiHandler("rulesLink")
    void rulesClick(ClickEvent event) {
        // BraketEntryPoint.doRulesPopup();
    }

    @UiHandler("adminLink")
    void adminClick(ClickEvent event) {
        // BraketEntryPoint.doAdminPopup();
    }

    @UiHandler("helpLink")
    void helpClick(ClickEvent event) {
        // BraketEntryPoint.doHelpPopup();
    }

    @UiHandler("doneEditing")
    void doneClick(ClickEvent event) {
        doneEditing.setVisible(false);
        History.newItem("braket");
        History.fireCurrentHistoryState();
    }

}
