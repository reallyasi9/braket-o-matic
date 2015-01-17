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
import java.util.Map;

import net.exclaimindustries.paste.braket.client.Game;
import net.exclaimindustries.paste.braket.client.Tournament;
import net.exclaimindustries.paste.braket.client.GameService;
import net.exclaimindustries.paste.braket.shared.UserNotAdminException;
import net.exclaimindustries.paste.braket.shared.UserNotLoggedInException;

import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.VoidWork;
import com.googlecode.objectify.Work;

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
  public Map<Long, Game> getGames(Tournament tournament) {
    return OfyService.ofy().load().type(Game.class).parent(tournament)
        .ids(tournament.getGames());
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.exclaimindustries.paste.braket.client.GameService#storeGames(java
   * .util.Collection)
   */
  @Override
  public void storeGames(Iterable<Game> games)
      throws UserNotLoggedInException, UserNotAdminException {
    LogInServiceHelper.assertAdmin();
    OfyService.ofy().save().entities(games);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.exclaimindustries.paste.braket.client.GameService#storeGame(net.
   * exclaimindustries.paste.braket.client.BraketGame)
   */
  @Override
  public Long storeGame(Game game) throws UserNotLoggedInException,
      UserNotAdminException {
    LogInServiceHelper.assertAdmin();
    return OfyService.ofy().save().entity(game).now().getId();
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.exclaimindustries.paste.braket.client.GameService#deleteGames(java
   * .util.Collection)
   */
  @Override
  public void deleteGames(Iterable<Game> games)
      throws UserNotLoggedInException, UserNotAdminException {
    LogInServiceHelper.assertAdmin();
    OfyService.ofy().delete().entities(games);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.exclaimindustries.paste.braket.client.GameService#deleteGame(net.
   * exclaimindustries.paste.braket.client.BraketGame)
   */
  @Override
  public void deleteGame(Game game) throws UserNotLoggedInException,
      UserNotAdminException {
    LogInServiceHelper.assertAdmin();
    OfyService.ofy().delete().entity(game);
  }

  @Override
  public void updateAndPropagateGame(Game game)
      throws UserNotLoggedInException, UserNotAdminException {
    // TODO Auto-generated method stub

  }

}
