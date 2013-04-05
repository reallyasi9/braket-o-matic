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
package net.exclaimindustries.paste.braket.server.backends;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.exclaimindustries.paste.braket.client.BraketTournament;

import com.google.common.collect.HashBasedTable;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;
import com.googlecode.objectify.annotation.Serialize;

/**
 * @author paste
 * 
 */
@Entity
@Cache
public class ExpectoValues {

    @Id
    Long id = null;

    /**
     * The tournament to which this BoM belongs.
     */
    @Parent
    private Ref<BraketTournament> tournament;

    /**
     * The iteration the BoM is considering.
     */
    private int iterations = 0;

    /**
     * The sum total of the probability calculated (the normalization factor)
     */
    double sumTotalProbability = 0;

    /**
     * Store the expected payout for each user for each next-round winner. The
     * total expected value for a particular user is is a sum across the user's
     * row divided by the number of iterations so far. The excite-o-matic is
     * just the entry in the user/team cell divided by the number of iterations
     * divided by the probability that the given team wins. That probability has
     * to be calculated separately.
     */
    @Serialize
    private HashBasedTable<String, Long, Double> valueTable = HashBasedTable
            .create();

    @Override
    public Object clone() {
        ExpectoValues v = new ExpectoValues();
        v.iterations = 0;
        v.lastUpdate = new Date(lastUpdate.getTime());
        v.tournament = tournament;
        v.valueTable = HashBasedTable.create(valueTable);
        v.sumTotalProbability = 0;
        return v;
    }

    public Ref<BraketTournament> getTournament() {
        return tournament;
    }

    public void setTournament(Ref<BraketTournament> tournament) {
        this.tournament = tournament;
    }

    public void setValueTable(HashBasedTable<String, Long, Double> valueTable) {
        this.valueTable = valueTable;
    }

    public Long getId() {
        return id;
    }

    /**
     * This is the last updated time
     */
    private Date lastUpdate = null;

    public int getIterations() {
        return iterations;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public int inc() {
        return ++iterations;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Return the user's value table
     */
    public HashBasedTable<String, Long, Double> getValueTable() {
        return valueTable;
    }

    /**
     * Parse the table to determine the total expected values for each user.
     */
    public Map<String, Double> getExpectedValues() {
        HashMap<String, Double> expectedValues = new HashMap<String, Double>();
        for (String userId : valueTable.rowKeySet()) {
            if (valueTable.contains(userId, 0l)) {
                expectedValues.put(userId, valueTable.get(userId, 0l)
                        / sumTotalProbability);
            } else {
                expectedValues.put(userId, 0.);
            }
        }

        return expectedValues;
    }

    /**
     * Parse the table to determine the total expected value for a given user.
     * 
     * @param userId
     *            The key of the user to look up.
     * @return the total expected value of that user's bracket for the remaining
     *         games.
     */
    public Double getExpectedValue(String userId) {
        if (valueTable.contains(userId, 0l)) {
            return valueTable.get(userId, 0l) / sumTotalProbability;
        } else {
            return 0.;
        }
    }

    /**
     * Determine the expected values of each team winning. The key is the team
     * ID, and the value is the expected value for a user's selection given that
     * team's victory in the upcoming game.
     */
    public Map<Long, Double> getExciteOMatic(String userId) {

        // These have to be divided by the iteration count.
        HashMap<Long, Double> result =
                new HashMap<Long, Double>();
        for (Entry<Long, Double> entry : valueTable.row(userId).entrySet()) {
            result.put(entry.getKey(), entry.getValue() / sumTotalProbability);
        }
        return result;

    }

    public double getSumTotalProbability() {
        return sumTotalProbability;
    }

    public void setSumTotalProbability(double sumTotalProbability) {
        this.sumTotalProbability = sumTotalProbability;
    }
    
    public void addToProbability(double inc) {
        sumTotalProbability += inc;
    }

}
