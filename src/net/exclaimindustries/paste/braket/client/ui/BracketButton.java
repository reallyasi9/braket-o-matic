package net.exclaimindustries.paste.braket.client.ui;

import java.util.ArrayList;
import java.util.List;

import net.exclaimindustries.paste.braket.client.BraketPrediction;
import net.exclaimindustries.paste.braket.client.Tournament;
import net.exclaimindustries.paste.braket.shared.Fixture;
import net.exclaimindustries.paste.braket.shared.Team;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ScrollEvent;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * A BracketButton is the thing in the middle of a BracketCell that displays the
 * user's selection of a winner for the given bout.  It's also what can be
 * hovered for data and clicked for editing power.
 * 
 * @author Nicholas Killewald
 */
public class BracketButton extends Composite implements ClickHandler, MouseOverHandler, MouseOutHandler, MouseMoveHandler {
    private class TeamHoverPopup extends PopupPanel {
        private BracketTeamHoverContents mContents;
        
        public TeamHoverPopup() {
            super(false);
            addStyleName("bracket-hover-popup");
            
            mContents = new BracketTeamHoverContents(mController.getTeams().get(mIndex), mType == Type.TEAM);
            setWidget(mContents);
        }
    }
    
    private class EditTeamClickPopup extends PopupPanel {
        private BracketEditTeamClickContents mContents;
        private List<Team> mTeamList;
        private BraketPrediction mSelection;
        private Tournament mTournament;
        
