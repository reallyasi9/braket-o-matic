package net.exclaimindustries.paste.braket.server.backends;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import net.exclaimindustries.paste.braket.client.BraketGame;

import org.jdom2.CDATA;
import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;

public final class GameDownloader {

    // A reasonable logger
    private static final Logger LOG = Logger.getLogger(GameDownloader.class
            .getName());

    // Just add the date in yyyyMMdd format!
    private static final String FEED_URL =
            "http://feeds.espn.go.com/ncb/widgets/s?c=games&a=feed&start_date=";

    // The format to add to the FEED_URL string
    private static final DateFormat yearFormat = new SimpleDateFormat(
            "yyyyMMdd");

    // The format to read from the games
    private static final DateFormat gameFormat = new SimpleDateFormat(
            "MMMM d, yyyy h:m a");

    // The format to write to the datastore
    /*
     * private final DateFormat datastoreFormat = new SimpleDateFormat(
     * "EEE, dd MMM yyyy HH:mm:ss Z");
     */

    private static final String ESPN_NAMESPACE_URI =
            "http://espn.go.com/docs/rss";
    private static final Namespace ESPN_NAMESPACE = Namespace.getNamespace(
            "espn", ESPN_NAMESPACE_URI);
    private static final String CHANNEL_NODE = "channel";
    private static final String ITEM_NODE = "item";
    private static final String CATEGORY_NODE = "category";
    private static final String CATEGORY_TEXT = "ncb";
    private static final String GAME_ID_NODE = "gameId";
    private static final String HOME_TEAM_ID_NODE = "homeTeamId";
    private static final String AWAY_TEAM_ID_NODE = "awayTeamId";
    private static final String HOME_SCORE_NODE = "homeScore";
    private static final String AWAY_SCORE_NODE = "awayScore";
    private static final String CLOCK_NODE = "clock";
    private static final String GAME_STATUS_ID_NODE = "gameStatusId";
    private static final String GAME_DATE_NODE = "gameDate";
    private static final String PERIOD_NODE = "period";
    private static final String GAME_NOTE_NODE = "gameNote";

    public static Document downloadGameFeed(Date day)
            throws URISyntaxException, MalformedURLException, JDOMException,
            IOException {
        String todayString = yearFormat.format(day);

        // Try downloading the game feed
        StringBuilder uriBuilder = new StringBuilder();
        uriBuilder.append(FEED_URL);
        uriBuilder.append(todayString);
        LOG.info("Attempting game download from " + uriBuilder.toString());
        URI uri = new URI(uriBuilder.toString());
        SAXBuilder feedParser = new SAXBuilder();
        return feedParser.build(uri.toURL());
    }

    public static Document downloadGameFeed() throws MalformedURLException,
            URISyntaxException, JDOMException, IOException {
        return downloadGameFeed(new Date());
    }

    public static Map<Long, Element> parseGameElements(Document feed) {

        Element rootNode = feed.getRootElement();

        // Make the return map now
        Map<Long, Element> parsedElements = new HashMap<Long, Element>();

        // Get the "channel"
        Element channelNode = rootNode.getChild(CHANNEL_NODE);

        // Get all the "items", which might or might not represent games
        List<Element> itemList = channelNode.getChildren(ITEM_NODE);

        for (Element item : itemList) {

            // The category I am looking for is "NCB"
            boolean ncbGame = false;
            List<Element> categoryList = item.getChildren(CATEGORY_NODE);
            for (Element category : categoryList) {
                if (category.getText().equals(CATEGORY_TEXT)) {
                    ncbGame = true;
                    break;
                }
            }

            // This was not an NCAA basketball game
            if (!ncbGame) {
                continue;
            }

            // Get the game ID
            Long gameId =
                    Long.valueOf(item
                            .getChildText(GAME_ID_NODE, ESPN_NAMESPACE));

            // This didn't have an ID?
            if (gameId == null) {
                LOG.warning("Parsed game element with no ID (?)");
                continue;
            }

            LOG.info("Parsed game element with ID " + Long.toString(gameId));

            // Add to the map
            parsedElements.put(gameId, item);
        }

        return parsedElements;
    }

