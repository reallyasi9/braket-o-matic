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

import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.exclaimindustries.paste.braket.client.BraketPrediction;
import net.exclaimindustries.paste.braket.client.Tournament;
import net.exclaimindustries.paste.braket.client.BraketUser;
import net.exclaimindustries.paste.braket.client.SelectionService;

import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.VoidWork;
import com.googlecode.objectify.Work;

/**
 * @author paste
 * 
 */
public class SelectionServiceImpl extends RemoteServiceServlet implements
        SelectionService {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /*
     * (non-Javadoc)
     * 
     * @see
     * net.exclaimindustries.paste.braket.client.SelectionService#getUserValues
     * ()
     */
    @Override
    public Map<String, Double> getUserValues() {

        if (!UserServiceFactory.getUserService().isUserLoggedIn()) {
            throw new SecurityException("you are not logged in");
        }

        Ref<Tournament> tournamentRef = CurrentTournament
                .getCurrentTournament();

        if (tournamentRef == null) {
            return null;
        }

        Tournament tournament = tournamentRef.get();

        if (!UserServiceFactory.getUserService().isUserAdmin()
                && tournament.isScheduled()) {
            throw new SecurityException("administration privileges required");
        }

        Collection<BraketPrediction> selections = getRegisteredSelectionsUnchecked(tournament);

        if (selections == null) {
            return null;
        }

        HashMap<String, Double> userValues = new HashMap<String, Double>();

        for (BraketPrediction selection : selections) {
            userValues.put(selection.getUserId(),
                    tournament.getSelectionValue(selection));
        }

        return userValues;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * net.exclaimindustries.paste.braket.client.SelectionService#getUserValues
     * (java.math.BigInteger)
     */
    @Override
    public Map<String, Double> getUserValues(BigInteger outcome, BigInteger mask) {

        if (!UserServiceFactory.getUserService().isUserLoggedIn()) {
            throw new SecurityException("you are not logged in");
        }

        Ref<Tournament> tournamentRef = CurrentTournament
                .getCurrentTournament();

        if (tournamentRef == null) {
            return null;
        }

        Tournament tournament = tournamentRef.get();

        if (!UserServiceFactory.getUserService().isUserAdmin()
                && tournament.isScheduled()) {
            throw new SecurityException("administration privileges required");
        }

        Collection<BraketPrediction> selections = getRegisteredSelectionsUnchecked(tournament);

        if (selections == null) {
            return null;
        }

        tournament.setCompletionMask(mask);
        tournament.setGameWinners(outcome);

        HashMap<String, Double> userValues = new HashMap<String, Double>();

        for (BraketPrediction selection : selections) {
            userValues.put(selection.getUserId(),
                    tournament.getSelectionValue(selection));
        }

        return userValues;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * net.exclaimindustries.paste.braket.client.SelectionService#getUserValue
     * (java.lang.Long)
     */
    @Override
    public Double getUserValue(BraketUser user) {

        if (!UserServiceFactory.getUserService().isUserLoggedIn()) {
            throw new SecurityException("you are not logged in");
        }

        Ref<Tournament> tournamentRef = CurrentTournament
                .getCurrentTournament();

        if (tournamentRef == null) {
            return null;
        }

        Tournament tournament = tournamentRef.get();

        if (!UserServiceFactory.getUserService().isUserAdmin()
                && tournament.isScheduled()) {
            throw new SecurityException("administration privileges required");
        }

        Long selectionId = user.getSelection(tournamentRef.getKey().getId());

        if (selectionId == null) {
            return null;
        }

        BraketPrediction selection = OfyService.ofy().load()
                .type(BraketPrediction.class).id(selectionId).now();

        if (selection == null) {
            return null;
        }

        return tournament.getSelectionValue(selection);

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * net.exclaimindustries.paste.braket.client.SelectionService#getUserValue
     * (java.lang.Long, java.math.BigInteger)
     */
    @Override
    public Double getUserValue(BraketUser user, BigInteger outcome,
            BigInteger mask) {

        if (!UserServiceFactory.getUserService().isUserLoggedIn()) {
            throw new SecurityException("you are not logged in");
        }

        Ref<Tournament> tournamentRef = CurrentTournament
                .getCurrentTournament();

        if (tournamentRef == null) {
            return null;
        }

        Tournament tournament = tournamentRef.get();

        if (!UserServiceFactory.getUserService().isUserAdmin()
                && tournament.isScheduled()) {
            throw new SecurityException("administration privileges required");
        }

        Long selectionId = user.getSelection(tournamentRef.getKey().getId());
        if (selectionId == null) {
            return null;
        }

        BraketPrediction selection = OfyService.ofy().load()
                .type(BraketPrediction.class).id(selectionId).now();

        if (selection == null) {
            return null;
        }

        tournament.setCompletionMask(mask);
        tournament.setGameWinners(outcome);

        return tournament.getSelectionValue(selection);
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.exclaimindustries.paste.braket.client.SelectionService#
     * getSelectionValue
     * (net.exclaimindustries.paste.braket.client.BraketSelection)
     */
    @Override
    public Double getSelectionValue(BraketPrediction selection) {

        if (!UserServiceFactory.getUserService().isUserLoggedIn()) {
            throw new SecurityException("you are not logged in");
        }

        Ref<Tournament> tournamentRef = CurrentTournament
                .getCurrentTournament();

        if (tournamentRef == null) {
            return null;
        }

        Tournament tournament = tournamentRef.get();

        if (!UserServiceFactory.getUserService().isUserAdmin()
                && tournament.isScheduled()) {
            throw new SecurityException("administration privileges required");
        }

        return tournament.getSelectionValue(selection);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * net.exclaimindustries.paste.braket.client.SelectionService#getSelections
     * (net.exclaimindustries.paste.braket.client.BraketUser)
     */
    @Override
    public Collection<BraketPrediction> getSelections(BraketUser user) {

        if (!UserServiceFactory.getUserService().getCurrentUser().getUserId()
                .equals(user.getId())
                && !UserServiceFactory.getUserService().isUserAdmin()) {
            throw new SecurityException("administration privileges required");
        }

        Map<Long, Long> selectionMap = user.getSelections();

        return OfyService.ofy().load().type(BraketPrediction.class)
                .ids(selectionMap.values()).values();

    }

    /*
     * (non-Javadoc)
     * 
     * @see net.exclaimindustries.paste.braket.client.SelectionService#
     * getRegisteredSelections()
     */
    @Override
    public Collection<BraketPrediction> getRegisteredSelections() {

        if (!UserServiceFactory.getUserService().isUserLoggedIn()) {
            throw new SecurityException("you are not logged in");
        }

        Ref<Tournament> tournamentRef = CurrentTournament
                .getCurrentTournament();

        if (tournamentRef == null) {
            return null;
        }

        Tournament tournament = tournamentRef.get();

        if (!UserServiceFactory.getUserService().isUserAdmin()
                && tournament.isScheduled()) {
            throw new SecurityException("administration privileges required");
        }

        return getRegisteredSelectionsUnchecked(tournament);
    }

    private Collection<BraketPrediction> getRegisteredSelectionsUnchecked(
            Tournament tournament) {
        return OfyService.ofy().load().type(BraketPrediction.class)
                .ids(tournament.getRegisteredPredictions().values()).values();
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.exclaimindustries.paste.braket.client.SelectionService#
     * getRegisteredSelection
     * (net.exclaimindustries.paste.braket.client.BraketUser)
     */
    @Override
    public BraketPrediction getRegisteredSelection(BraketUser user) {

        if (!UserServiceFactory.getUserService().isUserLoggedIn()) {
            throw new SecurityException("you are not logged in");
        }

        Ref<Tournament> tournamentRef = CurrentTournament
                .getCurrentTournament();

        if (tournamentRef == null) {
            return null;
        }

        Tournament tournament = tournamentRef.get();

        if (tournament.isScheduled()
                && !(UserServiceFactory.getUserService().isUserAdmin() || UserServiceFactory
                        .getUserService().getCurrentUser().getUserId()
                        .equals(user.getId()))) {
            throw new SecurityException("administration privileges required");
        }

        Long selectionId = user.getSelection(tournamentRef.getKey().getId());
        if (selectionId == null) {
            return null;
        }

        BraketPrediction selection = OfyService.ofy().load()
                .type(BraketPrediction.class).id(selectionId).now();

        if (selection == null || !selection.isRegistered()) {
            return null;
        }

        return selection;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * net.exclaimindustries.paste.braket.client.SelectionService#storeSelection
     * (net.exclaimindustries.paste.braket.client.BraketSelection)
     */
    @Override
    public Long storeSelection(BraketPrediction selection) {

        if (!UserServiceFactory.getUserService().getCurrentUser().getUserId()
                .equals(selection.getUserId())
                && !UserServiceFactory.getUserService().isUserAdmin()) {
            throw new SecurityException("administration privileges required");
        }

        if (selection.getUserId() == null) {
            throw new NullPointerException("user id must be set before storing");
        }

        if (selection.getTournamentId() == null) {
            throw new NullPointerException(
                    "tournament id must be set before storing");
        }

        // Check to see if the tournament has started
        Ref<Tournament> tournamentRef = CurrentTournament
                .getCurrentTournament();

        if (tournamentRef == null) {
            throw new NullPointerException("current tournament not set");
        }

        Tournament tournament = tournamentRef.get();

        if (!UserServiceFactory.getUserService().isUserAdmin()
                && !tournament.isScheduled()) {
            throw new SecurityException("administration privileges required");
        }

        return OfyService.ofy().save().entity(selection).now().getId();

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * net.exclaimindustries.paste.braket.client.SelectionService#storeSelections
     * (java.lang.Iterable)
     */
    @Override
    public void storeSelections(Iterable<BraketPrediction> selections) {

        if (!UserServiceFactory.getUserService().isUserAdmin()) {
            throw new SecurityException("administration privileges required");
        }

        for (BraketPrediction selection : selections) {
            if (selection.getUserId() == null) {
                throw new NullPointerException(
                        "user id must be set before storing");
            }

            if (selection.getTournamentId() == null) {
                throw new NullPointerException(
                        "tournament id must be set before storing");
            }
        }

        OfyService.ofy().save().entities(selections);

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * net.exclaimindustries.paste.braket.client.SelectionService#deleteSelection
     * (net.exclaimindustries.paste.braket.client.BraketSelection)
     */
    @Override
    public void deleteSelection(final BraketPrediction selection) {
        if (!UserServiceFactory.getUserService().isUserAdmin()) {
            throw new SecurityException("administration privileges required");
        }

        OfyService.ofy().transact(new VoidWork() {

            @Override
            public void vrun() {

                if (selection.isRegistered()) {
                    Tournament tournament = OfyService.ofy().load()
                            .type(Tournament.class)
                            .id(selection.getTournamentId()).now();
                    if (tournament != null) {
                        tournament.removeRegistration(selection.getUserId());
                        OfyService.ofy().save().entity(tournament);

                        BraketUser user = OfyService.ofy().load()
                                .type(BraketUser.class)
                                .id(selection.getUserId()).now();

                        if (user != null) {
                            user.removeSelection(selection.getTournamentId());
                            OfyService.ofy().save().entity(user);
                        }
                    }
                }

                OfyService.ofy().delete().entity(selection);
            }

        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * net.exclaimindustries.paste.braket.client.SelectionService#deleteSelections
     * (java.lang.Iterable)
     */
    @Override
    public void deleteSelections(final Iterable<BraketPrediction> selections) {
        if (!UserServiceFactory.getUserService().isUserAdmin()) {
            throw new SecurityException("administration privileges required");
        }

        // Nobody has the right parents, so I have to break out all the
        // transactions on a per-user basis.

        for (final BraketPrediction selection : selections) {
            OfyService.ofy().transact(new VoidWork() {

                @Override
                public void vrun() {

                    if (selection.isRegistered()) {
                        Tournament tournament = OfyService.ofy().load()
                                .type(Tournament.class)
                                .id(selection.getTournamentId()).now();
                        if (tournament != null) {
                            tournament.removeRegistration(selection.getUserId());
                            OfyService.ofy().save().entity(tournament);

                            BraketUser user = OfyService.ofy().load()
                                    .type(BraketUser.class)
                                    .id(selection.getUserId()).now();

                            if (user != null) {
                                user.removeSelection(selection
                                        .getTournamentId());
                                OfyService.ofy().save().entity(user);
                            }
                        }
                    }

                    OfyService.ofy().delete().entity(selection);
                }

            });
        }
    }

    @Override
    public BraketPrediction getSelection(final BraketUser user) {

        if (!UserServiceFactory.getUserService().isUserLoggedIn()) {
            throw new SecurityException("you are not logged in");
        }

        final Ref<Tournament> tournamentRef = CurrentTournament
                .getCurrentTournament();

        if (tournamentRef == null) {
            return null;
        }

        // All one transaction
        return OfyService.ofy().transact(new Work<BraketPrediction>() {

            @Override
            public BraketPrediction run() {

                Long selectionId = user.getSelection(tournamentRef.getKey()
                        .getId());

                // If the selection is null, make a new one
                BraketPrediction selection;
                if (selectionId == null) {
                    selection = new BraketPrediction();
                    selection.setRegistered(false);
                    selection.setTournamentId(tournamentRef.getKey().getId());
                    selection.setUserId(user.getId());
                    OfyService.ofy().save().entity(selection).now();
                    user.addSelection(tournamentRef.getKey().getId(),
                            selection.getId());
                    OfyService.ofy().save().entity(user);
                } else {
                    selection = OfyService.ofy().load()
                            .type(BraketPrediction.class).id(selectionId).now();
                }

                return selection;
            }

        });

    }
}
