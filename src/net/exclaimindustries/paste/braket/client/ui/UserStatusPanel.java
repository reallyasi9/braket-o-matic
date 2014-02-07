package net.exclaimindustries.paste.braket.client.ui;

import java.util.logging.Level;
import java.util.logging.Logger;

import net.exclaimindustries.paste.braket.client.BraketUser;
import net.exclaimindustries.paste.braket.client.LeaderboardService;
import net.exclaimindustries.paste.braket.client.LeaderboardServiceAsync;
import net.exclaimindustries.paste.braket.client.UserRanking;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;

public class UserStatusPanel extends Composite {

    private static UserStatusPanelUiBinder uiBinder = GWT
            .create(UserStatusPanelUiBinder.class);

    @UiField
    InlineLabel nameField;

    @UiField
    InlineLabel pointsField;

    @UiField
    InlineLabel rankField;

    @UiField
    Anchor logOutAnchor;

    interface UserStatusPanelUiBinder extends UiBinder<Widget, UserStatusPanel> {
    }

    private Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    private String logOutLink;

    private HandlerRegistration logOutHandlerRegistration;

    private ClickHandler logOutClickHandler = new ClickHandler() {
        @Override
        public void onClick(ClickEvent event) {
            // Disable multiple clicks
            logOutHandlerRegistration.removeHandler();
            // Load the log-out link
            Window.Location.assign(logOutLink);
        }
    };

    private LeaderboardServiceAsync leaderboardServiceRPC = GWT
            .create(LeaderboardService.class);

    private BraketUser currentUser;

    private AsyncCallback<UserRanking> rankingCallback =
            new AsyncCallback<UserRanking>() {
                @Override
                public void onFailure(Throwable caught) {
                    logger.log(
                            Level.SEVERE,
                            "failure getting ranking: "
                                    + caught.getLocalizedMessage());
                    clearRank();
                    pointsField.setText("(an error occured while getting points!)");
                    // TODO Do something else?
                }

                @Override
                public void onSuccess(UserRanking result) {
                    if (result == null) {
                        // don't display anything
                        clearPoints();
                        clearRank();
                    }
                    setPoints(result.getPoints(), result.getPointsPossible());
                    setRank(result.getRank(), result.getNumberOfParticipants(),
                            result.getTies());
                    // TODO call an update after a polling interval of, say, 1
                    // minute?
                }
            };

    public UserStatusPanel(BraketUser user) {
        initWidget(uiBinder.createAndBindUi(this));
        currentUser = user;
        setName(currentUser.getName().getFullName("anonymous loser"));
        logOutLink = user.getLogOutLink();
        logOutHandlerRegistration = logOutAnchor.addClickHandler(logOutClickHandler);
        leaderboardServiceRPC.getUserRanking(currentUser, rankingCallback);
    }

    private void setName(String name) {
        nameField.setText(name);
    }

    private void setPoints(double points, double possible) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(points).append('/').append(possible)
                .append(" Possible Points");
        pointsField.setText(stringBuilder.toString());
    }

    private void clearPoints() {
        pointsField.setText("(tournament has not yet begun)");
    }

    private void setRank(int rank, int competetors, int tie) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(rank);
        // Get the ordinal
        int remainder = rank % 100;
        if (remainder >= 10 && remainder <= 20) {
            stringBuilder.append("th");
        } else {
            remainder %= 10;
            switch (remainder) {
            case 1:
                stringBuilder.append("st");
                break;
            case 2:
                stringBuilder.append("nd");
                break;
            case 3:
                stringBuilder.append("rd");
                break;
            default:
                stringBuilder.append("th");
                break;
            }
        }

        stringBuilder.append(" of ").append(competetors);

        if (tie > 1) {
            stringBuilder.append(" (").append(tie).append("-Way Tie)");
        }

        rankField.setText(stringBuilder.toString());
    }

    private void clearRank() {
        rankField.setText("");
    }

}
