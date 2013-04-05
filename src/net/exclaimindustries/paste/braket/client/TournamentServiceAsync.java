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

import net.exclaimindustries.paste.braket.client.TournamentService.TournamentCollection;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author paste
 * 
 */
public interface TournamentServiceAsync {

    void getCurrentTournament(AsyncCallback<TournamentCollection> callback);

    void getTournaments(AsyncCallback<Collection<BraketTournament>> callback);

    void storeTournaments(Iterable<BraketTournament> tournaments,
            AsyncCallback<Void> callback);

    void storeTournament(BraketTournament tournament,
            AsyncCallback<Long> callback);

    void deleteTournaments(Iterable<BraketTournament> tournaments,
            AsyncCallback<Void> callback);

    void deleteTournament(BraketTournament tournament,
            AsyncCallback<Void> callback);

    void setCurrentTournament(BraketTournament tournament,
            AsyncCallback<Void> callback);

    void addTeam(BraketTeam team, AsyncCallback<Long> callback);

    void addTeam(BraketTeam team, BraketTournament tournament,
            AsyncCallback<Long> callback);

    void addTeams(Iterable<BraketTeam> teams, AsyncCallback<Void> callback);

    void addTeams(Iterable<BraketTeam> teams, BraketTournament tournament,
            AsyncCallback<Void> callback);

    void addGame(BraketGame game, AsyncCallback<Long> callback);

    void addGame(BraketGame game, BraketTournament tournament,
            AsyncCallback<Long> callback);

    void addGames(Iterable<BraketGame> games, AsyncCallback<Void> callback);

    void addGames(Iterable<BraketGame> games, BraketTournament tournament,
            AsyncCallback<Void> callback);

    void setRules(String rules, AsyncCallback<Void> callback);

    void updateAndPropagateGame(BraketGame game, AsyncCallback<Void> callback);

}
