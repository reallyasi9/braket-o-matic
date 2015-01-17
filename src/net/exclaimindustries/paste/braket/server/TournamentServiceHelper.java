package net.exclaimindustries.paste.braket.server;

import com.googlecode.objectify.Ref;

import net.exclaimindustries.paste.braket.client.Tournament;
import net.exclaimindustries.paste.braket.shared.NoCurrentTournamentException;
import net.exclaimindustries.paste.braket.shared.TournamentNotStartedException;

public final class TournamentServiceHelper {

    public static void assertCurrent() throws NoCurrentTournamentException {
        if (CurrentTournament.getCurrentTournament() == null) {
            throw new NoCurrentTournamentException("no current tournament defined");
        }
    }

    public static void assertStarted() throws NoCurrentTournamentException,
            TournamentNotStartedException {
        Ref<Tournament> tournament = CurrentTournament.getCurrentTournament();
        if (tournament == null) {
            throw new NoCurrentTournamentException("no current tournament defined");
        }
        if (tournament.get().isScheduled()) {
            throw new TournamentNotStartedException(
                    "tournament is scheduled to begin at a future time ["
                            + tournament.get().getStartTime().toString() + "]");
        }
    }
}
