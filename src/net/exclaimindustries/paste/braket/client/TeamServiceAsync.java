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
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author paste
 * 
 */
public interface TeamServiceAsync {

  void storeTeam(Team team, AsyncCallback<Long> callback);

  void deleteTeam(Team team, AsyncCallback<Void> callback);

  void downloadTeams(Iterable<Long> teamIds,
      AsyncCallback<Collection<Team>> callback);

  void storeTeams(Iterable<Team> teams, AsyncCallback<Void> callback);

  void deleteTeams(Iterable<Team> teams, AsyncCallback<Void> callback);

  void getTeams(AsyncCallback<List<Team>> callback);

  void getTeams(Tournament tournament,
      AsyncCallback<Map<Long, Team>> callback);

  void getTeams(Team.IndexName orderCondition, int offset, int limit,
      AsyncCallback<List<Team>> callback);
}
