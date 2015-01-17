package net.exclaimindustries.paste.braket.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map.Entry;

import net.exclaimindustries.paste.braket.client.BraketPrediction;
import net.exclaimindustries.paste.braket.client.Tournament;
import net.exclaimindustries.paste.braket.client.BraketUser;
import net.exclaimindustries.paste.braket.client.UserService;

import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.VoidWork;
import com.googlecode.objectify.Work;

public class UserServiceImpl extends RemoteServiceServlet implements
        UserService {

    /**
     * Generated
     */
    private static final long serialVersionUID = 1L;

    @Override
    public Collection<BraketUser> getUsers() {
        if (!UserServiceFactory.getUserService().isUserAdmin()) {
            throw new SecurityException("administration privileges required");
        }

        return new ArrayList<BraketUser>(OfyService.ofy().load()
                .type(BraketUser.class).list());
    }

    @Override
    public Collection<BraketUser> getRegisteredUsers() {

        Ref<Tournament> tournamentRef = CurrentTournament
                .getCurrentTournament();
        if (tournamentRef == null) {
            return null;
        }
        Tournament tournament = tournamentRef.get();

        // Get the users
        Collection<String> userKeys = tournament.getUserIds();

        Collection<BraketUser> users = OfyService.ofy().load()
                .type(BraketUser.class).ids(userKeys).values();

        // If not an admin, strip the email addresses from the returned users.
        if (!UserServiceFactory.getUserService().isUserAdmin()) {
            for (BraketUser user : users) {
                user.setEmail(null);
            }
        }

        return new ArrayList<BraketUser>(users);
    }

    @Override
    public void storeUsers(Collection<BraketUser> users) {
        if (!UserServiceFactory.getUserService().isUserAdmin()) {
            throw new SecurityException("administration privileges required");
        }

        OfyService.ofy().save().entities(users);
    }

    @Override
    public String storeUser(BraketUser user) {
        if (!UserServiceFactory.getUserService().getCurrentUser().getUserId()
                .equals(user.getId())
                && !UserServiceFactory.getUserService().isUserAdmin()) {
            throw new SecurityException("administration privileges required");
        }

        return OfyService.ofy().save().entity(user).now().getString();
    }

    @Override
    public Long registerUser(final BraketUser user) {
        if (!UserServiceFactory.getUserService().isUserAdmin()) {
            throw new SecurityException("administration privileges required");
        }

        Ref<Tournament> tournamentRef = CurrentTournament
                .getCurrentTournament();
        if (tournamentRef == null) {
            throw new NullPointerException("current tournament not defined");
        }
        final Tournament tournament = tournamentRef.get();

        return OfyService.ofy().transact(new Work<Long>() {

            @Override
            public Long run() {

                // If the selection doesn't exist, make an empty one for the
                // user
                Long tournamentId = tournament.getId();

                Long selectionId;
                if (!user.getSelections().containsKey(tournamentId)) {

                    BraketPrediction selection = new BraketPrediction();
                    selection.setUserId(user.getId());
                    selection.setTournamentId(tournamentId);
                    selection.setRegistered(true);
                    selectionId = OfyService.ofy().save().entity(selection)
                            .now().getId();
                    user.addSelection(tournamentId, selectionId);
                    OfyService.ofy().save().entity(user);

                } else {

                    selectionId = user.getSelections().get(tournamentId);
                    BraketPrediction selection = OfyService.ofy().load()
                            .type(BraketPrediction.class).id(selectionId).now();
                    selection.setRegistered(true);
                    OfyService.ofy().save().entity(selection);

                }

                // Update the tournament, too
                tournament.addRegistration(user.getId(), selectionId);
                OfyService.ofy().save().entity(tournament);

                return selectionId;

            }

        });

    }

    @Override
    public void unregisterUser(final BraketUser user) {
        if (!UserServiceFactory.getUserService().isUserAdmin()) {
            throw new SecurityException("administration privileges required");
        }

        Ref<Tournament> tournamentRef = CurrentTournament
                .getCurrentTournament();
        if (tournamentRef == null) {
            throw new NullPointerException("current tournament not defined");
        }
        final Tournament tournament = tournamentRef.get();

        final Long tournamentId = tournament.getId();

        // TODO fix me: I would like this to be a transaction with tournament,
        // but for some reason I can't do that....
        if (user.getSelections().containsKey(tournamentId)) {

            OfyService.ofy().transact(new VoidWork() {

                @Override
                public void vrun() {
                    String userId = user.getId();

                    tournament.removeRegistration(userId);

                    BraketPrediction selection = OfyService.ofy().load()
                            .type(BraketPrediction.class)
                            .id(user.getSelection(tournamentId)).now();
                    selection.setRegistered(false);
                    OfyService.ofy().save().entities(selection, tournament);
                }

            });

        }

    }

    @Override
    public void deleteUsers(final Collection<BraketUser> users) {
        if (!UserServiceFactory.getUserService().isUserAdmin()) {
            throw new SecurityException("administration privileges required");
        }

        // Delete the users and all their stuff
        OfyService.ofy().transact(new VoidWork() {

            @Override
            public void vrun() {

                // Delete all the selections
                for (BraketUser user : users) {

                    String userId = user.getId();

                    for (Entry<Long, Long> entry : user.getSelections()
                            .entrySet()) {
                        Tournament tournament = OfyService.ofy().load()
                                .type(Tournament.class)
                                .id(entry.getKey()).now();
                        tournament.removeRegistration(userId);
                        OfyService.ofy().save().entity(tournament);
                    }

                    OfyService.ofy().delete().type(BraketPrediction.class)
                            .ids(user.getSelections().values());
                    OfyService.ofy().delete().entity(user);

                }
            }

        });

    }

    @Override
    public void deleteUser(final BraketUser user) {
        if (!UserServiceFactory.getUserService().isUserAdmin()) {
            throw new SecurityException("administration privileges required");
        }

        // Delete the users and all their stuff
        OfyService.ofy().transact(new VoidWork() {

            @Override
            public void vrun() {

                // Delete all the selections
                String userId = user.getId();

                for (Entry<Long, Long> entry : user.getSelections().entrySet()) {
                    Tournament tournament = OfyService.ofy().load()
                            .type(Tournament.class).id(entry.getKey())
                            .now();
                    tournament.removeRegistration(userId);
                    OfyService.ofy().save().entity(tournament);
                }

                OfyService.ofy().delete().type(BraketPrediction.class)
                        .ids(user.getSelections().values());
                OfyService.ofy().delete().entity(user);
            }

        });

    }

}
