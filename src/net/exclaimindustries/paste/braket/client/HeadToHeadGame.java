package net.exclaimindustries.paste.braket.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.google.gwt.user.client.Random;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Load;
import com.googlecode.objectify.annotation.Subclass;

import net.exclaimindustries.paste.braket.shared.GameNotFinalException;
import net.exclaimindustries.paste.braket.shared.RefDereferencer;
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

  @Load
  private Ref<Game> winnerAdvancement = Ref.create(UndefinedGame.get());
  
  private Integer winnerAdvancementIndex = null;

  @Load
  private Ref<Game> loserAdvancement = Ref.create(UndefinedGame.get());

  private Integer loserAdvancementIndex = null;
  
  @Load
  private Ref<Game> topPlayIn = Ref.create(UndefinedGame.get());
  
  private Integer topPlayInRank = null;

  @Load
  private Ref<Game> bottomPlayIn = Ref.create(UndefinedGame.get());

  private Integer bottomPlayInRank = null;
  
  @Override
  public List<Team> getTeams() {
    // TODO lambdas would make this much nicer
    return Arrays.asList(topTeam.get(), bottomTeam.get());
  }

  @Override
  public Team getTeam(int gameOrderIndex)
      throws IndexOutOfBoundsException {
    validateGameOrderIndex(gameOrderIndex);
    Ref<Team> selected = (gameOrderIndex == 0) ? topTeam : bottomTeam;
    return selected.get();
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
    // TODO Lambdas would make this much nicer
    SortedMap<Integer, Team> map = new TreeMap<>();
    map.put(topScore, topTeam.get());
    map.put(bottomScore, bottomTeam.get());
    return map;
  }

  @Override
  public void setAdvancement(int rankIndex, GameRankPair targetGame)
      throws IndexOutOfBoundsException {
    setUnpropagatedAdvancement(rankIndex, targetGame);
    if (targetGame != null) {
      GameRankPair thisPair = new GameRankPair(this, rankIndex);
      targetGame.game.setUnpropagatedPlayInGameRankPair(targetGame.index,
          thisPair);
    }
  }

  @Override
  public GameRankPair getPlayInGame(int gameOrderIndex)
      throws IndexOutOfBoundsException {
    validateGameOrderIndex(gameOrderIndex);
    if (gameOrderIndex == 0) {
      return new GameRankPair(topPlayIn.get(), topPlayInRank);
    } else {
      return new GameRankPair(bottomPlayIn.get(), bottomPlayInRank);
    }
  }

  @Override
  public Map<Integer, GameRankPair> getPlayInGames() {
    Map<Integer, GameRankPair> games = new HashMap<>();
    games.put(0, new GameRankPair(topPlayIn.get(), topPlayInRank));
    games.put(1, new GameRankPair(bottomPlayIn.get(), bottomPlayInRank));
    return games;
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
  public void setPlayInGameRankPair(int gameOrderIndex, GameRankPair playInGame)
      throws IndexOutOfBoundsException {
    setUnpropagatedPlayInGameRankPair(gameOrderIndex, playInGame);
    if (playInGame != null) {
      GameRankPair thisPair = new GameRankPair(this, gameOrderIndex);
      playInGame.game.setUnpropagatedAdvancement(playInGame.index, thisPair);
    }
  }

  @Override
  protected void setUnpropagatedAdvancement(int rankIndex,
      GameRankPair targetGame) throws IndexOutOfBoundsException {
    validateGameOrderIndex(rankIndex);
    if (rankIndex == 0) {
      winnerAdvancement = Ref.create(targetGame.game);
    } else {
      loserAdvancement = targetGame;
    }
  }

  @Override
  public void setUnpropagatedPlayInGameRankPair(int gameOrderIndex,
      GameRankPair playInGame) throws IndexOutOfBoundsException {
    validateGameOrderIndex(gameOrderIndex);
    if (gameOrderIndex == 0) {
      topPlayIn = playInGame;
    } else {
      bottomPlayIn = playInGame;
    }
  }

  @Override
  public GameRankPair getAdvancement(int rankIndex)
      throws IndexOutOfBoundsException {
    validateGameOrderIndex(rankIndex);
    return (rankIndex == 0) ? winnerAdvancement : loserAdvancement;
  }

  @Override
  public List<GameRankPair> getAdvancements() {
    return Arrays.asList(winnerAdvancement, loserAdvancement);
  }

}
