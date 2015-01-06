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

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * A class that represents a contestant in a bracket game.
 * 
 * @author paste
 * 
 */
@Entity
@Cache
public final class BraketTeam implements IsSerializable {

  private static final String imageDirectory = "/images/teams/";

  /**
   * A Key Provider so that BraketTournaments can be placed in DataGrids.
   */
  public static final ProvidesKey<BraketTeam> KEY_PROVIDER = new ProvidesKey<BraketTeam>() {
    @Override
    public Object getKey(BraketTeam item) {
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
  private Long id;

  /**
   * The teamName of the team.
   */
  private TeamName teamName = new TeamName();

  /**
   * Location of a logo or picture representing the team
   */
  private String picture = null;

  /**
   * A single color that represents the team. Typically light.
   */
  private RGBAColor color = new RGBAColor(0xcccccc);

  /**
   * Default constructor.
   */
  public BraketTeam() {
  }

  public BraketTeam(TeamName name) {
    this.teamName = name;
  }

  public Object clone() {
    BraketTeam t = new BraketTeam();
    t.setColor(this.getColor());
    t.setName(this.getName());
    t.setPicture(this.getPicture());
    return t;
  }

  public TeamName getName() {
    return new TeamName(teamName);
  }

  public void setName(TeamName teamName) {
    this.teamName = new TeamName(teamName);
  }

  public String getPicture() {
    return imageDirectory + picture;
  }

  public void setPicture(String picture) {
    this.picture = picture;
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
