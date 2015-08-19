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

import com.google.gwt.user.client.rpc.AsyncCallback;

import net.exclaimindustries.paste.braket.shared.Fixture;

/**
 * @author paste
 *
 */
public interface GameServiceAsync {

    void getGames(AsyncCallback<List<Fixture>> callback);

    void storeGames(Iterable<Fixture> games, AsyncCallback<Void> callback);

    void storeGame(Fixture game, AsyncCallback<Long> callback);

    void deleteGames(Iterable<Fixture> games, AsyncCallback<Void> callback);

    void deleteGame(Fixture game, AsyncCallback<Void> callback);

}
