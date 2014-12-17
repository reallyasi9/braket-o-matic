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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.view.client.ProvidesKey;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.EntitySubclass;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.OnSave;
import com.googlecode.objectify.annotation.Serialize;

/**
 * A class representing a tournament and its outcome.
 * 
 * @author paste
 * 
 */
@EntitySubclass(index = true)
@Cache
public class BraketTournament extends BraketSelectable {

	/**
	 * A Key Provider so that BraketTournaments can be placed in DataGrids.
	 */
	public static final ProvidesKey<BraketTournament> KEY_PROVIDER = new ProvidesKey<BraketTournament>() {
		@Override
		public Object getKey(BraketTournament item) {
			return (item == null) ? null : item.getId();
		}
	};

	/**
	 * Enumerated index names.
	 */
	public static enum IndexName {
		name, startTime;
	}

	/**
	 * The name of this tournament.
	 */
	private String name = "New Tournament";

	/**
	 * The date and time when the tournament begins.
	 */
	@Index
	private Date startTime = new Date();

	/**
	 * If the nth bit of <code>completionMask</code> is 0, this means the nth
	 * game in the tournament does not have a winner determined.
	 */
	@Serialize
	private BigInteger completionMask = BigInteger.ZERO;

	/**
	 * The sum of the scores of the teams in the final game.
	 */
	private Integer tieBreaker = null;

	/**
	 * The point values for each round of the tournament. THIS MUST BE MUTABLE!
	 */
	private List<Double> roundValues = new ArrayList<Double>(Arrays.asList(13.,
			7., 5., 3., 2., 1.));

	/**
	 * The buyInValue of each buy-in.
	 */
	private Double buyInValue = 5.;

	/**
	 * The pay-out values. The first buyInValue represents the prize for first
	 * place, the second for second place, and so on. If the first buyInValue is
	 * null, this represents a pay-out of all the remaining prize buyInValue.
	 * THIS MUST BE MUTABLE!
	 */
	private List<Double> payOutValues = new ArrayList<Double>(
			Arrays.asList((Double) null));

	/**
	 * The value of an upset win
	 */
	private Double upsetValue = 0.;

	/**
	 * The seeds of the first round.
	 */
	public static final int[] firstRoundSeeds = { 1, 16, 8, 9, 5, 12, 4, 13, 6,
			11, 3, 14, 7, 10, 2, 15, 1, 16, 8, 9, 5, 12, 4, 13, 6, 11, 3, 14,
			7, 10, 2, 15, 1, 16, 8, 9, 5, 12, 4, 13, 6, 11, 3, 14, 7, 10, 2,
			15, 1, 16, 8, 9, 5, 12, 4, 13, 6, 11, 3, 14, 7, 10, 2, 15 };

	/**
	 * The selections that are registered to this tournament.
	 */
	@Serialize
	private HashMap<String, Long> registeredSelections = new HashMap<String, Long>();

	/**
	 * The games in this tournament. These are stored in "tournament order",
	 * meaning the 0th game is the first game in the first round, and the last
	 * game is the tournament game.
	 */
	private ArrayList<Long> games = new ArrayList<Long>();

	/**
	 * The teams in this tournament. These are stored in "tournament order",
	 * meaning the 0th team is the "top" or "away" team in the first game of the
	 * first round, and the last is the "bottom" or "home" team in the last game
	 * of the first round.
	 */
	private ArrayList<Long> teams = new ArrayList<Long>();

	/**
	 * The rules of the tournament.
	 */
	private String rules = "";

	/**
	 * Default constructor
	 */
	public BraketTournament() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getStartTime() {
		return new Date(startTime.getTime());
	}

	public void setStartTime(Date startTime) {
		this.startTime = new Date(startTime.getTime());
	}

	public BigInteger getCompletionMask() {
		return completionMask;
	}

	public void setCompletionMask(BigInteger completionMask) {
		this.completionMask = completionMask;
	}

	public BigInteger getGameMask() {
		return gameMask;
	}

	public void setGameMask(BigInteger gameMask) {
		this.gameMask = gameMask;
	}

