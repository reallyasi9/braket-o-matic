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

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author paste
 * 
 */
public interface ExpectedValueServiceAsync {

    void getExpectedValues(AsyncCallback<Map<String, Double>> callback);

    void getExpectedValueHistories(
            AsyncCallback<Map<String, SortedMap<Date, Double>>> callback);

    void getConditionalExpectedValues(String userId,
            AsyncCallback<Map<Long, Double>> callback);

    void startExpectOMatic(AsyncCallback<Void> callback);

}
