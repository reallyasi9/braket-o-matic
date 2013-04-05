package net.exclaimindustries.paste.braket.server.backends;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.exclaimindustries.paste.braket.client.BraketGame;
import net.exclaimindustries.paste.braket.client.BraketTeam;
import net.exclaimindustries.paste.braket.client.BraketTournament;
import net.exclaimindustries.paste.braket.server.CurrentTournament;
import net.exclaimindustries.paste.braket.server.OfyService;
import net.exclaimindustries.paste.braket.server.TournamentServiceImpl;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;

import com.google.appengine.api.backends.BackendServiceFactory;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskAlreadyExistsException;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.taskqueue.TaskOptions.Method;
import com.googlecode.objectify.Ref;

public class GameDownloaderServlet extends HttpServlet {

    // A reasonable logger
    private static final Logger LOG = Logger
            .getLogger(GameDownloaderServlet.class.getName());

    private static final long serialVersionUID = 1L;
    private static final int HTTP_SERVER_ERROR = 500;
    private static final int HTTP_OK = 200;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        LOG.info("Starting cron event to download and parse games");

        // Make sure this is an admin accessing
        // UserService us = UserServiceFactory.getUserService();
        // if (!us.isUserLoggedIn() || !us.isUserAdmin()) {
        // LOG.severe("User not logged in as administrator");
        // resp.setStatus(HTTP_FORBIDDEN);
        // return;
        // }

        // Get the current tournament
        Ref<BraketTournament> currentTournRef = CurrentTournament
                .getCurrentTournament();
        if (currentTournRef == null) {
            LOG.warning("Not downloading games because no current tournament is set");
            resp.setStatus(HTTP_SERVER_ERROR);
            return;
        }

        // Get the games and teams of the tournament
        Collection<BraketGame> tournGames = OfyService.ofy().load()
                .type(BraketGame.class).parent(currentTournRef)
                .ids(currentTournRef.get().getGames()).values();

        Collection<BraketTeam> tournTeams = OfyService.ofy().load()
                .type(BraketTeam.class).parent(currentTournRef)
                .ids(currentTournRef.get().getGames()).values();

        // Index these by ESPN ID for faster lookup
        HashMap<Long, BraketGame> gameMap = new HashMap<Long, BraketGame>();
        for (BraketGame game : tournGames) {
            if (game.getEspnId() != null) {
                gameMap.put(game.getEspnId(), game);
            }
        }

        HashMap<Long, BraketTeam> teamMap = new HashMap<Long, BraketTeam>();
        for (BraketTeam team : tournTeams) {
            teamMap.put(team.getId(), team);
        }

        // Download the games
        Document feed;
        try {
            feed = GameDownloader.downloadGameFeed();
        } catch (URISyntaxException e1) {
            LOG.warning("Parsing error downloading feed");
            LOG.throwing(GameDownloaderServlet.class.getName(), "doGet", e1);
            resp.setStatus(HTTP_SERVER_ERROR);
            return;
        } catch (JDOMException e1) {
            LOG.warning("Parsing error downloading feed");
            LOG.throwing(GameDownloaderServlet.class.getName(), "doGet", e1);
            resp.setStatus(HTTP_SERVER_ERROR);
            return;
        }
        Map<Long, Element> gameElements = GameDownloader
                .parseGameElements(feed);

        // Download yesterday's feed too, just in case
        Document yesterdayFeed;
        try {
            Calendar yesterday = Calendar.getInstance();
            yesterday.add(Calendar.DATE, -1);
            yesterdayFeed = GameDownloader
                    .downloadGameFeed(yesterday.getTime());
        } catch (URISyntaxException e1) {
            LOG.warning("Parsing error downloading feed");
            LOG.throwing(GameDownloaderServlet.class.getName(), "doGet", e1);
            resp.setStatus(HTTP_SERVER_ERROR);
            return;
        } catch (JDOMException e1) {
            LOG.warning("Parsing error downloading feed");
            LOG.throwing(GameDownloaderServlet.class.getName(), "doGet", e1);
            resp.setStatus(HTTP_SERVER_ERROR);
            return;
        }
        gameElements.putAll(GameDownloader.parseGameElements(yesterdayFeed));

