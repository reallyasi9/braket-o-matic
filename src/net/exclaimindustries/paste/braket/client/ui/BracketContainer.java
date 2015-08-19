package net.exclaimindustries.paste.braket.client.ui;

import java.util.ArrayList;
import java.util.List;

import net.exclaimindustries.paste.braket.client.BraketPrediction;
import net.exclaimindustries.paste.braket.client.Tournament;
import net.exclaimindustries.paste.braket.client.ui.BracketDrawable.Direction;
import net.exclaimindustries.paste.braket.shared.Fixture;
import net.exclaimindustries.paste.braket.shared.Team;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * The BracketContainer contains a bunch of BracketColumns, and thus makes up
 * the entirety of the bracket. It creates all Columns, Subcells, and Cells as
 * need be depending on what section is being rendered at the time.
 * 
 * @author Nicholas Killewald
 */
public class BracketContainer extends ScrollPanel {
    final static int COL_HEIGHT = 2048;
    final static int[] COL_WIDTHS = { 200, 200, 140, 140, 120, 100, 80 };

    private static BracketContainerUiBinder uiBinder = GWT
            .create(BracketContainerUiBinder.class);

    interface BracketContainerUiBinder extends
            UiBinder<Widget, BracketContainer> {
    }

    @UiField
    HTMLPanel container;

    /**
     * Enum to select a section of the bracket to display. Note that in all
     * cases except the quarters, the championship round will be included (the
     * quarters will terminate at their respective Final Fours).
     */
    public enum Section {
        /** The Final Four. */
        FINAL_FOUR,
        /** The Elite Eight. */
        ELITE_EIGHT,
        /** The Sweet Sixteen. */
        SWEET_SIXTEEN,
        /** The next level back from the Sweet Sixteen. */
        THUNDERSTRUCK_THIRTY_TWO,
        /** The entire sixty-four team bracket. */
        ENTIRE,
        /** Only the left half of the bracket. */
        LEFT,
        /** Only the right half of the bracket. */
        RIGHT,
        /** Only the top-left quarter of the bracket. */
        TOP_LEFT,
        /** Only the top-right quarter of the bracket. */
        TOP_RIGHT,
        /** Only the bottom-left quarter of the bracket. */
        BOTTOM_LEFT,
        /** Only the bottom-right quarter of the bracket. */
        BOTTOM_RIGHT
    }

    /**
     * Enum to specify what mode this container should be in, and thus what it
     * should display and what the buttons should be able to do with their
     * popups. This is mostly used by BracketCells, really.
     */
    public enum Mode {
        /**
         * Display the current tournament status without any user picks. All
         * unplayed games will show static.
         */
        TOURNAMENT_STATUS,
        /**
         * Display the current tournament status with the user's picks. All
         * unplayed games will show the user's pick for that game.
         */
        TOURNAMENT_STATUS_WITH_USER_PICKS,
        /**
         * Display the user's picks, and allow them to be edited. This might be
         * an admin editing someone else's picks for all we care.
         */
        USER_PICKS,
        /**
         * Display a user's picks, and make them read-only. This is for viewing
         * someone else's picks OR viewing picks after the tournament started,
         * just without the inconvenience of seeing all your failures in the
         * rest of the tournament.
         */
        USER_PICKS_READONLY
    }

    private Section mSection = Section.ENTIRE;
    private Mode mMode = Mode.TOURNAMENT_STATUS;
    private Tournament mTournament;
    private BraketPrediction mSelection;
    private List<Team> mTeams;
    private List<Fixture> mGames;

    private List<BracketColumn> mColumns;

