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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.exclaimindustries.paste.braket.shared.GameNotInOutcomeException;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;

/**
 * @author paste
 * 
 *         This interface describes a class that has selected games in a
 *         tournament format. This means that one can query the object for what
 *         team is selected for a given game index. Classes that implement this
 *         interface are designed to work in tandem with an array of teams in
 *         the tournament, returned by the RPC class TeamService.
 */
@Entity
@Cache
public class Outcome implements IsSerializable {

  /**
   * The base class must have the Id field.
   */
  @Id
  protected Long id = null;

  @Parent
  protected Key<Tournament> parentTournament = null;

  private Map<Long, List<Long>> outcome = new HashMap<>();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getParentTournamentId() {
    return (parentTournament == null) ? null : parentTournament.getId();
  }

  public void setResult(Game game, List<Team> result) {
    List<Long> teamIdList = new ArrayList<>();
    for (Team team : result) {
      teamIdList.add(team.getId());
    }
    outcome.put(game.getId(), teamIdList);
  }

  public List<Long> getResult(Game game) throws GameNotInOutcomeException {
    if (!outcome.containsKey(game.getId())) {
      throw new GameNotInOutcomeException("game with id [" + game.getId()
          + "] not in outcome");
    }
    return outcome.get(game.getId());
  }

}
