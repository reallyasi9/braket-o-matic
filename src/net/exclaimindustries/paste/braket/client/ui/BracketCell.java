/**
 * BracketCell.java
 * Copyright (C)2013 Nicholas Killewald
 * 
 * This file is distributed under the terms of the BSD license.
 * The source package should have a LICENCE file at the toplevel.
 */
package net.exclaimindustries.paste.braket.client.ui;

import net.exclaimindustries.paste.braket.client.BraketTeam;
import net.exclaimindustries.paste.braket.client.ui.BracketButton.Type;
import net.exclaimindustries.paste.braket.client.ui.BracketContainer.Mode;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IntegerBox;

/**
 * A BracketCell is where the fun happens.  Each Cell represents one bout in a
 * tournament.  It contains a BracketButton that indicates this data.  It also
 * contains a canvas element to draw out the bracket lines.
 * 
 * @author Nicholas Killewald
 */
public class BracketCell extends BracketDrawable {
    
    private static final double BUTTON_CLEARANCE_PERCENT = 0.2;
    private static final int TIEBREAKER_CLEARANCE = 100;

    protected static BracketCellUiBinder uiBinder = GWT
            .create(BracketCellUiBinder.class);

    interface BracketCellUiBinder extends UiBinder<FlowPanel, BracketCell> {
    }

    @UiField FlowPanel mainContainer;
    @UiField BracketButton buttonContainer;
    @UiField FlowPanel tiebreakerContainer;
    @UiField IntegerBox tiebreakerInput;
    
    Canvas mCanvas = Canvas.createIfSupported();
    
    /**
     * If true, this is a terminus cell.  In other words, this represents an
     * initially seeded team, one with no parent games.  There should be 64 of
     * that type in a full bracket.  Marking this as true makes the ID represent
     * a team, not a game.
     * 
     * This does NOT apply to the outer games in a Final Four, Elite Eight, etc
     * setup.
     */
    private boolean mTerminus = false;

    /**
     * The ID of this cell.  If this is a terminus cell, this is the ID of the
     * team inside it.  If not, this is the game number.
     */
    private int mId = -1;

    /**
     * Constructs a BracketCell, complete with its own game number.
     *
     * @param id this cell's team or game ID
     */
    public BracketCell(int id) {
        initWidget(uiBinder.createAndBindUi(this));
        
        if(mCanvas != null)
        {
            mainContainer.add(mCanvas);
        }
        
        addStyleName("bracket-cell");
        setId(id);
    }
  
    /**
     * Sets whether or not this is a terminus cell.  That is, if it's an initial
     * seed or not.  This dictates how the bracket is drawn, and also forces the
     * contained BracketButton into non-editable mode, even if the tournament
     * hasn't begun yet.
     *
     * @param term true to be a terminus, false to not be a terminus
     */
    void setTerminus(boolean term) {
        mTerminus = term;
    }

    /**
     * Assigns a game number or team ID to this cell.  That is, either the
     * internal number assigned to each game OR the ID of the team.  It's the
     * game number if this isn't a terminus, the team ID if it is.
     *
     * @param gameNum game number to assign to this cell
     */
    void setId(int id) {
        if(id < 0) throw new IllegalArgumentException("Cell IDs can't be negative!");

        mId = id;
    }
    
    public void setPixelSize(int width, int height) {
        super.setPixelSize(width, height);
        
        // We also need the BracketButton to resize itself if need be.  The max
        // size is covered in BracketButton itself, so all we'll do is assume we
        // want our pre-defined amount of clearance between the top and bottom
        // bracket lines and the left and right sides of the image.
        int idealWidth = (int)(getOffsetWidth() * (1 - BUTTON_CLEARANCE_PERCENT));
        int idealHeight = (int)((getOffsetHeight() / 2) * (1 - BUTTON_CLEARANCE_PERCENT));
        
        int newSize = (idealWidth < idealHeight ? idealWidth : idealHeight);
        
        buttonContainer.resizeTeamIcon(newSize);
        
        // Then, make sure it stays centered.
        recenterButton();
    }
    
