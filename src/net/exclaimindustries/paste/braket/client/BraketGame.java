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

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;

/**
 * A class that represents a bracket game.
 * 
 * @author paste
 * 
 */
@Entity
@Cache
public final class BraketGame implements IsSerializable {

    public static class WithTeams {
    }

    public static final String FINAL = "FINAL";

    public static final int STATUS_FINAL = 3;

    public static final int STATUS_IN_PROGRESS = 2;
    
    public static final int STATUS_HALFTIME = 22;

    public static final int STATUS_SCHEDULED = 1;

    @Id
    private Long id = null;

    /**
     * An Id representing the tournament to which this game belongs.
     */
    @Parent
    private transient Key<BraketTournament> tournamentKey = null;

    /**
     * The game index, counted from the championship game (0) to the last game
     * in the first round, all in heap order (that is, indices 1 and 2 feed into
     * game 0, indices 3 and 4 feed into game 1, and so on).
     */
    private int index = 0;

    /**
     * The "top" team in the game. TODO: This should be an index!
     */
    private Long topTeamId = null;

    /**
     * The "bottom" team in the game. TODO: This should be an index!
     */
    private Long bottomTeamId = null;

    /**
     * The "top" team's score.
     */
    private Integer topScore = null;

    /**
     * The "bottom" team's score.
     */
    private Integer bottomScore = null;

    /**
     * Whether or not the second team won (null if there has been no winner).
     */
    private Boolean winner = null;

    /**
     * When this game is scheduled to being.
     */
    private Date scheduledDate = new Date();

    /**
     * The location of this game.
     */
    private String location = new String();

    /**
     * The status of this game, including things like time remaining.
     */
    private String gameStatus = new String();
    
    /**
     * ESPN's id, so I can easily find it when parsing the XML feed.
     */
    private Long espnId = null;

    /**
     * Default constructor.
     */
    public BraketGame() {
    }

    public Key<BraketTournament> getTournamentKey() {
        return tournamentKey;
    }

    public void setTournamentKey(Key<BraketTournament> tournamentId) {
        this.tournamentKey = tournamentId;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int gameIndex) {
        this.index = gameIndex;
    }

    public Long getTopTeamId() {
        return topTeamId;
    }

    public Long getBottomTeamId() {
        return bottomTeamId;
    }
    
    public Long getWinningTeamId() {
        if(!isFinal()) throw new IllegalStateException("This game isn't over yet!");
        if(winner) return getBottomTeamId();
        else return getTopTeamId();
    }
    
    public Long getLosingTeamId() {
        if(!isFinal()) throw new IllegalStateException("This game isn't over yet!");
        if(winner) return getTopTeamId();
        else return getBottomTeamId();
    }

    public void setTopTeamId(Long team) {
        topTeamId = team;
    }

    public void setBottomTeamId(Long team) {
        bottomTeamId = team;
    }

    /**
     * Convenience method to set a particular team.
     * 
     * @param team
     *            The team index (indexed from zero).
     * @param teamId
     *            A reference to the team.
     */
    public void setTeamId(int team, Long teamId) {
        switch (team) {
        case 0:
            setTopTeamId(teamId);
            break;

        case 1:
            setBottomTeamId(teamId);
            break;

        default:
            throw new IllegalArgumentException();
        }
    }
    
    /**
     * Convenience method for returning a reference to a team.
     * 
     * @param team
     *            The Team, The Team, The Team.
     * @return A <code>Key</code> to the requested team, or <code>null</code> if
     *         that team has not yet been set.
     */
    public Long getTeamId(int team) {
        switch (team) {
        case 0:
            return getTopTeamId();

        case 1:
            return getBottomTeamId();

        default:
            throw new IllegalArgumentException();
        }
    }

    public Integer getTopScore() {
        return topScore;
    }

    public void setTopScore(Integer score) {
        if (score != null && score < 0) {
            throw new IllegalArgumentException();
        }
        topScore = score;
    }

    public Integer getBottomScore() {
        return bottomScore;
    }

    public void setBottomScore(Integer score) {
        if (score != null && score < 0) {
            throw new IllegalArgumentException();
        }
        bottomScore = score;
    }

    /**
     * Convenience method to return the score of a particular team.
     * 
     * @param team
     *            The Team, The Team, The Team.
     * @return The score of the team, or 0 if that score does not exist yet (for
     *         instance, if the game has not started).
     */
    public Integer getScore(int team) {
        switch (team) {
        case 0:
            return getTopScore();

        case 1:
            return getBottomScore();

        default:
            throw new IllegalArgumentException();
        }
    }

    /**
     * Convenience method to set the score of a particular team.
     * 
     * @return
     */
    public void setScore(int team, Integer score) {
        switch (team) {
        case 0:
            setTopScore(score);
            break;

        case 1:
            setBottomScore(score);
            break;

        default:
            throw new IllegalArgumentException();
        }
    }

    public Boolean getWinner() {
        return winner;
    }

    public void setWinner(Boolean winner) {
        this.winner = winner;
    }

    public Date getScheduledDate() {
        return new Date(scheduledDate.getTime());
    }

    public void setScheduledDate(Date scheduledDate) {
        this.scheduledDate = new Date(scheduledDate.getTime());
    }

    public boolean isScheduled() {
        return scheduledDate.getTime() > new Date().getTime() && winner == null;
    }

    public boolean isInProgress() {
        return scheduledDate.getTime() <= new Date().getTime()
                && winner == null;
    }

    public boolean isFinal() {
        return scheduledDate.getTime() <= new Date().getTime()
                && winner != null;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(String gameStatus) {
        this.gameStatus = gameStatus;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Long getEspnId() {
        return espnId;
    }

    public void setEspnId(Long espnId) {
        this.espnId = espnId;
    }

}
