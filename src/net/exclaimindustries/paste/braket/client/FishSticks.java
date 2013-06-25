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

import com.google.gwt.user.client.rpc.IsSerializable;
import com.googlecode.objectify.annotation.Embed;

@Embed
final public class FishSticks implements IsSerializable {

    private int dollars;

    private int cents;

    public FishSticks(int dollars, int cents) throws IllegalArgumentException {
        if (cents > 99 || cents < 0) {
            throw new IllegalArgumentException(
                    "cents argument must be between 0 and 99, "
                            + Integer.toString(cents) + " given");
        }
        this.dollars = dollars;
        this.cents = cents;
    }

    public FishSticks() {
        dollars = 0;
        cents = 0;
    }

    public FishSticks(double dollarsAndCents) {
        dollars = (int) dollarsAndCents;
        cents = (int) ((dollarsAndCents - dollars) * 100.);
    }

    public double toDouble() {
        return (double) dollars + (double) cents / 100.;
    }

    public float toFloat() {
        return (float) ((float) dollars + (float) cents / 100.);
    }

    public int toCents() {
        return dollars * 100 + cents;
    }

    public String toString() {
        // Hack, because GWT does not like String.format.
        StringBuilder sb = new StringBuilder();
        sb.append(dollars).append(".");
        if (cents < 10) {
            sb.append("0");
        }
        sb.append(cents);
        return sb.toString();
    }
}