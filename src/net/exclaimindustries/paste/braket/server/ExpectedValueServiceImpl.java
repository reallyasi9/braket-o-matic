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
package net.exclaimindustries.paste.braket.server;

import java.util.Date;
import java.util.Map;
import java.util.SortedMap;
import java.util.logging.Logger;

import net.exclaimindustries.paste.braket.client.BraketTournament;
import net.exclaimindustries.paste.braket.client.ExpectedValueService;
import net.exclaimindustries.paste.braket.server.backends.ExpectOMatic;
import net.exclaimindustries.paste.braket.server.backends.ExpectoValues;

import com.google.appengine.api.backends.BackendServiceFactory;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.taskqueue.TaskOptions.Method;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Ref;

/**
 * @author paste
 * 
 */
public class ExpectedValueServiceImpl extends RemoteServiceServlet implements
        ExpectedValueService {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = Logger
            .getLogger(ExpectedValueServiceImpl.class.getName());

    /*
     * (non-Javadoc)
     * 
     * @see net.exclaimindustries.paste.braket.client.ExpectedValueService#
     * getExpectedValues()
     */
    @Override
    public Map<String, Double> getExpectedValues() {

        // Get the current tournament
        Ref<BraketTournament> tournRef =
                CurrentTournament.getCurrentTournament();
        if (tournRef == null) {
            throw new NullPointerException("no current tournament");
        }

        UserService us = UserServiceFactory.getUserService();

        if (!us.isUserLoggedIn()) {
            throw new SecurityException(
                    "this feature is only for signed-in users");
        }

        if (tournRef.get().isScheduled() && !us.isUserAdmin()) {
            throw new SecurityException("tournament has not started yet");
        }

        Ref<ExpectoValues> current =
                CurrentExpectOMatic.getCurrentExpectOMatic();
        if (current == null) {
            // nothing yet!
            return null;
        }

        return current.get().getExpectedValues();
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.exclaimindustries.paste.braket.client.ExpectedValueService#
     * getConditionalExpectedValue(java.lang.Long, int)
     */
    @Override
    public Map<Long, Double> getConditionalExpectedValues(String userId) {

        // Get the current tournament
        Ref<BraketTournament> tournRef =
                CurrentTournament.getCurrentTournament();
        if (tournRef == null) {
            throw new NullPointerException("no current tournament");
        }

        UserService us = UserServiceFactory.getUserService();

        if (!us.isUserLoggedIn()) {
            throw new SecurityException(
                    "this feature is only for logged-in users");
        }

        if (tournRef.get().isScheduled() && !us.isUserAdmin()) {
            throw new SecurityException("tournament has not started yet");
        }

        if (!us.getCurrentUser().getUserId().equals(userId)
                && !us.isUserAdmin()) {
            throw new SecurityException("admin privileges required");
        }

        Ref<ExpectoValues> current =
                CurrentExpectOMatic.getCurrentExpectOMatic();
        if (current == null) {
            // nothing yet!
            return null;
        }

        return current.get().getExciteOMatic(userId);

    }

    /*
     * (non-Javadoc)
     * 
     * @see net.exclaimindustries.paste.braket.client.ExpectedValueService#
     * getExpectedValueHistories()
     */
    @Override
    public Map<String, SortedMap<Date, Double>> getExpectedValueHistories() {

        // Get the current tournament
        Ref<BraketTournament> tournRef =
                CurrentTournament.getCurrentTournament();
        if (tournRef == null) {
            throw new NullPointerException("no current tournament");
        }

        UserService us = UserServiceFactory.getUserService();

        if (!us.isUserLoggedIn()) {
            throw new SecurityException(
                    "this feature is only for logged-in users");
        }

        if (tournRef.get().isScheduled() && !us.isUserAdmin()) {
            throw new SecurityException("tournament has not started yet");
        }

        // FIXME Can't do this yet...
        // Need to be able to select all expectos, which requires an index
        // somewhere...
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.exclaimindustries.paste.braket.client.ExpectedValueService#
     * startExpectOMatic()
     */
    @Override
    public void startExpectOMatic() {
        // Get the current tournament
        Ref<BraketTournament> tournRef =
                CurrentTournament.getCurrentTournament();
        if (tournRef == null) {
            throw new NullPointerException("no current tournament");
        }

        UserService us = UserServiceFactory.getUserService();

        if (!us.isUserLoggedIn() || !us.isUserAdmin()) {
            throw new SecurityException(
                    "this feature is only for administrators");
        }

        // Issue the request via queue
        LOG.info("Telling Expect-o-Matic to start");
        String expectoTarget =
                BackendServiceFactory.getBackendService().getBackendAddress(
                        "expect-o-matic");
        String taskName = "expectoStart-" + Long.toString(new Date().getTime());
        TaskOptions task =
                TaskOptions.Builder.withUrl(ExpectOMatic.EXPECTO_URL)
                        .taskName(taskName).header("Host", expectoTarget)
                        .method(Method.POST);

        QueueFactory.getDefaultQueue().add(task);

    }

}
