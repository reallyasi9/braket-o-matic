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
import java.util.HashMap;
import java.util.Map;

import net.exclaimindustries.paste.braket.client.BraketSelection;
import net.exclaimindustries.paste.braket.client.BraketTournament;
import net.exclaimindustries.paste.braket.client.BraketUser;
import net.exclaimindustries.paste.braket.client.LeaderboardService;
import net.exclaimindustries.paste.braket.shared.SelectionInfo;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
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

    /*
     * (non-Javadoc)
     * 
     * @see
     * net.exclaimindustries.paste.braket.client.LeaderboardService#getLeaderboard
     * ()
     */
    @Override
    public Collection<SelectionInfo> getLeaderboard() {

        UserService us = UserServiceFactory.getUserService();
        if (!us.isUserLoggedIn()) {
            throw new SecurityException(
                    "you need to be logged in to use this feature");
        }

        Ref<BraketTournament> tournamentRef =
                CurrentTournament.getCurrentTournament();
        if (tournamentRef == null) {
            throw new NullPointerException("no current tournament defined");
        }

        BraketTournament tournament = tournamentRef.get();

        // FORCED CONSISTENCY CHECK (TRUST GAMES)
//        Collection<BraketGame> games =
//                OfyService.ofy().load().type(BraketGame.class)
//                        .parent(tournament).ids(tournament.getGames()).values();
//        for (BraketGame game : games) {
//            if (game.getWinner() == null) {
//                tournament.setCompletionMask(tournament.getCompletionMask()
//                        .clearBit(game.getIndex()));
//            } else {
//                tournament.setCompletionMask(tournament.getCompletionMask()
//                        .setBit(game.getIndex()));
//                if (game.getWinner() == true) {
//                    tournament.setGameWinners(tournament.getGameWinners()
//                            .setBit(game.getIndex()));
//                }
//            }
//        }
//        OfyService.ofy().save().entity(tournament).now();

        // Get users registered to this tournament
        Collection<BraketUser> users =
                OfyService.ofy().load().type(BraketUser.class)
                        .ids(tournament.getRegisteredSelections().keySet())
                        .values();

        // Get user selections
        Collection<BraketSelection> selections =
                OfyService.ofy().load().type(BraketSelection.class)
                        .ids(tournament.getRegisteredSelections().values())
                        .values();

        // Sort these by userID
        HashMap<String, BraketSelection> selectionMap =
                new HashMap<String, BraketSelection>();
        for (BraketSelection selection : selections) {
            selectionMap.put(selection.getUserId(), selection);
        }

        // Get expecto!
        ExpectedValueServiceImpl expectoService =
                new ExpectedValueServiceImpl();
        Map<String, Double> expecto = expectoService.getExpectedValues();

        // Sort everything out
        ArrayList<SelectionInfo> infos = new ArrayList<SelectionInfo>();
        for (BraketUser user : users) {

            // Match selection
            BraketSelection selection = selectionMap.get(user.getId());

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
}
