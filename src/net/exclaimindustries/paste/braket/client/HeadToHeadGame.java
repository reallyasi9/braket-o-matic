package net.exclaimindustries.paste.braket.client;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import net.exclaimindustries.paste.braket.shared.Fixture;
import net.exclaimindustries.paste.braket.shared.FixtureNotFinalException;
import net.exclaimindustries.paste.braket.shared.ResultProbabilityCalculator;
import net.exclaimindustries.paste.braket.shared.Team;

import com.google.common.base.Optional;
import com.google.gwt.user.client.Random;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Load;
import com.googlecode.objectify.annotation.Subclass;

@Subclass(index = true)
@Cache
public class HeadToHeadGame extends Fixture {

  @Load
  private Ref<Team> topTeam = null;

  @Load
  private Ref<Team> bottomTeam = null;

  private Integer topScore = null;

  private Integer bottomScore = null;

  @Load
  private Ref<Fixture> winnerAdvancement = Ref.create(UndefinedFixture.get());

  private Integer winnerAdvancementIndex = null;

  @Load
  private Ref<Fixture> loserAdvancement = Ref.create(UndefinedFixture.get());

  private Integer loserAdvancementIndex = null;

  @Load
  private Ref<Fixture> topPlayIn = Ref.create(UndefinedFixture.get());

  private Integer topPlayInRank = null;

  @Load
  private Ref<Fixture> bottomPlayIn = Ref.create(UndefinedFixture.get());

  private Integer bottomPlayInRank = null;

  private boolean isFinal = false;

  @Override
  public List<Team> getTeams() {
    // TODO lambdas would make this much nicer
    return Arrays.asList(topTeam.get(), bottomTeam.get());
  }

  @Override
  public Team getTeam(int gameOrderIndex) throws IndexOutOfBoundsException {
    validateGameOrderIndex(gameOrderIndex);
    Ref<Team> selected = (gameOrderIndex == 0) ? topTeam : bottomTeam;
    return selected.get();
  }

  @Override
  public List<Optional<Integer>> getScores() {
    return Arrays.asList(Optional.of(topScore), Optional.of(bottomScore));
  }

  @Override
  public Optional<Integer> getScore(int gameOrderIndex)
      throws IndexOutOfBoundsException {
    validateGameOrderIndex(gameOrderIndex);
    return (gameOrderIndex == 0) ? Optional.of(topScore) : Optional
        .of(bottomScore);
  }

  @Override
  public SortedMap<Integer, Team> getScoreSortedTeams()
      throws FixtureNotFinalException {
    if (!isFinal() || topScore == bottomScore || topScore == null
        || bottomScore == null) {
      throw new FixtureNotFinalException();
    }
    // TODO Lambdas would make this much nicer
    SortedMap<Integer, Team> map = new TreeMap<>();
    map.put(topScore, topTeam.get());
    map.put(bottomScore, bottomTeam.get());
    return map;
  }

  @Override
  public FixtureRankPair getPlayInFixture(int gameOrderIndex)
      throws IndexOutOfBoundsException {
    validateGameOrderIndex(gameOrderIndex);
    if (gameOrderIndex == 0) {
      return new FixtureRankPair(topPlayIn.get(), topPlayInRank);
    } else {
      return new FixtureRankPair(bottomPlayIn.get(), bottomPlayInRank);
    }
  }

