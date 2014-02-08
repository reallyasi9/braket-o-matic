package net.exclaimindustries.paste.braket.server;

import net.exclaimindustries.paste.braket.client.BraketTournament;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Load;

@Entity
public class CurrentTournament {

    /**
     * A Load Group, so that you can load the parent tournament at the same time
     * as this object is loaded.
     */
    public static class LoadTournament {
    }

    /**
     * This ID will never change, so only one BraketCurrentTournament will ever
     * exist in the datastore at a time.
     */
    @Id
    private long id = 1;

    @Load(LoadTournament.class)
    private Ref<BraketTournament> tournament = null;

    public CurrentTournament() {
        super();
    }

    public Ref<BraketTournament> getTournamentRef() {
        return tournament;
    }

    public BraketTournament getTournament() {
        return tournament.get();
    }

    public void setTournamentRef(Ref<BraketTournament> tournament) {
        this.tournament = tournament;
    }

    public void setTournament(BraketTournament tournament) {
        this.tournament = Ref.create(tournament);
    }

    public static Ref<BraketTournament> getCurrentTournament() {
        Key<CurrentTournament> key = Key.create(CurrentTournament.class, 1);
        CurrentTournament t =
                OfyService.ofy().load().group(LoadTournament.class).key(key).now();
        if (t == null) {
            return null;
        }
        return t.getTournamentRef();
    }

}
