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
package net.exclaimindustries.paste.braket.server;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import net.exclaimindustries.paste.braket.client.Tournament;
import net.exclaimindustries.paste.braket.client.TournamentService;
import net.exclaimindustries.paste.braket.shared.Fixture;
import net.exclaimindustries.paste.braket.shared.Team;
import net.exclaimindustries.paste.braket.shared.UserNotAdminException;
import net.exclaimindustries.paste.braket.shared.UserNotLoggedInException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.VoidWork;
import com.googlecode.objectify.Work;

/**
 * @author paste
 * 
 */
public class TournamentServiceImpl extends RemoteServiceServlet implements
    TournamentService {

  // private static Logger LOG = Logger.getLogger(TournamentServiceImpl.class
  // .toString());

  /**
   * Generated
   */
  private static final long serialVersionUID = 1L;

  /*
   * (non-Javadoc)
   * 
   * @see net.exclaimindustries.paste.braket.client.TournamentService#
   * getCurrentTournament()
   */
  @Override
  public TournamentCollection getCurrentTournament() {

    Ref<Tournament> tournament = CurrentTournament.getCurrentTournament();

    if (tournament == null) {
      return new TournamentCollection();
    }

    Tournament t = tournament.get();

    // Get games
    Map<Long, Fixture> games = OfyService.ofy().load()
        .type(Fixture.class).parent(tournament).ids(t.getGames());

    Map<Long, Team> teams = OfyService.ofy().load()
        .type(Team.class).ids(t.getTeams());

    return new TournamentCollection(t, games, teams);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * net.exclaimindustries.paste.braket.client.TournamentService#getTournaments
   * ()
   */
  @Override
  public Collection<Tournament> getTournaments()
      throws UserNotLoggedInException, UserNotAdminException {

    LogInServiceHelper.assertAdmin();

    List<Tournament> tournamentList = OfyService.ofy().load()
        .type(Tournament.class).list();

    return new HashSet<Tournament>(tournamentList);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * net.exclaimindustries.paste.braket.client.TournamentService#storeTournaments
   * (java.util.Collection)
   */
  @Override
  public void storeTournaments(Iterable<Tournament> tournaments)
      throws UserNotLoggedInException, UserNotAdminException {
    // TODO get rid of this.

    LogInServiceHelper.assertAdmin();
    OfyService.ofy().save().entities(tournaments).now();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * net.exclaimindustries.paste.braket.client.TournamentService#storeTournament
   * (net.exclaimindustries.paste.braket.client.BraketTournament)
   */
  @Override
  public Long storeTournament(Tournament tournament)
      throws UserNotLoggedInException, UserNotAdminException {
    LogInServiceHelper.assertAdmin();

    // If this is a new tournament, make all the games to go with it.
    if (tournament.getId() == null) {
      OfyService.ofy().save().entity(tournament).now();
      tournament.setGames(generateGames(tournament));
      OfyService.ofy().save().entity(tournament);
      return tournament.getId();
    } else {
      return OfyService.ofy().save().entity(tournament).now().getId();
    }
  }

  /**
   * Create games for a new tournament and store them in the datastore.
   * 
   * @param tournament
   *          The tournament for which the games should be generated.
   * @return A list of the IDs created, in tournament order.
   */
  private List<Long> generateGames(Tournament tournament) {
    BigInteger validGames = tournament.getGameMask();
    ArrayList<Fixture> gamesToGenerate = new ArrayList<Fixture>();
    for (int i = 0; i < validGames.bitLength(); ++i) {
      if (validGames.testBit(i)) {

        Fixture game = new Fixture();
        game.setIndex(i);
        game.setTournamentKey(Key.create(Tournament.class,
            tournament.getId()));
        gamesToGenerate.add(game);

      }
    }

    // Save them all together. now() will write the Ids.
    OfyService.ofy().save().entities(gamesToGenerate).now();
    ArrayList<Long> gamesInOrder = new ArrayList<Long>();
    for (Fixture game : gamesToGenerate) {
      if (game.getIndex() >= gamesInOrder.size()) {
        while (game.getIndex() > gamesInOrder.size()) {
          gamesInOrder.add(null);
        }
        gamesInOrder.add(game.getId());
      } else {
        gamesInOrder.set(game.getIndex(), game.getId());
      }
    }

    return gamesInOrder;

  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * net.exclaimindustries.paste.braket.client.TournamentService#deleteTournaments
   * (java.util.Collection)
   */
  @Override
  public void deleteTournaments(Iterable<Tournament> tournaments)
      throws UserNotLoggedInException, UserNotAdminException {
    LogInServiceHelper.assertAdmin();
    OfyService.ofy().delete().entities(tournaments);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * net.exclaimindustries.paste.braket.client.TournamentService#deleteTournament
   * (net.exclaimindustries.paste.braket.client.BraketTournament)
   */
  @Override
  public void deleteTournament(Tournament tournament)
      throws UserNotLoggedInException, UserNotAdminException {
    LogInServiceHelper.assertAdmin();
    OfyService.ofy().delete().entity(tournament);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.exclaimindustries.paste.braket.client.TournamentService#
   * setCurrentTournament
   * (net.exclaimindustries.paste.braket.client.BraketTournament)
   */
  @Override
  public Tournament setCurrentTournament(Tournament tournament)
      throws UserNotLoggedInException, UserNotAdminException {
    LogInServiceHelper.assertAdmin();

    Key<CurrentTournament> key = Key.create(CurrentTournament.class, 1);
    CurrentTournament current = OfyService.ofy().load().key(key).now();
    if (current == null) {
      current = new CurrentTournament();
    }
    current.setTournament(tournament);
    OfyService.ofy().save().entity(current).now();

    return tournament;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * net.exclaimindustries.paste.braket.client.TournamentService#addGame(net
   * .exclaimindustries.paste.braket.client.BraketGame,
   * net.exclaimindustries.paste.braket.client.BraketTournament)
   */
  @Override
  public Long addGame(final Fixture game, final Tournament tournament)
      throws UserNotLoggedInException, UserNotAdminException {
    LogInServiceHelper.assertAdmin();

    if (game.getIndex() < 0) {
      throw new IllegalArgumentException(
          "game number must be greater than or equal to zero");
    }

    return OfyService.ofy().transact(new Work<Long>() {

      @Override
      public Long run() {

        game.setTournamentKey(Key.create(Tournament.class,
            tournament.getId()));
        Long id = OfyService.ofy().save().entity(game).now().getId();

        tournament.setGame(game.getIndex(), id);
        OfyService.ofy().save().entity(tournament);

        return id;
      }

    });
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.exclaimindustries.paste.braket.client.TournamentService#addGames(
   * java.lang.Iterable,
   * net.exclaimindustries.paste.braket.client.BraketTournament)
   */
  @Override
  public void addGames(final Iterable<Fixture> games,
      final Tournament tournament) throws UserNotLoggedInException,
      UserNotAdminException {
    LogInServiceHelper.assertAdmin();

    for (Fixture game : games) {
      if (game.getIndex() < 0) {
        throw new IllegalArgumentException(
            "game number must be greater than or equal to zero");
      }
      game.setTournamentKey(Key.create(Tournament.class,
          tournament.getId()));
    }

    OfyService.ofy().transact(new VoidWork() {

      @Override
      public void vrun() {

        OfyService.ofy().save().entities(games).now();

        for (Fixture game : games) {
          tournament.setGame(game.getIndex(), game.getId());
        }
        OfyService.ofy().save().entity(tournament);

      }

    });
  }

  public void uncheckedUpdateAndPropagateGame(
      final Ref<Tournament> currentRef, final Fixture game) {

    final Tournament tournament = currentRef.get();

    // FORCE OVERRIDE
    // FORCE OVERRIDE
    // FORCE OVERRIDE
    // List<Integer> list =
    // Arrays.asList(17, 18, 19, 20, 22, 24, 25, 26, 28, 29, 30, 33,
    // 40, 41, 42, 44, 45, 51, 54, 56, 57);
    // BigInteger forceOverride = BigInteger.ZERO;
    // BigInteger forceMask = BigInteger.ZERO;
    // BigInteger maskyMask = BigInteger.ZERO;
    // for (Integer i : list) {
    // forceOverride = forceOverride.setBit(i);
    // }
    // for (int i = 62; i >= 15; --i) {
    // forceMask = forceMask.setBit(i);
    // }
    // for (int i = 0; i < 63; ++i) {
    // maskyMask = maskyMask.setBit(i);
    // }
    // LOG.warning("OVERRIDE FORCING " + forceOverride.toString(2) + " and "
    // + forceMask.toString(2));
    // tournament.setGameWinners(forceOverride);
    //
    // tournament.setGameMask(maskyMask);
    // tournament.setCompletionMask(forceMask);
    // OfyService.ofy().save().entity(tournament).now();
    // LOG.warning("OVERRIDE NOW     "
    // + tournament.getGameWinners().toString(2) + " and "
    // + tournament.getGameMask().toString(2));
    //
    // // FORCE GAME UPDATE

    // return;

    OfyService.ofy().transact(new VoidWork() {

      @Override
      public void vrun() {
        game.setTournamentKey(currentRef.getKey());

        Fixture thisGame = game;

        ArrayList<Fixture> gamesToSave = new ArrayList<Fixture>();
        gamesToSave.add(thisGame);

        // Update the completion mask
        if (thisGame.isFinal()) {
          tournament.setCompletionMask(tournament.getCompletionMask().setBit(
              thisGame.getIndex()));
          if (thisGame.getWinner()) {
            tournament.setGameWinners(tournament.getGameWinners().setBit(
                thisGame.getIndex()));
          }
        }

        // Assumes the id does not change and update the tournament plus
        // the parent games.
        while (thisGame.isFinal()
            && tournament.hasParentGame(thisGame.getIndex())) {

          int thisGameIndex = thisGame.getIndex();
          Long thisWinnerId = thisGame.getTeamId(thisGame.getWinner() ? 1 : 0);
          int parentGameIndex = tournament.getParentGameIndex(thisGameIndex);
          long parentGameId = tournament.getGame(parentGameIndex);
          Key<Fixture> parentKey = Key.create(currentRef.getKey(),
              Fixture.class, parentGameId);

          Fixture parentGame = OfyService.ofy().load().key(parentKey).now();

          parentGame.setTeamId((thisGameIndex + 1) % 2, thisWinnerId);

          gamesToSave.add(parentGame);

          thisGame = parentGame;
        }

        // Update the tiebreaker, maybe
        if (thisGame.isFinal()
            && !tournament.hasParentGame(thisGame.getIndex())) {

          tournament.setTieBreaker(thisGame.getBottomScore()
              + thisGame.getTopScore());

        }

        OfyService.ofy().save().entity(tournament);
        OfyService.ofy().save().entities(gamesToSave);

      }

    });
  }

  @Override
  public List<Tournament> getTournaments(
      Tournament.IndexName orderCondition, int offset, int limit)
      throws UserNotLoggedInException, UserNotAdminException {
    LogInServiceHelper.assertAdmin();

    List<Tournament> tournamentList = OfyService.ofy().load()
        .type(Tournament.class).order(orderCondition.toString())
        .offset(offset).limit(limit).list();

    return new ArrayList<Tournament>(tournamentList);
  }
}
