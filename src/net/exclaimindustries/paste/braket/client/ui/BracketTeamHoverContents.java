package net.exclaimindustries.paste.braket.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import net.exclaimindustries.paste.braket.shared.Team;

/**
 * The BracketTeamHoverContents are what the user sees when mousing over a team
 * icon.  It shows the team's icon and the team's name.  And that's it.
 * 
 * @author Nicholas Killewald
 */
public class BracketTeamHoverContents extends Composite {

    private static BracketTeamHoverContentsUiBinder uiBinder = GWT
            .create(BracketTeamHoverContentsUiBinder.class);

    interface BracketTeamHoverContentsUiBinder extends
            UiBinder<Widget, BracketTeamHoverContents> {
    }

    @UiField ImageElement teamIcon;
    @UiField DivElement teamName;
    @UiField DivElement hintText;
    
    public BracketTeamHoverContents(Team team, boolean editable) {
        initWidget(uiBinder.createAndBindUi(this));
        
        // This popup doesn't need to do ANYTHING but display an icon and some
        // team info.
        teamIcon.setSrc(team.getImage());
        teamName.setInnerText(team.getName().getDisplayName());
        
        if(!editable)
            hintText.getStyle().setDisplay(Display.NONE);
    }
}
