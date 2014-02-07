package net.exclaimindustries.paste.braket.client;

import net.exclaimindustries.paste.braket.shared.SelectionInfo;

import com.google.gwt.user.client.rpc.IsSerializable;

public class UserRanking extends SelectionInfo implements IsSerializable {

    // The user's rank
    protected int rank;

    // The number of participants in the tournament
    protected int numberOfParticipants;

    // The number of participants with whom this user is tied
    protected int ties;

    public UserRanking() {
        super();
        rank = 0;
        numberOfParticipants = 0;
        ties = 0;
    }

    /**
     * @param info
     * @param rank
     * @param numberOfParticipants
     * @param ties
     */
    public UserRanking(SelectionInfo info, int rank,
            int numberOfParticipants, int ties) {
        super(info);
        this.rank = rank;
        this.numberOfParticipants = numberOfParticipants;
        this.ties = ties;
    }

    public int getRank() {
        return rank;
    }

    public int getNumberOfParticipants() {
        return numberOfParticipants;
    }

    public int getTies() {
        return ties;
    }

}
