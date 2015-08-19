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

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.exclaimindustries.paste.braket.client.BraketPrediction;
import net.exclaimindustries.paste.braket.client.Tournament;
import net.exclaimindustries.paste.braket.server.CurrentExpectOMatic;
import net.exclaimindustries.paste.braket.server.CurrentTournament;
import net.exclaimindustries.paste.braket.server.OfyService;
import net.exclaimindustries.paste.braket.shared.Fixture;
import net.exclaimindustries.paste.braket.shared.Team;

import com.google.appengine.api.LifecycleManager;
import com.google.appengine.api.ThreadManager;
import com.google.common.collect.Ordering;
import com.google.common.collect.TreeMultimap;
import com.googlecode.objectify.Ref;

public final class ExpectOMatic extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final static String STARTUP_URL = "/_ah/start";
    private final static String SHUTDOWN_URL = "/_ah/stop";
    public final static String EXPECTO_URL = "/start-o-matic";
    public final static String RESETO_URL = "/reset-o-matic";

    private final static int HTTP_OK = 200;
    private final static int HTTP_NOT_IMPLEMENTED = 501;
    private final static int HTTP_SERVER_ERROR = 500;
    private final static int ITERATIONS_BEFORE_PAUSING = 1000;

    /**
     * A simple logger
     */
    private static final Logger LOG = Logger.getLogger(ExpectOMatic.class.getName());

    /**
     * The Expect-o-Matic model, by Sasha
     */
    private static final double kenpomScoreDifferenceCoefficient = -9.747395;

    private static final double winPercentDifferenceCoefficient = -4.809096;

    private static final double modelConstant = -0.1154165;

    /**
     * A Semaphore to allow the game update thread to interrupt the simulation
     * thread.
     */
    private transient final Semaphore semaphore = new Semaphore(1);

    /**
     * A local cached copy of the tournament's pay-out values.
     */
    private List<Double> payOuts;

    /**
     * A local copy of the users' selections, keyed by the id user who selected
     * it.
     */
    private HashMap<String, BraketPrediction> selections =
            new HashMap<String, BraketPrediction>();

    /**
     * A local copy of the teams in the tournament, in tournament order
     */
    private List<Team> teams;

    /**
     * A set of bits representing the games left to simulate.
     */
    private BigInteger gamesNotCompleted = BigInteger.ZERO;

    /**
     * The SimulationRunner thread
     */
    private Thread simulationThread;

    /**
     * The expectoValue
     */
    private ExpectoValues expectoValues;

    /**
     * A cached copy of the tournament
     */
    private Tournament tournament;

    /**
     * @author paste
     * 
     */
    public final class SimulationRunner implements Runnable {

        // Need a good RNG
        private final Random rng = new Random();

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Runnable#run()
         */
        @Override
        public void run() {

            while (!LifecycleManager.getInstance().isShuttingDown()) {

                try {

                    // Lock my thread out to make sure the game update thread
                    // isn't clobbering my stuff.
                    semaphore.acquire();

                    // I need to know the games that are waiting on a decision.
                    BigInteger gamesWaiting = tournament.getGamesWaitingOrPlaying();

                    if (expectoValues.getIterations() % 1000 == 0) {
                        LOG.info("Iteration "
                                + Integer.toString(expectoValues.getIterations()));
                    }

                    expectoValues.inc();

                    // Make a random outcome
                    BigInteger outcome =
                            new BigInteger(tournament.getGameMask().bitLength(), rng)
                                    .and(gamesNotCompleted).or(
                                            tournament.getGameWinners().and(
                                                    tournament.getCompletionMask()));

                    // Calculate the users' scores
                    TreeMultimap<Double, String> userScores = getUserScores(outcome);

                    // Calculate the users' payouts
                    Map<String, Double> userPayOuts = getUserPayOuts(userScores);

                    // Calculate the total selection probability
                    double probability =
                            getOutcomeProbability(outcome, gamesNotCompleted);
                    expectoValues.addToProbability(probability);

                    // By definition, the zero team ID is the full expecto
                    for (String userId : userPayOuts.keySet()) {
                        double expectedPayout =
                                userPayOuts.get(userId) * probability;
                        double oldValue = 0;

                        if (expectoValues.getValueTable().contains(userId, 0l)) {
                            oldValue = expectoValues.getValueTable().get(userId, 0l);
                        }
                        expectoValues.getValueTable().put(userId, 0l,
                                expectedPayout + oldValue);

                        // Store the win probabilities for each team that
                        // has yet to play
                        for (int i = 0; i < gamesWaiting.bitLength(); ++i) {
                            if (gamesWaiting.testBit(i)) {
                                // BraketTournament outcomeT = new
                                // BraketTournament();
                                // outcomeT.setGameWinners(outcome);
                                // outcomeT.setCompletionMask(tournament.getCompletionMask().setBit(i);
                                // outcomeT.setTeams(tournament.getTeams());
                                // outcomeT.setGames(tournament.getGames());

                                double olderValue = 0;
                                Long teamId = null;
                                if (outcome.testBit(i)) {
                                    teamId =
                                            tournament.getTeam(tournament
                                                    .getBottomTeamIndex(i));
                                } else {
                                    teamId =
                                            tournament.getTeam(tournament
                                                    .getTopTeamIndex(i));
                                }
                                if (expectoValues.getValueTable().contains(userId,
                                        teamId)) {
                                    olderValue =
                                            expectoValues.getValueTable().get(
                                                    userId, teamId);
                                }
                                double conditionalP =
                                        getOutcomeProbability(outcome,
                                                gamesNotCompleted.clearBit(i));
                                expectoValues.getValueTable().put(
                                        userId,
                                        teamId,
                                        userPayOuts.get(userId) * conditionalP
                                                + olderValue);
                            }
                        }
                    }

                    // save myself every n iterations and continue
                    if (expectoValues.getIterations() % ITERATIONS_BEFORE_PAUSING == 0) {
                        // Save, but don't wipe
                        saveExpectOMatic(false);
                    }

                } catch (InterruptedException e) {

                    LOG.info("Simulation thread ITERRUPTED!");

                    // The game thread does the saving, so I can just break.

                    semaphore.release();
                    break;

                } finally {

                    // Release the thread and let the game update runner have a
                    // go at it.
                    semaphore.release();
                }

            }

            // All done!
            LOG.info("Simulation thread DONE!");
            return;

        }

        /**
         * Gets the scores for all users given a certain outcome.
         * 
         * @param thisOutcome
         *            The outcome to consider.
         * @return A multimap of values to user keys, sorted by value with the
         *         largest value at the front.
         */
        public TreeMultimap<Double, String> getUserScores(BigInteger thisOutcome) {

            TreeMultimap<Double, String> userScores =
                    TreeMultimap.create(Ordering.natural().reverse(),
                            Ordering.natural());

            // A pseudo-tournament
            Tournament t = new Tournament();
            t.setCompletionMask(tournament.getGameMask());
            t.setGameMask(tournament.getGameMask());
            t.setGameWinners(thisOutcome);
            t.setRoundValues(tournament.getRoundValues());
            t.setUpsetValue(tournament.getUpsetValue());

            for (Entry<String, BraketPrediction> entry : selections.entrySet()) {
                Double value = t.getSelectionValue(entry.getValue());
                userScores.put(value, entry.getKey());
            }

            return userScores;

        }

        /**
         * For a given map of points to sets of user keys who scored those
         * points, returns a map of user keys to the pay-out value they earned.
         * 
         * @param userScores
         *            A <code>Multimap</code> of <code>BraketUser</code> values
         *            keyed by the number of points earned.
         * @return A <code>Map</code> of payout values keyed by the user keys.
         *         Those users who did not earn any payout are not listed in the
         *         map.
         */
        public Map<String, Double> getUserPayOuts(
                TreeMultimap<Double, String> userScores) {

            Map<String, Double> payOutsByUser = new HashMap<String, Double>();

            Deque<Double> remainingPayOuts = new ArrayDeque<Double>();
            for (double payout : payOuts) {
                remainingPayOuts.add(payout);
            }

            // Already sorted, so start counting the places
            for (Double points : userScores.keySet()) {
                Collection<String> users = userScores.get(points);
                double valueForThisPosition = 0;
                int positionsPopped = 0;
                while (remainingPayOuts.size() > 0 && positionsPopped < users.size()) {
                    ++positionsPopped;
                    valueForThisPosition += remainingPayOuts.removeFirst();
                }

                // All of the users split this total prize.
                double totalPrize = valueForThisPosition / users.size();
                for (String userId : users) {
                    payOutsByUser.put(userId, totalPrize);
                }
            }

            return payOutsByUser;
        }

    }

    /**
     * Saves the EOM and stores myself to the current EOM for easier lookup
     * later.
     */
    private void saveExpectOMatic(boolean wipeAfter) {
        // Update and save myself!
        LOG.info("Saving at iteration "
                + Integer.toString(expectoValues.getIterations()));
        expectoValues.setLastUpdate(new Date());

        OfyService.ofy().save().entity(expectoValues).now();
        CurrentExpectOMatic c = new CurrentExpectOMatic();
        c.setExpectOMatic(expectoValues);
        OfyService.ofy().save().entity(c);

        // Clone myself to reset the ID
        if (wipeAfter) {
            // Re-initialize everything!
            initialize();
            // Clone myself
            expectoValues = (ExpectoValues) expectoValues.clone();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Check the request
        String requestPath = req.getServletPath();
        if (requestPath != null) {
            if (requestPath.equals(STARTUP_URL)) {

                try {
                    LOG.info("Initializing Expect-o-Matic");
                    initialize();
                } catch (Throwable e) {
                    LOG.throwing("ExpectOMatic", "doGet", e);
                    resp.setStatus(HTTP_SERVER_ERROR);
                    return;
                }

                LOG.info("Expect-o-Matic initialized");
                resp.setStatus(HTTP_OK);
                return;

            } else if (requestPath.equals(SHUTDOWN_URL)) {
                stopThreads();
                resp.setStatus(HTTP_OK);
                return;
            } else if (requestPath.endsWith(EXPECTO_URL)) {
                // Only start the threads if the tournament is doing something
                if (simulationThread == null || simulationThread.isAlive()) {
                    if (tournament != null && tournament.isOngoing()) {
                        LOG.info("Starting threads");
                        startThreads();
                    } else {
                        LOG.info("No tournament to run, ignoring request to start threads");
                    }
                } else {
                    LOG.info("Threads already running");
                }

                resp.setStatus(HTTP_OK);
                return;
            } else {
                LOG.warning("Unknown request: " + requestPath);
                resp.setStatus(HTTP_NOT_IMPLEMENTED);
                return;
            }
        } else {
            LOG.warning("No request path given");
            resp.setStatus(HTTP_NOT_IMPLEMENTED);
            return;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        /*
         * UserService us = UserServiceFactory.getUserService(); if
         * (!us.isUserLoggedIn() || !us.isUserAdmin()) {
         * Logger.getAnonymousLogger().severe(
         * "User not logged in as administrator");
         * resp.setStatus(HTTP_FORBIDDEN); return; }
         */
        String requestPath = req.getServletPath();
        LOG.info("Received signal " + requestPath);
        if (requestPath != null) {

            if (requestPath.endsWith(EXPECTO_URL)) {
                // Only start the threads if the tournament is doing something
                if (simulationThread == null || simulationThread.isAlive()) {
                    if (tournament != null && tournament.isOngoing()) {
                        LOG.info("Starting threads");
                        startThreads();
                    } else {
                        LOG.info("No tournament to run, ignoring request to start threads");
                    }
                } else {
                    LOG.info("Threads already running");
                }

                resp.setStatus(HTTP_OK);
                return;
            } else if (requestPath.endsWith(RESETO_URL)) {
                // This method call saves and resets the expect-o-matic
                LOG.info("Attempting save and reset Expect-o-Matic");
                try {
                    semaphore.acquire();

                    saveExpectOMatic(true);

                } catch (InterruptedException e) {
                    LOG.info("Expect-o-Matic save and reset was interrupted");
                } finally {
                    semaphore.release();
                }

                // Do nothing more.
                resp.setStatus(HTTP_OK);
                return;

            } else {
                LOG.warning("Unknown request: " + requestPath);
                resp.setStatus(HTTP_NOT_IMPLEMENTED);
                return;
            }
        } else {
            LOG.warning("No request path given");
            resp.setStatus(HTTP_NOT_IMPLEMENTED);
            return;
        }
    }

    /**
     * Initialize the internal workings of the ExpectOMatic
     */
    private void initialize() {

        // Get the tournament
        Ref<Tournament> tournRef = CurrentTournament.getCurrentTournament();
        if (tournRef == null) {
            tournament = null;
            return;
        }
        tournament = tournRef.get();

        // Cache the games not yet completed
        gamesNotCompleted =
                tournament.getGameMask().andNot(tournament.getCompletionMask());

        // Cache the teams
        teams =
                new ArrayList<Team>(OfyService.ofy().load()
                        .type(Team.class).parent(tournament)
                        .ids(tournament.getTeams()).values());

        new ArrayList<Fixture>(OfyService.ofy().load().type(Fixture.class)
                .parent(tournament).ids(tournament.getGames()).values());

        // Cache the registered users' selections
        Collection<BraketPrediction> nakedSelections =
                OfyService.ofy().load().type(BraketPrediction.class)
                        .ids(tournament.getRegisteredPredictions().values()).values();

        selections.clear();
        for (BraketPrediction selection : nakedSelections) {
            selections.put(selection.getUserId(), selection);
        }

        // Cache the pay out values
        payOuts = tournament.getPayOutValues();

        // Get the most recent ExpectOMatic results, if they exist
        Ref<ExpectoValues> current = CurrentExpectOMatic.getCurrentExpectOMatic();
        if (current != null) {
            expectoValues = current.get();
        } else {
            expectoValues = new ExpectoValues();
            expectoValues.setTournament(tournRef);
        }

    }

    /**
     * Begin threads.
     */
    public void startThreads() {

        // Start a game status downloader first
        /*
         * downloaderThread = ThreadManager.createBackgroundThread(new
         * GameWatchRunner()); downloaderThread.start();
         */

        // Start a simulation thread
        simulationThread =
                ThreadManager.createBackgroundThread(new SimulationRunner());
        simulationThread.start();
    }

    /**
     * Stop threads.
     */
    public void stopThreads() {
        // downloaderThread.interrupt();
        if (simulationThread != null) {
            simulationThread.interrupt();
            saveExpectOMatic(true);
        }
    }

    /**
     * Determines the probability of a given complete tournament outcome.
     * 
     * @param outcome
     *            The tournament outcome.
     * @param mask
     *            The mask indicating which bits of the outcome to use when
     *            calculating probabilities.
     * @return The probability that the masked bits occurred in the tournament
     *         outcome.
     * @throws BraketException
     *             if the tournament is poorly defined such that the winning and
     *             losing seeds can not be determined.
     */
    public double getOutcomeProbability(BigInteger outcome, BigInteger mask) {
        if (mask == null) {
            mask = gamesNotCompleted;
        }
        if (mask.bitCount() == 0) {
            return 0;
        }

        BraketPrediction outcomeAsSelection = new BraketPrediction();
        outcomeAsSelection.setSelection(outcome);
        outcomeAsSelection.setSelectionMask(tournament.getGameMask());

        double totalProbability = 1;
        int nGames = mask.bitLength();
        for (int iGame = 0; iGame < nGames; ++iGame) {
            if (mask.testBit(iGame)) {
                int first = outcomeAsSelection.getTopTeamIndex(iGame);
                int second = outcomeAsSelection.getBottomTeamIndex(iGame);
                if (outcomeAsSelection.isBottomTeamSelected(iGame)) {
                    totalProbability *=
                            getWinProbability(teams.get(first), teams.get(second));
                } else {
                    totalProbability *=
                            (1 - getWinProbability(teams.get(first),
                                    teams.get(second)));
                }
            }
        }
        return totalProbability;
    }

    public double getOutcomeProbability(BigInteger outcome) {
        return getOutcomeProbability(outcome, gamesNotCompleted);
    }

    /**
     * This is the probability that team2 wins the game.
     * 
     * @param team1
     *            The first team in the game.
     * @param team2
     *            The second team in the game.
     * @return The modeled probability that team 2 wins.
     */
    public static double getWinProbability(Team team1, Team team2) {
        // From Sasha's calculations.
        double kpDiff = team1.getKenpomScore() - team2.getKenpomScore();
        double wpDiff = team1.getWinPercentage() - team2.getWinPercentage();
        double denominator =
                fastExp(-modelConstant - kenpomScoreDifferenceCoefficient * kpDiff
                        - winPercentDifferenceCoefficient * wpDiff) + 1;
        return 1. / denominator;
    }

    /**
     * A fast way to calculate e^x.
     * 
     * @param exponent
     *            The parameter x in the function e^x.
     * @return e^(exponent), to within a few percent for small x.
     */
    public static double fastExp(double exponent) {
        // See the paper
        // "A Fast, Compact Approximation of the Exponential Function" by Nicol
        // N. Schraudolph.
        final long tmp = (long) (1512775 * exponent + 1072632447);
        return Double.longBitsToDouble(tmp << 32);
    }

}
