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
package net.exclaimindustries.paste.braket.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * A service for getting and sending tournaments from and to the datastore.
 * 
 * @author paste
 * 
 */
@RemoteServiceRelativePath("tournament")
public interface TournamentService extends RemoteService {

    public final static class TournamentCollection implements IsSerializable {
        private BraketTournament tournament = null;
        private List<BraketGame> games = new ArrayList<BraketGame>();
        private List<BraketTeam> teams = new ArrayList<BraketTeam>();

        public TournamentCollection() {
        }

        public TournamentCollection(BraketTournament tournament,
                List<BraketGame> games, List<BraketTeam> teams) {
            this.tournament = tournament;
            this.games = new ArrayList<BraketGame>(games);
            this.teams = new ArrayList<BraketTeam>(teams);
        }

        public BraketTournament getTournament() {
            return tournament;
        }

        public List<BraketGame> getGames() {
            return games;
        }

        public List<BraketTeam> getTeams() {
            return teams;
        }
    }

    /**
     * Fetches the currently active tournament from the datastore.
     * 
     * @return The currently running tournament, including all the games and
     *         teams. If no such tournament exists, this still returns, but the
     *         getTournament() method of the returned object will return null.
     */
    public TournamentCollection getCurrentTournament();

    /**
     * Sets the current tournament to the one given.
     * 
     * @param tournament
     *            The tournament to set as the current tournament. This
     *            tournament should already exist in the datastore. If it does
     *            not (the Id is null), then the current tournament will no
     *            longer be associated with a valid tournament, and any RPC call
     *            to get the current tournament will return null.
     * 
     * @throws SecurityException
     *             If the user is not logged in as an administrator.
     */
    public void setCurrentTournament(BraketTournament tournament);

    /**
     * Fetches all the tournaments from the datastore.
     * 
     * @return A collection of all the tournaments in the datastore.
     * 
     * @throws SecurityException
     *             If the user is not logged in as an administrator.
     */
    public Collection<BraketTournament> getTournaments();

    /**
     * Saves or updates the given collection of tournaments in the datastore.
     * 
     * @param tournaments
     *            The collection of tournaments to either save or update.
     *            Tournaments that already exist in the datastore will be
     *            updated, and tournaments that do not exist will be created.
     * 
     * @return A map of the stored BraketTournaments, keyed by their (possibly
     *         new) datastore keys.
     * 
     * @throws SecurityException
     *             If the user is not logged in as an administrator.
     */
    public void storeTournaments(Iterable<BraketTournament> tournaments);

    /**
     * Saves or updates a particular tournament in the datastore.
     * 
     * @param tournament
     *            The tournament to save or update. If the Id is not found in
     *            the datastore, a new entity will be written. If the Id is
     *            found, this method will update the tournament.
     * 
     * @return The (possibly new) datastore key for the stored tournament.
     * 
     * @throws SecurityException
     *             If the user is not logged in as an administrator.
     */
    public Long storeTournament(BraketTournament tournament);

    /**
     * Deletes a set of tournaments from the datastore.
     * 
     * @param tournaments
     *            The tournaments to delete. Tournaments not found in the
     *            datastore will be ignored.
     * 
     * @throws SecurityException
     *             If the user is not logged in as an administrator.
     */
    public void deleteTournaments(Iterable<BraketTournament> tournaments);

    /**
     * Deletes a particular tournament from the datastore.
     * 
     * @param tournament
     *            The tournament to delete. Will be ignored if it is not already
     *            in the datastore.
     * 
     * @throws SecurityException
     *             If the user is not logged in as an administrator.
     */
    public void deleteTournament(BraketTournament tournament);

    /**
     * Add a team to the current tournament. Will save the team to the datastore
     * with the current tournament as the parent.
     * 
     * @param team
     *            The team to add to the datastore and the current tournament.
     * @return The Id of the (possibly new) team.
     * 
     * @throws SecurityException
     *             If the user is not logged in as an administrator.
     * @throws NullPointerException
     *             If there is no current tournament.
     * @throws IllegalArgumentException
     *             If the team's index is nonsensical.
     */
    public Long addTeam(BraketTeam team);

