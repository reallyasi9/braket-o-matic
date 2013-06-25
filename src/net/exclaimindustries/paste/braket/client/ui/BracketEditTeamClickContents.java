package net.exclaimindustries.paste.braket.client.ui;

import java.util.List;

import net.exclaimindustries.paste.braket.client.BraketTeam;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * The BracketEditTeamClickContents is what shows its filthy, ratty face around
 * whenever the user clicks an editable BracketButton.  This should display
 * enough information to allow the user to make an educated decision as to what
 * team they believe is to be the superior victor, which is important, as this
 * sort of decision is the sort that will follow them for the rest of their
 * lives.  You don't want to be that guy who picked Oakland University to go all
 * the way back in 2005, of course.  Nobody wants to be THAT guy.
 *  
 * @author Nicholas Killewald
 */
public class BracketEditTeamClickContents extends Composite {

    private static BracketEditTeamClickContentsUiBinder uiBinder = GWT
            .create(BracketEditTeamClickContentsUiBinder.class);

    interface BracketEditTeamClickContentsUiBinder extends
            UiBinder<Widget, BracketEditTeamClickContents> {
    }

    @UiField Image teamIcon;
    @UiField ListBox listBox;
    @UiField Button submitButton;
    
    /**
     * Our list of teams.  The order is important.
     */
    private List<BraketTeam> mTeams;
    
    private ChangeHandler mChanger = new ChangeHandler() {

        @Override
        public void onChange(ChangeEvent event) {
            // You know what?  Forget the event, we'll just check ourselves.
            updatePicture(listBox.getSelectedIndex());
        }
        
    };
    
    public BracketEditTeamClickContents() {
        initWidget(uiBinder.createAndBindUi(this));
        
        listBox.setVisibleItemCount(1);
        updatePicture(0);
        
        listBox.sinkEvents(Event.ONCHANGE);
        listBox.addHandler(mChanger, ChangeEvent.getType());
    }
    
    void assignClickHandler(ClickHandler handler) {
        submitButton.addClickHandler(handler);
    }
    
    BraketTeam getSelectedTeam() {
        // Returning a null means they didn't pick anything.
        int selection = listBox.getSelectedIndex();
        
        if(selection == 0) return null;
        selection--;
        return mTeams.get(selection);
    }
    
    void updateTeamList(List<BraketTeam> teams, BraketTeam currentTeam) {
        mTeams = teams;
        listBox.clear();
        
        // Always have "Unknown" be an option.
        listBox.addItem("-- Dunno -- ", "-1");
        
        int curIndex = 1;
        for(BraketTeam team : mTeams) {
            listBox.addItem(team.getName().getSchoolName(), Integer.toString(team.getIndex()));
            
            if(currentTeam != null && currentTeam.getIndex() == team.getIndex()) {
                listBox.setSelectedIndex(curIndex);
                updatePicture(curIndex);
            }
            
            curIndex++;
        }
        
        if(currentTeam == null) {
            listBox.setSelectedIndex(0);
            updatePicture(0);
        }
    }
    
    void updatePicture(int selectedIndex) {
        if(selectedIndex == 0) {
            teamIcon.setUrl("images/unknown-selection.png");
            return;
        }
        
        // PAY ATTENTION!  Since index 0 is the unknown entry, WE MUST SUBTRACT
        // ONE FROM THE INDEX so it lines up with mTeams.  Or PAIN.
        selectedIndex--;
        
        teamIcon.setUrl(mTeams.get(selectedIndex).getPicture());
    }
    
}
