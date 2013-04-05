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
package net.exclaimindustries.paste.braket.client;

import java.util.Date;
import java.util.Map;
import java.util.SortedMap;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * @author paste
 * 
 */
@RemoteServiceRelativePath("expect_o_matic")
public interface ExpectedValueService extends RemoteService {

    /**
     * Calculates and returns the expected values for all users. The value is
     * calculated using the most recent <code>ExpectOMatic</code> results.
     * 
     * @return Expected earnings, as a double, keyed by <code>BraketUser</code>
     *         Id. Users without a value (because their selection does not
     *         produce any expected value with the simulations so far) will not
     *         be included in the map.
     */
    public Map<String, Double> getExpectedValues();

    /**
     * Calculates the expected earnings for the two possible results of a given
     * game.
     * 
     * @param userId
     *            The Id of the user to look up.
     * @return A map of values with keys of team IDs and values representing the
     *         expected value of the user's selection for the current tournament
     *         assuming that team won its upcoming game.
     */
    public Map<Long, Double> getConditionalExpectedValues(String userId);

    /**
     * Get the history of expected values for all users.
     * 
     * @return A map of maps. The outer map is keyed by user Id. The inner map
     *         is keyed by date and the value represents the simulated expected
     *         value for the user at that point in time. If a user does not have
     *         a history of expected values, the user's Id will not appear in
     *         the outer map. Not all users will have values for all dates. The
     *         expected values are updated every time a game is completed. The
     *         keys of the second map are sorted in chronological order.
     */
    public Map<String, SortedMap<Date, Double>> getExpectedValueHistories();

    /**
     * Signal the Expect-o-Matic thread to begin.
     * 
     * @throw SecurityException If the user is not logged in as an
     *        administrator.
     */
    public void startExpectOMatic();

}