    public static BraketGame buildGame(Element item) throws ParseException {

        // Get the game ID
        Long gameId =
                Long.valueOf(item.getChildText(GAME_ID_NODE, ESPN_NAMESPACE));

        // Get the home and away team IDs
        // FIXME: ESPN calls the top team the "home" team in the tournament. I
        // guess that's okay.
        Long homeId =
                Long.valueOf(item.getChildText(HOME_TEAM_ID_NODE,
                        ESPN_NAMESPACE));
        Long awayId =
                Long.valueOf(item.getChildText(AWAY_TEAM_ID_NODE,
                        ESPN_NAMESPACE));

        // I should always be able to parse these. If I can't, then something is
        // wrong.
        if (homeId == null || awayId == null) {
            return null;
        }

        // Get the start time
        String gameStartTime =
                item.getChildText(GAME_DATE_NODE, ESPN_NAMESPACE);
        Date startTime = gameFormat.parse(gameStartTime);

        // Get the game status
        Integer gameStatusId =
                Integer.valueOf(item.getChildText(GAME_STATUS_ID_NODE,
                        ESPN_NAMESPACE));

        // Get the game "location"
        String gameLocation =
                getCdataAsText(item, GAME_NOTE_NODE, ESPN_NAMESPACE);

        String gameStatus = "";
        Integer homeScore = null;
        Integer awayScore = null;
        Boolean awayWon = null;

        // If the game is in progress or final, do additional parsing
        switch (gameStatusId) {
        case (BraketGame.STATUS_SCHEDULED):
            gameStatus = "Starts " + gameStartTime;
            break;
        case (BraketGame.STATUS_IN_PROGRESS):
            String period = item.getChildText(PERIOD_NODE, ESPN_NAMESPACE);
            String timeRemaining =
                    item.getChildText(CLOCK_NODE, ESPN_NAMESPACE);
            gameStatus = timeRemaining + " " + period;

            homeScore =
                    Integer.valueOf(item.getChildText(HOME_SCORE_NODE,
                            ESPN_NAMESPACE));
            awayScore =
                    Integer.valueOf(item.getChildText(AWAY_SCORE_NODE,
                            ESPN_NAMESPACE));
            break;
        case (BraketGame.STATUS_HALFTIME):

            gameStatus = "Halftime";

            homeScore =
                    Integer.valueOf(item.getChildText(HOME_SCORE_NODE,
                            ESPN_NAMESPACE));
            awayScore =
                    Integer.valueOf(item.getChildText(AWAY_SCORE_NODE,
                            ESPN_NAMESPACE));
            break;
        case (BraketGame.STATUS_FINAL):
            gameStatus = "FINAL";
            homeScore =
                    Integer.valueOf(item.getChildText(HOME_SCORE_NODE,
                            ESPN_NAMESPACE));
            awayScore =
                    Integer.valueOf(item.getChildText(AWAY_SCORE_NODE,
                            ESPN_NAMESPACE));

            if (awayScore > homeScore) {
                awayWon = true;
            } else {
                awayWon = false;
            }
            break;
        default:
            LOG.warning("Could not parse node " + GAME_STATUS_ID_NODE
                    + " from the following XML: ");
            LOG.warning(item.toString());
            return null;
        }

        // Build the game
        BraketGame game = new BraketGame();
        game.setBottomScore(awayScore);
        game.setTopScore(homeScore);
        game.setBottomTeamId(awayId);
        game.setTopTeamId(homeId);
        game.setEspnId(gameId);
        game.setGameStatus(gameStatus);
        game.setLocation(gameLocation);
        game.setScheduledDate(startTime);
        game.setWinner(awayWon);

        // Done
        return game;

    }

    private static String getCdataAsText(Element element, String cname,
            Namespace ns) {
        List<Content> list = element.getChild(cname, ns).getContent();
        String returnString = null;
        for (Content content : list) {
            if (content instanceof CDATA) {
                returnString = ((CDATA) content).getValue();
                break;
            }
        }
        return returnString;
    }
}
