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

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client-side stub for the Team RPC.
 * 
 * @author paste
 */
@RemoteServiceRelativePath("team")
public interface TeamService extends RemoteService {

    /**
     * @return The teams for the current tournament, in "tournament order" (the
     *         first team in the list occupies the "top" position in the first
     *         game of the first round, while the last team occupies that
     *         "bottom" position in the last game of the first round). Will
     *         return null if the current tournament is not set, and will return
     *         nulls in any places where no teams are set.
     */
    public List<BraketTeam> getTeams();

    /**
     * Update an existing team in the datastore, or write a new team to the
     * datastore. This method WILL NOT populate the list of team IDs in the
     * parent tournament, so should only be used for updating a team.
     * 
     * @see TournamentService.addTeam(BraketTeam)
     * 
     * @param team
     *            The team to write to the datastore.
     * 
     * @return The (possibly new) Id of the stored team.
     * 
     * @throws SecurityException
     *             If the user is not logged in as an administrator.
     * @throws NullPointerException
     *             If there is no <code>BraketTournament</code> referenced to
     *             the team.
     */
    public Long storeTeam(BraketTeam team);

    /**
     * Update existing teams in the datastore, or write new teams to the
     * datastore. This method WILL NOT populate the list of team IDs in the
     * parent tournament, so should only be used for updating teams.
     * 
     * @see TournamentService.addTeams(Iterable<BraketTeam>)
     * 
     * @param teams
     *            The teams to write to the datastore.
     * 
     * @return A map of the given teams keyed by their (possibly new) Ids in the
     *         datastore.
     * 
     * @throws SecurityException
     *             If the user is not logged in as an administrator.
     * @throws NullPointerException
     *             If there is no <code>BraketTournament</code> referenced to
     *             any of the teams.
     */
    public void storeTeams(Iterable<BraketTeam> teams);

    /**
     * Delete an existing team in the datastore.
     * 
     * @param team
     *            The team to be deleted.
     * 
     * @throws SecurityException
     *             If the user is not logged in as an administrator.
     * @throws NullPointerException
     *             If there is no <code>BraketTournament</code> referenced to a
     *             team in the collection (that is, the
     *             <code>team.getTournamentRef()</code> returns
     *             <code>null</code>.
     */
    public void deleteTeam(BraketTeam team);

    /**
     * Delete multiple teams in the datastore.
     * 
     * @param teamIds
     *            The teams to be deleted.
     * 
     * @throws SecurityException
     *             If the user is not logged in as an administrator.
     */
    public void deleteTeams(Iterable<BraketTeam> teams);

    /**
     * Downloads teams from ESPN and returns them without writing them to the
     * datastore.
     * 
     * As a warning: do not try to download more than about 20 teams at a time,
     * else the request will take too long and App Engine will kill it before
     * you get your teams back!
     * 
     * @param teamIds
     *            The Ids of the teams you wish to download. To find the Id of a
     *            team, go to <a
     *            href="http://espn.go.com/mens-college-basketball/teams"
     *            >http://espn.go.com/mens-college-basketball/teams</a>, find a
     *            team, then view its logo image. The name of the logo image
     *            will be the Id of the team, followed by an extension. For
     *            instance, Duke's logo can be found at <a
     *            href="http://a.espncdn.com/i/teamlogos/ncaa/50x50/150.png"
     *            >http://a.espncdn.com/i/teamlogos/ncaa/50x50/150.png</a>, so
     *            Duke's Id is 150.
     * @return A list of teams requested to be downloaded. <b>THIS IS
     *         IMPORTANT:</b> The returned team is not associated to any
     *         tournament, so if you simply write it as it is returned, you will
     *         not be able to get the teams back from the datastore. Because of
     *         this, the RPCs that store the teams will check that each team is
     *         associated with a tournament before saving them to the datastore.
     * @throws IOException
     *             If there was an IO problem, or if the XML couldn't be parsed.
     * @throws SecurityException
     *             If the user is not logged in as an administrator.
     */
    public Collection<BraketTeam> downloadTeams(Iterable<Long> teamIds)
            throws IOException;
}
