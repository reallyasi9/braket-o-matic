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
package net.exclaimindustries.paste.braket.client;

import java.math.BigInteger;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Serialize;

/**
 * @author paste
 * 
 *         This interface describes a class that has selected games in a
 *         tournament format. This means that one can query the object for what
 *         team is selected for a given game index. Classes that implement this
 *         interface are designed to work in tandem with an array of teams in
 *         the tournament, returned by the RPC class TeamService.
 */
@Entity
@Cache
public abstract class BraketSelectable implements IsSerializable {

    // private static Logger LOG = Logger.getLogger(BraketSelectable.class
    // .getName());

    static final long[] ceilingBitMasks = { 0xFFFFFFFF00000000l,
            0x00000000FFFF0000l, 0x000000000000FF00l, 0x00000000000000F0l,
            0x000000000000000Cl, 0x0000000000000002l };

    /**
     * The base class must have the Id field.
     */
    @Id
    protected Long id = null;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * A mask with 1's set where a game with a standard number matching that bit
     * offset from the LSB is valid. The LSB is the championship game.
     */
    @Serialize
    protected BigInteger gameMask = BigInteger.valueOf(0x7fffffffffffffffl);

    /**
     * A binary number with 1's set where a game with a standard number matching
     * that bit offset from the LSB has the "bottom" team winning. The LSB is
     * the championship game.
     */
    @Serialize
    protected BigInteger gameWinners = BigInteger.ZERO;

    /**
     * A class to calculate which game winners in this object match the game
     * winners in some other BraketSelectable object.
     * 
     * @author paste
     * 
     */
    protected final class SelectionCalculator {

        private final BigInteger tempMask;

        private final BigInteger tempResult;

        /**
         * A mask with 1's where I know that the game with a number matching the
         * bit offset from the LSB is incorrect.
         */
        private BigInteger wrong = BigInteger.ZERO;

        /**
         * A mask with 1's where I know that the game with a number matching the
         * bit offset from the LSB is correct.
         */
        private BigInteger correct = BigInteger.ZERO;

        protected SelectionCalculator() {
            tempMask = getValidGameMask();
            tempResult = gameWinners;
        }

        protected SelectionCalculator(BigInteger gameMask,
                BigInteger gameWinners) {
            tempMask = gameMask;
            tempResult = gameWinners;
        }

        /**
         * @param selection
         *            The selection against which this object is checked.
         * @return A mask with 1's set where the given selection game winners
         *         match this selection's game winners.
         */
        public BigInteger calculateMatchingWinners(BraketSelectable selection) {

            BigInteger result = BigInteger.ZERO;

            // The only games that can possibly match are those that are valid
            // in both the selection and the tournament.

            // LOG.info("Checking " + selection.gameWinners.toString(2)
            // + " against " + tempResult.toString(2));
            BigInteger maybe = selection.getValidGameMask().and(tempMask);
            // LOG.info("from masks " + selection.getValidGameMask().toString(2)
            // + " and " + tempMask.toString(2) + " generated "
            // + maybe.toString(2));

            int nBits = maybe.bitLength();
            // Loop through all these maybes and check...
            for (int i = 0; i < nBits; ++i) {
                if (maybe.testBit(i) && isSelectionCorrect(selection, i)) {
                    // LOG.info("game " + Integer.toString(i) + ": CORRECT");
                    result = result.setBit(i);
                } else if (maybe.testBit(i)) {
                    // LOG.info("game " + Integer.toString(i) + ": INCORRECT");
                }
            }
            return result;
        }