	public Double getUpsetValue() {
		return upsetValue;
	}

	public void setUpsetValue(Double upsetValue) {
		this.upsetValue = upsetValue;
	}

	public BigInteger getGameWinners() {
		return gameWinners;
	}

	public void setGameWinners(BigInteger gameWinners) {
		this.gameWinners = gameWinners;
	}

	public Integer getTieBreaker() {
		return tieBreaker;
	}

	public void setTieBreaker(Integer tieBreaker)
			throws IllegalArgumentException {
		if (tieBreaker < 0) {
			throw new IllegalArgumentException("argument must be positive, "
					+ tieBreaker.toString() + " given");
		}
		this.tieBreaker = tieBreaker;
	}

	public List<Double> getRoundValues() {
		return new ArrayList<Double>(roundValues);
	}

	public void setRoundValues(List<Double> roundValues) {
		this.roundValues = new ArrayList<Double>(roundValues);
	}

	public Double getBuyInValue() {
		return buyInValue;
	}

	public void setBuyInValue(Double fishSticks) {
		this.buyInValue = fishSticks;
	}

	public List<Double> getPayOutValues() {
		if (payOutValues.get(0) == null) {

			double remainingBuyIn = buyInValue * registeredSelections.size();
			for (int i = 1; i < payOutValues.size(); ++i) {
				remainingBuyIn -= payOutValues.get(i);
			}
			ArrayList<Double> values = new ArrayList<Double>(payOutValues);
			values.set(0, remainingBuyIn);
			return values;

		} else {
			return new ArrayList<Double>(payOutValues);
		}
	}

	public void setPayOutValues(List<Double> payOutValues) {
		// FishSticks is immutable
		this.payOutValues = new ArrayList<Double>(payOutValues);
	}

	public HashMap<String, Long> getRegisteredSelections() {
		return new HashMap<String, Long>(registeredSelections);
	}

	public Long getRegisteredSelectionId(String userKey) {
		return registeredSelections.get(userKey);
	}

	public ArrayList<Long> getGames() {
		return new ArrayList<Long>(games);
	}

	public Long getGame(int game) {
		return games.get(game);
	}

	public void setGames(List<Long> games) {
		this.games = new ArrayList<Long>(games);
	}

	public void setGame(int index, Long game) {
		// If we're adding to the end, ADD. If we're changing something in the
		// middle, SET. If we fly off the end, CRASH.
		if (games.size() == index)
			games.add(index, game);
		else
			games.set(index, game);
	}

	public void addGame(BraketGame game) {
		if (game.getIndex() < 0) {
			throw new IllegalArgumentException();
		}
		games.set(game.getIndex(), game.getId());
	}

	public ArrayList<Long> getTeams() {
		return new ArrayList<Long>(teams);
	}

	public Long getTeam(int team) {
		return teams.get(team);
	}

	public void setTeams(List<Long> teams) {
		this.teams = new ArrayList<Long>(teams);
	}

	public void setTeam(int index, Long team) {
		if (index >= teams.size()) {
			while (teams.size() < index) {
				teams.add(null);
			}
			teams.add(team);
		} else {
			teams.set(index, team);
		}
	}

	public void addTeam(BraketTeam team) {
		teams.add(team.getIndex(), team.getId());
	}

	public String getRules() {
		return rules;
	}

	public void setRules(String rules) {
		this.rules = rules;
	}

	public Collection<String> getUserIds() {
		return registeredSelections.keySet();
	}

	public void addRegistration(String userKey, Long selectionKey) {

		registeredSelections.put(userKey, selectionKey);
	}

	public void removeRegistration(String userKey) {

		registeredSelections.remove(userKey);
	}

	/**
	 * Get the number of rounds in the tournament.
	 * 
	 * @return The number of rounds in the tournament given the particular
	 *         <code>gameMask</code>.
	 */
	public int getNumberOfRounds() {
		int bitLength = gameMask.bitLength();
		return RoundOffset.getRoundNumber(bitLength - 1) + 1;
	}

