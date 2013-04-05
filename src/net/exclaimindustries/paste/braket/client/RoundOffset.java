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

public final class RoundOffset {

    private final int round;

    private final int offset;

    private final int gameNumber;

    /**
     * Constructor with arguments.
     * 
     * @param round
     *            The round of the game. Round 0 corresponds to the championship
     *            game.
     * @param offset
     *            The offset of the game from the first game in the round.
     */
    public RoundOffset(int round, int offset) {
        if (round < 0) {
            throw new IllegalArgumentException(
                    "round number argument needs to be positive, "
                            + Integer.toString(round) + " given");
        }
        this.round = round;
        int numberOfGames = getNumberOfGames(round);
        if (offset < 0 || offset >= numberOfGames) {
            throw new IllegalArgumentException(
                    "game offset argument for round " + Integer.toString(round)
                            + " needs to be between 0 and "
                            + Integer.toString(numberOfGames) + ", "
                            + Integer.toString(offset) + " given");
        }
        this.offset = offset;

        this.gameNumber = getGameNumber();
    }

    /**
     * Create a RoundOffset object from a given game number.
     * 
     * @param gameNumber
     *            The number of the game, starting from 0, which is the
     *            championship game.
     * @return A new RoundOffset object matching the given game number.
     */
    public RoundOffset(int gameNumber) {
        if (gameNumber < 0) {
            throw new IllegalArgumentException(
                    "game number argument needs to be positive, "
                            + Integer.toString(gameNumber) + " given");
        }
        round = RoundOffset.getRoundNumber(gameNumber);
        offset = gameNumber - RoundOffset.getCumulativeNumberOfGames(round);
        this.gameNumber = gameNumber;
    }

    /**
     * Calculates the total number of games in the rounds before the given
     * round.
     * 
     * @param round
     *            The round to check.
     * @return <code>getNumberOfGames(round + 1) - 1</code>
     */
    private static int getCumulativeNumberOfGames(int round) {
        return getNumberOfGames(round + 1) - 1;
    }

    public int getRound() {
        return round;
    }

    public int getOffset() {
        return offset;
    }

    /**
     * Convert the round and offset into a game number, counting up from 0.
     * 
     * @return A game number corresponding to this RoundOffset.
     */
    public int getGameNumber() {
        return gameNumber;
    }

    /**
     * Get the first child RoundOffset of this RoundOffset.
     * 
     * @return A RoundOffset representing the first child of this RoundOffset
     *         (the game that feeds into the top slot of this game).
     */
    public RoundOffset getChild() {
        return new RoundOffset(gameNumber * 2 + 1);
    }

    /**
     * Get this game's parent.
     * 
     * @return The parent to this game (the game into which this game feeds), or
     *         null if this is the championship game.
     */
    public RoundOffset getParent() {
        if (round == 0) {
            return null;
        }
        return new RoundOffset((gameNumber - 1) / 2);
    }

    /**
     * Generates a mask of bits that represent the games in a given round.
     * 
     * @param round
     *            The round in question.
     * @return A BigInteger represnting a mask of the games in the given round.
     */
    public static BigInteger getRoundMask(int round) {
        if (round < 0) {
            throw new IllegalArgumentException();
        }
        if (round == 0) {
            return BigInteger.ONE;
        }
        int fromBit = (1 << round) - 1;
        int toBit = (1 << (round + 1)) - 1;
        BigInteger bi = BigInteger.ZERO;
        for (int i = fromBit; i < toBit; ++i) {
            bi = bi.setBit(i);
        }
        return bi;
    }

    /**
     * Calculate the round a certain game number is in.
     * 
     * @param gameNumber
     *            The game in question.
     * @return The round in which the given game resides.
     */
    public static int getRoundNumber(int gameNumber) {
        if (gameNumber < 0) {
            throw new IllegalArgumentException();
        }
        int roundNumber = 1;
        while ((1 << roundNumber) - 1 < gameNumber) {
            ++roundNumber;
        }
        return roundNumber - 1;
    }

    /**
     * Calculate the number of games in a given round.
     * 
     * @param round
     *            The round in question.
     * @return The number of games in the given round.
     */
    public static int getNumberOfGames(int round) {
        if (round < 0) {
            throw new IllegalArgumentException();
        }
        return 1 << round;
    }
}