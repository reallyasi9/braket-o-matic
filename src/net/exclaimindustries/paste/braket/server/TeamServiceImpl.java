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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.exclaimindustries.paste.braket.client.BraketTeam;
import net.exclaimindustries.paste.braket.client.BraketTournament;
import net.exclaimindustries.paste.braket.client.TeamService;

import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Ref;

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
        Ref<BraketTournament> tournamentRef =
                CurrentTournament.getCurrentTournament();
        if (tournamentRef == null) {
            return null;
        }
        BraketTournament tournament = tournamentRef.get();

        Collection<BraketTeam> teams =
                OfyService.ofy().load().type(BraketTeam.class)
                        .parent(tournament).ids(tournament.getTeams()).values();

        // Can't guarantee the order returned from the datastore.
        ArrayList<BraketTeam> returnTeams =
                new ArrayList<BraketTeam>(tournament.getNumberOfTeams());
        for (BraketTeam team : teams) {
            if (returnTeams.size() <= team.getIndex()) {
                while (returnTeams.size() < team.getIndex()) {
                    returnTeams.add(null);
                }
                returnTeams.add(team);
            } else {
                returnTeams.set(team.getIndex(), team);
            }
        }
        return returnTeams;
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.exclaimindustries.paste.braket.client.TeamService#storeTeam(net.
     * exclaimindustries.paste.braket.client.BraketTeam)
     */
    @Override
    public Long storeTeam(BraketTeam team) {
        // Check to see if you are an administrator.
        if (!UserServiceFactory.getUserService().isUserAdmin()) {
            throw new SecurityException("administration privileges required");
        }
        Ref<BraketTournament> tournamentRef =
                CurrentTournament.getCurrentTournament();
        if (tournamentRef == null) {
            throw new NullPointerException(
                    "need a current tournament to be set");
        }
        team.setTournamentKey(tournamentRef.getKey());
        return OfyService.ofy().save().entity(team).now().getId();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * net.exclaimindustries.paste.braket.client.TeamService#storeTeams(java
     * .util.Collection)
     */
    @Override
    public void storeTeams(Iterable<BraketTeam> teams) {
        if (!UserServiceFactory.getUserService().isUserAdmin()) {
            throw new SecurityException("administration privileges required");
        }
        Ref<BraketTournament> tournamentRef =
                CurrentTournament.getCurrentTournament();
        if (tournamentRef == null) {
            throw new NullPointerException(
                    "need a current tournament to be set");
        }
        for (BraketTeam team : teams) {
            team.setTournamentKey(tournamentRef.getKey());
        }
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
            throws IOException {
        if (!UserServiceFactory.getUserService().isUserAdmin()) {
            throw new SecurityException("administration privileges required");
        }
        return TeamDownloader.downloadTeams(teamIds);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * net.exclaimindustries.paste.braket.client.TeamService#deleteTeam(net.
     * exclaimindustries.paste.braket.client.BraketTeam)
     */
    @Override
    public void deleteTeam(BraketTeam team) {
        if (!UserServiceFactory.getUserService().isUserAdmin()) {
            throw new SecurityException("administration privileges required");
        }
        Ref<BraketTournament> tournamentRef =
                CurrentTournament.getCurrentTournament();
        if (tournamentRef == null) {
            throw new NullPointerException(
                    "need a current tournament to be set");
        }
        team.setTournamentKey(tournamentRef.getKey());
        OfyService.ofy().delete().entity(team);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * net.exclaimindustries.paste.braket.client.TeamService#deleteTeams(java
     * .util.Collection)
     */
    @Override
    public void deleteTeams(Iterable<BraketTeam> teams) {
        if (!UserServiceFactory.getUserService().isUserAdmin()) {
            throw new SecurityException("administration privileges required");
        }
        Ref<BraketTournament> tournamentRef =
                CurrentTournament.getCurrentTournament();
        if (tournamentRef == null) {
            throw new NullPointerException(
                    "need a current tournament to be set");
        }
        for (BraketTeam team : teams) {
            team.setTournamentKey(tournamentRef.getKey());
        }
        OfyService.ofy().delete().entities(teams);
    }

}