	/**
	 * Convenience method for getting the number of games in the tournament.
	 * 
	 * @return The number of games not masked out in the tournament.
	 */
	public int getNumberOfGames() {
		return gameMask.bitCount();
	}

	/**
	 * Get the winner of a particular game.
	 * 
	 * @param gameIndex
	 *            The standard index of the game, counting from the tournament
	 *            championship game outward in heap order.
	 * @return The array index of the winner of the game represented by the
	 *         given game number, for use with the
	 *         {@link net.exclaimindustries.paste.braket.client.BraketGame#getTeamId(int)
	 *         BraketGame.getTeam} method.
	 */
	public int getGameWinner(int gameIndex) throws IllegalArgumentException {
		if (!isValidGame(gameIndex)) {
			throw new IllegalArgumentException();
		}
		if (gameWinners.testBit(gameIndex)) {
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * Get the team that won the given game.
	 * 
	 * @param gameIndex
	 *            The standard index of the game, counting from the championship
	 *            game outward in heap order.
	 * @return The standard index of the team that won the given game.
	 */
	public int getWinningTeamIndex(int gameIndex) {
		return getSelectedTeamIndex(gameIndex);
	}

	/**
	 * Get the team that lost the given game.
	 * 
	 * @param gameIndex
	 *            The standard index of the game, counting from the championship
	 *            game outward in heap order.
	 * @return The standard index of the team that lost the given game.
	 */
	public int getLosingTeamIndex(int gameIndex) {
		return getUnselectedTeamIndex(gameIndex);
	}

	/**
	 * Checks the game mask to determine if a game is valid or not.
	 * 
	 * @param gameIndex
	 *            The game index to check, with 0 as the championship game.
	 * @return true if the game is valie, false otherwise.
	 */
	public boolean isValidGame(int gameIndex) {
		return gameMask.testBit(gameIndex);
	}

	/**
	 * Checks whether or not a game has been completed.
	 * 
	 * @param gameIndex
	 *            The index to check, with 0 as the championship game.
	 * @return true if the game has been completed, false otherwise.
	 */
	public boolean isCompletedGame(int gameIndex) {
		return completionMask.testBit(gameIndex);
	}

	/**
	 * Calculates the point value of a selection based on the current tournament
	 * outcome.
	 * 
	 * @param selection
	 *            The selection to check.
	 * @return The value of the selection given based on the current status of
	 *         the tournament.
	 */
	public double getSelectionValue(BraketSelection selection) {
		// Get the selections...
		BigInteger correctSelection = getCorrectSelections(selection);

		// Calculate the value of each won game
		double value = 0;
		int nRounds = getNumberOfRounds();
		for (int iRound = 0; iRound < nRounds; ++iRound) {
			BigInteger roundMask = RoundOffset.getRoundMask(iRound);
			value += correctSelection.and(roundMask).bitCount()
					* roundValues.get(iRound);
		}

		// Calculate upset bonus
		if (upsetValue != 0) {
			value += getUpsetValue(correctSelection);
		}

		return value;
	}

	/**
	 * Calculates a BigInteger mask of the games selected correctly in the given
	 * selection.
	 * 
	 * @param selection
	 *            The selection in question.
	 * @return A BigInteger mask with 1's in positions where the selection has
	 *         the correct winner, 0's elsewhere.
	 */
	public BigInteger getCorrectSelections(BraketSelection selection) {
		SelectionCalculator sc = new SelectionCalculator();
		return sc.calculateMatchingWinners(selection);
	}

	public int getUpsetCount(BraketSelection selection) {
		return getUpsetCount(getCorrectSelections(selection));
	}

	private int getUpsetCount(BigInteger correctSelection) {
		int nUpsets = 0;
		int nBits = correctSelection.bitLength();
		for (int i = 0; i < nBits; ++i) {
			if (correctSelection.testBit(i)) {
				int loserTeam = getLosingTeamIndex(i);
				int winnerTeam = getWinningTeamIndex(i);

				if (firstRoundSeeds[winnerTeam] > firstRoundSeeds[loserTeam]) {
					++nUpsets;
				}
			}
		}
		return nUpsets;
	}

	public double getUpsetValue(BraketSelection selection) {
		return getUpsetValue(getCorrectSelections(selection));
	}

	private double getUpsetValue(BigInteger correctSelection) {
		return getUpsetCount(correctSelection) * upsetValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.exclaimindustries.paste.braket.client.BraketSelectable#getValidGameMask
	 * ()
	 */
	@Override
	public BigInteger getValidGameMask() {
		return gameMask.and(completionMask);
	}

	/**
	 * @return The number of teams in this tournament, based on the number of
	 *         end games.
	 */
	public int getNumberOfTeams() {
		return getNumberOfTeamsInSubtournament(0);
	}

	private int getNumberOfTeamsInSubtournament(int game) {
		int teams = 0;
		if (hasChildGame(game, false)) {
			teams += getNumberOfTeamsInSubtournament(getChildGameIndex(game,
					false));
		} else {
			++teams;
		}
		if (hasChildGame(game, true)) {
			teams += getNumberOfTeamsInSubtournament(getChildGameIndex(game,
					true));
		} else {
			++teams;
		}
		return teams;
	}

	@OnSave
	private void onSave() {
		games.trimToSize();
		teams.trimToSize();
	}

	/**
	 * Determine whether or not the tournament has started.
	 * 
	 * @return True if the tournament has NOT already started, false otherwise.
	 */
	public boolean isScheduled() {
		return new Date().getTime() < startTime.getTime();
	}

	/**
	 * Determine whether or not the tournament is ongoing.
	 * 
	 * @return True if the tournament has already started but the last game has
	 *         not already finished, false otherwise.
	 */
	public boolean isOngoing() {
		return new Date().getTime() >= startTime.getTime() && !isCompleted();
	}

	/**
	 * Determine whether or not the tournament is over.
	 * 
	 * @return True if all the games in the tournament have ended, false
	 *         otherwise.
	 */
	public boolean isCompleted() {
		return completionMask.equals(gameMask);
	}

	@Override
	protected int getSelectedTeamBase(int gameIndex)
			throws IllegalArgumentException {
		// -1 is our basic out-of-band response. isCompletedGame will throw if
		// the game isn't even valid.
		if (!isCompletedGame(gameIndex)) {
			return -1;
		}

		// Same thing as BraketSelection, only using isCompletedGame instead.
		int curGame = gameIndex;
		try {
			while (true) {
				if (!isCompletedGame(curGame))
					throw new IllegalStateException(
							"This tournament is corrupt!  There's a hole around game "
									+ curGame + "!");

				curGame = getChildGameIndex(curGame,
						gameWinners.testBit(curGame));
			}
		} catch (IllegalArgumentException iae) {
			// Got it! Moving along...
		}

		return curGame;
	}

	/**
	 * Calculate the total possible value of a selection.
	 * 
	 * @param selection
	 * @return
	 */
	public double getPossibleValue(BraketSelection selection) {
		// Assume that all remaining games match the selection's, then calculate
		// the value of that tournament against the selection.
		BigInteger bestPossibleOutcome = completionMask.and(gameWinners).or(
				selection.getSelection().andNot(completionMask));

		// Make a new pseudo-tournament with this as the outcome
		BraketTournament t = new BraketTournament();
		t.gameMask = gameMask;
		t.completionMask = gameMask;
		t.gameWinners = bestPossibleOutcome;
		t.roundValues = roundValues;
		t.upsetValue = upsetValue;
		return t.getSelectionValue(selection);

	}

	/**
	 * @return A mask of the games that have two teams defined and are NOT
	 *         final.
	 */
	public BigInteger getGamesWaitingOrPlaying() {
		BigInteger maybe = gameMask.andNot(completionMask);
		for (int i = 0; i < maybe.bitLength(); ++i) {
			if (maybe.testBit(i)
					&& (getTopTeamIndex(i) < 0 || getBottomTeamIndex(i) < 0)) {
				maybe = maybe.clearBit(i);
			}
		}
		return maybe;
	}
}
