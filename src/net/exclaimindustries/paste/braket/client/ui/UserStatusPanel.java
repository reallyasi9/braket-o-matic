package net.exclaimindustries.paste.braket.client.ui;

import net.exclaimindustries.paste.braket.client.BraketUser;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
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
    Anchor logoutAnchor;

    interface UserStatusPanelUiBinder extends UiBinder<Widget, UserStatusPanel> {
    }

    public UserStatusPanel(BraketUser user) {
        initWidget(uiBinder.createAndBindUi(this));
        setName(user.getName().getFullName("anonymous loser"));
        setPoints(0, 124);
        setRank(100, 100, 100);
    }

    public void setName(String name) {
        nameField.setText(name);
    }

    public void setPoints(int points, int possible) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(points).append('/').append(possible)
                .append(" Possible Points");
        pointsField.setText(stringBuilder.toString());
    }

    public void setRank(int rank, int competetors, int tie) {
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

}