        public boolean isSelectionCorrect(BraketSelectable selection, int game) {
            // Check the cached wrong selections
            if (wrong.testBit(game)) {
                // LOG.info("Game " + Integer.toString(game)
                // + " already marked as INCORRECT");
                return false;
            }
            // Check the cached correct selections
            if (correct.testBit(game)) {
                // LOG.info("Game " + Integer.toString(game)
                // + " already marked as CORRECT");
                return true;
            }

            // Check if the selections for this game are the same
            if (selection.gameWinners.testBit(game) != tempResult.testBit(game)) {
                wrong = wrong.setBit(game);
                // LOG.info("Game " + Integer.toString(game)
                // + " INCORRECT because selections do not match");
                return false;
            }

            // If I have no children, then this is a correct selection
            boolean bottomTeamSelected = selection.isBottomTeamSelected(game);
            if (!selection.hasChildGame(game, bottomTeamSelected)) {
                // LOG.info("Game " + Integer.toString(game)
                // + " CORRECT because selections match and no children");
                correct = correct.setBit(game);
                return true;
            }

            // If I have children, recurse to the child
            boolean childCorrect = isSelectionCorrect(selection,
                    selection.getChildGameIndex(game, bottomTeamSelected));
            if (childCorrect) {
                correct = correct.setBit(game);
                // LOG.info("Game "
                // + Integer.toString(game)
                // + " CORRECT because selections match and child "
                // + Integer.toString(selection.getChildGameIndex(game,
                // bottomTeamSelected)) + " is CORRECT");
            } else {
                wrong = wrong.setBit(game);
                // LOG.info("Game "
                // + Integer.toString(game)
                // + " INCORRECT because selections match but child "
                // + Integer.toString(selection.getChildGameIndex(game,
                // bottomTeamSelected)) + " is INCORRECT");
            }
            return childCorrect;
        }

    }

    /**
     * Get the index of the team that was selected to win a given game.
     * 
     * @param gameIndex
     *            The standard game index to query.
     * @return An integer index that describes which team was selected to win
     *         the given game.
     * @throws IllegalArgumentException
     *             If the given game is not valid for the selection.
     */
    public int getSelectedTeamIndex(int gameIndex)
            throws IllegalArgumentException {
        int curGame = getSelectedTeamBase(gameIndex);

        if (curGame == -1)
            return curGame;

        // So, with curGame in hand, we can figure out where we were going with
        // the team.
        return ((curGame - 31) * 2) + (gameWinners.testBit(curGame) ? 1 : 0);
    }

    /**
     * Get the index of the team that was not selected to win a given game.
     * 
     * @param gameIndex
     *            The standard game index to query.
     * @return An integer index that describes which team was not selected to
     *         win the given game.
     * @throws IllegalArgumentException
     *             If the given game is not valid for the selection.
     */
    public int getUnselectedTeamIndex(int gameIndex)
            throws IllegalArgumentException {
        int curGame = getSelectedTeamBase(gameIndex);

        if (curGame == -1)
            return curGame;

        // So, with curGame in hand, we can figure out where we were going with
        // the team.
        return ((curGame - 31) * 2) + (gameWinners.testBit(curGame) ? 0 : 1);
    }

    /**
     * The base selection portion. This is just inheritance magic.
     * 
     * @param gameIndex
     *            Thing
     * @return Other thing
     * @throws IllegalArgumentException
     *             Oops
     */
    protected abstract int getSelectedTeamBase(int gameIndex)
            throws IllegalArgumentException;

    /**
     * Get the team index of the top team in a given game.
     * 
     * @param gameIndex
     *            The standard game index.
     * @return The standard team index of the team occupying the bottom position
     *         in the given game.
     */
    public int getTopTeamIndex(int gameIndex) {
        if (hasChildGame(gameIndex, false)) {
            return getSelectedTeamIndex(getChildGameIndex(gameIndex, false));
        } else {
            // FIXME Assumes 63 games
            return (gameIndex - 31) * 2;
        }
    }

    /**
     * Get the team index of the bottom team in a given game.
     * 
     * @param gameIndex
     *            The standard game index.
     * @return The standard team index of the team occupying the bottom position
     *         in the given game.
     */
    public int getBottomTeamIndex(int gameIndex) {
        if (hasChildGame(gameIndex, true)) {
            return getSelectedTeamIndex(getChildGameIndex(gameIndex, true));
        } else {
            // FIXME Assumes 63 games
            return (gameIndex - 31) * 2 + 1;
        }
    }

