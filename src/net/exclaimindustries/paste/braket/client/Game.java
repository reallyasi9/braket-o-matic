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

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import net.exclaimindustries.paste.braket.shared.GameNotFinalException;
import net.exclaimindustries.paste.braket.shared.ResultProbabilityCalculator;
import net.exclaimindustries.paste.braket.shared.Team;

import com.google.common.base.Optional;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;
import com.googlecode.objectify.annotation.Subclass;

/**
 * A class that represents a bracket game.
 * 
 * Games hold Slots. Each Slot represents a possible place where a Team can
 * play. Games know to what other Slots their winners (and losers) propagate, as
 * well as to what Games those Slots belong. A Game can also query its Slots to
 * know from what Games those Teams come from.
 * 
 * @author paste
 * 
 */
@Subclass(index = true)
@Cache
public abstract class Game implements IsSerializable {

  public static class WithTeams {
  }

  public static class GameIndexPair {
    public Game game;
    public int index;

    GameIndexPair(Game game, int index) {
      this.game = game;
      this.index = index;
    }
  }

  public static class GameRankPair {
    public Game game;
    public int rank;

    GameRankPair(Game game, int rank) {
      this.game = game;
      this.rank = rank;
    }
  }

  public static final String FINAL = "FINAL";

  public static final int STATUS_FINAL = 3;

  public static final int STATUS_IN_PROGRESS = 2;

  public static final int STATUS_HALFTIME = 22;

  public static final int STATUS_SCHEDULED = 1;

  /**
   * The ID of the entity in the datastore.
   */
  @Id
  protected Long id = null;

  /**
   * An Id representing the tournament to which this game belongs.
   */
  @Parent
  private transient Key<Tournament> tournamentKey = null;

  /**
   * When this game is scheduled to being.
   */
  private Date scheduledDate = new Date();

  /**
   * The location of this game.
   */
  private String location = new String();

  /**
   * The status of this game, including things like time remaining.
   */
  private String gameStatus = new String();

  /**
   * @return the key of the tournament to which this game belongs.
   */
  public Key<Tournament> getTournamentKey() {
    return tournamentKey;
  }

  /**
   * Sets the tournament to which this game belongs.
   * 
   * @param tournamentId
   *          The new tournament.
   * @note By changing the parent tournament and saving this entity, a new
   *       entity will be made in the datastore. In order to move this game to
   *       another parent, the original game must be deleted manually.
   */
  public void setTournamentKey(Key<Tournament> tournamentId) {
    this.tournamentKey = tournamentId;
  }

  /**
   * Get teams in game order.
   * 
   * @return A list of teams participating in this game, in game order. These should never be null.
   */
  abstract public List<Team> getTeams();

  /**
   * Adds a team to the game at the given index.
   * 
   * @param index
   *          The game-order index where the team will be.
   * @param team
   *          The team to add.
   * @throws IndexOutOfBoundsException
   *           If index is negative or if index > getNumberOfTeams().
   */
  abstract public void setTeam(int index, Team team)
      throws IndexOutOfBoundsException;

  /**
   * Get the number of teams
   * 
   * @return The number of teams playing in this game.
   */
  abstract public int getNumberOfTeams();

  /**
   * Get a particular team.
   * 
   * @param gameOrderIndex
   *          The game-order index of the team desired.
   * @return The desired team, or null if the team for that index is not yet
   *         defined.
   * @throws IndexOutOfBoundsException
   *           If the given game-order index is out of bounds for the number of
   *           teams in the game.
   */
  abstract public Team getTeam(int gameOrderIndex)
      throws IndexOutOfBoundsException;

  /**
   * Get scores in game order.
   * 
   * @return A list of scores for each team in the game, in game order. A null
   *         value corresponds to a team that has not yet been defined for the
   *         game. All teams begin with a defined score by default, but what
   *         that score is specifically is implementation-specific.
   */
  abstract public List<Optional<Integer>> getScores();

  /**
   * Get a particular score.
   * 
   * @param gameOrderIndex
   *          The game-order index of the score desired.
   * @return The desired score, or null if the team for that index is not yet
   *         defined.
   * @throws IndexOutOfBoundsException
   *           If the given game-order index is out of bounds for the number of
   *           teams in the game.
   */
  abstract public Optional<Integer> getScore(int gameOrderIndex)
      throws IndexOutOfBoundsException;

  /**
   * Get teams sorted by final score in the game.
   * 
   * @note No team can have a null score, meaning all teams must be defined for
   *       this method to return a sensible value.
   * @return The teams, sorted by their scores, keyed by the scores themselves.
   * @throws GameNotFinalException
   *           If the game is not yet final.
   */
  abstract public SortedMap<Integer, Team> getScoreSortedTeams()
      throws GameNotFinalException;