    /**
     * Creates a BracketContainer. You need a valid tournament and team data,
     * else this won't have any clue what's going on.
     * 
     * @param tourn
     *            the BraketTournament containing all the data
     * @param teams
     *            a list of BraketTeams
     */
    public BracketContainer(Tournament tourn, List<Team> teams,
            List<Fixture> games) {
        if (tourn == null)
            throw new IllegalArgumentException(
                    "You need to pass a non-null BraketTournament to BracketContainer's constructor!");
        if (teams == null || teams.isEmpty())
            throw new IllegalArgumentException(
                    "You need to pass a non-null, non-empty list of BraketTeams to BracketContainer's constructor!");
        if (games == null || games.isEmpty())
            throw new IllegalArgumentException(
                    "You need to pass a non-null, non-empty list of BraketGames to BracketContainer's constructor!");

        setWidget(uiBinder.createAndBindUi(this));
        
        setHeight("100%");

        mTournament = tourn;
        mTeams = teams;
        mGames = games;

        mColumns = new ArrayList<BracketColumn>();
    }

    /**
     * Gets the BraketTournament object we're paying attention to.
     * 
     * @return a BraketTournament
     */
    public Tournament getTournament() {
        return mTournament;
    }

    /**
     * Gets the BraketSelection object that the user selected. Note that this
     * may be null if there's no user logged in.
     * 
     * @return a BraketSelection or null
     */
    public BraketPrediction getSelection() {
        return mSelection;
    }

    /**
     * Gets a list of BraketTeams this Container understands.
     * 
     * @return teams, teams, teams!
     */
    public List<Team> getTeams() {
        return mTeams;
    }

    /**
     * Gets a list of BraketGames this Container understands.
     * 
     * @return that there list of games
     */
    public List<Fixture> getGames() {
        return mGames;
    }

    /**
     * Gets the current mode of this Container.
     * 
     * @return the Mode in question
     */
    public Mode getMode() {
        return mMode;
    }

    /**
     * Sets a new tournament, list of teams, and selection. You must reset the
     * teams and selection here due to the fact those are both associated with
     * tournaments. The selection can be null, of course. The tournament and
     * teams should be less so.
     * 
     * @param tourn
     *            the new tournament
     * @param teams
     *            the new list of teams
     * @param selection
     *            the new selection (can be null)
     */
    public void setTournament(Tournament tourn, List<Team> teams,
            List<Fixture> games, BraketPrediction selection) {
        if (tourn == null)
            throw new IllegalArgumentException(
                    "The BraketTournament can't be null!");
        if (teams == null || teams.isEmpty())
            throw new IllegalArgumentException(
                    "The team list can't be null or empty!");
        if (games == null || games.isEmpty())
            throw new IllegalArgumentException(
                    "The game list can't be null or empty!");
        mTournament = tourn;
        mTeams = teams;
        mGames = games;
        mSelection = selection;
    }

    /**
     * Sets the BraketSelection. This CAN be null if the user isn't logged in.
     * Remember to redraw!
     * 
     * @param selection
     *            a new selection of choice
     */
    public void setSelection(BraketPrediction selection) {
        mSelection = selection;
    }

    /**
     * Sets the mode of this Container. Remember to redraw!
     * 
     * @param mode
     *            a brand new mode
     */
    public void setMode(Mode mode) {
        mMode = mode;
    }

    void clearContainer() {
        int count = container.getWidgetCount();

        for (int i = count - 1; i >= 0; i--) {
            container.remove(i);
        }

        mColumns.clear();
    }

    public void setSection(Section section) {
        mSection = section;
    }

