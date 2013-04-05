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

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * A service to allow RPCs fetching and storing BraketGame objects from and to
 * the datastore.
 * 
 * @author paste
 * 
 */
@RemoteServiceRelativePath("game")
public interface GameService extends RemoteService {
    /**
     * Fetches the games from the currently active tournament.
     * 
     * @return The games from the currently running tournament, or null if no
     *         such tournament exists. These games will be in so-called
     *         "tournament order", with the first game in the list representing
     *         the first game of the first round, and the last game representing
     *         the championship game.
     */
    public List<BraketGame> getGames();

    /**
     * Saves or updates the given collection of games in the datastore. This
     * method will not update the tournament to which these games belong. Use
     * TournamentService.addGames(Iterable<BraketGame>) instead.
     * 
     * @see TournamentService.addGame(BraketGame)
     * 
     * @param games
     *            The collection of games to either save or update. Games that
     *            already exist in the datastore will be updated, and games that
     *            do not exist will be created.
     * 
     * @return A map of games keyed by their (possibly new) datastore keys.
     * 
     * @throws SecurityException
     *             If the user is not logged in as an administrator.
     * 
     * @throws NullPointerException
     *             If any game in the collection is not associated with a valid
     *             <code>BraketTournament</code>.
     */
    public void storeGames(Iterable<BraketGame> games);

    long storeGame(BraketGame game);

    /**
     * Deletes a set of games from the datastore.
     * 
     * @param games
     *            The games to delete. Games not found in the datastore will be
     *            ignored.
     * 
     * @throws SecurityException
     *             If the user is not logged in as an administrator.
     */
    public void deleteGames(Iterable<BraketGame> games);

    /**
     * Deletes a particular game from the datastore.
     * 
     * @param game
     *            The game to delete. Will be ignored if it is not already in
     *            the datastore.
     * 
     * @throws SecurityException
     *             If the user is not logged in as an administrator.
     */
    public void deleteGame(BraketGame game);
}