  /**
   * Set what game and slot to where a winner will propagate from this game.
   * 
   * @param rankIndex
   *          Which rank is to advance. This number is 0-indexed, with 0 being
   *          the first-place team from this game, 1 being the second-place
   *          team, and so on down to the Nth place team, where N is the number
   *          of teams defined in the game.
   * @param targetGame
   *          The game and place within the game to where the team will advance.
   *          If null, this signifies that the given team ranked by placeIndex
   *          does not advance, and is eliminated from the tournament.
   * @throws IndexOutOfBoundsException
   *           If placeIndex is out-of-bounds for the number of teams in the
   *           game, or if the index given in targetGame is out of bounds for
   *           the game given in targetGame.
   * @note Calls setUnpropagatedPlayInGameRankPair in corresponding targetGame.
   */
  abstract public void setAdvancement(int rankIndex, GameIndexPair targetGame)
      throws IndexOutOfBoundsException;

  /**
   * @see setAdvancement(int, GameIndexPair)
   * 
   * @param rankIndex
   *          Which rank is to advance. This number is 0-indexed, with 0 being
   *          the first-place team from this game, 1 being the second-place
   *          team, and so on down to the Nth place team, where N is the number
   *          of teams defined in the game.
   * @throws IndexOutOfBoundsException
   *           If placeIndex is out-of-bounds for the number of teams in the
   *           game.
   * @note Calls setUnpropagatedPlayInGameRankPair in corresponding targetGame.
   */
  public void setNoAdvancement(int rankIndex) throws IndexOutOfBoundsException {
    this.setAdvancement(rankIndex, new GameIndexPair(UndefinedGame.get(), 0));
  }

  /**
   * Get the game/index pair to which the given rank advances.
   * 
   * @param rankIndex
   *          The advancing rank.
   * @return The game/index pair to which the given rank advances.
   * @throws IndexOutOfBoundsException
   *           If rankIndex is out-of-bounds for the number of teams in this
   *           game.
   */
  abstract public GameIndexPair getAdvancement(int rankIndex)
      throws IndexOutOfBoundsException;

  /**
   * Get all the games to where winners (and losers) of this game advance.
   * 
   * @return The game/index pairs to which a given rank advances, in rank order.
   */
  abstract public Map<Integer, GameIndexPair> getAdvancements();

  /**
   * Get the game and placing within that game which advances to the given index
   * of this game.
   * 
   * @param gameOrderIndex
   *          The index in this game being queried.
   * @return The combination game and rank that feeds into the queried index of
   *         this game. If null, then the selected index of this game represents
   *         a seeded team.
   * @throws IndexOutOfBoundsException
   *           If gameOrderIndex is out-of-bounds for the number of teams in the
   *           game.
   */
  abstract public GameRankPair getPlayInGame(int gameOrderIndex)
      throws IndexOutOfBoundsException;

  /**
   * Get all of the play-in game/rank pairs that feed into this game.
   * 
   * @return The game/rank pairs that feed into this game, sorted by game order
   *         index. Null values imply the team in that index is a seeded team.
   */
  abstract public Map<Integer, GameRankPair> getPlayInGames();

  /**
   * Set the play-in game for a particular game-order index.
   * 
   * @param gameOrderIndex
   *          The game-order index of the team that plays in.
   * @param playInGame
   *          The play-in game and rank. If null, it implies that the given
   *          game-order index represents a seeded team.
   * @throws IndexOutOfBoundsException
   *           If gameOrderIndex is out-of-bounds for the number of teams in the
   *           game, or if the index given in playInGame is out of bounds for
   *           the game given in playInGame.
   * @note Calls setUnpropagatedAdvancement in corresponding targetGame.
   */
  abstract public void setPlayInGameRankPair(int gameOrderIndex,
      GameRankPair playInGame) throws IndexOutOfBoundsException;

  /**
   * @see setPlayInGameRankPair(int, GameRankPair)
   * 
   * @param gameOrderIndex
   *          The game-order index of the team that plays in.
   * @throws IndexOutOfBoundsException
   *           If gameOrderIndex is out-of-bounds for the number of teams in the
   *           game.
   * @note Calls setUnpropagatedAdvancement in corresponding targetGame.
   */
  public void setNoPlayInGameRankPair(int gameOrderIndex)
      throws IndexOutOfBoundsException {
    setPlayInGameRankPair(gameOrderIndex, new GameRankPair(UndefinedGame.get(),
        0));
  }

  public Date getScheduledDate() {
    return new Date(scheduledDate.getTime());
  }

  public void setScheduledDate(Date scheduledDate) {
    this.scheduledDate = new Date(scheduledDate.getTime());
  }