    public void draw() {
        // First, wipe everything.
        clearContainer();

        // Then, create a generic bracket based on what section we're set to.
        // To which we're set.
        //
        // TODO: Find a better way to calculate the total width of the
        // container.
        switch (mSection) {
        case ENTIRE:
            applyColumn(createColumn(6, Direction.LEFT, true, 0, 31));
            applyColumn(createColumn(5, Direction.LEFT, false, 31, 46));
            applyColumn(createColumn(4, Direction.LEFT, false, 15, 22));
            applyColumn(createColumn(3, Direction.LEFT, false, 7, 10));
            applyColumn(createColumn(2, Direction.LEFT, false, 3, 4));
            applyColumn(createColumn(1, Direction.LEFT, false, 1, 1));
            applyColumn(createColumn(0, Direction.FINAL, false, 0, 0));
            applyColumn(createColumn(1, Direction.RIGHT, false, 2, 2));
            applyColumn(createColumn(2, Direction.RIGHT, false, 5, 6));
            applyColumn(createColumn(3, Direction.RIGHT, false, 11, 14));
            applyColumn(createColumn(4, Direction.RIGHT, false, 23, 30));
            applyColumn(createColumn(5, Direction.RIGHT, false, 47, 62));
            applyColumn(createColumn(6, Direction.RIGHT, true, 32, 63));
            container.setWidth(Integer.toString(1760) + "px");
            break;
        // case THUNDERSTRUCK_THIRTY_TWO:
        // applyColumn(createColumn(5, Direction.LEFT, false));
        // applyColumn(createColumn(4, Direction.LEFT, false));
        // applyColumn(createColumn(3, Direction.LEFT, false));
        // applyColumn(createColumn(2, Direction.LEFT, false));
        // applyColumn(createColumn(1, Direction.LEFT, false));
        // applyColumn(createColumn(0, Direction.FINAL, false));
        // applyColumn(createColumn(1, Direction.RIGHT, false));
        // applyColumn(createColumn(2, Direction.RIGHT, false));
        // applyColumn(createColumn(3, Direction.RIGHT, false));
        // applyColumn(createColumn(4, Direction.RIGHT, false));
        // applyColumn(createColumn(5, Direction.RIGHT, false));
        // container.setWidth(Integer.toString(1600) + "px");
        // break;
        // case SWEET_SIXTEEN:
        // applyColumn(createColumn(4, Direction.LEFT, false));
        // applyColumn(createColumn(3, Direction.LEFT, false));
        // applyColumn(createColumn(2, Direction.LEFT, false));
        // applyColumn(createColumn(1, Direction.LEFT, false));
        // applyColumn(createColumn(0, Direction.FINAL, false));
        // applyColumn(createColumn(1, Direction.RIGHT, false));
        // applyColumn(createColumn(2, Direction.RIGHT, false));
        // applyColumn(createColumn(3, Direction.RIGHT, false));
        // applyColumn(createColumn(4, Direction.RIGHT, false));
        // container.setWidth(Integer.toString(1400) + "px");
        // break;
        // case ELITE_EIGHT:
        // applyColumn(createColumn(3, Direction.LEFT, false));
        // applyColumn(createColumn(2, Direction.LEFT, false));
        // applyColumn(createColumn(1, Direction.LEFT, false));
        // applyColumn(createColumn(0, Direction.FINAL, false));
        // applyColumn(createColumn(1, Direction.RIGHT, false));
        // applyColumn(createColumn(2, Direction.RIGHT, false));
        // applyColumn(createColumn(3, Direction.RIGHT, false));
        // container.setWidth(Integer.toString(1160) + "px");
        // break;
        // case FINAL_FOUR:
        // applyColumn(createColumn(2, Direction.LEFT, false));
        // applyColumn(createColumn(1, Direction.LEFT, false));
        // applyColumn(createColumn(0, Direction.FINAL, false));
        // applyColumn(createColumn(1, Direction.RIGHT, false));
        // applyColumn(createColumn(2, Direction.RIGHT, false));
        // container.setWidth(Integer.toString(880) + "px");
        // break;
        // case LEFT:
        // applyColumn(createColumn(6, Direction.LEFT, true));
        // applyColumn(createColumn(5, Direction.LEFT, false));
        // applyColumn(createColumn(4, Direction.LEFT, false));
        // applyColumn(createColumn(3, Direction.LEFT, false));
        // applyColumn(createColumn(2, Direction.LEFT, false));
        // applyColumn(createColumn(1, Direction.LEFT, false));
        // container.setWidth(Integer.toString(780) + "px");
        // break;
        // case RIGHT:
        // applyColumn(createColumn(1, Direction.RIGHT, false));
        // applyColumn(createColumn(2, Direction.RIGHT, false));
        // applyColumn(createColumn(3, Direction.RIGHT, false));
        // applyColumn(createColumn(4, Direction.RIGHT, false));
        // applyColumn(createColumn(5, Direction.RIGHT, false));
        // applyColumn(createColumn(6, Direction.RIGHT, true));
        // container.setWidth(Integer.toString(780) + "px");
        // break;
        // case TOP_LEFT:
        // case BOTTOM_LEFT:
        // applyColumn(createColumn(5, Direction.LEFT, true));
        // applyColumn(createColumn(4, Direction.LEFT, false));
        // applyColumn(createColumn(3, Direction.LEFT, false));
        // applyColumn(createColumn(2, Direction.LEFT, false));
        // applyColumn(createColumn(1, Direction.LEFT, false));
        // container.setWidth(Integer.toString(580) + "px");
        // break;
        // case TOP_RIGHT:
        // case BOTTOM_RIGHT:
        // applyColumn(createColumn(1, Direction.RIGHT, false));
        // applyColumn(createColumn(2, Direction.RIGHT, false));
        // applyColumn(createColumn(3, Direction.RIGHT, false));
        // applyColumn(createColumn(4, Direction.RIGHT, false));
        // applyColumn(createColumn(5, Direction.RIGHT, true));
        // container.setWidth(Integer.toString(580) + "px");
        // break;
        default:
            // Blah blah we'll work out the IDs later.
            break;
        }

        // Then, populate the entire tree with data. Later.
    }

