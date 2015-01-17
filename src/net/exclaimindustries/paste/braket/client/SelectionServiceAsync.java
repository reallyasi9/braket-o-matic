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

import java.math.BigInteger;
import java.util.Collection;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author paste
 * 
 */
public interface SelectionServiceAsync {

    void getUserValues(AsyncCallback<Map<String, Double>> callback);

    void getUserValue(User user, AsyncCallback<Double> callback);

    void getSelectionValue(BraketPrediction selection,
            AsyncCallback<Double> callback);

    void getUserValue(User user, BigInteger outcome, BigInteger mask,
            AsyncCallback<Double> callback);

    void getUserValues(BigInteger outcome, BigInteger mask,
            AsyncCallback<Map<String, Double>> callback);

    void getSelections(User user,
            AsyncCallback<Collection<BraketPrediction>> callback);

    void getRegisteredSelection(User user,
            AsyncCallback<BraketPrediction> callback);

    void
            storeSelection(BraketPrediction selection,
                    AsyncCallback<Long> callback);

    void storeSelections(Iterable<BraketPrediction> selections,
            AsyncCallback<Void> callback);

    void
            deleteSelection(BraketPrediction selection,
                    AsyncCallback<Void> callback);

    void deleteSelections(Iterable<BraketPrediction> selections,
            AsyncCallback<Void> callback);

    void getRegisteredSelections(
            AsyncCallback<Collection<BraketPrediction>> callback);

    void getSelection(User user, AsyncCallback<BraketPrediction> callback);

}
