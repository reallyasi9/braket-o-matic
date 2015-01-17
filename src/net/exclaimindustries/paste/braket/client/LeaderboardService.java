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

import net.exclaimindustries.paste.braket.shared.NoCurrentTournamentException;
import net.exclaimindustries.paste.braket.shared.SelectionInfo;
import net.exclaimindustries.paste.braket.shared.TournamentNotStartedException;
import net.exclaimindustries.paste.braket.shared.UserNotLoggedInException;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * @author paste
 * 
 */
@RemoteServiceRelativePath("leaderboard")
public interface LeaderboardService extends RemoteService {

    /**
     * Get the selection info for filling in the leaderboard.
     * 
     * @return A collection of selection info for registered users of the
     *         current tournament that can be fed into the leaderboard.
     * @throws NoCurrentTournamentException
     * @throws UserNotLoggedInException
     * @throws TournamentNotStartedException
     */
    public Collection<SelectionInfo> getLeaderboard()
            throws NoCurrentTournamentException, UserNotLoggedInException,
            TournamentNotStartedException;

    /**
     * Get The rank information for a particular user.
     * 
     * @param user
     *            The user whose rank should be returned.
     * 
     * @return An object containing the user's SelectionInfo along with rank
     *         information, or null if there is no definable rank for that user
     *         (or instance, if the tournament hasn't yet begun).
     * @throws NoCurrentTournamentException
     * @throws UserNotLoggedInException
     * @throws TournamentNotStartedException
     */
    public UserRanking getUserRanking(User user)
            throws NoCurrentTournamentException, UserNotLoggedInException,
            TournamentNotStartedException;

}
