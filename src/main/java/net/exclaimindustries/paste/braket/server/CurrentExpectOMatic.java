package net.exclaimindustries.paste.braket.server;

import net.exclaimindustries.paste.braket.server.backends.ExpectoValues;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Load;

@Entity
public class CurrentExpectOMatic {

    /**
     * A Load Group, so that you can load the parent tournament at the same time
     * as this object is loaded.
     */
    public static class LoadExpectOMatic {
    }

    /**
     * This ID will never change, so only one BraketCurrentTournament will ever
     * exist in the datastore at a time.
     */
    @Id
    private long id = 1;

    @Load(LoadExpectOMatic.class)
    private Ref<ExpectoValues> expecto = null;

    public CurrentExpectOMatic() {
    }

    public Ref<ExpectoValues> getExpectOMaticRef() {
        return expecto;
    }

    public ExpectoValues getExpectOMatic() {
        return expecto.get();
    }

    public void setExpectOMaticRef(Ref<ExpectoValues> expecto) {
        this.expecto = expecto;
    }

    public void setExpectOMatic(ExpectoValues expecto) {
        this.expecto = Ref.create(expecto);
    }

    public static Ref<ExpectoValues> getCurrentExpectOMatic() {
        Key<CurrentExpectOMatic> key = Key.create(CurrentExpectOMatic.class, 1);
        Ref<CurrentExpectOMatic> t =
                OfyService.ofy().load().group(LoadExpectOMatic.class).key(key);
        if (t == null || t.get() == null) {
            return null;
        }
        return t.get().getExpectOMaticRef();
    }

}
