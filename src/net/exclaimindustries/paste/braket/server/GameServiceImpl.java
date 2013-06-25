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
import java.util.List;

import net.exclaimindustries.paste.braket.client.BraketGame;
import net.exclaimindustries.paste.braket.client.BraketTournament;
import net.exclaimindustries.paste.braket.client.GameService;

import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Ref;

/**
 * @author paste
 * 
 */
public class GameServiceImpl extends RemoteServiceServlet implements
        GameService {

    /**
     * Generated
     */
    private static final long serialVersionUID = 1L;

    /*
     * (non-Javadoc)
     * 
     * @see net.exclaimindustries.paste.braket.client.GameService#getGames()
     */
    @Override
    public List<BraketGame> getGames() {

        Ref<BraketTournament> currentRef =
                CurrentTournament.getCurrentTournament();
        if (currentRef == null) {
            return null;
        }

        BraketTournament current =
                CurrentTournament.getCurrentTournament().get();

        Collection<BraketGame> games =
                OfyService.ofy().load().type(BraketGame.class).parent(current)
                        .ids(current.getGames()).values();
        // Can't guarantee order from the datastore.
        ArrayList<BraketGame> gameList =
                new ArrayList<BraketGame>(games.size());
        for (BraketGame game : games) {
            if (gameList.size() <= game.getIndex()) {
                while (gameList.size() < game.getIndex()) {
                    gameList.add(null);
                }
                gameList.add(game);
            } else {
                gameList.set(game.getIndex(), game);
            }
        }
        return gameList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * net.exclaimindustries.paste.braket.client.GameService#storeGames(java
     * .util.Collection)
     */
    @Override
    public void storeGames(Iterable<BraketGame> games) {
        if (!UserServiceFactory.getUserService().isUserAdmin()) {
            throw new SecurityException("administration privileges required");
        }

        Ref<BraketTournament> tournamentRef =
                CurrentTournament.getCurrentTournament();
        if (tournamentRef == null) {
            throw new NullPointerException(
                    "need a current tournament to be set");
        }

        for (BraketGame game : games) {
            game.setTournamentKey(tournamentRef.getKey());
        }

        OfyService.ofy().save().entities(games);
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.exclaimindustries.paste.braket.client.GameService#storeGame(net.
     * exclaimindustries.paste.braket.client.BraketGame)
     */
    @Override
    public long storeGame(BraketGame game) {
        if (!UserServiceFactory.getUserService().isUserAdmin()) {
            throw new SecurityException("administration privileges required");
        }

        Ref<BraketTournament> tournamentRef =
                CurrentTournament.getCurrentTournament();
        if (tournamentRef == null) {
            throw new NullPointerException(
                    "need a current tournament to be set");
        }
        game.setTournamentKey(tournamentRef.getKey());

        return OfyService.ofy().save().entity(game).now().getId();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * net.exclaimindustries.paste.braket.client.GameService#deleteGames(java
     * .util.Collection)
     */
    @Override
    public void deleteGames(Iterable<BraketGame> games) {
        if (!UserServiceFactory.getUserService().isUserAdmin()) {
            throw new SecurityException("administration privileges required");
        }

        Ref<BraketTournament> tournamentRef =
                CurrentTournament.getCurrentTournament();
        if (tournamentRef == null) {
            throw new NullPointerException(
                    "need a current tournament to be set");
        }
        for (BraketGame game : games) {
            game.setTournamentKey(tournamentRef.getKey());
        }

        OfyService.ofy().delete().entities(games);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * net.exclaimindustries.paste.braket.client.GameService#deleteGame(net.
     * exclaimindustries.paste.braket.client.BraketGame)
     */
    @Override
    public void deleteGame(BraketGame game) {
        if (!UserServiceFactory.getUserService().isUserAdmin()) {
            throw new SecurityException("administration privileges required");
        }

        Ref<BraketTournament> tournamentRef =
                CurrentTournament.getCurrentTournament();
        if (tournamentRef == null) {
            throw new NullPointerException(
                    "need a current tournament to be set");
        }
        game.setTournamentKey(tournamentRef.getKey());

        OfyService.ofy().delete().entity(game);
    }

}
