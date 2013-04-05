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
import java.util.ArrayList;
import java.util.List;

import net.exclaimindustries.paste.braket.client.TournamentService.TournamentCollection;
import net.exclaimindustries.paste.braket.client.ui.AdminDialog;
import net.exclaimindustries.paste.braket.client.ui.BracketBanner;
import net.exclaimindustries.paste.braket.client.ui.BracketContainer;
import net.exclaimindustries.paste.braket.client.ui.BracketContainer.Mode;
import net.exclaimindustries.paste.braket.client.ui.BracketCopyright;
import net.exclaimindustries.paste.braket.client.ui.BracketUserBar;
import net.exclaimindustries.paste.braket.client.ui.ChangeCurrentTournamentDialog;
import net.exclaimindustries.paste.braket.client.ui.CreateTournamentDialog;
import net.exclaimindustries.paste.braket.client.ui.DownloadTeamsDialog;
import net.exclaimindustries.paste.braket.client.ui.EditGameDialog;
import net.exclaimindustries.paste.braket.client.ui.EditRulesDialog;
import net.exclaimindustries.paste.braket.client.ui.EditTeamDialog;
import net.exclaimindustries.paste.braket.client.ui.EditTournamentDialog;
import net.exclaimindustries.paste.braket.client.ui.EditUserDialog;
import net.exclaimindustries.paste.braket.client.ui.ExceptionDialog;
import net.exclaimindustries.paste.braket.client.ui.ExciteOMatic;
import net.exclaimindustries.paste.braket.client.ui.HelpDialog;
import net.exclaimindustries.paste.braket.client.ui.Leaderboard;
import net.exclaimindustries.paste.braket.client.ui.RulesDialog;
import net.exclaimindustries.paste.braket.client.ui.Toast;
import net.exclaimindustries.paste.braket.client.ui.UserDialog;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class BraketEntryPoint implements EntryPoint, ValueChangeHandler<String> {
    /**
     * The message displayed to the user when the server cannot be reached or
     * returns an error.
     */
    // private static final String SERVER_ERROR = "An error occurred while "
    // + "attempting to contact the server. Please check your network "
    // + "connection and try again.";

    /**
     * Create a remote service proxy for logging in, made static to allow the
     * dialogs to use them at will.
     */
    public static final LoginServiceAsync loginService = GWT
            .create(LoginService.class);
    public static final TournamentServiceAsync tournaService = GWT
            .create(TournamentService.class);
    public static final TeamServiceAsync teamService = GWT
            .create(TeamService.class);
    public static final SelectionServiceAsync selectService = GWT
            .create(SelectionService.class);
    public static final GameServiceAsync gameService = GWT
            .create(GameService.class);
    public final static UserServiceAsync userService = GWT
            .create(UserService.class);
    public final static ExpectedValueServiceAsync expectoService = GWT
            .create(ExpectedValueService.class);

    /**
     * All the great little dialogs I use for options
     */
    private static final UserDialog userDialog = new UserDialog();
    private static final AdminDialog adminDialog = new AdminDialog();
    private static final RulesDialog rulesDialog = new RulesDialog();
    private static final CreateTournamentDialog createTournamentDialog =
            new CreateTournamentDialog();
    private static final ChangeCurrentTournamentDialog changeCurrentTournamentDialog =
            new ChangeCurrentTournamentDialog();
    private static final ExceptionDialog exceptionDialog =
            new ExceptionDialog();
    private static final EditTournamentDialog editCurrentTournamentDialog =
            new EditTournamentDialog();
    private static final EditRulesDialog editRulesDialog =
            new EditRulesDialog();
    private static final DownloadTeamsDialog downloadTeamsDialog =
            new DownloadTeamsDialog();
    private static final EditTeamDialog editTeamsDialog = new EditTeamDialog();
    private static final EditGameDialog editGamesDialog = new EditGameDialog();
    private static final EditUserDialog editUsersDialog = new EditUserDialog();
    private static final HelpDialog helpDialog = new HelpDialog();
    private static final Leaderboard leaderboard = new Leaderboard();
    /**
     * A bunch of other little UI elements scattered around the page
     */
    private static final BracketBanner topBanner = new BracketBanner();
    private static final BracketUserBar userBar = new BracketUserBar();
    private static final SimpleLayoutPanel mainContent =
            new SimpleLayoutPanel();

    /**
     * Toast is always handy!
     */
    private static final Toast toast = new Toast();

    /**
     * Login info for the current user
     */
    public static BraketUser thisUser = null;

    /**
     * The current tournament. This is what we're fighting for.
     */
    public static BraketTournament currTournament;
    public static List<BraketTeam> currTeams;
    public static List<BraketGame> currGames;

    /**
     * The main container. Brackety stuff goes in here.
     */
    public static BracketContainer bracketContainer;

    // private static Logger rootLogger = Logger.getLogger("");

    // History tokens
    public static class HistoryToken {
        public static final String BRAKET = "braket";
        public static final String LEADERBOARD = "leaderboard";
        public static final String EDIT_USERS = "edit-users";
        public static final String EDIT_TEAMS = "edit-teams";
        public static final String EDIT_GAMES = "edit-games";
        public static final String EXCITE_O_MATIC = "excite-o-matic";
    }

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {

        // If the application starts with no history token, redirect to a new
        // 'braket' state
        String initToken = History.getToken();
        if (initToken.length() == 0) {
            History.newItem(HistoryToken.BRAKET);
        }

        // Add history listener
        History.addValueChangeHandler(this);

        // Set up the UI
        setupMainUI();

        // At this point, attempt to automatically log in
        attemptLogin(false);

    }

    private void setupMainUI() {
        // Toast is tasty!
        RootLayoutPanel.get().add(toast);

        // Make the header and footer
        DockLayoutPanel mainLayout = new DockLayoutPanel(Unit.PX);

        // The header is a banner and a toolbar
        // The banner is a simple hyperlink
        mainLayout.addNorth(topBanner, 100);

        // The userBar is a more complex element
        mainLayout.addNorth(userBar, 62);

        // Copyright is on the bottom
        mainLayout.addSouth(new BracketCopyright(), 16);

        // The middle is a scrollpanel
        mainLayout.add(mainContent);
        mainLayout.setHeight("100%");
        RootLayoutPanel.get().add(mainLayout);
        mainContent.setHeight("100%");

    }

    private void attemptLogin(final Boolean immediate) {
        loginService.signIn(Window.Location.getHref(),
                new AsyncCallback<BraketUser>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        displayException(caught);
                    }

                    @Override
                    public void onSuccess(BraketUser result) {
                        thisUser = result;
                        if (thisUser.isSignedIn()) {
                            setupLoggedInContent();
                        } else {
                            if (immediate) {
                                // redirect!
                                Window.Location.replace(thisUser
                                        .getSignInLink());
                            } else {
                                setupLoggedOutContent();
                            }
                        }
                    }
                });
    }

    /**
     * Do the various tasks needed after successful login, like update the UI.
     */
    private void setupLoggedInContent() {

        userBar.setUser(thisUser);

        // Prithee, knave! Fetch forth the currentest of thine tournaments
        // and/or the finestest of thine wenches!
        tournaService
                .getCurrentTournament(new AsyncCallback<TournamentCollection>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        displayException(caught);
                    }

                    @Override
                    public void onSuccess(TournamentCollection result) {
                        // The currentest of thine tournaments it is!
                        currTournament = result.getTournament();
                        currGames = result.getGames();
                        currTeams = result.getTeams();

                        // Now that we've setup our listener, fire the initial
                        // history state.
                        History.fireCurrentHistoryState();

                    }
                });
    }

    /**
     * Do the various tasks needed when not logged in.
     */
    private void setupLoggedOutContent() {

        userBar.setUser(thisUser);

        // Requisition me a tournament!
        tournaService
                .getCurrentTournament(new AsyncCallback<TournamentCollection>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        displayException(caught);
                    }

                    @Override
                    public void onSuccess(TournamentCollection result) {
                        // The currentest of thine tournaments it is!
                        currTournament = result.getTournament();
                        currGames = result.getGames();
                        currTeams = result.getTeams();

                        // Now that we've setup our listener, fire the initial
                        // history state.
                        History.fireCurrentHistoryState();
                    }
                });
    }

    /**
     * Display the bracket and close everything else.
     */
    public static void doBraketDisplay(BraketSelection selection) {
        mainContent.clear();
        if (currTournament == null) {

            topBanner.setCurrentTournamentName("(no tournament active)");
        } else {
            if (bracketContainer == null) {
                bracketContainer =
                        new BracketContainer(currTournament, currTeams,
                                currGames);
                bracketContainer.setSelection(selection);
            } else {
                bracketContainer.setTournament(currTournament, currTeams,
                        currGames, selection);
            }

            topBanner.setCurrentTournamentName(currTournament.getName());

            if (selection == null) {
                bracketContainer.setMode(Mode.TOURNAMENT_STATUS);
            } else {
                if (currTournament.isScheduled())
                    bracketContainer.setMode(Mode.USER_PICKS);
                else
                    bracketContainer
                            .setMode(Mode.TOURNAMENT_STATUS_WITH_USER_PICKS);
            }

            mainContent.add(bracketContainer);

            bracketContainer.draw();
        }
    }

    public static void doBraketUpdate() {
        if (currTournament == null) {
            topBanner.setCurrentTournamentName("(no tournament active)");
        } else {
            topBanner.setCurrentTournamentName(currTournament.getName());
            bracketContainer.refetchData();
        }
    }

    /**
     * Show the user settings popup.
     */
    public static void doUserPopup() {
        if (thisUser == null || !thisUser.isSignedIn()) {
            displayToast("You need to be signed in to use this feature.");
            return;
        }
        userDialog.setUser(thisUser);
        userDialog.center();
    }

    /**
     * Display the leaderboards
     */
    public static void doLeaderboardDisplay() {
        if (currTournament == null || currTournament.isScheduled()) {
            displayToast("Leaderboards will not be visibile until the tournament begins.");
        } else if (!thisUser.isSignedIn()) {
            displayToast("You need to be signed in to use this feature.");
        } else {
            mainContent.clear();
            mainContent.add(leaderboard);
            leaderboard.reset();
        }
    }

    /**
     * Display an exception in a nice way.
     */
    public static void displayException(Throwable caught) {
        exceptionDialog.setException(caught);
        exceptionDialog.center();
    }

    /**
     * Display a toast message in a nice way.
     */
    public static void displayToast(String message) {
        toast.setText(message);
        toast.show();
    }

    /**
     * 
     */
    public static void displayExciteo() {
        // TODO Auto-generated method stub
    }

    /**
     * 
     */
    public static void doRulesPopup() {
        rulesDialog.reset();
        rulesDialog.center();
    }

    /**
     * 
     */
    public static void doAdminPopup() {
        adminDialog.center();
    }

    public static void doNewTournamentPopup() {
        createTournamentDialog.center();
    }

    public static void doChangeTournamentPopup() {
        changeCurrentTournamentDialog.updateTournamentList();
        changeCurrentTournamentDialog.center();
    }

    public static void doEditTournamentPopup() {
        editCurrentTournamentDialog.resetValues();
        editCurrentTournamentDialog.center();
    }

    public static void doEditRulesPopup() {
        editRulesDialog.reset();
        editRulesDialog.center();
    }

    public static void doDownloadTeamsPopup() {
        downloadTeamsDialog.reset();
        downloadTeamsDialog.center();
    }

    /**
     * 
     */
    public static void doEditTeams() {
        if (!thisUser.isAdmin()) {
            displayToast("This function is only available for administrators.");
        } else {
            mainContent.clear();
            mainContent.add(editTeamsDialog);
            editTeamsDialog.reset();
        }
    }

    /**
     * 
     */
    public static void doEditGames() {
        if (!thisUser.isAdmin()) {
            displayToast("This function is only available for administrators.");
        } else {
            mainContent.clear();
            mainContent.add(editGamesDialog);
            editGamesDialog.reset();
        }
    }

    /**
     * 
     */
    public static void doEditUsers() {
        if (!thisUser.isAdmin()) {
            displayToast("This function is only available for administrators.");
        } else {
            mainContent.clear();
            mainContent.add(editUsersDialog);
            editUsersDialog.reset();
        }
    }

    public static void doExciteOMatic() {
        if (!thisUser.isSignedIn()) {
            displayToast("This function is only available for signed-in users.");
        } else {
            mainContent.clear();
            BigInteger waitingGames = currTournament.getGamesWaitingOrPlaying();
            ArrayList<BraketGame> games = new ArrayList<BraketGame>();
            for (int i = 0; i < waitingGames.bitLength(); ++i) {
                if (waitingGames.testBit(i)) {
                    games.add(currGames.get(i));
                }
            }
            ExciteOMatic exciteOMatic =
                    new ExciteOMatic(thisUser, games, currTeams);
            mainContent.add(exciteOMatic);
            exciteOMatic.reset();
        }
    }

    public static void doEditUserSelection(BraketUser user) {
        selectService.getSelection(user, new AsyncCallback<BraketSelection>() {

            @Override
            public void onFailure(Throwable caught) {
                displayException(caught);
            }

            @Override
            public void onSuccess(BraketSelection result) {
                if (bracketContainer == null) {
                    bracketContainer =
                            new BracketContainer(currTournament, currTeams,
                                    currGames);
                    bracketContainer.setSelection(result);
                } else {
                    bracketContainer.setTournament(currTournament, currTeams,
                            currGames, result);
                }
                bracketContainer.setMode(BracketContainer.Mode.USER_PICKS);
                mainContent.clear();
                mainContent.add(bracketContainer);
                bracketContainer.draw();
                userBar.startEditing();
                displayToast("Click 'done editing' when finished.");
            }

        });
    }

    public static void doneEditingUser() {
        selectService.getSelection(thisUser,
                new AsyncCallback<BraketSelection>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        displayException(caught);
                    }

                    @Override
                    public void onSuccess(BraketSelection result) {
                        bracketContainer.setSelection(result);
                        if (currTournament.isScheduled()) {
                            bracketContainer
                                    .setMode(BracketContainer.Mode.USER_PICKS);
                        } else {
                            bracketContainer
                                    .setMode(BracketContainer.Mode.TOURNAMENT_STATUS_WITH_USER_PICKS);
                        }
                        bracketContainer.draw();
                    }

                });
    }

    /**
     * 
     */
    public static void doHelpPopup() {
        helpDialog.center();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.google.gwt.event.logical.shared.ValueChangeHandler#onValueChange(
     * com.google.gwt.event.logical.shared.ValueChangeEvent)
     */
    @Override
    public void onValueChange(ValueChangeEvent<String> event) {
        String eventString = event.getValue();
        if (eventString.equals(HistoryToken.BRAKET)) {
            // Finally, we need the
            // user's selection.
            if (!thisUser.isSignedIn()) {
                doBraketDisplay(null);
            } else {
                selectService.getSelection(thisUser,
                        new AsyncCallback<BraketSelection>() {

                            @Override
                            public void onFailure(Throwable caught) {
                                displayException(caught);
                            }

                            @Override
                            public void onSuccess(BraketSelection result) {
                                // Finally!
                                // Draw!
                                doBraketDisplay(result);
                            }

                        });
            }
        } else if (eventString.equals(HistoryToken.LEADERBOARD)) {
            doLeaderboardDisplay();
        } else if (eventString.equals(HistoryToken.EDIT_USERS)) {
            doEditUsers();
        } else if (eventString.equals(HistoryToken.EDIT_TEAMS)) {
            doEditTeams();
        } else if (eventString.equals(HistoryToken.EDIT_GAMES)) {
            doEditGames();
        } else if (eventString.equals(HistoryToken.EXCITE_O_MATIC)) {
            doExciteOMatic();
        }
    }
}
