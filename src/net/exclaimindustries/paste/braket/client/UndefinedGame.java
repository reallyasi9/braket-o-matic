package net.exclaimindustries.paste.braket.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import net.exclaimindustries.paste.braket.shared.Fixture;
import net.exclaimindustries.paste.braket.shared.GameNotFinalException;
import net.exclaimindustries.paste.braket.shared.ResultProbabilityCalculator;
import net.exclaimindustries.paste.braket.shared.Team;

import com.google.common.base.Optional;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Subclass;

/**
 * A datastore entity that represents a game that has not been defined yet
 * @author paste
 *
 */
@Subclass(index = true)
@Cache
public class UndefinedGame extends Fixture {

  public UndefinedGame() {
    super();
    id = 0l;
  }

  public static Fixture get() {
    return new UndefinedGame();
  }

  @Override
  public List<Team> getTeams() {
    return new ArrayList<>();
  }

  @Override
  public Team getTeam(int gameOrderIndex) throws IndexOutOfBoundsException {
    return UndefinedTeam.get();
  }

  @Override
  public List<Optional<Integer>> getScores() {
    return new ArrayList<>();
  }

  @Override
  public Optional<Integer> getScore(int gameOrderIndex)
      throws IndexOutOfBoundsException {
    return Optional.absent();
  }

  @Override
  public SortedMap<Integer, Team> getScoreSortedTeams()
      throws GameNotFinalException {
    return new TreeMap<>();
  }

  @Override
  public void setAdvancement(int rankIndex, GameIndexPair targetGame)
      throws IndexOutOfBoundsException {
    return;
  }

  @Override
  public GameIndexPair getAdvancement(int rankIndex)
      throws IndexOutOfBoundsException {
    return new GameIndexPair(UndefinedGame.get(), 0);
  }

  @Override
  public Map<Integer, GameIndexPair> getAdvancements() {
    return new HashMap<>();
  }

  @Override
  public GameRankPair getPlayInGame(int gameOrderIndex)
      throws IndexOutOfBoundsException {
    return new GameRankPair(UndefinedGame.get(), 0);
  }

  @Override
  public Map<Integer, GameRankPair> getPlayInGames() {
    return new HashMap<>();
  }

  @Override
  public void setPlayInGameRankPair(int gameOrderIndex, GameRankPair playInGame)
      throws IndexOutOfBoundsException {
    return;
  }

  @Override
  public double randomizeResult(ResultProbabilityCalculator calculator) {
    return 1;
  }

  @Override
  protected void setUnpropagatedAdvancement(int rankIndex,
      GameIndexPair targetGame) throws IndexOutOfBoundsException {
    return;
  }

  @Override
  public void setUnpropagatedPlayInGameRankPair(int gameOrderIndex,
      GameRankPair playInGame) throws IndexOutOfBoundsException {
    return;
  }

  @Override
  public void setScheduledDate(Date scheduledDate) {
    return;
  }

  @Override
  public void setLocation(String location) {
    return;
  }

  @Override
  public void setGameStatus(String gameStatus) {
    return;
  }

  @Override
  public void setId(Long id) {
    return;
  }

  @Override
  public int getNumberOfTeams() {
    return 0;
  }

  @Override
  public boolean isReadyToPlay() {
    return false;
  }

  @Override
  public boolean isFinal() {
    return false;
  }

  @Override
  protected void setFinal() {
  }

  @Override
  public void setTeam(int index, Team team) throws IndexOutOfBoundsException {
  }

  @Override
  protected double doRandomiztion(ResultProbabilityCalculator calculator) {
    return 1;
  }

}