  @Override
  public Map<Integer, FixtureRankPair> getPlayInFixtures() {
    Map<Integer, FixtureRankPair> games = new HashMap<>();
    games.put(0, new FixtureRankPair(topPlayIn.get(), topPlayInRank));
    games.put(1, new FixtureRankPair(bottomPlayIn.get(), bottomPlayInRank));
    return games;
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
  public void setPlayInFixtureRankPair(int gameOrderIndex, FixtureRankPair playInGame)
      throws IndexOutOfBoundsException {
    setUnpropagatedPlayInFixtureRankPair(gameOrderIndex, playInGame);
    if (playInGame != null) {
      FixtureIndexPair thisPair = new FixtureIndexPair(this, gameOrderIndex);
      playInGame.game.setUnpropagatedAdvancement(playInGame.rank, thisPair);
    }
  }

  @Override
  public void setUnpropagatedPlayInFixtureRankPair(int gameOrderIndex,
      FixtureRankPair playInGame) throws IndexOutOfBoundsException {
    validateGameOrderIndex(gameOrderIndex);
    if (gameOrderIndex == 0) {
      topPlayIn = Ref.create(playInGame.game);
      topPlayInRank = playInGame.rank;
    } else {
      bottomPlayIn = Ref.create(playInGame.game);
      bottomPlayInRank = playInGame.rank;
    }
  }

  @Override
  public FixtureIndexPair getAdvancement(int rankIndex)
      throws IndexOutOfBoundsException {
    validateGameOrderIndex(rankIndex);
    return (rankIndex == 0) ? new FixtureIndexPair(winnerAdvancement.get(),
        winnerAdvancementIndex) : new FixtureIndexPair(loserAdvancement.get(),
        loserAdvancementIndex);
  }

  @Override
  public Map<Integer, FixtureIndexPair> getAdvancements() {
    Map<Integer, FixtureIndexPair> map = new HashMap<>();
    map.put(0, new FixtureIndexPair(winnerAdvancement.get(),
        winnerAdvancementIndex));
    map.put(1, new FixtureIndexPair(loserAdvancement.get(), loserAdvancementIndex));
    return map;
  }

  @Override
  public int getNumberOfTeams() {
    return 2; // always
  }

  @Override
  public void setAdvancement(int rankIndex, FixtureIndexPair targetGame)
      throws IndexOutOfBoundsException {
    setUnpropagatedAdvancement(rankIndex, targetGame);
    if (targetGame != null) {
      FixtureRankPair thisPair = new FixtureRankPair(this, rankIndex);
      targetGame.game.setUnpropagatedPlayInFixtureRankPair(targetGame.index,
          thisPair);
    }
  }

  @Override
  protected void setUnpropagatedAdvancement(int rankIndex,
      FixtureIndexPair targetGame) throws IndexOutOfBoundsException {
    validateGameOrderIndex(rankIndex);
    if (rankIndex == 0) {
      winnerAdvancement = Ref.create(targetGame.game);
      winnerAdvancementIndex = targetGame.index;
    } else {
      loserAdvancement = Ref.create(targetGame.game);
      loserAdvancementIndex = targetGame.index;
    }
  }

  @Override
  public boolean isReadyToPlay() {
    return topTeam != null && bottomTeam != null && !isFinal();
  }

  @Override
  public boolean isFinal() {
    return isFinal;
  }

  @Override
  protected void setFinal() {
    isFinal = true;
  }

  @Override
  public void setTeam(int index, Team team) throws IndexOutOfBoundsException {
    if (index < 0 || index > 1) {
      throw new IndexOutOfBoundsException("index [" + Integer.toString(index)
          + "] is out of bounds (only 2 teams in this game)");
    }
    if (index == 0) {
      topTeam = Ref.create(team);
    } else {
      bottomTeam = Ref.create(team);
    }
  }

  @Override
  protected double doRandomiztion(ResultProbabilityCalculator calculator) {
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

  @Override
  protected void propagateResult() throws FixtureNotFinalException {
    if (!isFinal()) {
      throw new FixtureNotFinalException();
    }
    Team winningTeam = (topScore > bottomScore) ? topTeam.get() : bottomTeam.get();
    Team losingTeam = (topScore < bottomScore) ? topTeam.get() : bottomTeam.get();
    if (winnerAdvancementIndex != null) {
      winnerAdvancement.get().setTeam(winnerAdvancementIndex, winningTeam);
    }
    if (loserAdvancementIndex != null) {
      loserAdvancement.get().setTeam(loserAdvancementIndex, losingTeam);
    }
  }

}
