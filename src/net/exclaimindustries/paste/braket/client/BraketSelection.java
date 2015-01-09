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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.EntitySubclass;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Serialize;

@Entity
@Cache
public final class BraketSelection {

  /**
   * The ID of the selection.
   */
  @Id
  private Long id = null;

  /**
   * The ID of the tournament to which this selection belongs.
   */
  @Index
  private Long tournamentId = null;

  /**
   * The ID of the user to whom this selection belongs.
   */
  @Index
  private String userId = null;

  /**
   * A tiebreaker value.
   */
  private Integer tieBreaker = null;

  /**
   * Whether or not this selection has been approved by an admin.
   */
  @Index
  private boolean isRegistered = false;

  /**
   * The selected winning team IDs for each game in the tournament, keyed by
   * game ID.
   */
  @Serialize
  private Map<Long, Long> gameWinners = new HashMap<Long, Long>();

  /**
   * Default constructor
   */
  public BraketSelection() {
  }

  /**
   * Constructor with template tournament
   * 
   * @param tournament
   *          The tournament to use as a template. The game IDs will be copied
   *          over to this selection.
   */
  public BraketSelection(BraketUser user, BraketTournament tournament) {
    tournamentId = tournament.getId();
    userId = user.getId();
    gameWinners = new HashMap<Long, Long>();
    for (Long gameID : tournament.getGames()) {
      gameWinners.put(gameID, null);
    }
  }

  /**
   * @param selection
   *          The selection against which this object is checked.
   * @return A map with reporting whether the selected team for a given game is
   *         correct (true), incorrect (false), or not yet determined (null),
   *         keyed by game ID.
   */
  public Map<Long, Boolean> calculateMatchingWinners(Map<Long, Game> games) {

    Map<Long, Boolean> result = new HashMap<Long, Boolean>();

    Set<Long> eliminatedTeams = new HashSet<Long>();

    // Determine the eliminated teams first
    for (Game game : games.values()) {
      if (game.isFinal()) {
        eliminatedTeams.add(game.getLosingTeamId());
      }
    }

    // Calculate the winners
    for (Entry<Long, Long> winnerEntry : gameWinners.entrySet()) {
      Long gameId = winnerEntry.getKey();
      if (!games.containsKey(gameId)) {
        throw new IllegalArgumentException("gameId [" + Long.toString(gameId)
            + "] not found in given set of games");
      }
      Long winningTeamId = winnerEntry.getValue();
      Game game = games.get(gameId);
      if (game.isFinal()) {
        result.put(gameId, game.getWinningTeamId() == winningTeamId);
      } else if (eliminatedTeams.contains(winningTeamId)) {
        result.put(gameId, false);
      } else {
        result.put(gameId, null);
      }
    }

    return result;
  }