        public EditTeamClickPopup() {
            super(true);
            addStyleName("bracket-click-popup");
            
            mContents = new BracketEditTeamClickContents();
            setWidget(mContents);
            
            mTournament = mController.getTournament();
            mSelection = mController.getSelection();
            mTeamList = mController.getTeams();
            
            mContents.assignClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    actOnSelection();
                }
                
            });
        }
        
        private void actOnSelection() {
            super.hide();
            
            Team selection = mContents.getSelectedTeam();
            
            // Now, past this, the controller will finish the job and tell all
            // the columns to update.
            mController.pickTeam(mIndex, selection);
        }
        
        @Override
        public void show() {
            // Before showing, we need to set things up a bit.
            int selectedTeam = mController.getSelection().getSelectedTeamIndex(mIndex);
            Team team = (selectedTeam != -1 ? mController.getTeams().get(selectedTeam) : null);
            mContents.updateTeamList(getPossibleTeams(), team);
            super.show();
        }
        
        private List<Team> getPossibleTeams() {
            List<Team> toReturn = new ArrayList<Team>();
            
            // If this is null, we should be here in the first place!
            assert(mSelection != null);
            
            // So, we start hereabouts and start chaining upward.
            recursePossibleTeams(toReturn, mIndex);
            
            return toReturn;
        }
        
        private void recursePossibleTeams(List<Team> teams, int curGame) {
            // Recurses!  Foiled again!
            if(mSelection.hasSelection(curGame) && curGame != mIndex) {
                // If the user's already picked something here, return that.
                teams.add(mTeamList.get(mSelection.getSelectedTeamIndex(curGame)));
            } else if(!mTournament.hasChildGame(curGame, true)) {
                // If we've topped out, add both the children.  To figure this
                // one out, we'll need to use... MATH.  And the fact that we
                // know how the outside of the bracket looks.
                teams.add(mTeamList.get((curGame - 31) * 2));
                teams.add(mTeamList.get(((curGame - 31) * 2) + 1));
            } else {
                // Otherwise, keep on recursing.
                recursePossibleTeams(teams, mTournament.getChildGameIndex(curGame, true));
                recursePossibleTeams(teams, mTournament.getChildGameIndex(curGame, false));
            }
        }
    }
    
    /**
     * The InnerWindowScrollHandler, to be honest, is a bit of a hack job.  This
     * being static prevents us from making a new ScrollHandler for each of the
     * potentially 127 Buttons every time we update the Container.
     * 
     * Problem is, of course, we need extra static juggling to keep everything
     * in check.  That's where reinit comes in; since we can only have one
     * popup at any time, we take advantage of this by just reinitting a single
     * static object every time.
     * 
     * @author Nicholas Killewald
     */
    private static class InnerWindowScrollHandler implements Window.ScrollHandler {
        private int oldScrollLeft = Window.getScrollLeft();
        private int oldScrollTop = Window.getScrollTop();
        private PopupPanel mPopup;
        
        /**
         * Reinits the popup, declaring that we're supposed to be controlling a
         * new one.
         * 
         * @param popup the new popup
         */
        public void reinit(PopupPanel popup) {
            oldScrollLeft = Window.getScrollLeft();
            oldScrollTop = Window.getScrollTop();
            mPopup = popup;
        }
        
        @Override
        public void onWindowScroll(ScrollEvent event) {
            if(mPopup != null && !mPopup.isShowing()) return;
            // Unlike in the mouse handler, we're at the mercy of the last
            // known location of the popup (the mouse handler forcibly moves
            // the popup, so the last location doesn't matter).
            int deltaLeft = event.getScrollLeft() - oldScrollLeft;
            int deltaTop = event.getScrollTop() - oldScrollTop;
            
            mPopup.setPopupPosition(mPopup.getPopupLeft() + deltaLeft, mPopup.getPopupTop() + deltaTop);
            
            oldScrollLeft = event.getScrollLeft();
            oldScrollTop = event.getScrollTop();
        }
        
    }
    
    private static InnerWindowScrollHandler mWinHandler = new InnerWindowScrollHandler();
    private static boolean mYouAlreadyRegisteredForWindowScrolls = false;
    
    /** Maximum size of the team icon (square in pixels). */
    static final int MAX_DIM = 110;
    /** Maximum size of the icon padding. */
    static final int MAX_PADDING = 5;
    
    /** Background color for an unknown result. */
    static final String COLOR_UNKNOWN = "#777777";
    /** Background color for a victorious result. */
    static final String COLOR_VICTORY = "#00CC00";
    /** Background color for a complete failure of a result. */
    static final String COLOR_FAILURE = "#CC0000";
    
    private static BracketButtonUiBinder uiBinder = GWT
            .create(BracketButtonUiBinder.class);

    interface BracketButtonUiBinder extends UiBinder<HTMLPanel, BracketButton> {
    }
    
    public enum GameResult {
        /** Status of this game is unknown. */
        UNKNOWN,
        /** This game ended in victory for the user. */
        VICTORY,
        /**
         * This game ended in failure for the user.  This includes any games
         * dependent on this one (that is, if the user's pick for the grand
         * champion loses in the first round, all subsequent games requiring
         * that winner also get this enum).
         */
        FAILURE
    }
    
    private GameResult mResult;
    
    private PopupPanel mHoverPopup;
    private PopupPanel mClickPopup;
    
    @UiField DivElement outline;
    @UiField ImageElement icon;
    @UiField ImageElement overlay;
    @UiField Label seed;
    
    enum Type {
        /** Don't use this. */
        INVALID,
        /** A team leaf in an editable context. */
        TEAM,
        /**
         * A team leaf in a non-editable context (tournament started, viewing
         * someone else's picks).
         */
        TEAM_NONEDITABLE,
        /** A pick in an editable context. */
        PICK_EDITABLE,
        /** A pick in a non-editable context (viewing someone else's picks) */
        PICK_NONEDITABLE,
        /** A result for an in-progress tournament. */
        RESULT
    }
    
    private Type mType = Type.INVALID;
    private int mIndex = -1;
    private BracketContainer mController;
    
    public BracketButton() {
        initWidget(uiBinder.createAndBindUi(this));
        addStyleName("bracket-button-container");
       
        // Make it the unknown result icon until we know better.
        setTeamIcon("images/unknown-selection.png");
        resizeTeamIcon(MAX_DIM);
        setGameResult(GameResult.UNKNOWN);
        
        sinkEvents(Event.ONCLICK | Event.ONMOUSEOVER | Event.ONMOUSEOUT | Event.ONMOUSEMOVE);
        addHandler(this, ClickEvent.getType());
        addHandler(this, MouseOverEvent.getType());
        addHandler(this, MouseOutEvent.getType());
        addHandler(this, MouseMoveEvent.getType());
        
        // Start the window scroll registration the first time we ever call for
        // a button.  This is a very cheap hack to speed things up a bit.  Like,
        // really really cheap.  It's a hack.  I'm ashamed.
        if(!mYouAlreadyRegisteredForWindowScrolls) {
            Window.addWindowScrollHandler(mWinHandler);
            mYouAlreadyRegisteredForWindowScrolls = true;
        }
    
        // Hide the seed until something tells us what the value is.
        seed.addStyleName("bracket-seed");
        seed.setVisible(false);
    }
    
    /**
     * BracketCell calls this after construction to configure stuff.  This is
     * largely just for the popup.  ButtonCell should control the rest.
     * 
     * @param type
     * @param index
     * @param controller
     */
    void initData(Type type, int index, BracketContainer controller) {
            mType = type;
            mIndex = index;
            mController = controller;
            
            // Now, what's all this mean for us?  Well, we can set up popups.
            switch(type)
            {
                case TEAM:
                case TEAM_NONEDITABLE:
                    mHoverPopup = new TeamHoverPopup();
                    getElement().getStyle().setCursor(Cursor.DEFAULT);
                    break;
                case PICK_EDITABLE:
                    mClickPopup = new EditTeamClickPopup();
                    break;
                case RESULT:
                case PICK_NONEDITABLE:
                    getElement().getStyle().setCursor(Cursor.DEFAULT);
                setGameResult(determineResult());
                    break;
                default:
                    break;
            }
        }
    
    private GameResult determineResult() {
        // If this isn't a result or someone else's picks, this is unknown.
        // Period.  Same applies if there's no selection.
        if((mType != Type.RESULT && mType != Type.PICK_NONEDITABLE) || mController.getSelection() == null) return GameResult.UNKNOWN;
        
        List<Fixture> gameList = mController.getGames();
        Fixture game = gameList.get(mIndex); 
        
        // So what we have here is a team the user picked.  If the game's been
        // played, we know there's SOME result.  If it hasn't been, we need to
        // backtrack to see if this result is impossible (i.e. the user picked a
        // team that lost earlier).
        int pickedIndex = mController.getSelection().getSelectedTeamIndex(mIndex);
        // It could be that you didn't pick a game.  That means you get no
        // points, no matter what!
        if (pickedIndex < 0) {
            return GameResult.FAILURE;
        }
        Team pickedTeam = mController.getTeams().get(pickedIndex);
        
        if(game.isFinal()) {
            // The game's done!  Who won?
            if(game.getWinningTeamId().longValue() == pickedTeam.getId().longValue()) {
                // WE WIN!!!
                return GameResult.VICTORY;
            } else {
                // We lose. :-(
                return GameResult.FAILURE;
            }
        } else {
            // Hm.  Now it gets tricky.  Start from the leaf nodes and work
            // forward.  If we hit a point at which the selected team lost, then
            // this is also a loss.  If we hit an unknown or the game itself
            // before doing so, this is an unknown.  There's no victory
            // condition here, as this game hasn't been played.
            //
            // TODO: I really should generalize the traversal method...
            Tournament tourn = mController.getTournament();
            
            int curGame = (pickedTeam.getIndex() / 2) + (tourn.getNumberOfGames() / 2);
            
            do {
                Fixture checkGame = gameList.get(curGame);
                
                if(!checkGame.isFinal()) {
                    // If we haven't returned AND this game isn't final yet,
                    // then this must be unknown.
                    return GameResult.UNKNOWN;
                }
                
                // The game has been decided!  Who won?
                if(checkGame.getWinningTeamId().longValue() != pickedTeam.getId().longValue()) {
                    // Our team lost.  So, we're losers. :-(
                    return GameResult.FAILURE;
                }
    
                // We didn't return, so our team won that last game.  Advance to
                // the next one!
                curGame = (curGame - 1) / 2;
            } while(curGame > mIndex);
            
            // If my math and logic are right, we shouldn't have gotten here,
            // but if we did, this is clearly unknown.
            return GameResult.UNKNOWN;
        }
    }
    
    void setSeed(int seedNum) {
        if(seedNum > 0) {
            seed.setText(Integer.toString(seedNum));
            seed.setVisible(true);
        }
        else {
            seed.setVisible(false);
        }
    }
    
    boolean isClickable() {
        if(mType == Type.PICK_EDITABLE) return true;
        else return false;
    }
    
    boolean isHoverable() {
        if(mType == Type.TEAM || mType == Type.TEAM_NONEDITABLE) return true;
        else return false;
    }
    
    /**
     * Updates the team icon.  This should be called any time the user changes
     * a selection in the tree, or on initial setup of a full results tree.
     * 
     * @param url URL to the new icon
     */
    public void setTeamIcon(String url) {
        icon.setSrc(url);
    }
    
    /**
     * Resizes the team icon.  The dimensions will always be capped at MAX_WIDTH
     * and MAX_HEIGHT.  Note that the overall size of the button will still
     * account for the outline and border.
     * 
     * @param width new width
     * @param height new height
     */
    void resizeTeamIcon(int dim) {
        // First, normalize.
        dim = (dim > MAX_DIM ? MAX_DIM : dim);
        
        // Apply size liberally.
        icon.setWidth(dim);
        icon.setHeight(dim);
        overlay.setWidth(dim);
        overlay.setHeight(dim);
        outline.getStyle().setWidth(dim, Unit.PX);
        outline.getStyle().setHeight(dim, Unit.PX);
        
        // The outline's padding needs to be adjusted, too.
        int padding = dim / 20;
        
        padding = (padding > MAX_PADDING ? MAX_PADDING : (padding < 1 ? 1 : padding));
        outline.getStyle().setPadding(padding, Unit.PX);
        overlay.getStyle().setLeft(padding, Unit.PX);
        overlay.getStyle().setTop(padding, Unit.PX);
    }
    
    /**
     * Assigns a result to this button.  Note that all buttons default to the
     * unknown result state.  That's not "undefined behavior"; I literally mean
     * we don't know the result of the game this button represents.
     * 
     * @param result the result
     */
    public void setGameResult(GameResult result) {
        mResult = result;
        
        // Now, update the outline.
        switch(mResult) {
            case UNKNOWN:
                outline.getStyle().setBackgroundColor(COLOR_UNKNOWN);
                overlay.getStyle().setDisplay(Display.NONE);
                break;
            case VICTORY:
                outline.getStyle().setBackgroundColor(COLOR_VICTORY);
                overlay.getStyle().setDisplay(Display.INLINE);
                overlay.setSrc("images/overlay-victory.png");
                break;
            case FAILURE:
                outline.getStyle().setBackgroundColor(COLOR_FAILURE);
                icon.addClassName("bracket-button-icon-failure");
                overlay.getStyle().setDisplay(Display.INLINE);
                overlay.setSrc("images/overlay-failure.png");
                break;
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        if(isClickable()) {
            // First, dismiss the current popup, whatever it is.
            if(mHoverPopup != null) mHoverPopup.hide();
            
            // Then, out comes our click friend.
            mClickPopup.show();
            
            // We do NOT track the mouse in this case.  But, the initial
            // location of the popup is still where the other one puts it.
            positionPopupOnMouse(mClickPopup, event.getClientX(), event.getClientY());
        }
    }

    @Override
    public void onMouseOut(MouseOutEvent event) {
        if(isHoverable())
            mHoverPopup.hide();
    }

    @Override
    public void onMouseOver(MouseOverEvent event) {
        if(isHoverable()) {
            // If the click popup is showing, ignore this.
            if(mClickPopup != null && mClickPopup.isShowing()) return;
            
            mHoverPopup.show();
            
            mWinHandler.reinit(mHoverPopup);
            
            positionPopupOnMouse(mHoverPopup, event.getClientX(), event.getClientY());
        }
    }

    @Override
    public void onMouseMove(MouseMoveEvent event) {
        if(isHoverable()) {
            if(mClickPopup != null && mClickPopup.isShowing()) return;
            
            positionPopupOnMouse(mHoverPopup, event.getClientX(), event.getClientY());
        }
    }
    
    private void positionPopupOnMouse(PopupPanel popup, int mouseX, int mouseY) {
        // The popup should go 20px OVER the mouse cursor, horizontally centered
        // where the cursor is.  If this would push it over the top of the
        // window, place it 10px below the mouse (if THAT pushes it over the
        // bottom of the window, well, too bad, user).  If this would push it
        // off one of the sides, clamp it to the edge.
        int left = mouseX - (popup.getOffsetWidth() / 2);
        int top = mouseY - popup.getOffsetHeight() - 20;
        
        if(left < 0)
            left = 0;
        if(left + popup.getOffsetWidth() > Window.getClientWidth())
            left = Window.getClientWidth() - popup.getOffsetWidth();
        
        if(top < 0) 
            top = mouseY + 20;
        // Don't repeat for "too far below"; the only way that'll happen is if
        // the window is so small that the popup can neither fit above nor below
        // the mouse, and we don't have a use case for THAT.
        
        // The popup is always placed in relation to the DOCUMENT, not the
        // WINDOW.  So, to correct this, add the current scroll status.
        left += Window.getScrollLeft();
        top += Window.getScrollTop();
        
        popup.setPopupPosition(left, top);
    }
}
