package net.exclaimindustries.paste.braket.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import net.exclaimindustries.paste.braket.client.Game.GameIndexPair;
import net.exclaimindustries.paste.braket.client.Game.GameRankPair;
import net.exclaimindustries.paste.braket.shared.ResultProbabilityCalculator;
import net.exclaimindustries.paste.braket.shared.TeamNotInTournamentException;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Load;
import com.googlecode.objectify.annotation.Subclass;

@Subclass(index = true)
@Cache
public class SingleEliminationTournament extends Tournament {

  @Load
  private Collection<Ref<Game>> seedGames = new ArrayList<>();

  @Load
  private Collection<Ref<Game>> games = new HashSet<>();

  @Load
  private Ref<Game> championshipGame = Ref.create(UndefinedGame.get());

  private Map<Long, Integer> teamSeeds = new HashMap<>();

  @Load
  private Collection<Ref<Team>> teams = new ArrayList<>();

  @Override
  public Collection<Game> getGames() {
    Collection<Game> games = new HashSet<>();
    Map<Integer, GameRankPair> playInGames = championshipGame.get()
        .getPlayInGames();
    for (GameRankPair pair : playInGames.values()) {
      games.add(pair.game);
    }
    return games;
  }

  @Override
  public Collection<Game> getSeedGames() {
    // TODO Lambdas would make this nicer
    Collection<Game> derefGames = new ArrayList<>();
    for (Ref<Game> game : seedGames) {
      derefGames.add(game.get());
    }
    return derefGames;
  }

  @Override
  public Game getChampionshipGame() {
    return championshipGame.get();
  }

  @Override
  public void addGame(Game game) {
    games.add(Ref.create(game));
  }

  @Override
  public Collection<Team> getTeams() {
    // TODO Lambdas would make this much nicer
    Collection<Team> derefTeams = new HashSet<>();
    for (Ref<Team> team : teams) {
      derefTeams.add(team.get());
    }
    return derefTeams;
  }

  @Override
  public void setTeams(Collection<Team> teams) {
    this.teams.clear();
    for (Team team : teams) {
      this.teams.add(Ref.create(team));
    }
  }

  @Override
  public void addTeam(Team team) {
    // because teams is a hash set, this simply overwrites the team if it is
    // already in the set.
    teams.add(Ref.create(team));
  }

  @Override
  public double getValue(Outcome selection) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public double getPossibleValue(Outcome selection) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int getSeed(Team team) throws TeamNotInTournamentException {
    Integer seed = teamSeeds.get(team.id);
    if (seed == null) {
      throw new TeamNotInTournamentException("team with id ["
          + Long.toString(team.getId()) + "] not in tournament");
    }
    return seed;
  }

  @Override
  public Tournament randomizeRemainder(ResultProbabilityCalculator calculator) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Tournament randomizeNextGames(ResultProbabilityCalculator calculator) {
    // Start with those games I can simulate, then work my way forward
    Tournament randomizedTournament = new Tournament(this);
    Queue<Game> nextGames = new LinkedList<>(
        randomizedTournament.getScheduledGames());
    Game game = nextGames.peek();
    while (game != null) {
      game.randomizeResult(calculator);
      game.finalize();
      game.propagateResult();
      // TODO Possibly re-add the game to the tournament, depending on how
      // Ref.get() functions
      Map<Integer, GameIndexPair> advancementGames = game.getAdvancements();
      for (GameIndexPair gameIndexPair : advancementGames.values()) {
        nextGames.add(gameIndexPair.game);
      }
      game = nextGames.peek();
    }
    return null;
  }

  @Override
  public Collection<Game> getScheduledGames() {
    Collection<Game> nextGames = new HashSet<Game>();
    for (Ref<Game> game : games) {
      Game derefGame = game.get();
      if (derefGame.isScheduled()) {
        nextGames.add(derefGame);
      }
    }
    return nextGames;
  }

}