  /**
   * Sets the given team as the winner of the given game, AND propagates this
   * change up and down the tree as need be. That is, if the child games leading
   * to this decision haven't been picked yet, they'll all be set to this team
   * winning, and if parent games have already been picked to be whatever this
   * game USED to be, they'll all be cleared back to unpicked.
   * 
   * The team index is needed to traverse the tree. It'll also throw if the
   * given outcome is impossible given the user's other choices (i.e. picking a
   * winner that was EXPLICITLY picked as a loser earlier in the tourney).
   * 
   * @param gameId
   *          the ID of the game in question
   * @param pickedTopTeam
   *          whether or not the NEW winning team is the top team (null to clear
   *          the team to unknown)
   * @param games
   *          a map of games, keyed by the game ID
   * @return true if anything changed (and thus needs redraw), false if not
   * 
   * @throws IllegalArgumentException
   *           if the gameIndex or teamIndex are invalid
   * @throws IllegalStateException
   *           if this decision is impossible given the user's other picks
   */
  public boolean setAndPropagateWinner(Long gameId, Boolean pickedTopTeam,
      Map<Long, Game> games) {

    if (!games.containsKey(gameId)) {
      throw new IllegalArgumentException("gameId [" + Long.toString(gameId)
          + "] not present in given set of games");
    }
    if (!gameWinners.containsKey(gameId)) {
      throw new IllegalArgumentException("gameId [" + Long.toString(gameId)
          + "] not present in set of game winners");
    }

    boolean redraw = false;

    // Figure out if a team has been selected yet and what that team is
    Long currentWinner = gameWinners.get(gameId);
    if (currentWinner == pickedTeam) {
      // That was easy.
      return redraw;
    }

    // Determine if this ID is selectable for this game

    Game thisGame = games.get(gameId);

    // TODO: Find a way to query the length of the tourney!
    if (gameIndex > gameMask.bitLength())
      throw new IllegalArgumentException(
          "That's not a valid gameIndex in setAndPropagateWinner!");
    if (teamIndex > gameMask.bitLength() + 1)
      throw new IllegalArgumentException(
          "That's not a valid teamIndex in setAndPropagateWinner!");

    int initialGame = (teamIndex / 2) + (gameMask.bitLength() / 2);

    // Trapdoor 1: If teamIndex is -1 and the game is invalid, we're not
    // changing anything, so forget it.
    if (teamIndex == -1 && !hasSelection(gameIndex))
      return false;

    // We'll work on a copy of the data and replace it when we're done,
    // assuming we don't throw first. Did you know BigInteger doesn't
    // implement Cloneable? It's true! So we'll cheat.
    BigInteger workingWinners = gameWinners.add(BigInteger.ZERO);
    BigInteger workingGames = selectionMask.add(BigInteger.ZERO);

    // Unless we're clearing, track backwards.
    if (teamIndex != -1) {
      boolean anythingChanged = false;

      int curGame = initialGame;
      boolean bottomWinner = (teamIndex % 2 == 1);
      while (curGame >= gameIndex) {
        // Bail out if this game contradicts the choice. If this isn't
        // marked as valid (and isn't the main game in question),
        // though, ignore it.
        if (hasSelection(curGame)
            && workingWinners.testBit(curGame) != bottomWinner
            && curGame != gameIndex)
          throw new IllegalStateException(
              "That choice contradicts the choice made in game " + curGame
                  + "!");

        // Okay, this seems good. If this game isn't set yet (or it's
        // the game in question), update the working copy with the new
        // result.
        if (!hasSelection(curGame)
            || (gameIndex == curGame && workingWinners.testBit(curGame) != bottomWinner)) {
          if (bottomWinner)
            workingWinners = workingWinners.setBit(curGame);
          else
            workingWinners = workingWinners.clearBit(curGame);

          workingGames = workingGames.setBit(curGame);

          // This makes it a change!
          anythingChanged = true;
        }

        // Special case: If this is the championship game (game zero),
        // break now. The math won't work otherwise.
        if (curGame == 0)
          break;

        // Now, if this GAME (not team) modulo 2 is 0 (that is, even),
        // it's a bottom feeder. If it's 1, it's a top.
        bottomWinner = (curGame % 2 == 0);

        // The immediate parent game can be found by easy math, thanks
        // to how it's stored. Yay math! And yay for the fact that
        // integer division automatically chops off the decimal part!
        curGame = (curGame - 1) / 2;
      }

      // Trapdoor 2: If nothing changed in that pass (and we didn't
      // throw), that means the user picked the same team that was already
      // there.
      if (!anythingChanged)
        return false;
    }

    // Okay, now track forward. If any parent game to where we are now
    // decided us as the winner, it needs to be marked as invalid now.
    int curGame = gameIndex;
    boolean bottomWinner = (curGame % 2 == 0);
    while (curGame > 0) {
      // Do the parenting FIRST; we don't need to run this check on the
      // CURRENT game, and this ensures we'll stop at the championship
      // while still performing the check there.
      curGame = (curGame - 1) / 2;

      if (hasSelection(curGame)) {
        if (workingWinners.testBit(curGame) == bottomWinner) {
          workingGames = workingGames.clearBit(curGame);
        } else {
          // If this DOES have a selection, but it ISN'T the same as
          // what we're picking, this makes this selection irrelevant
          // past this point, as the user declared it a loser already.
          break;
        }
      }

      bottomWinner = (curGame % 2 == 0);
    }

    // Finally, if we're clearing, set THIS game as such.
    if (teamIndex == -1) {
      workingGames = workingGames.clearBit(gameIndex);
    }

    // And dump in the new integers.
    selectionMask = workingGames;
    gameWinners = workingWinners;

    return true;
  }

  public void setSelection(BigInteger selection) {
    this.gameWinners = selection;
  }

  public BigInteger getSelectionMask() {
    return selectionMask;
  }

  public void setSelectionMask(BigInteger selectionMask) {
    this.selectionMask = selectionMask;
  }

  public Integer getTieBreaker() {
    return tieBreaker;
  }

  public void setTieBreaker(Integer tieBreaker) {
    this.tieBreaker = tieBreaker;
  }

  public boolean isRegistered() {
    return isRegistered;
  }

  public void setRegistered(boolean isRegistered) {
    this.isRegistered = isRegistered;
  }

  public Long getTournamentId() {
    return tournamentId;
  }

  public void setTournamentId(Long tournament) {
    this.tournamentId = tournament;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String user) {
    this.userId = user;
  }

  /**
   * Check the value of this selection against the given tournamentId truth.
   * 
   * @param tournamentId
   *          The tournamentId truth.
   * @return The point value of this selection.
   */
  public double getSelectionValue(BraketTournament tournament) {
    return tournament.getSelectionValue(this);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.exclaimindustries.paste.braket.client.BraketSelectable#isValidGame
   * (int)
   */
  @Override
  public boolean isValidGame(int gameNumber) {
    return gameNumber >= 0 && gameMask.testBit(gameNumber);
  }

  public boolean hasSelection(int gameNumber) {
    if (!isValidGame(gameNumber)) {
      throw new IllegalArgumentException("That game isn't valid!");
    }

    return selectionMask.testBit(gameNumber);
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
    return gameMask.and(selectionMask);
  }

  @Override
  protected int getSelectedTeamBase(int gameIndex)
      throws IllegalArgumentException {
    // -1 is our basic out-of-band response. hasSelection will throw if
    // the game isn't even valid.
    if (!hasSelection(gameIndex)) {
      return -1;
    }

    // Okay, tell you what: We're going to follow children from this game,
    // stopping once we get to the outer stuff (that is, when
    // getChildGameIndex throws).
    int curGame = gameIndex;
    try {
      while (true) {
        if (!hasSelection(curGame))
          throw new IllegalStateException(
              "This selection is corrupt!  There's a hole around game "
                  + curGame + "!");

        curGame = getChildGameIndex(curGame, gameWinners.testBit(curGame));
      }
    } catch (IllegalArgumentException iae) {
      // Got it! Moving along...
    }

    return curGame;
  }
}
