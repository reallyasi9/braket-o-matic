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

import java.io.Serializable;

/**
 * A class representing the name of a team.
 * 
 * @author paste
 * 
 */
public final class TeamName implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * The name of the team's school, like "University of Michigan".
     */
    private String schoolName = null;

    /**
     * The name of the team itself, like "Wolverines".
     */
    private String teamName = null;

    /**
     * A human-readable name that uniquely identifies the team, like
     * "Michigan Wolverines".
     */
    private String displayName = null;

    /**
     * A short name that can be used in small contexts, like "UMich"
     */
    private String shortName = null;

    /**
     * An abbreviation that uniquely identifies the team, like "MICH".
     */
    private String abbreviation = null;

    public TeamName() {
        super();
    }

    /**
     * Constructor using fields.
     * 
     * @param schoolName
     *            The name of the team's school, like "University of Michigan".
     * @param teamName
     *            The name of the team itself, like "Wolverines".
     * @param displayName
     *            A human-readable name that identifies the team, like
     *            "Michigan Wolverines".
     * @param shortName
     *            A name that can be used in tight situations, like "UMich".
     * @param abbreviation
     *            An abbreviation for the team, like "MICH".
     */
    public TeamName(String schoolName, String teamName, String displayName,
            String shortName, String abbreviation) {
        super();
        this.schoolName = schoolName;
        this.teamName = teamName;
        this.displayName = displayName;
        this.shortName = shortName;
        this.abbreviation = abbreviation;
    }

    /**
     * Copy constructor.
     * 
     * @param other
     *            The object to copy.
     */
    public TeamName(TeamName other) {
        schoolName = other.schoolName;
        teamName = other.teamName;
        displayName = other.displayName;
        shortName = other.shortName;
        abbreviation = other.abbreviation;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

}