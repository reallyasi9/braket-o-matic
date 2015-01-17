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
import java.util.List;

import net.exclaimindustries.paste.braket.client.TournamentService.TournamentCollection;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author paste
 * 
 */
public interface TournamentServiceAsync {

  void getCurrentTournament(AsyncCallback<TournamentCollection> callback);

  void getTournaments(AsyncCallback<Collection<Tournament>> callback);

  void storeTournaments(Iterable<Tournament> tournaments,
      AsyncCallback<Void> callback);

  void storeTournament(Tournament tournament, AsyncCallback<Long> callback);

  void deleteTournaments(Iterable<Tournament> tournaments,
      AsyncCallback<Void> callback);

  void deleteTournament(Tournament tournament,
      AsyncCallback<Void> callback);

  void setCurrentTournament(Tournament tournament,
      AsyncCallback<Tournament> callback);

  void addGame(Game game, Tournament tournament,
      AsyncCallback<Long> callback);

  void addGames(Iterable<Game> games, Tournament tournament,
      AsyncCallback<Void> callback);

  void getTournaments(Tournament.IndexName orderCondition, int offset,
      int limit, AsyncCallback<List<Tournament>> callback);

}
