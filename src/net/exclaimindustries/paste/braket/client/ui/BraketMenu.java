/**
 * 
 */
package net.exclaimindustries.paste.braket.client.ui;

import net.exclaimindustries.paste.braket.client.BraketTournament;
import net.exclaimindustries.paste.braket.client.BraketUser;
import net.exclaimindustries.paste.braket.client.resources.UiConstants.HistoryToken;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
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

	interface Style extends CssResource {
		String menuButton();

		String adminButton();
	}

	private static BraketMenuUiBinder uiBinder = GWT
			.create(BraketMenuUiBinder.class);

	@UiField
	FlowPanel panel;

	@UiField
	Style style;

	interface BraketMenuUiBinder extends UiBinder<Widget, BraketMenu> {
	}

	public BraketMenu(BraketTournament tournament, BraketUser user) {
		initWidget(uiBinder.createAndBindUi(this));

		// If the tournament is null, then only show an admin button
		if (tournament != null) {
			// Add a braket button
			Hyperlink myBracket = new Hyperlink("My Bracket",
					HistoryToken.MY_BRACKET);
			myBracket.setStyleName(style.menuButton());
			panel.add(myBracket);

			if (tournament.isOngoing()) {
				// Add the tournament status option
				Hyperlink tournamentStatus = new Hyperlink("Tournament Status",
						HistoryToken.TOURNAMENT_STATUS);
				tournamentStatus.setStyleName(style.menuButton());
				panel.add(tournamentStatus);

				// Add a leaderboard button
				Hyperlink leaderboards = new Hyperlink("Leaderboards",
						HistoryToken.LEADERBOARDS);
				leaderboards.setStyleName(style.menuButton());
				panel.add(leaderboards);

				// Add an excite-o-matic button
				Hyperlink exciteo = new Hyperlink("Excite-o-Matic",
						HistoryToken.EXCITE_O_MATIC);
				exciteo.setStyleName(style.menuButton());
				panel.add(exciteo);
			}
		}

		if (user.isAdmin()) {
			Hyperlink admin = new Hyperlink("Administration",
					HistoryToken.ADMIN);
			admin.setStyleName(style.menuButton());
			admin.addStyleName(style.adminButton());
			panel.add(admin);
		}
	}

}