    private void applyColumn(BracketColumn col) {
        container.add(col);
        mColumns.add(col);
        col.draw();
    }

    private BracketColumn createColumn(int depth, Direction dir,
            boolean terminus, int lowId, int highId) {
        if (depth < 0)
            throw new IllegalArgumentException(
                    "The column depth needs to be zero or higher!");

        // Depth 0: Champion (one BracketCell, Direction.FINAL)
        // Depth 1: Champion contender (one BracketCell)
        // Depth 2: Final Four contenders (one BracketSubcell layer)
        // Depth 3: Elite Eight contenders (two BracketSubcell layers)
        // Depth 4: Sweet Sixteen contenders (three BracketSubcell layers)
        // Depth 5: Thunderstruck Thirty-Two contenders (four BracketSubcell
        // layers)
        // Depth 6: The initial contenders (five BracketSubcell layers)

        if (depth == 0 || dir == Direction.FINAL) {
            // So from that, we first see that depth 0 is a special case.
            return createSingleCellColumn(Direction.FINAL, lowId);
        } else if (depth == 1) {
            // Depth 1 is also a special case, as it doesn't make a Subcell.
            return createSingleCellColumn(dir, lowId);
        } else {
            // In all other cases, we start recursing.
            return createMultiCellColumn(depth, dir, terminus, lowId, highId);
        }
    }

    private BracketColumn createSingleCellColumn(Direction dir, int id) {
        // This one's simple.
        BracketColumn toReturn = new BracketColumn(dir);

        BracketCell cell = new BracketCell(id);

        toReturn.setChild(cell);
        toReturn.setPixelSize(COL_WIDTHS[0], COL_HEIGHT);

        toReturn.setController(this);

        return toReturn;
    }

    private BracketColumn createMultiCellColumn(int depth, Direction dir,
            boolean terminus, int lowId, int highId) {
        BracketColumn toReturn = new BracketColumn(dir);

        BracketSubcell sub = new BracketSubcell();
        createRecursiveSubcells(sub, depth - 1, dir, terminus, lowId, highId);

        toReturn.setChild(sub);
        toReturn.setPixelSize(COL_WIDTHS[depth], COL_HEIGHT);

        // We need to assign the initial controller to the column.
        toReturn.setController(this);

        return toReturn;
    }

    private void createRecursiveSubcells(BracketSubcell sub, int depth,
            Direction dir, boolean terminus, int lowId, int highId) {
        // IT'S RECURSION TIME, BEEYOTCH!
        if (depth == 1) {
            // If depth is down to 1, we're applying cells.
            BracketCell cell1 = new BracketCell(lowId);
            cell1.setTerminus(terminus);

            BracketCell cell2 = new BracketCell(highId);
            cell2.setTerminus(terminus);

            sub.setChildren(cell1, cell2);

        } else {
            // Otherwise, we're applying subcells and recursing each of THOSE.
            // To do so, we'll need to split the ID range into quarters.
            int split = ((highId - lowId) - 1) / 2;

            BracketSubcell sub1 = new BracketSubcell();
            createRecursiveSubcells(sub1, depth - 1, dir, terminus, lowId,
                    highId - split - 1);

            BracketSubcell sub2 = new BracketSubcell();
            createRecursiveSubcells(sub2, depth - 1, dir, terminus, highId
                    - split, highId);

            sub.setChildren(sub1, sub2);
        }
    }

