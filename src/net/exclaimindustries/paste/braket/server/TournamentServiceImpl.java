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

import net.exclaimindustries.paste.braket.client.BraketGame;
import net.exclaimindustries.paste.braket.client.BraketTeam;
import net.exclaimindustries.paste.braket.client.BraketTournament;
import net.exclaimindustries.paste.braket.client.TournamentService;
import net.exclaimindustries.paste.braket.shared.NoCurrentTournamentException;
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

        Ref<BraketTournament> tournament = CurrentTournament.getCurrentTournament();
        
        if (tournament == null) {
            return new TournamentCollection();
        }

        BraketTournament t = tournament.get();

        // Get games
        List<BraketGame> games =
                new ArrayList<BraketGame>(OfyService.ofy().load()
                        .type(BraketGame.class).parent(tournament).ids(t.getGames())
                        .values());

        List<BraketTeam> teams =
                new ArrayList<BraketTeam>(OfyService.ofy().load()
                        .type(BraketTeam.class).parent(tournament).ids(t.getTeams())
                        .values());

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
    public Collection<BraketTournament> getTournaments()
            throws UserNotLoggedInException, UserNotAdminException {

        LogInServiceHelper.assertAdmin();

        List<BraketTournament> tournamentList =
                OfyService.ofy().load().type(BraketTournament.class).list();

        return new HashSet<BraketTournament>(tournamentList);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * net.exclaimindustries.paste.braket.client.TournamentService#storeTournaments
     * (java.util.Collection)
     */
    @Override
    public void storeTournaments(Iterable<BraketTournament> tournaments)
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
    public Long storeTournament(BraketTournament tournament)
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
     *            The tournament for which the games should be generated.
     * @return A list of the IDs created, in tournament order.
     */
    private List<Long> generateGames(BraketTournament tournament) {
        BigInteger validGames = tournament.getGameMask();
        ArrayList<BraketGame> gamesToGenerate = new ArrayList<BraketGame>();
        for (int i = 0; i < validGames.bitLength(); ++i) {
            if (validGames.testBit(i)) {

                BraketGame game = new BraketGame();
                game.setIndex(i);
                game.setTournamentKey(Key.create(BraketTournament.class,
                        tournament.getId()));
                gamesToGenerate.add(game);

            }
        }

        // Save them all together. now() will write the Ids.
        OfyService.ofy().save().entities(gamesToGenerate).now();
        ArrayList<Long> gamesInOrder = new ArrayList<Long>();
        for (BraketGame game : gamesToGenerate) {
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
    public void deleteTournaments(Iterable<BraketTournament> tournaments)
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
    public void deleteTournament(BraketTournament tournament)
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
    public void setCurrentTournament(BraketTournament tournament)
            throws UserNotLoggedInException, UserNotAdminException {
        LogInServiceHelper.assertAdmin();

        Key<CurrentTournament> key = Key.create(CurrentTournament.class, 1);
        CurrentTournament current = OfyService.ofy().load().key(key).now();
        if (current == null) {
            current = new CurrentTournament();
        }
        current.setTournament(tournament);
        OfyService.ofy().save().entity(current).now();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * net.exclaimindustries.paste.braket.client.TournamentService#addTeam(net
     * .exclaimindustries.paste.braket.client.BraketTeam)
     */
    @Override
    public Long addTeam(final BraketTeam team) throws NoCurrentTournamentException,
            UserNotLoggedInException, UserNotAdminException {

        // FIXME Consolidate with addTeam(BraketTeam, BraketTournament), and
        // probably addTeams(*).
        LogInServiceHelper.assertAdmin();

        if (team.getIndex() < 0) {
            throw new IllegalArgumentException(
                    "team index must be greater than or equal to zero");
        }

        final Ref<BraketTournament> currentRef =
                CurrentTournament.getCurrentTournament();
        if (currentRef == null) {
            throw new NullPointerException("current tournament is not set");
        }

        return OfyService.ofy().transact(new Work<Long>() {

            @Override
            public Long run() {

                team.setTournamentKey(currentRef.getKey());
                Long id = OfyService.ofy().save().entity(team).now().getId();

                BraketTournament tournament = currentRef.get();
                tournament.setTeam(team.getIndex(), id);

                // Update the games of the tournament
                // FIXME This only works for tournaments of 63 games!
                int startingGame = (team.getIndex() / 2) + 31;
                BraketGame game =
                        OfyService.ofy().load().type(BraketGame.class)
                                .parent(tournament)
                                .id(tournament.getGame(startingGame)).now();
                if (game != null) {
                    game.setTeamId(team.getIndex() % 2, team.getId());
                    OfyService.ofy().save().entity(game);
                }

                OfyService.ofy().save().entity(tournament);

                return id;
            }

        });

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * net.exclaimindustries.paste.braket.client.TournamentService#addTeam(net
     * .exclaimindustries.paste.braket.client.BraketTeam,
     * net.exclaimindustries.paste.braket.client.BraketTournament)
     */
    @Override
    public Long addTeam(final BraketTeam team, final BraketTournament tournament)
            throws UserNotLoggedInException, UserNotAdminException {
        LogInServiceHelper.assertAdmin();

        if (team.getIndex() < 0) {
            throw new IllegalArgumentException(
                    "team index must be greater than or equal to zero");
        }

        return OfyService.ofy().transact(new Work<Long>() {

            @Override
            public Long run() {

                team.setTournamentKey(Key.create(BraketTournament.class,
                        tournament.getId()));
                Long id = OfyService.ofy().save().entity(team).now().getId();

                tournament.setTeam(team.getIndex(), id);

                // Update the games of the tournament
                // FIXME This only works for tournaments of 63 games!
                int startingGame = (team.getIndex() / 2) + 31;
                BraketGame game =
                        OfyService.ofy().load().type(BraketGame.class)
                                .parent(tournament)
                                .id(tournament.getGame(startingGame)).now();
                if (game != null) {
                    game.setTeamId(team.getIndex() % 2, team.getId());
                    OfyService.ofy().save().entity(game);
                }

                OfyService.ofy().save().entity(tournament);

                return id;

            }

        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * net.exclaimindustries.paste.braket.client.TournamentService#addTeams(
     * java.lang.Iterable)
     */
    @Override
    public void addTeams(final Iterable<BraketTeam> teams)
            throws NoCurrentTournamentException, UserNotLoggedInException,
            UserNotAdminException {
        LogInServiceHelper.assertAdmin();

        final Ref<BraketTournament> currentRef =
                CurrentTournament.getCurrentTournament();
        if (currentRef == null) {
            throw new NullPointerException("current tournament is not set");
        }

        for (BraketTeam team : teams) {
            if (team.getIndex() < 0) {
                throw new IllegalArgumentException(
                        "team index must be greater than or equal to zero");
            }
            team.setTournamentKey(currentRef.getKey());
        }

        OfyService.ofy().transact(new VoidWork() {

            @Override
            public void vrun() {

                BraketTournament tournament = currentRef.get();

                OfyService.ofy().save().entities(teams).now();

                List<BraketGame> games = new ArrayList<BraketGame>();
                for (BraketTeam team : teams) {
                    tournament.setTeam(team.getIndex(), team.getId());

                    // Update the games of the tournament
                    // FIXME This only works for tournaments of 63 games!
                    int startingGame = (team.getIndex() / 2) + 31;
                    BraketGame game =
                            OfyService.ofy().load().type(BraketGame.class)
                                    .id(tournament.getGame(startingGame)).now();
                    if (game != null) {
                        game.setTeamId(team.getIndex() % 2, team.getId());
                        games.add(game);
                    }
                }
                OfyService.ofy().save().entities(games);
                OfyService.ofy().save().entity(tournament);

            }

        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * net.exclaimindustries.paste.braket.client.TournamentService#addTeams(
     * java.lang.Iterable,
     * net.exclaimindustries.paste.braket.client.BraketTournament)
     */
    @Override
    public void addTeams(final Iterable<BraketTeam> teams,
            final BraketTournament tournament) throws UserNotLoggedInException,
            UserNotAdminException {

        LogInServiceHelper.assertAdmin();

        for (BraketTeam team : teams) {
            if (team.getIndex() < 0) {
                throw new IllegalArgumentException(
                        "team index must be greater than or equal to zero");
            }
            team.setTournamentKey(Key.create(BraketTournament.class,
                    tournament.getId()));
        }

        OfyService.ofy().transact(new VoidWork() {

            @Override
            public void vrun() {

                OfyService.ofy().save().entities(teams).now();

                List<BraketGame> games = new ArrayList<BraketGame>();
                for (BraketTeam team : teams) {
                    tournament.setTeam(team.getIndex(), team.getId());

                    // Update the games of the tournament
                    // FIXME This only works for tournaments of 63 games!
                    int startingGame = (team.getIndex() / 2) + 31;
                    BraketGame game =
                            OfyService.ofy().load().type(BraketGame.class)
                                    .id(tournament.getGame(startingGame)).now();
                    if (game != null) {
                        game.setTeamId(team.getIndex() % 2, team.getId());
                        games.add(game);
                    }
                }
                OfyService.ofy().save().entities(games);
                OfyService.ofy().save().entity(tournament);
            }

        });

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * net.exclaimindustries.paste.braket.client.TournamentService#addGame(net
     * .exclaimindustries.paste.braket.client.BraketGame)
     */
    @Override
    public Long addGame(final BraketGame game) throws NoCurrentTournamentException,
            UserNotLoggedInException, UserNotAdminException {
        LogInServiceHelper.assertAdmin();

        if (game.getIndex() < 0) {
            throw new IllegalArgumentException(
                    "game number must be greater than or equal to zero");
        }

        final Ref<BraketTournament> currentRef =
                CurrentTournament.getCurrentTournament();
        if (currentRef == null) {
            throw new NullPointerException("current tournament is not set");
        }

        return OfyService.ofy().transact(new Work<Long>() {

            @Override
            public Long run() {

                game.setTournamentKey(currentRef.getKey());
                Long id = OfyService.ofy().save().entity(game).now().getId();

                BraketTournament tournament = currentRef.get();
                tournament.setGame(game.getIndex(), id);
                OfyService.ofy().save().entity(tournament);

                return id;
            }

        });
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
    public Long addGame(final BraketGame game, final BraketTournament tournament)
            throws UserNotLoggedInException, UserNotAdminException {
        LogInServiceHelper.assertAdmin();

        if (game.getIndex() < 0) {
            throw new IllegalArgumentException(
                    "game number must be greater than or equal to zero");
        }

        return OfyService.ofy().transact(new Work<Long>() {

            @Override
            public Long run() {

                game.setTournamentKey(Key.create(BraketTournament.class,
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
     * @see
     * net.exclaimindustries.paste.braket.client.TournamentService#addGames(
     * java.lang.Iterable)
     */
    @Override
    public void addGames(final Iterable<BraketGame> games)
            throws UserNotLoggedInException, UserNotAdminException,
            NoCurrentTournamentException {

        LogInServiceHelper.assertAdmin();

        final Ref<BraketTournament> currentRef =
                CurrentTournament.getCurrentTournament();
        if (currentRef == null) {
            throw new NullPointerException("current tournament is not set");
        }

        for (BraketGame game : games) {
            if (game.getIndex() < 0) {
                throw new IllegalArgumentException(
                        "game number must be greater than or equal to zero");
            }
            game.setTournamentKey(currentRef.getKey());
        }

        OfyService.ofy().transact(new VoidWork() {

            @Override
            public void vrun() {

                OfyService.ofy().save().entities(games).now();

                BraketTournament tournament = currentRef.get();

                for (BraketGame game : games) {
                    tournament.setGame(game.getIndex(), game.getId());
                }
                OfyService.ofy().save().entity(tournament);

            }

        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * net.exclaimindustries.paste.braket.client.TournamentService#addGames(
     * java.lang.Iterable,
     * net.exclaimindustries.paste.braket.client.BraketTournament)
     */
    @Override
    public void addGames(final Iterable<BraketGame> games,
            final BraketTournament tournament) throws UserNotLoggedInException,
            UserNotAdminException {
        LogInServiceHelper.assertAdmin();

        for (BraketGame game : games) {
            if (game.getIndex() < 0) {
                throw new IllegalArgumentException(
                        "game number must be greater than or equal to zero");
            }
            game.setTournamentKey(Key.create(BraketTournament.class,
                    tournament.getId()));
        }

        OfyService.ofy().transact(new VoidWork() {

            @Override
            public void vrun() {

                OfyService.ofy().save().entities(games).now();

                for (BraketGame game : games) {
                    tournament.setGame(game.getIndex(), game.getId());
                }
                OfyService.ofy().save().entity(tournament);

            }

        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * net.exclaimindustries.paste.braket.client.TournamentService#setRules(
     * java.lang.String)
     */
    @Override
    public void setRules(String rules) throws UserNotLoggedInException,
            UserNotAdminException, NoCurrentTournamentException {
        LogInServiceHelper.assertAdmin();

        Ref<BraketTournament> currentRef = CurrentTournament.getCurrentTournament();
        if (currentRef == null) {
            throw new NullPointerException("current tournament is not set");
        }

        BraketTournament tournament = currentRef.get();
        tournament.setRules(rules);
        OfyService.ofy().save().entity(tournament);
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.exclaimindustries.paste.braket.client.TournamentService#
     * updateAndPropagateGame
     * (net.exclaimindustries.paste.braket.client.BraketGame)
     */
    @Override
    public void updateAndPropagateGame(final BraketGame game)
            throws NoCurrentTournamentException, UserNotLoggedInException,
            UserNotAdminException {
        LogInServiceHelper.assertAdmin();

        if (game.getIndex() < 0) {
            throw new IllegalArgumentException(
                    "game number must be greater than or equal to zero");
        }

        final Ref<BraketTournament> currentRef =
                CurrentTournament.getCurrentTournament();
        if (currentRef == null) {
            throw new NullPointerException("current tournament is not set");
        }

        uncheckedUpdateAndPropagateGame(currentRef, game);

    }

    public void uncheckedUpdateAndPropagateGame(
            final Ref<BraketTournament> currentRef, final BraketGame game) {

        final BraketTournament tournament = currentRef.get();

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

                BraketGame thisGame = game;

                ArrayList<BraketGame> gamesToSave = new ArrayList<BraketGame>();
                gamesToSave.add(thisGame);

                // Update the completion mask
                if (thisGame.isFinal()) {
                    tournament.setCompletionMask(tournament.getCompletionMask()
                            .setBit(thisGame.getIndex()));
                    if (thisGame.getWinner()) {
                        tournament.setGameWinners(tournament.getGameWinners()
                                .setBit(thisGame.getIndex()));
                    }
                }

                // Assumes the id does not change and update the tournament plus
                // the parent games.
                while (thisGame.isFinal()
                        && tournament.hasParentGame(thisGame.getIndex())) {

                    int thisGameIndex = thisGame.getIndex();
                    Long thisWinnerId =
                            thisGame.getTeamId(thisGame.getWinner() ? 1 : 0);
                    int parentGameIndex =
                            tournament.getParentGameIndex(thisGameIndex);
                    long parentGameId = tournament.getGame(parentGameIndex);
                    Key<BraketGame> parentKey =
                            Key.create(currentRef.getKey(), BraketGame.class,
                                    parentGameId);

                    BraketGame parentGame =
                            OfyService.ofy().load().key(parentKey).now();

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
}
