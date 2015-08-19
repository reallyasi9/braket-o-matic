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

package net.exclaimindustries.paste.braket.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import net.exclaimindustries.paste.braket.client.RGBAColor;

/**
 * A class that represents a contestant in a bracket game.
 * 
 * @author paste
 * 
 */
@Entity
@Cache
public class Team implements IsSerializable, Competitor {

  /**
   * A Key Provider so that BraketTournaments can be placed in DataGrids.
   */
  public static final ProvidesKey<Team> KEY_PROVIDER = new ProvidesKey<Team>() {
    @Override
    public Object getKey(Team item) {
      return (item == null) ? null : item.getId();
    }
  };

  /**
   * Enumerated index names.
   */
  public static enum IndexName {
    schoolName, teamName, displayName, shortName, abbreviation;
  }

  @Id
  protected Long id = null;

  /**
   * The teamName of the team.
   */
  private TeamName teamName = new TeamName();

  /**
   * A logo or picture representing the team
   */
  private Image image = Image.UNKNOWN;

  /**
   * A single color that represents the team. Typically light.
   */
  private RGBAColor color = new RGBAColor(0xcccccc);

  /**
   * Default constructor.
   */
  public Team() {
  }

  public Team(TeamName name) {
    this.teamName = name;
  }

  public Object clone() {
    Team t = new Team();
    t.setColor(this.getColor());
    t.setName(this.getName());
    t.setImage(this.getImage());
    return t;
  }

  public TeamName getName() {
    return new TeamName(teamName);
  }

  public void setName(TeamName teamName) {
    this.teamName = new TeamName(teamName);
  }

  public Image getImage() {
    return this.image;
  }

  public void setImage(Image image) {
    this.image = image;
  }

  public RGBAColor getColor() {
    // Color is immutable
    return color;
  }

  public void setColor(RGBAColor color) {
    // Color is immutable
    this.color = color;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

}