    /**
     * Recenters the button in the middle of the cell.  In general, don't call
     * this directly; setPixelSize and draw will take care of it.
     */
    void recenterButton() {
        int centerX = (getOffsetWidth() - buttonContainer.getOffsetWidth()) / 2;
        int centerY = (getOffsetHeight() - buttonContainer.getOffsetHeight()) / 2;

        buttonContainer.getElement().getStyle().setLeft(centerX, Unit.PX);
        buttonContainer.getElement().getStyle().setTop(centerY, Unit.PX);
    }

    @Override
    void fetchData() {
        // Hello!  I have data!
        if(mTerminus) {
            // Terminus nodes (that is, teams) ALWAYS just display the team
            // without any click popups.  Hovers, yes.
            BraketTeam team = mController.getTeams().get(mId);
            
            buttonContainer.setTeamIcon(team.getPicture());
            buttonContainer.setSeed(team.getSeed());
            
            Type type = Type.TEAM_NONEDITABLE;
            if(mController.getMode() == Mode.USER_PICKS)
                type = Type.TEAM;
            
            buttonContainer.initData(type, mId, mController);
        } else if(mController.getMode() == Mode.USER_PICKS
                || mController.getMode() == Mode.USER_PICKS_READONLY) {

            if(mController.getSelection().hasSelection(mId)) {
                int teamIndex = mController.getSelection().getSelectedTeamIndex(mId);
                BraketTeam team = mController.getTeams().get(teamIndex);
                buttonContainer.setTeamIcon(team.getPicture());
                buttonContainer.setSeed(team.getSeed());
            } else {
                buttonContainer.setTeamIcon("images/unknown-selection.png");
                buttonContainer.setSeed(-1);
            }
            
            Type type = Type.PICK_NONEDITABLE;
            if(mController.getMode() == Mode.USER_PICKS)
                type = Type.PICK_EDITABLE;
            
            buttonContainer.initData(type, mId, mController);
        } else if(mController.getMode() == Mode.TOURNAMENT_STATUS) {
            // Tournament status WITHOUT picks just traces out the teams in the
            // current tournament, if the team's played.
            if(mController.getTournament().isCompletedGame(mId)) {
                int teamIndex = mController.getTournament().getSelectedTeamIndex(mId);
                BraketTeam team = mController.getTeams().get(teamIndex);
                buttonContainer.setTeamIcon(team.getPicture());
                buttonContainer.setSeed(team.getSeed());
            } else {
                buttonContainer.setTeamIcon("images/unknown-selection.png");
                buttonContainer.setSeed(-1);
            }
            buttonContainer.initData(Type.PICK_NONEDITABLE, mId, mController);
        } else if(mController.getMode() == Mode.TOURNAMENT_STATUS_WITH_USER_PICKS) {
            if(mController.getSelection().hasSelection(mId)) {
                int teamIndex = mController.getSelection().getSelectedTeamIndex(mId);
                BraketTeam team = mController.getTeams().get(teamIndex);
                buttonContainer.setTeamIcon(team.getPicture());
                buttonContainer.setSeed(team.getSeed());
            } else {
                buttonContainer.setTeamIcon("images/unknown-selection.png");
                buttonContainer.setSeed(-1);
            }
            buttonContainer.initData(Type.RESULT, mId, mController);
        }
    }
    
