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

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.exclaimindustries.paste.braket.shared.UserNotAdminException;
import net.exclaimindustries.paste.braket.shared.UserNotLoggedInException;

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
    private Tournament tournament = null;
    private Map<Long, Game> games = new HashMap<Long, Game>();
    private Map<Long, BraketTeam> teams = new HashMap<Long, BraketTeam>();

    public TournamentCollection() {
    }

    public TournamentCollection(Tournament tournament,
        Map<Long, Game> games, Map<Long, BraketTeam> teams) {
      this.tournament = tournament;
      this.games = new HashMap<Long, Game>(games);
      this.teams = new HashMap<Long, BraketTeam>(teams);
    }

    public Tournament getTournament() {
      return tournament;
    }

    public Map<Long, Game> getGames() {
      return games;
    }

    public Map<Long, BraketTeam> getTeams() {
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
   *          The tournament to set as the current tournament. This tournament
   *          should already exist in the datastore. If it does not (the Id is
   *          null), then the current tournament will no longer be associated
   *          with a valid tournament, and any RPC call to get the current
   *          tournament will return null.
   * 
   * @return The tournament that was passed (for proper atomic handling of async
   *         events)
   * 
   * @throws UserNotAdminException
   * @throws UserNotLoggedInException
   * 
   * @throws SecurityException
   *           If the user is not logged in as an administrator.
   */
  public Tournament setCurrentTournament(Tournament tournament)
      throws UserNotLoggedInException, UserNotAdminException;

  /**
   * Fetches all the tournaments from the datastore.
   * 
   * @return A collection of all the tournaments in the datastore.
   * @throws UserNotAdminException
   * @throws UserNotLoggedInException
   * 
   * @throws SecurityException
   *           If the user is not logged in as an administrator.
   */
  public Collection<Tournament> getTournaments()
      throws UserNotLoggedInException, UserNotAdminException;

  /**
   * Fetches some number of tournaments by order from the datastore.
   * 
   * @param orderCondition
   *          The (named) index to use when ordering, prepended optionally by a
   *          minus sign to signify reverse ordering.
   * @param offset
   *          Return results starting at the nth offset.
   * @param limit
   *          Only return this many results.
   * @return <code>limit</code> (or fewer) Tournaments sorted by the
   *         <code>sortCondition</code> condition, starting at offset
   *         <code>offset</code>.
   */
  public List<Tournament> getTournaments(
      Tournament.IndexName orderCondition, int offset, int limit)
      throws UserNotLoggedInException, UserNotAdminException;

  /**
   * Saves or updates the given collection of tournaments in the datastore.
   * 
   * @param tournaments
   *          The collection of tournaments to either save or update.
   *          Tournaments that already exist in the datastore will be updated,
   *          and tournaments that do not exist will be created.
   * 
   * @return A map of the stored BraketTournaments, keyed by their (possibly
   *         new) datastore keys.
   * @throws UserNotAdminException
   * @throws UserNotLoggedInException
   * 
   * @throws SecurityException
   *           If the user is not logged in as an administrator.
   */
  public void storeTournaments(Iterable<Tournament> tournaments)
      throws UserNotLoggedInException, UserNotAdminException;

  /**
   * Saves or updates a particular tournament in the datastore.
   * 
   * @param tournament
   *          The tournament to save or update. If the Id is not found in the
   *          datastore, a new entity will be written. If the Id is found, this
   *          method will update the tournament.
   * 
   * @return The (possibly new) datastore key for the stored tournament.
   * @throws UserNotAdminException
   * @throws UserNotLoggedInException
   * 
   * @throws SecurityException
   *           If the user is not logged in as an administrator.
   */
  public Long storeTournament(Tournament tournament)
      throws UserNotLoggedInException, UserNotAdminException;

  /**
   * Deletes a set of tournaments from the datastore.
   * 
   * @param tournaments
   *          The tournaments to delete. Tournaments not found in the datastore
   *          will be ignored.
   * @throws UserNotAdminException
   * @throws UserNotLoggedInException
   * 
   * @throws SecurityException
   *           If the user is not logged in as an administrator.
   */
  public void deleteTournaments(Iterable<Tournament> tournaments)
      throws UserNotLoggedInException, UserNotAdminException;

  /**
   * Deletes a particular tournament from the datastore.
   * 
   * @param tournament
   *          The tournament to delete. Will be ignored if it is not already in
   *          the datastore.
   * @throws UserNotAdminException
   * @throws UserNotLoggedInException
   * 
   * @throws SecurityException
   *           If the user is not logged in as an administrator.
   */
  public void deleteTournament(Tournament tournament)
      throws UserNotLoggedInException, UserNotAdminException;

  /**
   * Add a game to the given tournament. Will save the game to the datastore
   * with the given tournament as the parent.
   * 
   * @param game
   *          The game to add to the datastore and the given tournament.
   * @param tournament
   *          The tournament to which the game should belong.
   * @return The Id of the (possibly new) game.
   * @throws UserNotAdminException
   * @throws UserNotLoggedInException
   * 
   * @throws SecurityException
   *           If the user is not logged in as an administrator.
   * @throws IllegalArgumentException
   *           If the game's index is nonsensical.
   */
  public Long addGame(Game game, Tournament tournament)
      throws UserNotLoggedInException, UserNotAdminException;

  /**
   * Add multiple games to the given tournament. Will save the games to the
   * datastore with the given tournament as the parent.
   * 
   * @param games
   *          The games to add to the datastore and the given tournament.
   * @param tournament
   *          The tournament to which the games should belong.
   * @throws UserNotAdminException
   * @throws UserNotLoggedInException
   * 
   * @throws SecurityException
   *           If the user is not logged in as an administrator.
   * @throws IllegalArgumentException
   *           If any of the games' index members are nonsensical.
   */
  public void addGames(Iterable<Game> games, Tournament tournament)
      throws UserNotLoggedInException, UserNotAdminException;

}
