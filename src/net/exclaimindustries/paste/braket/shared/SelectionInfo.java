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
package net.exclaimindustries.paste.braket.shared;

import java.util.Date;
import java.util.SortedMap;

import net.exclaimindustries.paste.braket.client.BraketPrediction;
import net.exclaimindustries.paste.braket.client.User;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

// This holds all the information about a given selection. It represents a
// single row in the leaderboard table.
public class SelectionInfo implements Comparable<SelectionInfo>, IsSerializable {

    // The user
    protected User user;

    // The user's selection
    protected BraketPrediction selection;

    // The user's points
    protected double points;

    // The user's possible points
    protected double pointsPossible;

    // The user's expected payout
    protected double expectedPayout;

    // The user's payout history
    protected SortedMap<Date, Double> expectedPayoutHistory;

    /**
     * The key provider that provides the unique ID of a selection.
     */
    public static final ProvidesKey<SelectionInfo> KEY_PROVIDER =
            new ProvidesKey<SelectionInfo>() {

                @Override
                public Object getKey(SelectionInfo item) {
                    return (item == null) ? null : item.user.getId();
                }
            };

    public SelectionInfo() {
        user = null;
        selection = null;
        points = 0;
        pointsPossible = 0;
        expectedPayout = 0;
        expectedPayoutHistory = null;
    }

    /**
     * @param user
     * @param selection
     * @param points
     * @param pointsPossible
     * @param expectedPayout
     * @param expectedPayoutHistory
     */
    public SelectionInfo(User user, BraketPrediction selection, double points,
            double pointsPossible, double expectedPayout,
            SortedMap<Date, Double> expectedPayoutHistory) {
        this.user = user;
        this.selection = selection;
        this.points = points;
        this.pointsPossible = pointsPossible;
        this.expectedPayout = expectedPayout;
        this.expectedPayoutHistory = expectedPayoutHistory;
    }

    public SelectionInfo(SelectionInfo info) {
        this.user = info.user;
        this.selection = info.selection;
        this.points = info.points;
        this.pointsPossible = info.pointsPossible;
        this.expectedPayout = info.expectedPayout;
        this.expectedPayoutHistory = info.expectedPayoutHistory;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(SelectionInfo o) {
        // compare by number of points, then number of points possible, then
        // expected payout
        if (o == null) {
            return -1;
        }
        int comp = Double.compare(points, o.points);
        if (comp == 0) {
            comp = Double.compare(pointsPossible, o.pointsPossible);
        }
        if (comp == 0) {
            comp = Double.compare(expectedPayout, o.expectedPayout);
        }
        return comp;
    }

    public User getUser() {
        return user;
    }

    public BraketPrediction getSelection() {
        return selection;
    }

    public double getPoints() {
        return points;
    }

    public double getPointsPossible() {
        return pointsPossible;
    }

    public double getExpectedPayout() {
        return expectedPayout;
    }

    public SortedMap<Date, Double> getExpectedPayoutHistory() {
        return expectedPayoutHistory;
    }

}