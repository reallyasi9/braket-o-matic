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

package net.exclaimindustries.paste.braket.shared;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import com.google.common.base.Optional;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;
import com.googlecode.objectify.annotation.Subclass;

import net.exclaimindustries.paste.braket.client.Tournament;
import net.exclaimindustries.paste.braket.client.UndefinedFixture;

/**
 * A class that represents a bracket fixture.
 * 
 * Fixtures hold Slots. Each Slot represents a possible place where a Team can
 * play. Fixtures know to what other Slots their winners (and losers) propagate, as
 * well as to what Fixtures those Slots belong. A Fixture can also query its Slots to
 * know from what Fixtures those Teams come from.
 * 
 * @author paste
 * 
 */
@Subclass(index = true)
@Cache
public abstract class Fixture implements IsSerializable {

  public static class WithTeams {
  }

  public static class FixtureIndexPair {
    public Fixture fixture;
    public int index;

    FixtureIndexPair(Fixture fixture, int index) {
      this.fixture = fixture;
      this.index = index;
    }
  }

  public static class FixtureRankPair {
    public Fixture fixture;
    public int rank;

    FixtureRankPair(Fixture fixture, int rank) {
      this.fixture = fixture;
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
   * An Id representing the tournament to which this fixture belongs.
   */
  @Parent
  private transient Key<Tournament> tournamentKey = null;

  /**
   * When this fixture is scheduled to being.
   */
  private Date scheduledDate = new Date();

  /**
   * The location of this fixture.
   */
  private String location = new String();

  /**
   * The status of this fixture, including things like time remaining.
   */
  private String fixtureStatus = new String();

  /**
   * @return the key of the tournament to which this fixture belongs.
   */
  public Key<Tournament> getTournamentKey() {
    return tournamentKey;
  }

  /**
   * Sets the tournament to which this fixture belongs.
   * 
   * @param tournamentId
   *          The new tournament.
   * @note By changing the parent tournament and saving this entity, a new
   *       entity will be made in the datastore. In order to move this fixture to
   *       another parent, the original fixture must be deleted manually.
   */
  public void setTournamentKey(Key<Tournament> tournamentId) {
    this.tournamentKey = tournamentId;
  }

  /**
   * Get teams in fixture order.
   * 
   * @return A list of teams participating in this fixture, in fixture order. These should never be null.
   */
  abstract public List<Team> getTeams();

  /**
   * Adds a team to the fixture at the given index.
   * 
   * @param index
   *          The fixture-order index where the team will be.
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
   * @return The number of teams playing in this fixture.
   */
  abstract public int getNumberOfTeams();

  /**
   * Get a particular team.
   * 
   * @param fixtureOrderIndex
   *          The fixture-order index of the team desired.
   * @return The desired team, or null if the team for that index is not yet
   *         defined.
   * @throws IndexOutOfBoundsException
   *           If the given fixture-order index is out of bounds for the number of
   *           teams in the fixture.
   */
  abstract public Team getTeam(int fixtureOrderIndex)
      throws IndexOutOfBoundsException;

  /**
   * Get scores in fixture order.
   * 
   * @return A list of scores for each team in the fixture, in fixture order. A null
   *         value corresponds to a team that has not yet been defined for the
   *         fixture. All teams begin with a defined score by default, but what
   *         that score is specifically is implementation-specific.
   */
  abstract public List<Optional<Integer>> getScores();

  /**
   * Get a particular score.
   * 
   * @param fixtureOrderIndex
   *          The fixture-order index of the score desired.
   * @return The desired score, or null if the team for that index is not yet
   *         defined.
   * @throws IndexOutOfBoundsException
   *           If the given fixture-order index is out of bounds for the number of
   *           teams in the fixture.
   */
  abstract public Optional<Integer> getScore(int fixtureOrderIndex)
      throws IndexOutOfBoundsException;

  /**
   * Get teams sorted by final score in the fixture.
   * 
   * @note No team can have a null score, meaning all teams must be defined for
   *       this method to return a sensible value.
   * @return The teams, sorted by their scores, keyed by the scores themselves.
   * @throws FixtureNotFinalException
   *           If the fixture is not yet final.
   */
  abstract public SortedMap<Integer, Team> getScoreSortedTeams()
      throws FixtureNotFinalException;

  /**
   * Set what fixture and slot to where a winner will propagate from this fixture.
   * 
   * @param rankIndex
   *          Which rank is to advance. This number is 0-indexed, with 0 being
   *          the first-place team from this fixture, 1 being the second-place
   *          team, and so on down to the Nth place team, where N is the number
   *          of teams defined in the fixture.
   * @param targetFixture
   *          The fixture and place within the fixture to where the team will advance.
   *          If null, this signifies that the given team ranked by placeIndex
   *          does not advance, and is eliminated from the tournament.
   * @throws IndexOutOfBoundsException
   *           If placeIndex is out-of-bounds for the number of teams in the
   *           fixture, or if the index given in targetFixture is out of bounds for
   *           the fixture given in targetFixture.
   * @note Calls setUnpropagatedPlayInFixtureRankPair in corresponding targetFixture.
   */
  abstract public void setAdvancement(int rankIndex, FixtureIndexPair targetFixture)
      throws IndexOutOfBoundsException;

  /**
   * @see setAdvancement(int, FixtureIndexPair)
   * 
   * @param rankIndex
   *          Which rank is to advance. This number is 0-indexed, with 0 being
   *          the first-place team from this fixture, 1 being the second-place
   *          team, and so on down to the Nth place team, where N is the number
   *          of teams defined in the fixture.
   * @throws IndexOutOfBoundsException
   *           If placeIndex is out-of-bounds for the number of teams in the
   *           fixture.
   * @note Calls setUnpropagatedPlayInFixtureRankPair in corresponding targetFixture.
   */
  public void setNoAdvancement(int rankIndex) throws IndexOutOfBoundsException {
    this.setAdvancement(rankIndex, new FixtureIndexPair(UndefinedFixture.get(), 0));
  }

  /**
   * Get the fixture/index pair to which the given rank advances.
   * 
   * @param rankIndex
   *          The advancing rank.
   * @return The fixture/index pair to which the given rank advances.
   * @throws IndexOutOfBoundsException
   *           If rankIndex is out-of-bounds for the number of teams in this
   *           fixture.
   */
  abstract public FixtureIndexPair getAdvancement(int rankIndex)
      throws IndexOutOfBoundsException;

  /**
   * Get all the fixtures to where winners (and losers) of this fixture advance.
   * 
   * @return The fixture/index pairs to which a given rank advances, in rank order.
   */
  abstract public Map<Integer, FixtureIndexPair> getAdvancements();

  /**
   * Get the fixture and placing within that fixture which advances to the given index
   * of this fixture.
   * 
   * @param fixtureOrderIndex
   *          The index in this fixture being queried.
   * @return The combination fixture and rank that feeds into the queried index of
   *         this fixture. If null, then the selected index of this fixture represents
   *         a seeded team.
   * @throws IndexOutOfBoundsException
   *           If fixtureOrderIndex is out-of-bounds for the number of teams in the
   *           fixture.
   */
  abstract public FixtureRankPair getPlayInFixture(int fixtureOrderIndex)
      throws IndexOutOfBoundsException;

  /**
   * Get all of the play-in fixture/rank pairs that feed into this fixture.
   * 
   * @return The fixture/rank pairs that feed into this fixture, sorted by fixture order
   *         index. Null values imply the team in that index is a seeded team.
   */
  abstract public Map<Integer, FixtureRankPair> getPlayInFixtures();

  /**
   * Set the play-in fixture for a particular fixture-order index.
   * 
   * @param fixtureOrderIndex
   *          The fixture-order index of the team that plays in.
   * @param playInFixture
   *          The play-in fixture and rank. If null, it implies that the given
   *          fixture-order index represents a seeded team.
   * @throws IndexOutOfBoundsException
   *           If fixtureOrderIndex is out-of-bounds for the number of teams in the
   *           fixture, or if the index given in playInFixture is out of bounds for
   *           the fixture given in playInFixture.
   * @note Calls setUnpropagatedAdvancement in corresponding targetFixture.
   */
  abstract public void setPlayInFixtureRankPair(int fixtureOrderIndex,
      FixtureRankPair playInFixture) throws IndexOutOfBoundsException;

  /**
   * @see setPlayInFixtureRankPair(int, FixtureRankPair)
   * 
   * @param fixtureOrderIndex
   *          The fixture-order index of the team that plays in.
   * @throws IndexOutOfBoundsException
   *           If fixtureOrderIndex is out-of-bounds for the number of teams in the
   *           fixture.
   * @note Calls setUnpropagatedAdvancement in corresponding targetFixture.
   */
  public void setNoPlayInFixtureRankPair(int fixtureOrderIndex)
      throws IndexOutOfBoundsException {
    setPlayInFixtureRankPair(fixtureOrderIndex, new FixtureRankPair(UndefinedFixture.get(),
        0));
  }

  public Date getScheduledDate() {
    return new Date(scheduledDate.getTime());
  }

  public void setScheduledDate(Date scheduledDate) {
    this.scheduledDate = new Date(scheduledDate.getTime());
  }

  /**
   * Determine if the fixture is ready to be played.
   * 
   * @return true if all the teams are defined, but the fixture is not over yet.
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

  public String getFixtureStatus() {
    return fixtureStatus;
  }

  public void setFixtureStatus(String fixtureStatus) {
    this.fixtureStatus = fixtureStatus;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  /**
   * Generate a random result for this fixture.
   * 
   * @param calculator
   *          A class that can calculate the probability of a certain outcome.
   * @return The probability of the generated outcome.
   * @post Leaves the fixture in a condition where isFinal() == true, and
   *       propagates the randomized results to the advancement fixtures.
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
   * @post The scores of the teams in the fixture should be such that the
   *       randomized outcome is the result of the fixture, but isFinal() should
   *       not necessarily return true.
   */
  abstract protected double doRandomiztion(
      ResultProbabilityCalculator calculator);

  /**
   * Set what fixture and slot to where a winner will propagate from this fixture.
   * 
   * @param rankIndex
   *          Which rank is to advance. This number is 0-indexed, with 0 being
   *          the first-place team from this fixture, 1 being the second-place
   *          team, and so on down to the Nth place team, where N is the number
   *          of teams defined in the fixture.
   * @param targetFixture
   *          The fixture and place within the fixture to where the team will advance.
   *          If null, this signifies that the given team ranked by placeIndex
   *          does not advance, and is eliminated from the tournament.
   * @note Does not call setPlayInFixtureRankPair in corresponding targetFixture.
   */
  abstract protected void setUnpropagatedAdvancement(int rankIndex,
      FixtureIndexPair targetFixture) throws IndexOutOfBoundsException;

  /**
   * @see setAdvancement(int, FixtureIndexPair)
   * 
   * @param rankIndex
   *          Which rank is to advance. This number is 0-indexed, with 0 being
   *          the first-place team from this fixture, 1 being the second-place
   *          team, and so on down to the Nth place team, where N is the number
   *          of teams defined in the fixture.
   * @note Does not call setPlayInFixtureRankPair in corresponding targetFixture.
   */
  protected void setUnpropagatedNoAdvancement(int rankIndex)
      throws IndexOutOfBoundsException {
    this.setUnpropagatedAdvancement(rankIndex,
        new FixtureIndexPair(UndefinedFixture.get(), 0));
  }

  /**
   * Set the play-in fixture for a particular fixture-order index.
   * 
   * @param fixtureOrderIndex
   *          The fixture-order index of the team that plays in.
   * @param playInFixture
   *          The play-in fixture and rank. If null, it implies that the given
   *          fixture-order index represents a seeded team.
   * @note Does not call setAdvancement in corresponding targetFixture.
   */
  abstract public void setUnpropagatedPlayInFixtureRankPair(int fixtureOrderIndex,
      FixtureRankPair playInFixture) throws IndexOutOfBoundsException;

  /**
   * @see setPlayInFixtureRankPair(int, FixtureRankPair)
   * 
   * @param fixtureOrderIndex
   *          The fixture-order index of the team that plays in.
   * @note Does not call setAdvancement in corresponding targetFixture.
   */
  protected void setUnpropagatedNoPlayInFixtureRankPair(int fixtureOrderIndex)
      throws IndexOutOfBoundsException {
    this.setUnpropagatedPlayInFixtureRankPair(fixtureOrderIndex, new FixtureRankPair(
        UndefinedFixture.get(), 0));
  }

  /**
   * Mark a fixture as finalized.
   */
  public void finalize() {
    fixtureStatus = "Final";
    setFinal();
    try {
      propagateResult();
    } catch (FixtureNotFinalException e) {
      // This will never happen due to the setFinal() call above
    }
  }

  /**
   * Set the fixture as finalized internally
   */
  protected abstract void setFinal();

  /**
   * If the fixture is final, propagate the teams to the advancement fixtures.
   * 
   * @throws FixtureNotFinalException
   *           If the fixture is not marked as final yet.
   */
  protected void propagateResult() throws FixtureNotFinalException {
    Map<Integer, Team> sortedTeams = getScoreSortedTeams();
    Map<Integer, FixtureIndexPair> advancementFixtures = getAdvancements();
    Integer rank = 0;
    for (Team team : sortedTeams.values()) {
      if (advancementFixtures.containsKey(rank)) {
        FixtureIndexPair advancement = advancementFixtures.get(rank);
        advancement.fixture.setTeam(advancement.index, team);
      }
      ++rank;
    }
  }
}
