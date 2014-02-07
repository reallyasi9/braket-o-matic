/**
 * 
 */
package net.exclaimindustries.paste.braket.client.ui;

import net.exclaimindustries.paste.braket.client.BraketTournament;
import net.exclaimindustries.paste.braket.client.BraketUser;
import net.exclaimindustries.paste.braket.client.resources.Resources;
import net.exclaimindustries.paste.braket.client.resources.UiConstants.HistoryToken;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author paste
 * 
 */
public class BraketMenu extends Composite {

    private static BraketMenuUiBinder uiBinder = GWT
            .create(BraketMenuUiBinder.class);

    @UiField
    FlowPanel panel;

    interface BraketMenuUiBinder extends UiBinder<Widget, BraketMenu> {
    }

    public BraketMenu(BraketTournament tournament, BraketUser user) {
        initWidget(uiBinder.createAndBindUi(this));

        // Add a braket button
        Hyperlink myBracket = new Hyperlink(HistoryToken.MY_BRACKET,
                "My Bracket");
        myBracket.setStyleName(Resources.INSTANCE.style().menuButton());
        panel.add(myBracket);

        if (tournament.isOngoing()) {
            // Add the tournament status option
            Hyperlink tournamentStatus = new Hyperlink(
                    HistoryToken.TOURNAMENT_STATUS, "Tournament Status");
            tournamentStatus.setStyleName(Resources.INSTANCE.style()
                    .menuButton());
            panel.add(tournamentStatus);

            // Add a leaderboard button
            Hyperlink leaderboards = new Hyperlink(HistoryToken.LEADERBOARDS,
                    "Leaderboards");
            leaderboards.setStyleName(Resources.INSTANCE.style().menuButton());
            panel.add(leaderboards);

            // Add an excite-o-matic button
            Hyperlink exciteo = new Hyperlink(HistoryToken.EXCITE_O_MATIC,
                    "Excite-o-Matic");
            exciteo.setStyleName(Resources.INSTANCE.style().menuButton());
            panel.add(exciteo);
        }

        if (user.isAdmin()) {
            Hyperlink admin = new Hyperlink(HistoryToken.ADMIN,
                    "Administration");
            admin.setStyleName(Resources.INSTANCE.style().menuButton());
            admin.addStyleName(Resources.INSTANCE.style().adminButton());
            panel.add(admin);
        }
    }

}