    @Override
    public void setDirection(Direction dir) {
        super.setDirection(dir);
        
        if(mDirection == Direction.FINAL && mController.getSelection() != null) {
            // The final game needs to turn on the tiebreaker, position it, and
            // attach the change handler.  Positioning it takes place later.
            tiebreakerContainer.getElement().getStyle().setVisibility(Visibility.VISIBLE);
            
            tiebreakerInput.setValue(mController.getSelection().getTieBreaker());
            
            if(mController.isTournamentEditableRightNow() && mController.canUserEditThis()) {
                tiebreakerInput.addValueChangeHandler(new ValueChangeHandler<Integer>() {
    
                    @Override
                    public void onValueChange(ValueChangeEvent<Integer> event) {
                        // First, make sure the integer's valid.  It might not be,
                        // since apparently IntegerBox doesn't actually make sure it
                        // is before going anywhere.
                        Integer val = tiebreakerInput.getValue();
                        if(val == null || val <= 0) {
                            // NO!  WRONG!  TOTALLY WRONG!  WHERE DID YOU LEARN TO
                            // INPUT TIEBREAKERS LIKE THAT?  STOP IT!
                            tiebreakerInput.addStyleName("bracket-tiebreaker-input-invalid");
                        } else {
                            tiebreakerInput.removeStyleName("bracket-tiebreaker-input-invalid");
                            mController.setTieBreaker(val);
                        }
                    }
                    
                });
            } else {
                tiebreakerInput.setEnabled(false);
            }
        }
    }
    
    void recenterTieBreaker() {
        int centerY = ((getOffsetHeight() - tiebreakerContainer.getOffsetHeight()) / 2) + (buttonContainer.getOffsetHeight() / 2) + TIEBREAKER_CLEARANCE;
        tiebreakerContainer.getElement().getStyle().setTop(centerY, Unit.PX);
    }

    @Override
    public void draw() {
        mCanvas.setPixelSize(getOffsetWidth(), getOffsetHeight());
        recenterButton();
        
        switch(mDirection) {
            case LEFT:
                drawLeftBracket();
                break;
            case RIGHT:
                drawRightBracket();
                break;
            case FINAL:
                drawFinalBracket();
                recenterTieBreaker();
                break;
        }
    }
    
    private void drawLeftBracket() {
        Context2d con = initBracketCanvas(mCanvas);
        
        con.beginPath();
        
        int width = mCanvas.getOffsetWidth();
        int height = mCanvas.getOffsetHeight();
        
        if(!mTerminus) {
            con.moveTo(0, height / 4.0);
            con.lineTo(width / 2.0, height / 4.0);
            con.lineTo(width / 2.0, (3.0 * height) / 4.0);
            con.lineTo(0, (3.0 * height) / 4);
            con.stroke();
        }

        con.moveTo(width / 2.0, height / 2.0);
        con.lineTo(width, height / 2.0);
        con.stroke();
    }
    
    private void drawRightBracket() {
        Context2d con = initBracketCanvas(mCanvas);

        con.beginPath();
        
        int width = mCanvas.getOffsetWidth();
        int height = mCanvas.getOffsetHeight();
        
        if(!mTerminus) {
            con.moveTo(width, height / 4.0);
            con.lineTo(width / 2.0, height / 4.0);
            con.lineTo(width / 2.0, (3.0 * height) / 4.0);
            con.lineTo(width, (3.0 * height) / 4.0);
            con.stroke();
        }
        
        con.moveTo(width / 2.0, height / 2.0);
        con.lineTo(0, height / 2.0);
        con.stroke();
    }
    
    private void drawFinalBracket() {
        Context2d con = initBracketCanvas(mCanvas);

        con.beginPath();
        
        int width = mCanvas.getOffsetWidth();
        int height = mCanvas.getOffsetHeight();
        
        con.moveTo(0, height / 2.0);
        con.lineTo(width, height / 2.0);
        con.stroke();
    }
    
    private Context2d initBracketCanvas(Canvas can) {
        can.setCoordinateSpaceHeight(can.getOffsetHeight());
        can.setCoordinateSpaceWidth(can.getOffsetWidth());
        
        Context2d con = can.getContext2d();
        
        con.setLineWidth(3);
        
        return con;
    }
}
