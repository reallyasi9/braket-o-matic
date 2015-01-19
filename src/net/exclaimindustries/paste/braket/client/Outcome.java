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

import com.google.gwt.user.client.rpc.IsSerializable;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

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
public abstract class Selectable implements IsSerializable {

  /**
   * The base class must have the Id field.
   */
  @Id
  protected Long id = null;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public abstract void setOutcome(Game game, List<Team> outcome);

  public abstract List<Team> getOutcome(Game game);

}
