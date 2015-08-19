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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.validation.constraints.Size;

import net.exclaimindustries.paste.braket.shared.Fixture;
import net.exclaimindustries.paste.braket.shared.OutcomeNotPairedToTournamentException;
import net.exclaimindustries.paste.braket.shared.ResultProbabilityCalculator;
import net.exclaimindustries.paste.braket.shared.Team;
import net.exclaimindustries.paste.braket.shared.TeamNotInTournamentException;

import com.google.gwt.view.client.ProvidesKey;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;

/**
 * A class representing a tournament and its outcome.
 * 
 * @author paste
 * 
 */
@Entity
@Cache
abstract public class Tournament {

  /**
   * A Key Provider so that BraketTournaments can be placed in DataGrids.
   */
  public static final ProvidesKey<Tournament> KEY_PROVIDER = new ProvidesKey<Tournament>() {
    @Override
    public Object getKey(Tournament item) {
      return (item == null) ? null : item.getId();
    }
  };

  /**
   * Enumerated index names.
   */
  public static enum IndexName {
    name, startTime;
  }

  @Id
  protected Long id = null;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  /**
   * The name of this tournament.
   */
  @Size(min = 1, message = "name cannot be empty")
  private String name = "New Tournament";

  /**
   * The date and time when the tournament begins.
   */
  @Index
  private Date startTime = new Date();

  /**
   * The buyInValue of each buy-in.
   */
  private Double buyInValue = 5.;

  /**
   * The pay-out values. The first buyInValue represents the prize for first
   * place, the second for second place, and so on. If the first buyInValue is
   * null, this represents a pay-out of all the remaining prize buyInValue. THIS
   * MUST BE MUTABLE!
   */
  @Size(min = 1, message = "at least one pay-out value must be defined")
  private List<Double> payOutValues = new ArrayList<>(
      Arrays.asList((Double) null));

  /**
   * The selections that are registered to this tournament, keyed to the user ID
   * (which is a String, unlike other IDs in the datastore)
   */
  @Load
  private Map<String, Ref<BraketPrediction>> registeredPredictions = new HashMap<>();

  /**
   * The rules of the tournament.
   */
  private String rules = "";

  /**
   * Default constructor
   */
  public Tournament() {
  }

  /**
   * Copy constructor.
   * 
   * @param other
   *          The tournament to copy.
   */
  public Tournament(Tournament other) {
    this.buyInValue = other.buyInValue;
    this.id = null;
    this.name = other.name;
    this.payOutValues = new ArrayList<>(other.payOutValues);
    this.registeredPredictions = new HashMap<>(other.registeredPredictions);
    this.rules = other.rules;
    this.startTime = other.startTime;
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

  public Double getBuyInValue() {
    return buyInValue;
  }

  public void setBuyInValue(Double fishSticks) {
    this.buyInValue = fishSticks;
  }

  public List<Double> getPayOutValues() {
    if (payOutValues.get(0) == null) {

      double remainingBuyIn = buyInValue * registeredPredictions.size();
      for (int i = 1; i < payOutValues.size(); ++i) {
        remainingBuyIn -= payOutValues.get(i);
      }
      ArrayList<Double> values = new ArrayList<>(payOutValues);
      values.set(0, remainingBuyIn);
      return values;

    } else {
      return new ArrayList<>(payOutValues);
    }
  }

  public List<Double> getRawPayOutValues() {
    return new ArrayList<>(payOutValues);
  }

  public void setPayOutValues(List<Double> payOutValues) {
    // FishSticks is immutable
    this.payOutValues = new ArrayList<>(payOutValues);
  }

  public Map<String, BraketPrediction> getRegisteredPredictions() {
    Map<String, BraketPrediction> predictions = new HashMap<>();
    for (Entry<String, Ref<BraketPrediction>> entry : registeredPredictions
        .entrySet()) {
      Ref<BraketPrediction> value = entry.getValue();
      predictions.put(entry.getKey(), value.get());
    }
    return predictions;
  }

  public BraketPrediction getRegisteredPrediction(String userKey) {
    return registeredPredictions.get(userKey).get();
  }

  abstract public Collection<Fixture> getGames();

  abstract public Collection<Fixture> getSeedGames();

  abstract public Fixture getChampionshipGame();

  abstract public void addGame(Fixture game);

  abstract public Collection<Team> getTeams();

  abstract public void setTeams(Collection<Team> teams);

  abstract public void addTeam(Team team);

  public String getRules() {
    return rules;
  }

  public void setRules(String rules) {
    this.rules = rules;
  }

  public void addPrediction(String userKey, BraketPrediction prediction) {
    registeredPredictions.put(userKey, Ref.create(prediction));
  }

  public void removePrediction(String userKey) {
    registeredPredictions.remove(userKey);
  }

  /**
   * Calculates the point value of a prediction based on the current tournament
   * outcome.
   * 
   * @param prediction
   *          The prediction to check.
   * @return The value of the prediction given based on the current status of
   *         the tournament.
   * @throws OutcomeNotPairedToTournamentException
   *           If the given outcome does not correspond to this tournament.
   */
  abstract public double getValue(Outcome selection)
      throws OutcomeNotPairedToTournamentException;

  /**
   * Calculates the possible point value of a prediction based on the current
   * tournament outcome.
   * 
   * @param prediction
   *          The prediction to check.
   * @return The total possible value of the prediction given based on the
   *         current status of the tournament, including games that have not yet
   *         finished.
   */
  abstract public double getPossibleValue(Outcome selection);

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
    return getChampionshipGame().isFinal();
  }

  /**
   * Get the seed associated with a particular team.
   * 
   * @param team
   *          The team to look up.
   * @return The seed that the team carries in the tournament.
   * @throws TeamNotInTournamentException
   *           if the given team is not seeded in the tournament.
   */
  abstract public int getSeed(Team team) throws TeamNotInTournamentException;

  /**
   * Randomize the remaining games in the tournament.
   * 
   * @param calculator
   *          The probability calculator to use to generate the random outcomes.
   * @return A new tournament with the remaining games randomized.
   */
  abstract public Tournament randomizeRemainder(
      ResultProbabilityCalculator calculator);

  /**
   * Randomize the games that are to be played next (for Excite-o-Matic)
   * 
   * @param calculator
   *          The probability calculator to use to generate the random outcomes.
   * @return A new tournament with the outcome of the next games to be played
   *         randomized.
   */
  abstract public Tournament randomizeNextGames(
      ResultProbabilityCalculator calculator);

  /**
   * Get the next games to be played.
   * 
   * @return Those games that have all of the teams defined, but no outcome yet
   *         defined.
   */
  abstract public Collection<Fixture> getScheduledGames();
}