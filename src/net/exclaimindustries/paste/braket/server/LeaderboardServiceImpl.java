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
package net.exclaimindustries.paste.braket.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import net.exclaimindustries.paste.braket.client.BraketPrediction;
import net.exclaimindustries.paste.braket.client.Tournament;
import net.exclaimindustries.paste.braket.client.BraketUser;
import net.exclaimindustries.paste.braket.client.LeaderboardService;
import net.exclaimindustries.paste.braket.client.UserRanking;
import net.exclaimindustries.paste.braket.shared.NoCurrentTournamentException;
import net.exclaimindustries.paste.braket.shared.SelectionInfo;
import net.exclaimindustries.paste.braket.shared.TournamentNotStartedException;
import net.exclaimindustries.paste.braket.shared.UserNotLoggedInException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Ref;

/**
 * @author paste
 * 
 */
public class LeaderboardServiceImpl extends RemoteServiceServlet implements
        LeaderboardService {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Collection<SelectionInfo> getSelectionInfos(Tournament tournament)
            throws NoCurrentTournamentException, UserNotLoggedInException,
            TournamentNotStartedException {
        // FORCED CONSISTENCY CHECK (TRUST GAMES)
        // Collection<BraketGame> games =
        // OfyService.ofy().load().type(BraketGame.class)
        // .parent(tournament).ids(tournament.getGames()).values();
        // for (BraketGame game : games) {
        // if (game.getWinner() == null) {
        // tournament.setCompletionMask(tournament.getCompletionMask()
        // .clearBit(game.getIndex()));
        // } else {
        // tournament.setCompletionMask(tournament.getCompletionMask()
        // .setBit(game.getIndex()));
        // if (game.getWinner() == true) {
        // tournament.setGameWinners(tournament.getGameWinners()
        // .setBit(game.getIndex()));
        // }
        // }
        // }
        // OfyService.ofy().save().entity(tournament).now();

        // Get users registered to this tournament
        Collection<BraketUser> users =
                OfyService.ofy().load().type(BraketUser.class)
                        .ids(tournament.getRegisteredPredictions().keySet()).values();

        // Get user selections
        Collection<BraketPrediction> selections =
                OfyService.ofy().load().type(BraketPrediction.class)
                        .ids(tournament.getRegisteredPredictions().values()).values();

        // Sort these by userID
        HashMap<String, BraketPrediction> selectionMap =
                new HashMap<String, BraketPrediction>();
        for (BraketPrediction selection : selections) {
            selectionMap.put(selection.getUserId(), selection);
        }

        // Get expecto!
        ExpectedValueServiceImpl expectoService = new ExpectedValueServiceImpl();
        Map<String, Double> expecto = expectoService.getExpectedValues();

        // Sort everything out
        ArrayList<SelectionInfo> infos = new ArrayList<SelectionInfo>();
        for (BraketUser user : users) {

            // Match selection
            BraketPrediction selection = selectionMap.get(user.getId());

            // Calculate points
            double points = tournament.getSelectionValue(selection);

            // Calculate points possible
            double pointsPossible = tournament.getPossibleValue(selection);

            // Get the expected value from expecto
            double expected = 0;
            if (expecto != null && expecto.containsKey(user.getId())) {
                // It's just the most recent
                expected = expecto.get(user.getId());
            }

            SelectionInfo info =
                    new SelectionInfo(user, selection, points, pointsPossible,
                            expected, null);

            infos.add(info);
        }

        return infos;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * net.exclaimindustries.paste.braket.client.LeaderboardService#getLeaderboard
     * ()
     */
    @Override
    public Collection<SelectionInfo> getLeaderboard()
            throws NoCurrentTournamentException, UserNotLoggedInException,
            TournamentNotStartedException {

        LogInServiceHelper.assertLoggedIn();
        TournamentServiceHelper.assertStarted();

        Ref<Tournament> tournamentRef =
                CurrentTournament.getCurrentTournament();

        Tournament tournament = tournamentRef.get();

        return getSelectionInfos(tournament);
    }

    @Override
    public UserRanking getUserRanking(BraketUser user)
            throws NoCurrentTournamentException, UserNotLoggedInException {

        LogInServiceHelper.assertLoggedIn();

        // Return null if the tournament exists but has not yet started.
        try {
            TournamentServiceHelper.assertStarted();
        } catch (TournamentNotStartedException e) {
            return null;
        }

        Ref<Tournament> tournamentRef =
                CurrentTournament.getCurrentTournament();

        Tournament tournament = tournamentRef.get();

        // If the tournament hasn't started, return immediately
        if (!tournament.isOngoing()) {
            return null;
        }

        // Get all the users' info from the leaderboard
        Collection<SelectionInfo> infos;
        try {
            infos = getSelectionInfos(tournament);
        } catch (TournamentNotStartedException e) {
            // I already checked for this earlier.
            return null;
        }

        int participants = infos.size();

        // Put these onto a priority queue so that I can pop off by ranks
        PriorityQueue<SelectionInfo> sortedInfos =
                new PriorityQueue<SelectionInfo>(participants,
                        Collections.reverseOrder());

        // Add the selections to the queue
        sortedInfos.addAll(infos);

        // Pop off infos until I find my user
        int virtualRank = 0;
        double lastPoints = -1;
        int ties = 0;
        SelectionInfo val;
        while ((val = sortedInfos.poll()) != null) {
            ++virtualRank;
            if (val.getPoints() == lastPoints) {
                ++ties;
            } else {
                ties = 0;
                lastPoints = val.getPoints();
            }
            if (val.getUser() == user) {
                // make sure we account for all the ties
                SelectionInfo subVal;
                while ((subVal = sortedInfos.poll()) != null
                        && subVal.getPoints() == lastPoints) {
                    ++virtualRank;
                    ++ties;
                }
                return new UserRanking(val, virtualRank - ties, participants, ties);
            }
        }

        // Whoopsie!
        throw new NullPointerException("user not found in tournament leaderboards");

    }
}
