package net.exclaimindustries.paste.braket.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.exclaimindustries.paste.braket.shared.RefDereferencer;
import net.exclaimindustries.paste.braket.shared.TeamNotInTournamentException;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Load;
import com.googlecode.objectify.annotation.Subclass;

@Subclass(index = true)
@Cache
public class SingleEliminationTournament extends Tournament {

  @Load
  private List<Ref<Game>> seedGames = new ArrayList<>();

  @Load
  private Ref<Game> championshipGame = null;

  @Load
  private BiMap<Integer, Ref<Team>> seededTeams = HashBiMap.create();

  public SingleEliminationTournament() {
  }

  @Override
  public Collection<Game> getGames() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Collection<Game> getSeedGames() {
    // TODO Lambdas would make this nicer
    Collection<Game> derefGames = new ArrayList<>();
    for (Ref<Game> game : seedGames) {
      derefGames.add(RefDereferencer.dereference(game));
    }
    return derefGames;
  }

  @Override
  public Game getChampionshipGame() {
    return RefDereferencer.dereference(championshipGame);
  }

  @Override
  public void addGame(Game game) {
    // TODO Auto-generated method stub

  }

  @Override
  public Collection<Team> getTeams() {
    // TODO Lambdas would make this much nicer
    ArrayList<Team> teams = new ArrayList<>();
    for (Ref<Team> team : seededTeams.values()) {
      teams.add(RefDereferencer.dereference(team));
    }
    return teams;
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
  public double getValue(Selectable selection) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public double getPossibleValue(Selectable selection) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int getSeed(Team team) throws TeamNotInTournamentException {
    Integer seed = seededTeams.inverse().get(Ref.create(team));
    if (seed == null) {
      throw new TeamNotInTournamentException();
    }
    return seed;
  }

  @Override
  public void setOutcome(Game game, List<Team> outcome) {
    // TODO Auto-generated method stub

  }

  @Override
  public List<Team> getOutcome(Game game) {
    // TODO Auto-generated method stub
    return null;
  }

}