    /**
     * Add a team to the given tournament. Will save the team to the datastore
     * with the given tournament as the parent.
     * 
     * @param team
     *            The team to add to the datastore and the given tournament.
     * @param tournament
     *            The tournament to which the team should belong.
     * @return The Id of the (possibly new) team.
     * 
     * @throws SecurityException
     *             If the user is not logged in as an administrator.
     * @throws IllegalArgumentException
     *             If the team's index is nonsensical.
     */
    public Long addTeam(BraketTeam team, BraketTournament tournament);

    /**
     * Add multiple teams to the current tournament. Will save the teams to the
     * datastore with the current tournament as the parent.
     * 
     * @param teams
     *            The teams to add to the datastore and the current tournament.
     * 
     * @throws SecurityException
     *             If the user is not logged in as an administrator.
     * @throws NullPointerException
     *             If there is no current tournament.
     * @throws IllegalArgumentException
     *             If any of the teams' index members are nonsensical.
     */
    public void addTeams(Iterable<BraketTeam> teams);

    /**
     * Add multiple teams to the given tournament. Will save the teams to the
     * datastore with the given tournament as the parent.
     * 
     * @param teams
     *            The teams to add to the datastore and the given tournament.
     * @param tournament
     *            The tournament to which the teams should belong.
     * 
     * @throws SecurityException
     *             If the user is not logged in as an administrator.
     * @throws IllegalArgumentException
     *             If any of the teams' index members are nonsensical.
     */
    public void
            addTeams(Iterable<BraketTeam> teams, BraketTournament tournament);

    /**
     * Add a game to the current tournament. Will save the game to the datastore
     * with the current tournament as the parent.
     * 
     * @param game
     *            The game to add to the datastore and the current tournament.
     * @return The Id of the (possibly new) game.
     * 
     * @throws SecurityException
     *             If the user is not logged in as an administrator.
     * @throws NullPointerException
     *             If there is no current tournament.
     * @throws IllegalArgumentException
     *             If the game's index is nonsensical.
     */
    public Long addGame(BraketGame game);

    /**
     * Add a game to the given tournament. Will save the game to the datastore
     * with the given tournament as the parent.
     * 
     * @param game
     *            The game to add to the datastore and the given tournament.
     * @param tournament
     *            The tournament to which the game should belong.
     * @return The Id of the (possibly new) game.
     * 
     * @throws SecurityException
     *             If the user is not logged in as an administrator.
     * @throws IllegalArgumentException
     *             If the game's index is nonsensical.
     */
    public Long addGame(BraketGame game, BraketTournament tournament);

    /**
     * Add multiple games to the current tournament. Will save the games to the
     * datastore with the current tournament as the parent.
     * 
     * @param games
     *            The games to add to the datastore and the current tournament.
     * 
     * @throws SecurityException
     *             If the user is not logged in as an administrator.
     * @throws NullPointerException
     *             If there is no current tournament.
     * @throws IllegalArgumentException
     *             If any of the games' index members are nonsensical.
     */
    public void addGames(Iterable<BraketGame> games);

    /**
     * Add multiple games to the given tournament. Will save the games to the
     * datastore with the given tournament as the parent.
     * 
     * @param games
     *            The games to add to the datastore and the given tournament.
     * @param tournament
     *            The tournament to which the games should belong.
     * 
     * @throws SecurityException
     *             If the user is not logged in as an administrator.
     * @throws IllegalArgumentException
     *             If any of the games' index members are nonsensical.
     */
    public void
            addGames(Iterable<BraketGame> games, BraketTournament tournament);

    /**
     * Set the rules for the current tournament.
     * 
     * @param rules
     *            The rules to set.
     * 
     * @throws SecurityException
     *             If the user is not logged in as an administrator.
     */
    public void setRules(String rules);

    /**
     * Update a given game and possibly propagate that game's winner forward.
     * 
     * @param game
     *            The game to update.
     * 
     * @throws SecurityException
     *             If the user is not logged in as an administrator.
     */
    public void updateAndPropagateGame(BraketGame game);
}