    /**
     * Tells all the columns to refetch data, updating all cells contained
     * therein in the process.
     */
    public void refetchData() {
        for (BracketColumn col : mColumns) {
            col.fetchData();
        }
    }

    /**
     * Determines if this user has the rights to edit this bracket. Note this
     * does NOT account for whether or not the tournament is in progress.
     * 
     * @return true if editable by this user, false if not
     */
    boolean canUserEditThis() {
        //if (mSelection == null
        //        || BraketEntryPoint.thisUser == null
        //        || (!BraketEntryPoint.thisUser.isAdmin() && !(BraketEntryPoint.thisUser
        //                .getId().equals(mSelection.getUserId())))) {
            return false;
        //} else {
        //    return true;
        //}
    }

    /**
     * Determines if the tournament can be edited in any way at the moment. It's
     * editable if the tournament hasn't started yet OR if the user is an admin.
     * 
     * @return true if that thing I said, false if not
     */
    boolean isTournamentEditableRightNow() {
        //if (BraketEntryPoint.thisUser.isAdmin() || mTournament.isScheduled()) {
            return true;
        //} else {
            //return false;
        //}
    }

    void pickTeam(int index, Team pick) {
        // Are we even allowed to do this?
        if (!canUserEditThis()) {
            throw new IllegalStateException(
                    "You don't have the rights to pick a team on that selection.");
        }

        if (!isTournamentEditableRightNow()) {
            throw new IllegalStateException(
                    "The tournament is in progress!  You had your chance to pick teams!");
        }

        // Oh boy oh boy! We've got a selection! Let's do something about
        // it!
        boolean changed =
                mSelection.setAndPropagateWinner(index,
                        (pick != null ? pick.getIndex() : -1));

        if (changed) {
            //final BraketTeam pickFinal = pick;

//            BraketEntryPoint.selectService.storeSelection(mSelection,
//                    new AsyncCallback<Long>() {
//                        @Override
//                        public void onFailure(Throwable caught) {
//                            BraketEntryPoint.displayException(caught);
//                        }
//
//                        @Override
//                        public void onSuccess(Long result) {
//                            if (pickFinal != null)
//                                BraketEntryPoint
//                                        .displayToast(pickFinal.getName()
//                                                .getSchoolName() + " picked!");
//                            else
//                                BraketEntryPoint
//                                        .displayToast("Selection cleared!");
//
//                            refetchData();
//                        }
//                    });
        }
    }

    void setTieBreaker(Integer value) {
        // Are we even allowed to do this?
        if (!canUserEditThis()) {
            throw new IllegalStateException(
                    "You don't have the rights to set a tiebreaker on that selection.");
        }

        if (!isTournamentEditableRightNow()) {
            throw new IllegalStateException(
                    "The tournament is in progress!  You can't change your mind NOW!");
        }

        if (mSelection.getTieBreaker() == null
                || !(mSelection.getTieBreaker().equals(value))) {
            mSelection.setTieBreaker(value);

            //final Integer valFinal = value;

//            BraketEntryPoint.selectService.storeSelection(mSelection,
//                    new AsyncCallback<Long>() {
//
//                        @Override
//                        public void onFailure(Throwable caught) {
//                            BraketEntryPoint.displayException(caught);
//
//                        }
//
//                        @Override
//                        public void onSuccess(Long result) {
//                            BraketEntryPoint
//                                    .displayToast("Tiebreaker updated to "
//                                            + valFinal.toString() + "!");
//                        }
//
//                        // We don't need to update anything, as the textbox
//                        // already
//                        // holds the data needed.
//
//                    });
        }
    }
}
