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

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.exclaimindustries.paste.braket.client.BraketTeam;
import net.exclaimindustries.paste.braket.client.BraketTournament;
import net.exclaimindustries.paste.braket.client.TeamService;
import net.exclaimindustries.paste.braket.shared.UserNotAdminException;
import net.exclaimindustries.paste.braket.shared.UserNotLoggedInException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * @author paste
 * 
 */
public class TeamServiceImpl extends RemoteServiceServlet implements
    TeamService {

  /**
   * Generated
   */
  private static final long serialVersionUID = 1L;

  /*
   * (non-Javadoc)
   * 
   * @see net.exclaimindustries.paste.braket.client.TeamService#getTeams()
   */
  @Override
  public List<BraketTeam> getTeams() {
    return OfyService.ofy().load().type(BraketTeam.class).list();
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.exclaimindustries.paste.braket.client.TeamService#storeTeam(net.
   * exclaimindustries.paste.braket.client.BraketTournament)
   */
  @Override
  public Map<Long, BraketTeam> getTeams(BraketTournament tournament) {
    return OfyService.ofy().load().type(BraketTeam.class)
        .ids(tournament.getTeams());
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.exclaimindustries.paste.braket.client.TeamService#storeTeam(net.
   * exclaimindustries.paste.braket.client.BraketTeam)
   */
  @Override
  public Long storeTeam(BraketTeam team) throws UserNotLoggedInException,
      UserNotAdminException {
    // Check to see if you are an administrator.
    LogInServiceHelper.assertAdmin();
    return OfyService.ofy().save().entity(team).now().getId();
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.exclaimindustries.paste.braket.client.TeamService#storeTeams(java
   * .util.Collection)
   */
  @Override
  public void storeTeams(Iterable<BraketTeam> teams)
      throws UserNotLoggedInException, UserNotAdminException {
    LogInServiceHelper.assertAdmin();
    OfyService.ofy().save().entities(teams);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * net.exclaimindustries.paste.braket.client.TeamService#downloadTeams(java
   * .util.Collection)
   */
  @Override
  public Collection<BraketTeam> downloadTeams(Iterable<Long> teamIds)
      throws IOException, UserNotLoggedInException, UserNotAdminException {
    LogInServiceHelper.assertAdmin();
    return TeamDownloader.downloadTeams(teamIds);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.exclaimindustries.paste.braket.client.TeamService#deleteTeam(net.
   * exclaimindustries.paste.braket.client.BraketTeam)
   */
  @Override
  public void deleteTeam(BraketTeam team) throws UserNotLoggedInException,
      UserNotAdminException {
    LogInServiceHelper.assertAdmin();
    OfyService.ofy().delete().entity(team);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.exclaimindustries.paste.braket.client.TeamService#deleteTeams(java
   * .util.Collection)
   */
  @Override
  public void deleteTeams(Iterable<BraketTeam> teams)
      throws UserNotLoggedInException, UserNotAdminException {
    LogInServiceHelper.assertAdmin();
    OfyService.ofy().delete().entities(teams);
  }

  @Override
  public List<BraketTeam> getTeams(BraketTeam.IndexName orderCondition,
      int offset, int limit) throws UserNotLoggedInException,
      UserNotAdminException {
    LogInServiceHelper.assertAdmin();
    return OfyService.ofy().load().type(BraketTeam.class)
        .order(orderCondition.toString()).offset(offset).limit(limit).list();
  }

}
