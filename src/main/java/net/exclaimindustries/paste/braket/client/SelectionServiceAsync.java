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

    void getUserValue(BraketUser user, AsyncCallback<Double> callback);

    void getSelectionValue(BraketSelection selection,
            AsyncCallback<Double> callback);

    void getUserValue(BraketUser user, BigInteger outcome, BigInteger mask,
            AsyncCallback<Double> callback);

    void getUserValues(BigInteger outcome, BigInteger mask,
            AsyncCallback<Map<String, Double>> callback);

    void getSelections(BraketUser user,
            AsyncCallback<Collection<BraketSelection>> callback);

    void getRegisteredSelection(BraketUser user,
            AsyncCallback<BraketSelection> callback);

    void
            storeSelection(BraketSelection selection,
                    AsyncCallback<Long> callback);

    void storeSelections(Iterable<BraketSelection> selections,
            AsyncCallback<Void> callback);

    void
            deleteSelection(BraketSelection selection,
                    AsyncCallback<Void> callback);

    void deleteSelections(Iterable<BraketSelection> selections,
            AsyncCallback<Void> callback);

    void getRegisteredSelections(
            AsyncCallback<Collection<BraketSelection>> callback);

    void getSelection(BraketUser user, AsyncCallback<BraketSelection> callback);

}
