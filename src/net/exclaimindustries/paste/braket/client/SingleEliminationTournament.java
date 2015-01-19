package net.exclaimindustries.paste.braket.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.exclaimindustries.paste.braket.client.Game.GameRankPair;
import net.exclaimindustries.paste.braket.shared.GameNotInOutcomeException;
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
    // TODO Auto-generated method stub

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
  public void setTeams(Collection<Long> teams) {
    // TODO Auto-generated method stub

  }

  @Override
  public void addTeam(Team team) {
    // TODO Auto-generated method stub

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
      throw new TeamNotInTournamentException();
    }
    return seed;
  }

  @Override
  public void setOutcome(Game game, List<Team> outcome)
      throws GameNotInOutcomeException {
    // TODO Auto-generated method stub
    Ref<Game> ref = Ref.create(game);
    if (!games.contains(ref)) {
      throw new GameNotInOutcomeException();
    }
  }

  @Override
  public List<Team> getOutcome(Game game) throws GameNotInOutcomeException {
    // TODO Auto-generated method stub
    return null;
  }

}