  /**
   * Determine if the game is ready to be played.
   * 
   * @return true if all the teams are defined, but the game is not over yet.
   */
  public abstract boolean isReadyToPlay();

  public boolean isScheduled() {
    return scheduledDate.getTime() > new Date().getTime() && !isFinal();
  }

  public boolean isInProgress() {
    return scheduledDate.getTime() <= new Date().getTime() && !isFinal();
  }

  public abstract boolean isFinal();

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getGameStatus() {
    return gameStatus;
  }

  public void setGameStatus(String gameStatus) {
    this.gameStatus = gameStatus;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  /**
   * Generate a random result for this game.
   * 
   * @param calculator
   *          A class that can calculate the probability of a certain outcome.
   * @return The probability of the generated outcome.
   * @post Leaves the game in a condition where isFinal() == true, and
   *       propagates the randomized results to the advancement games.
   */
  public double randomizeResult(ResultProbabilityCalculator calculator) {
    double prob = doRandomiztion(calculator);
    finalize();
    return prob;
  }

  /**
   * Actually perform the randomization of the result.
   * 
   * @param calculator
   * @return The probability of the generated outcome.
   * @post The scores of the teams in the game should be such that the
   *       randomized outcome is the result of the game, but isFinal() should
   *       not necessarily return true.
   */
  abstract protected double doRandomiztion(
      ResultProbabilityCalculator calculator);

  /**
   * Set what game and slot to where a winner will propagate from this game.
   * 
   * @param rankIndex
   *          Which rank is to advance. This number is 0-indexed, with 0 being
   *          the first-place team from this game, 1 being the second-place
   *          team, and so on down to the Nth place team, where N is the number
   *          of teams defined in the game.
   * @param targetGame
   *          The game and place within the game to where the team will advance.
   *          If null, this signifies that the given team ranked by placeIndex
   *          does not advance, and is eliminated from the tournament.
   * @note Does not call setPlayInGameRankPair in corresponding targetGame.
   */
  abstract protected void setUnpropagatedAdvancement(int rankIndex,
      GameIndexPair targetGame) throws IndexOutOfBoundsException;

  /**
   * @see setAdvancement(int, GameIndexPair)
   * 
   * @param rankIndex
   *          Which rank is to advance. This number is 0-indexed, with 0 being
   *          the first-place team from this game, 1 being the second-place
   *          team, and so on down to the Nth place team, where N is the number
   *          of teams defined in the game.
   * @note Does not call setPlayInGameRankPair in corresponding targetGame.
   */
  protected void setUnpropagatedNoAdvancement(int rankIndex)
      throws IndexOutOfBoundsException {
    this.setUnpropagatedAdvancement(rankIndex,
        new GameIndexPair(UndefinedGame.get(), 0));
  }

  /**
   * Set the play-in game for a particular game-order index.
   * 
   * @param gameOrderIndex
   *          The game-order index of the team that plays in.
   * @param playInGame
   *          The play-in game and rank. If null, it implies that the given
   *          game-order index represents a seeded team.
   * @note Does not call setAdvancement in corresponding targetGame.
   */
  abstract public void setUnpropagatedPlayInGameRankPair(int gameOrderIndex,
      GameRankPair playInGame) throws IndexOutOfBoundsException;

  /**
   * @see setPlayInGameRankPair(int, GameRankPair)
   * 
   * @param gameOrderIndex
   *          The game-order index of the team that plays in.
   * @note Does not call setAdvancement in corresponding targetGame.
   */
  protected void setUnpropagatedNoPlayInGameRankPair(int gameOrderIndex)
      throws IndexOutOfBoundsException {
    this.setUnpropagatedPlayInGameRankPair(gameOrderIndex, new GameRankPair(
        UndefinedGame.get(), 0));
  }

  /**
   * Mark a game as finalized.
   */
  public void finalize() {
    gameStatus = "Final";
    setFinal();
    try {
      propagateResult();
    } catch (GameNotFinalException e) {
      // This will never happen due to the setFinal() call above
    }
  }

  /**
   * Set the game as finalized internally
   */
  protected abstract void setFinal();

  /**
   * If the game is final, propagate the teams to the advancement games.
   * 
   * @throws GameNotFinalException
   *           If the game is not marked as final yet.
   */
  protected void propagateResult() throws GameNotFinalException {
    Map<Integer, Team> sortedTeams = getScoreSortedTeams();
    Map<Integer, GameIndexPair> advancementGames = getAdvancements();
    Integer rank = 0;
    for (Team team : sortedTeams.values()) {
      if (advancementGames.containsKey(rank)) {
        GameIndexPair advancement = advancementGames.get(rank);
        advancement.game.setTeam(advancement.index, team);
      }
      ++rank;
    }
  }
}
