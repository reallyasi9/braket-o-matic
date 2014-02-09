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
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;

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

    @Id
    private Long id;

    /**
     * The ID of the tournament to which this team belongs.
     */
    @Parent
    private transient Key<BraketTournament> tournamentKey = null;

    /**
     * The starting seed of this team.
     */
    private int seed = 0;

    /**
     * The index of this team. An index of 0 means this team starts in the top
     * slot of the first game in the outer-most round of the tournamentKey.
     */
    private int index = 0;

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
     * The team's KenPom Pythagorean score.
     */
    private double kenpomScore = 0;

    /**
     * The team's season win percentage
     */
    private double winPercentage = 0;

    /**
     * Default constructor.
     */
    public BraketTeam() {
    }
    
    public BraketTeam(int seed, TeamName name) {
        this.seed = seed;
        this.teamName = name;
    }

    public Object clone() {
        BraketTeam t = new BraketTeam();
        t.setColor(this.getColor());
        t.setIndex(this.getIndex());
        t.setKenpomScore(this.getKenpomScore());
        t.setName(this.getName());
        t.setPicture(this.getPicture());
        t.setSeed(this.getSeed());
        t.setTournamentKey(this.getTournamentKey());
        t.setWinPercentage(this.getWinPercentage());
        return t;
    }

    public Key<BraketTournament> getTournamentKey() {
        return tournamentKey;
    }

    public void setTournamentKey(Key<BraketTournament> tournament) {
        this.tournamentKey = tournament;
    }

    public int getSeed() {
        return seed;
    }

    public void setSeed(int seed) {
        if (seed < 1) {
            throw new IllegalArgumentException(
                    "seed must be between greater than 0, "
                            + Integer.toString(seed) + " given");
        }
        this.seed = seed;
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

    public double getKenpomScore() {
        return kenpomScore;
    }

    public void setKenpomScore(double kenpomScore) {
        this.kenpomScore = kenpomScore;
    }

    public double getWinPercentage() {
        return winPercentage;
    }

    public void setWinPercentage(double winPercentage) {
        this.winPercentage = winPercentage;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

}
