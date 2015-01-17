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

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * @author paste
 * 
 */
@RemoteServiceRelativePath("selection")
public interface SelectionService extends RemoteService {

    /**
     * Get all the values of all the users against the current tournament.
     * 
     * @return A map of point values of every registered user's selection
     *         against the current tournament, keyed by the user's Id string.
     *         Returns null if there is no current tournament set.
     * 
     * @throws SecurityException
     *             If the user is not logged in, or if the user is not an
     *             administrator and the current tournament has not yet begun.
     */
    public Map<String, Double> getUserValues();

    /**
     * Gets all the values of all the users against a hypothetical outcome and
     * game mask.
     * 
     * @param outcome
     *            The hypothetical tournament outcome.
     * @param mask
     *            The hypothetical tournament games to consider.
     * @return A map of point values of every user's selection that is
     *         registered to the current tournament against the hypothetical
     *         selection, keyed by the user's Id string. Returns null if there
     *         is no current tournament set.
     * 
     * @throws SecurityException
     *             If the user is not logged in, or if the user is not an
     *             administrator and the current tournament has not yet begun.
     */
    public Map<String, Double> getUserValues(BigInteger outcome, BigInteger mask);

    /**
     * Calculates the value of an individual user's selection against the
     * current tournament.
     * 
     * @param user
     *            The user whose selection value should be calculated.
     * @return The value of the user's selection, or null if the user doesn't
     *         exist or does not have a selection registered to the current
     *         tournament, or if the current tournament doesn't exist.
     * 
     * @throws SecurityException
     *             If the user is not logged in, or if the user is not an
     *             administrator and the current tournament has not yet begun.
     */
    public Double getUserValue(User user);

    /**
     * Same as above, but for a hypothetical tournament.
     * 
     * @param user
     *            The user whose selection value should be calculated.
     * @param outcome
     *            The hypothetical tournament outcome.
     * @param mask
     *            The hypothetical tournament games to consider.
     * @return The value of the user's selection, or null if the user doesn't
     *         exist or does not have a selection registered to the current
     *         tournament, or if the current tournament doesn't exist.
     * 
     * @throws SecurityException
     *             If the user is not logged in, or if the user is not an
     *             administrator and the current tournament has not yet begun.
     */
    public Double getUserValue(User user, BigInteger outcome,
            BigInteger mask);

    /**
     * Gets the selection value for an arbitrary selection against the current
     * tournament.
     * 
     * @param selection
     *            The selection to calculate.
     * 
     * @return The value of the selection or null if the current tournament
     *         doesn't exist.
     * 
     * @throws SecurityException
     *             If the user is not logged in, or if the user is not an
     *             administrator and the current tournament has not yet begun.
     */
    public Double getSelectionValue(BraketPrediction selection);

    /**
     * Get the selections for a user.
     * 
     * @param user
     *            The user for whom to get the selections. From.
     * @return The selections that the user has filled out. These are not
     *         necessarily registered selections. Returns null if the user is
     *         not in the datastore.
     * 
     * @throws SecurityException
     *             If the current user is not logged in as an administrator and
     *             is attempting to get the selections of a user other than
     *             himself.
     */
    public Collection<BraketPrediction> getSelections(User user);

    /**
     * Get a selection for a user for the current tournament. Will create a new,
     * empty selection for the user for the current tournament if the selection
     * does not exist in the datastore.
     * 
     * @param user
     *            The user for whom to get the selections. From.
     * @return The selection that the user has filled out for the current
     *         tournament. This is not necessarily a registered selection.
     *         Returns null if the user is not in the datastore or the current
     *         tournament is not set.
     * 
     * @throws SecurityException
     *             If the current user is not logged in as an administrator and
     *             is attempting to get the selections of a user other than
     *             himself before the tournament has started.
     */
    public BraketPrediction getSelection(User user);

    /**
     * Get all the selections registered to the current tournament.
     * 
     * @return All the selections registered to the current tournament, or null
     *         if there is no current tournament.
     * 
     * @throws SecurityException
     *             If the user is not logged in as an administrator and is
     *             attempting to get selections before the tournament has
     *             started.
     */
    public Collection<BraketPrediction> getRegisteredSelections();

    /**
     * Get the selection from the given user that is registered to the current
     * tournament.
     * 
     * @param user
     *            The user that filled out the desired selection.
     * @return The selection that the given user filled out that is registered
     *         to the current tournament. Returns null if there is no current
     *         tournament, if the user is not found in the datastore, or if the
     *         user does not have a registered selection for the current
     *         tournament.
     * 
     * @throws SecurityException
     *             If the user is not logged in, or if the user is not an
     *             administrator and the current tournament has not yet begun.
     */
    public BraketPrediction getRegisteredSelection(User user);

    /**
     * Write or update a selection. DO NOT use this for changing whether or not
     * a selection is registered to a tournament, as edits to the isRegistered
     * field will not be properly propagated to the tournament objects. Use the
     * UserService methods instead to register the user.
     * 
     * @param selection
     *            The selection to write or update.
     * @return The Id of the (possibly new) selection.
     * @throws SecurityException
     *             If the current user is not logged in as an administrator and
     *             attempts to store or update a selection for a user other than
     *             himself.
     * @throws NullPointerException
     *             If the selection is not associated with a tournament.
     * 
     * @throws SecurityException
     *             If the user is not logged in as an administrator and the
     *             current tournament has already begun.
     */
    public Long storeSelection(BraketPrediction selection);

    /**
     * Write or update a collection of selections. DO NOT use this for changing
     * whether or not a selection is registered to a tournament, as edits to the
     * isRegistered field will not be properly propagated to the tournament
     * objects. Use the UserService methods instead to register the user.
     * 
     * @param selections
     *            The selections to write or update.
     * @throws SecurityException
     *             If the current user is not logged in as an administrator.
     * @throws NullPointerException
     *             If any of the selections is not associated with a tournament.
     */
    public void storeSelections(Iterable<BraketPrediction> selections);

    /**
     * Delete a selection. Will automatically unregister it from its tournament.
     * 
     * @param selection
     *            The selection to delete.
     * @throws SecurityException
     *             If the current user is not logged in as an administrator.
     */
    public void deleteSelection(BraketPrediction selection);

    /**
     * Delete a collection of selections. Will automatically unregister them
     * from their respective tournaments.
     * 
     * @param selections
     *            The selections to delete.
     * @throws SecurityException
     *             If the current user is not logged in as an administrator.
     */
    public void deleteSelections(Iterable<BraketPrediction> selections);
}