    /**
     * Query whether or not the given game has a top or bottom child.
     * 
     * @param gameIndex
     *            The standard game index to query.
     * @param bottomTeamSelected
     *            Whether or not to query the bottom child.
     * @return True if the winner of this game is fed by a past game.
     */
    public boolean hasChildGame(int gameIndex, boolean bottomTeamSelected) {
        return isValidGame(calculatePotentialChildIndex(gameIndex,
                bottomTeamSelected));
    }

    /**
     * Get the game index of a child game.
     * 
     * @param gameIndex
     *            The standard game index to query.
     * @param bottomTeamSelected
     *            Whether or not to query the bottom child.
     * @return The game index of the game that feeds the selected winner to this
     *         game.
     */
    public int getChildGameIndex(int gameIndex, boolean bottomTeamSelected) {
        int childGame = calculatePotentialChildIndex(gameIndex,
                bottomTeamSelected);

        if (!isValidGame(childGame)) {
            throw new IllegalArgumentException();
        }

        return childGame;
    }

    protected int calculatePotentialChildIndex(int gameIndex,
            boolean bottomTeamSelected) {
        // Guess what, boys and girls! It's time to play Take Advantage Of The
        // Heap Layout Of The Tournament!
        int childGame = (gameIndex * 2) + 1;
        if (bottomTeamSelected) {
            ++childGame;
        }
        return childGame;
    }

    /**
     * Checks if this is the root game or not.
     * 
     * @param gameIndex
     *            The index to check.
     * @return <code>gameIndex != 0</code>.
     */
    public boolean hasParentGame(int gameIndex) {
        return gameIndex != 0;
    }

    /**
     * Gets the game that is parent to the given game.
     * 
     * @param gameIndex
     *            The child game to check.
     * @return The game index of the game into which this child feeds.
     */
    public int getParentGameIndex(int gameIndex) {
        if (gameIndex == 0) {
            throw new IllegalArgumentException();
        }
        return (gameIndex - 1) / 2;
    }

    /**
     * @param gameIndex
     *            The standard game index to query.
     * @return True if the given game has the bottom team selected, false
     *         otherwise.
     * @throws IllegalArgumentException
     *             If the given game is not valid for the selection.
     */
    public boolean isBottomTeamSelected(int gameIndex)
            throws IllegalArgumentException {
        if (!isValidGame(gameIndex)) {
            throw new IllegalArgumentException();
        }
        return gameWinners.testBit(gameIndex);
    }

    /**
     * @param gameIndex
     *            The standard game index to query.
     * @return True if the given game has the top team selected, false
     *         otherwise.
     * @throws IllegalArgumentException
     *             if the given game is not valid for the selection.
     */
    public boolean isTopTeamSelected(int gameIndex)
            throws IllegalArgumentException {
        return !isBottomTeamSelected(gameIndex);
    }

    /**
     * Calculate the ceiling of the log-base-2 of a given number.
     * 
     * @param i
     *            The number!
     * @return The ceiling of the log-base-2!
     */
    protected static int ceilingLg(long x) {

        int y = (((x & (x - 1)) == 0) ? 0 : 1);
        int j = 32;

        for (int i = 0; i < 6; i++) {
            int k = (((x & ceilingBitMasks[i]) == 0) ? 0 : j);
            y += k;
            x >>= k;
            j >>= 1;
        }

        return y;
    }

    /**
     * Determine whether or not this game is valid. Validity is up to the
     * implementing class to define.
     * 
     * @param gameIndex
     *            The standard game index to query.
     * @return True if the game is valid for this selection, false otherwise.
     */
    public abstract boolean isValidGame(int gameIndex);

    /**
     * @return A mask with 1's where a game with a number matching the bit
     *         offset from the LSB is valid.
     */
    public abstract BigInteger getValidGameMask();

}
