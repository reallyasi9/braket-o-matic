package net.exclaimindustries.paste.braket.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.SortedMap;

import net.exclaimindustries.paste.braket.shared.Fixture;
import net.exclaimindustries.paste.braket.shared.GameNotFinalException;
import net.exclaimindustries.paste.braket.shared.GameNotInOutcomeException;
import net.exclaimindustries.paste.braket.shared.OutcomeNotPairedToTournamentException;
import net.exclaimindustries.paste.braket.shared.ResultProbabilityCalculator;
import net.exclaimindustries.paste.braket.shared.Team;
import net.exclaimindustries.paste.braket.shared.TeamNotInTournamentException;
import net.exclaimindustries.paste.braket.shared.Fixture.GameIndexPair;
import net.exclaimindustries.paste.braket.shared.Fixture.GameRankPair;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Load;
import com.googlecode.objectify.annotation.Subclass;

@Subclass(index = true)
@Cache
public class SingleEliminationTournament extends Tournament {

  @Load
  private Collection<Ref<Fixture>> seedGames = new ArrayList<>();

  @Load
  private Collection<Ref<Fixture>> games = new HashSet<>();

  @Load
  private Ref<Fixture> championshipGame = Ref.create(UndefinedGame.get());

  private Map<Long, Integer> teamSeeds = new HashMap<>();

  @Load
  private Collection<Ref<Team>> teams = new ArrayList<>();

  private Map<Long, Double> gameValues = new HashMap<>();

  /**
   * Default constructor
   */
  public SingleEliminationTournament() {
    super();
  }

  @Override
  public Collection<Fixture> getGames() {
    Collection<Fixture> games = new HashSet<>();
    Map<Integer, GameRankPair> playInGames = championshipGame.get()
        .getPlayInGames();
    for (GameRankPair pair : playInGames.values()) {
      games.add(pair.game);
    }
    return games;
  }

  @Override
  public Collection<Fixture> getSeedGames() {
    // TODO Lambdas would make this nicer
    Collection<Fixture> derefGames = new ArrayList<>();
    for (Ref<Fixture> game : seedGames) {
      derefGames.add(game.get());
    }
    return derefGames;
  }

  @Override
  public Fixture getChampionshipGame() {
    return championshipGame.get();
  }

  @Override
  public void addGame(Fixture game) {
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
  public double getValue(Outcome selection)
      throws OutcomeNotPairedToTournamentException {
    if (selection.getParentTournamentId() != id) {
      throw new OutcomeNotPairedToTournamentException(
          "outcome paired to tournament with id ["
              + Long.toString(selection.getParentTournamentId())
              + "], this tournament has id [" + Long.toString(id) + "]");
    }

    double value = 0;

    for (Ref<Fixture> game : games) {
      Fixture derefGame = game.get();
      if (!derefGame.isFinal()) {
        continue;
      }
      List<Long> selectionResult;
      try {
        selectionResult = selection.getResult(derefGame);
      } catch (GameNotInOutcomeException e) {
        throw new OutcomeNotPairedToTournamentException(e);
      }
      SortedMap<Integer, Team> result;
      try {
        result = derefGame.getScoreSortedTeams();
      } catch (GameNotFinalException e) {
        // The game is already checked for isFinal.
        // If this happens, it's the programmer's fault, not the user's.
        throw new RuntimeException(e);
      }
      if (selectionResult.size() != result.size()) {
        throw new OutcomeNotPairedToTournamentException("number of teams ["
            + Integer.toString(selectionResult.size())
            + "] in outcome game with id [" + Long.toString(derefGame.getId())
            + "] not equal to the number of teams ["
            + Integer.toString(result.size()) + "] in the tournament game");
      }
      // TODO fix how I am storing things to make this more sensible...
      // TODO possibly a GameValueCalculator class?
      Iterator<Long> selectionIterator = selectionResult.iterator();
      Iterator<Team> resultIterator = result.values().iterator();
      boolean same = true;
      while (selectionIterator.hasNext()) {
        if (selectionIterator.next() != resultIterator.next().getId()) {
          same = false;
          break;
        }
      }
      if (same) {
        value += gameValues.get(derefGame.getId());
      }
    }

    return value;
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
    // Start with those games I can simulate, then work my way forward
    Tournament randomizedTournament = new SingleEliminationTournament(this);
    Queue<Fixture> nextGames = new LinkedList<>(
        randomizedTournament.getScheduledGames());
    Fixture game = nextGames.peek();
    while (game != null) {
      game.randomizeResult(calculator);
      game.finalize();
      // TODO Possibly re-add the game to the tournament, depending on how
      // Ref.get() functions
      Map<Integer, GameIndexPair> advancementGames = game.getAdvancements();
      for (GameIndexPair gameIndexPair : advancementGames.values()) {
        nextGames.add(gameIndexPair.game);
      }
      game = nextGames.peek();
    }
    return randomizedTournament;
  }

  @Override
  public Tournament randomizeNextGames(ResultProbabilityCalculator calculator) {
    Tournament randomizedTournament = new SingleEliminationTournament(this);
    Queue<Fixture> nextGames = new LinkedList<>(
        randomizedTournament.getScheduledGames());
    Fixture game = nextGames.peek();
    while (game != null) {
      game.randomizeResult(calculator);
      game.finalize();
      // TODO Possibly re-add the game to the tournament, depending on how
      // Ref.get() functions
      game = nextGames.peek();
    }
    return null;
  }

  @Override
  public Collection<Fixture> getScheduledGames() {
    Collection<Fixture> nextGames = new HashSet<Fixture>();
    for (Ref<Fixture> game : games) {
      Fixture derefGame = game.get();
      if (derefGame.isScheduled()) {
        nextGames.add(derefGame);
      }
    }
    return nextGames;
  }

  public SingleEliminationTournament(SingleEliminationTournament other) {
    super(other);
    this.championshipGame = other.championshipGame;
    this.games = new HashSet<>(other.games);
    this.id = null;
    this.seedGames = new ArrayList<>(other.seedGames);
    this.teams = new ArrayList<>(other.teams);
    this.teamSeeds = new HashMap<>(other.teamSeeds);
  }

}