        boolean doResetExpecto = false;
        // Check for new winners, IDs, etc.
        for (Entry<Long, Element> gameElement : gameElements.entrySet()) {

            Long espnId = gameElement.getKey();

            BraketGame game = null; // to be filled in later
            BraketGame gameToUpdate = null;

            // See if this is a game I know already
            if (gameMap.containsKey(espnId)) {

                gameToUpdate = gameMap.get(espnId);
                // If that game is done, skip it
                if (gameToUpdate.isFinal()) {
                    continue;
                }

                // Alright, get the game
                try {
                    game = GameDownloader.buildGame(gameElement.getValue());
                } catch (ParseException e) {
                    LOG.warning("Parsing error building game with element "
                            + gameElement.getValue().toString());
                    LOG.throwing(GameDownloaderServlet.class.getName(),
                            "doGet", e);
                    continue;
                }

                // Check if the game was and still is scheduled
                if (gameToUpdate.isScheduled() && game.isScheduled()) {
                    continue;
                }

                // At this point, I know it's a game I have to update.

            } else {
                // I don't know this game yet, so see if I can figure it out.
                try {
                    game = GameDownloader.buildGame(gameElement.getValue());
                } catch (ParseException e) {
                    LOG.warning("Parsing error building game with element "
                            + gameElement.getValue().toString());
                    LOG.throwing(GameDownloaderServlet.class.getName(),
                            "doGet", e);
                    continue;
                }

                Long topTeamId = game.getTopTeamId();
                Long bottomTeamId = game.getBottomTeamId();

                for (BraketGame tournGame : tournGames) {
                    if ((topTeamId.equals(tournGame.getTopTeamId()) && bottomTeamId
                            .equals(tournGame.getBottomTeamId()))
                            || (topTeamId.equals(tournGame.getBottomTeamId()) && bottomTeamId
                                    .equals(tournGame.getTopTeamId()))) {
                        // This is the right game, so save that information
                        gameToUpdate = tournGame;
                        gameToUpdate.setEspnId(espnId);
                        break;
                    }
                }
            }

            // Now I might be ready to do the updates
            if (gameToUpdate == null) {
                // Nothing to do with this game
                continue;
            }

            // Save the ID and parent
            game.setId(gameToUpdate.getId());
            game.setTournamentKey(currentTournRef.getKey());

            // Save the index (VERY IMPORTANT!)
            game.setIndex(gameToUpdate.getIndex());

            // Check to see if I need to flop the home/away teams
            StringBuilder sb = new StringBuilder();
            sb.append("Parsed ").append(game.getTopTeamId()).append(" vs ")
                    .append(game.getBottomTeamId()).append(" with scores ")
                    .append(game.getTopScore()).append(" to ")
                    .append(game.getBottomScore());
            LOG.info(sb.toString());

            Long oldTopTeamId = gameToUpdate.getTopTeamId();
            Long newTopTeamId = game.getTopTeamId();
            Long oldBottomTeamId = gameToUpdate.getBottomTeamId();
            Long newBottomTeamId = game.getBottomTeamId();
            if (oldTopTeamId.equals(newBottomTeamId)
                    && oldBottomTeamId.equals(newTopTeamId)) {
                LOG.info("Team flop detected");
                game.setTopTeamId(oldTopTeamId);
                game.setBottomTeamId(oldBottomTeamId);
                Integer topScore = game.getTopScore();
                game.setTopScore(game.getBottomScore());
                game.setBottomScore(topScore);
                if (game.isFinal()) {
                    game.setWinner(!game.getWinner());
                }
            }

            // Do the call to make the update
            // FIXME The user has to be logged in as an administrator to do
            // this, and cron doesn't do that.
            TournamentServiceImpl tServ = new TournamentServiceImpl();
            // tServ.updateAndPropagateGame(game);
            tServ.uncheckedUpdateAndPropagateGame(currentTournRef, game);

            // Tell the Expect-O-Matic to save and start over if the game is
            // finished!
            if (game.isFinal()) {
                doResetExpecto = true;
            }

        }

        // First, tell the Expect-O-Matic to start up if it isn't already
        // running.
        String expectoTarget = BackendServiceFactory.getBackendService()
                .getBackendAddress("expect-o-matic");
        String taskName = "expectoStart-" + Long.toString(new Date().getTime());
        TaskOptions startTask = TaskOptions.Builder
                .withUrl(ExpectOMatic.EXPECTO_URL).taskName(taskName)
                .header("Host", expectoTarget).method(Method.POST);

        QueueFactory.getDefaultQueue().add(startTask);

        // Then tell it to reset if a game has ended.
        if (doResetExpecto) {
            LOG.info("Some game(s) ended: queing reset of Expect-o-Matic");
            TaskOptions task = TaskOptions.Builder
                    .withUrl(ExpectOMatic.RESETO_URL).taskName("expectoReset")
                    .header("Host", expectoTarget).method(Method.POST);

            try {
                QueueFactory.getDefaultQueue().add(task);
            } catch (TaskAlreadyExistsException e) {
                // No need to retry: the reset is already scheduled.
            }
        }

        // DONE!
        resp.setStatus(HTTP_OK);
    }
}
