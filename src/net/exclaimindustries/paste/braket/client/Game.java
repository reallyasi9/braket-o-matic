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
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.exclaimindustries.paste.braket.shared.GameNotFinalException;
import net.exclaimindustries.paste.braket.shared.ResultProbabilityCalculator;

import com.google.common.base.Optional;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Load;
import com.googlecode.objectify.annotation.Parent;

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
@Entity
@Cache
public final class Game implements IsSerializable {

  public static class WithTeams {
  }

  public static final String FINAL = "FINAL";

  public static final int STATUS_FINAL = 3;

  public static final int STATUS_IN_PROGRESS = 2;

  public static final int STATUS_HALFTIME = 22;

  public static final int STATUS_SCHEDULED = 1;

  @Id
  private Long id = null;

  /**
   * An Id representing the tournament to which this game belongs.
   */
  @Parent
  private transient Key<BraketTournament> tournamentKey = null;

  /**
   * The Slots that live inside this Game, fillable with Teams. Slots are not
   * allowed to be null, else exceptions will be thrown.
   */
  @Load
  private List<Ref<Slot>> slots = new ArrayList<>();

  /**
   * The Slots where the winners (or losers) of the game propagate. This is
   * expected to be the same size as Game.slots, where the ith entry here means
   * the ith rank winner advances to that slot. Can be null (meaning the team in
   * that slot is eliminated upon achieving the given rank.
   */
  @Load
  private List<Ref<Slot>> advancementSlots = new ArrayList<>();

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
   * ESPN's id, so I can easily find it when parsing the XML feed.
   */
  private Long espnId = null;

  /**
   * Whether or not the game has ended
   */
  private boolean isFinal = false;

  public Key<BraketTournament> getTournamentKey() {
    return tournamentKey;
  }

  public void setTournamentKey(Key<BraketTournament> tournamentId) {
    this.tournamentKey = tournamentId;
  }

  public List<Optional<BraketTeam>> getTeams() {
    List<Optional<BraketTeam>> teams = new ArrayList<>();
    for (Ref<Slot> slot : slots) {
      teams.add(slot.get().getTeam());
    }
    return teams;
  }

  /**
   * Assumes tiebreakers have already been somehow accounted for in Slot's
   * Compare interface. Runs in O(nSlots*log(nSlots)) time. Slots with no score
   * are considered to have a score equal to zero for sorting purposes.
   * 
   * @return
   */
  public List<Optional<BraketTeam>> getRankedTeams() {
    Set<Slot> sortedSlots = new TreeSet<>();
    for (Ref<Slot> slot : slots) {
      sortedSlots.add(slot.get());
    }
    List<Optional<BraketTeam>> sortedTeams = new ArrayList<>();
    for (Slot slot : sortedSlots) {
      sortedTeams.add(slot.getTeam());
    }
    return sortedTeams;
  }

  public Optional<BraketTeam> getTeam(int index) {
    return slots.get(index).get().getTeam();
  }

  public List<Optional<Integer>> getScores() {
    ArrayList<Optional<Integer>> scores = new ArrayList<>();
    for (Ref<Slot> slot : slots) {
      scores.add(slot.get().getScore());
    }
    return scores;
  }

  public Optional<Integer> getScore(int index) {
    return slots.get(index).get().getScore();
  }

  /**
   * Note: Server-side only!
   * 
   * @param slot
   */
  public void addSlot(Slot slot) {
    slots.add(Ref.create(slot));
  }

  public List<Slot> getSlots() {
    List<Slot> slotList = new ArrayList<>();
    for (Ref<Slot> slot : slots) {
      slotList.add(slot.get());
    }
    return slotList;
  }

  public Slot getSlot(int index) {
    return slots.get(index).get();
  }

  /**
   * Note: Server-side only!
   * 
   * @param slot
   */
  public void addAdvancementSlot(Slot slot) {
    advancementSlots.add(Ref.create(slot));
  }

  @SuppressWarnings("unchecked")
  public List<Optional<Slot>> getAdvancementSlots() {
    List<Optional<Slot>> slotList = new ArrayList<>();
    for (Ref<Slot> slot : advancementSlots) {
      slotList.add((Optional<Slot>) (slot == null ? Optional.absent()
          : Optional.of(slot.get())));
    }
    return slotList;
  }

  public Date getScheduledDate() {
    return new Date(scheduledDate.getTime());
  }

  public void setScheduledDate(Date scheduledDate) {
    this.scheduledDate = new Date(scheduledDate.getTime());
  }

  public boolean isScheduled() {
    return scheduledDate.getTime() > new Date().getTime() && isFinal == false;
  }

  public boolean isInProgress() {
    return scheduledDate.getTime() <= new Date().getTime() && isFinal == false;
  }

  public boolean isFinal() {
    return scheduledDate.getTime() <= new Date().getTime() && isFinal == true;
  }

  public void setFinal(boolean isFinal) {
    this.isFinal = isFinal;
  }

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

  public Long getEspnId() {
    return espnId;
  }

  public void setEspnId(Long espnId) {
    this.espnId = espnId;
  }

  public double generateRandomResult(ResultProbabilityCalculator calculator) {
    List<BraketTeam> teams = new ArrayList<>();
    for (Ref<Slot> slot : slots) {
      Optional<BraketTeam> team = slot.get().getTeam();
      if (!team.isPresent()) {
        throw new NullPointerException("not all teams are present");
      }
      teams.add(team.get());
    }
    Collections.shuffle(teams);
    return calculator.probabilityOf(teams);
  }

  public double getProbability(ResultProbabilityCalculator calculator)
      throws GameNotFinalException {
    if (!isFinal) {
      throw new GameNotFinalException("game is not yet final");
    }
    List<Optional<BraketTeam>> teams = getRankedTeams();
    List<BraketTeam> checkedTeams = new ArrayList<>();
    for (Optional<BraketTeam> team : teams) {
      if (!team.isPresent()) {
        throw new NullPointerException("not all teams are present");
      }
      checkedTeams.add(team.get());
    }
    return calculator.probabilityOf(checkedTeams);
  }

}
