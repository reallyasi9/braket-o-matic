package net.exclaimindustries.paste.braket.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import com.google.gwt.user.client.Random;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Load;
import com.googlecode.objectify.annotation.Subclass;

import net.exclaimindustries.paste.braket.shared.GameNotFinalException;
import net.exclaimindustries.paste.braket.shared.ResultProbabilityCalculator;

@Subclass(index = true)
@Cache
public class HeadToHeadGame extends Game {

  @Load
  private Ref<Team> topTeam = null;

  @Load
  private Ref<Team> bottomTeam = null;

  private Integer topScore = null;

  private Integer bottomScore = null;

  private GameIndexPair winnerAdvancement = null;

  private GameIndexPair loserAdvancement = null;

  private GameIndexPair topPlayIn = null;

  private GameIndexPair bottomPlayIn = null;

  @Override
  public List<Team> getTeams() {
    List<Team> derefTeams = new ArrayList<>();
    derefTeams.add((topTeam == null) ? null : topTeam.get());
    derefTeams.add((bottomTeam == null) ? null : bottomTeam.get());
    return derefTeams;
  }

  @Override
  public Team getTeam(int gameOrderIndex)
      throws IndexOutOfBoundsException {
    validateGameOrderIndex(gameOrderIndex);
    Ref<Team> selected = (gameOrderIndex == 0) ? topTeam : bottomTeam;
    return (selected == null) ? null : selected.get();
  }

  @Override
  public List<Integer> getScores() {
    return Arrays.asList(topScore, bottomScore);
  }

  @Override
  public Integer getScore(int gameOrderIndex) throws IndexOutOfBoundsException {
    validateGameOrderIndex(gameOrderIndex);
    return (gameOrderIndex == 0) ? topScore : bottomScore;
  }

  @Override
  public SortedMap<Integer, Team> getScoreSortedTeams()
      throws GameNotFinalException {
    if (!isFinal() || topScore == bottomScore || topScore == null
        || bottomScore == null) {
      throw new GameNotFinalException();
    }
    SortedMap<Integer, Team> map = new TreeMap<>();
    Team team1 = (topTeam == null) ? null : topTeam.get();
    Team team2 = (bottomTeam == null) ? null : bottomTeam.get();
    map.put(topScore, team1);
    map.put(bottomScore, team2);
    return map;
  }

  @Override
  public void setAdvancement(int rankIndex, GameIndexPair targetGame)
      throws IndexOutOfBoundsException {
    setUnpropagatedAdvancement(rankIndex, targetGame);
    if (targetGame != null) {
      GameIndexPair thisPair = new GameIndexPair(this, rankIndex);
      targetGame.game.setUnpropagatedPlayInGameRankPair(targetGame.index,
          thisPair);
    }
  }

  @Override
  public GameIndexPair getPlayInGameRankPair(int gameOrderIndex)
      throws IndexOutOfBoundsException {
    validateGameOrderIndex(gameOrderIndex);
    return (gameOrderIndex == 0) ? topPlayIn : bottomPlayIn;
  }

  @Override
  public List<GameIndexPair> getPlayInGameRankPairs() {
    return Arrays.asList(topPlayIn, bottomPlayIn);
  }

  @Override
  public double randomizeResult(ResultProbabilityCalculator calculator) {
    List<Team> result = getTeams();
    double weight = calculator.probabilityOf(result);
    if (Random.nextDouble() <= weight) {
      topScore = 1;
      bottomScore = 0;
      return weight;
    } else {
      topScore = 0;
      bottomScore = 1;
      return 1 - weight;
    }
  }

  private static void validateGameOrderIndex(int gameOrderIndex)
      throws IndexOutOfBoundsException {
    if (gameOrderIndex < 0 || gameOrderIndex > 1) {
      throw new IndexOutOfBoundsException(
          "only 2 teams in this game: given index ["
              + Integer.toString(gameOrderIndex) + "]");
    }
  }

  @Override
  public void setPlayInGameRankPair(int gameOrderIndex, GameIndexPair playInGame)
      throws IndexOutOfBoundsException {
    setUnpropagatedPlayInGameRankPair(gameOrderIndex, playInGame);
    if (playInGame != null) {
      GameIndexPair thisPair = new GameIndexPair(this, gameOrderIndex);
      playInGame.game.setUnpropagatedAdvancement(playInGame.index, thisPair);
    }
  }

  @Override
  protected void setUnpropagatedAdvancement(int rankIndex,
      GameIndexPair targetGame) throws IndexOutOfBoundsException {
    validateGameOrderIndex(rankIndex);
    if (rankIndex == 0) {
      winnerAdvancement = targetGame;
    } else {
      loserAdvancement = targetGame;
    }
  }

  @Override
  public void setUnpropagatedPlayInGameRankPair(int gameOrderIndex,
      GameIndexPair playInGame) throws IndexOutOfBoundsException {
    validateGameOrderIndex(gameOrderIndex);
    if (gameOrderIndex == 0) {
      topPlayIn = playInGame;
    } else {
      bottomPlayIn = playInGame;
    }
  }

  @Override
  public GameIndexPair getAdvancement(int rankIndex)
      throws IndexOutOfBoundsException {
    validateGameOrderIndex(rankIndex);
    return (rankIndex == 0) ? winnerAdvancement : loserAdvancement;
  }

  @Override
  public List<GameIndexPair> getAdvancements() {
    return Arrays.asList(winnerAdvancement, loserAdvancement);
  }

}
