package net.exclaimindustries.paste.braket.client.ui;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.exclaimindustries.paste.braket.client.BraketTournament;
import net.exclaimindustries.paste.braket.client.TournamentService;
import net.exclaimindustries.paste.braket.client.TournamentService.TournamentCollection;
import net.exclaimindustries.paste.braket.client.TournamentServiceAsync;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Constants;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.HasKeyboardPagingPolicy.KeyboardPagingPolicy;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

public class TournamentAdminTab extends Composite {

  /**
   * UIBinder boilerplate
   */
  interface TournamentAdminTabUiBinder extends
      UiBinder<Widget, TournamentAdminTab> {
  }

  /**
   * UIBinder boilerplate
   */
  private static TournamentAdminTabUiBinder uiBinder = GWT
      .create(TournamentAdminTabUiBinder.class);

  /**
   * Logging
   */
  private Logger logger = Logger.getLogger(this.getClass().getSimpleName());

  /**
   * Scrolling pager containing list of Tournaments
   */
  @UiField
  ShowMorePagerPanel pagerPanel;

  /**
   * Form for editing tournaments
   */
  @UiField(provided = true)
  TournamentInfoPanel tournamentInfoPanel;

  /**
   * Container for Tournament data
   */
  private CellList<BraketTournament> cellList;

  /**
   * The Cell used to render a {@link BraketTournament}.
   */
  static class TournamentCell extends AbstractCell<BraketTournament> {

    public TournamentCell() {
    }

    @Override
    public void render(Context context, BraketTournament value,
        SafeHtmlBuilder sb) {
      // Value can be null, so do a null check.
      if (value == null) {
        return;
      }

      sb.appendHtmlConstant("<table>");

      // Add whether or not this is the current tournament
      sb.appendHtmlConstant("<tr><td rowspan='2'>");
      if (currentTournament != null
          && value.getId() == currentTournament.getId()) {
        sb.appendHtmlConstant("***");
      }
      sb.appendHtmlConstant("</td>");

      // Add the tournament name
      sb.appendHtmlConstant("<td>");
      sb.appendEscaped(value.getName());

      // Add the start time
      sb.appendHtmlConstant("</td></tr><tr><td>");
      sb.appendEscaped(DateTimeFormat.getFormat("yyyy.MM.dd").format(
          value.getStartTime()));
      sb.appendHtmlConstant("</td></tr></table>");
    }
  }

  /**
   * Service for reading and editing Tournament data
   */
  private TournamentServiceAsync tournamentService = GWT
      .create(TournamentService.class);

  /**
   * Current tournament
   */
  static private BraketTournament currentTournament;

  /**
   * Provider for BraketTournaments to handle sort requests, offsets, and the
   * like.
   */
  private final AsyncDataProvider<BraketTournament> dataProvider = new AsyncDataProvider<BraketTournament>(
      BraketTournament.KEY_PROVIDER) {

    private BraketTournament.IndexName sortCondition = BraketTournament.IndexName
        .valueOf("startTime");

    @Override
    protected void onRangeChanged(HasData<BraketTournament> display) {
      final int offset = display.getVisibleRange().getStart();
      int limit = display.getVisibleRange().getLength();
      // RPC call to get more tournaments
      tournamentService.getTournaments(sortCondition, offset, limit,
          new AsyncCallback<List<BraketTournament>>() {

            @Override
            public void onFailure(Throwable caught) {
              logger.log(Level.SEVERE, "failed to get tournament list: "
                  + caught.getLocalizedMessage());
              Toast
                  .showErrorToast("Failed to load tournament list: see the log for more information.");
              updateRowCount(0, true);
            }

            @Override
            public void onSuccess(List<BraketTournament> result) {
              updateRowCount(result.size(), true);
              updateRowData(offset, result);
            }

          });
    }

  };

  /**
   * The constants used in this Content Widget.
   */
  public static interface MyConstants extends Constants {

    @DefaultStringValue("Name")
    String myDataGridColumnName();

    @DefaultStringValue("Starting Time")
    String myDataGridColumnStartingTime();

    @DefaultStringValue("Buy-In")
    String myDataGridColumnBuyIn();

    @DefaultStringValue("There are no Tournaments defined")
    String myDataGridEmpty();
  }

  // Callbacks ////////////////////////////////////////////////////////////////

  /**
   * An instance of the constants.
   */
  // private final MyConstants constants = GWT.create(MyConstants.class);

  public TournamentAdminTab() {

    // Initial TournamentCell
    TournamentCell tournamentCell = new TournamentCell();

    // CellList with a key provider
    cellList = new CellList<BraketTournament>(tournamentCell,
        BraketTournament.KEY_PROVIDER);
    tournamentInfoPanel = new TournamentInfoPanel(cellList);

    // Initialize the widget
    initWidget(uiBinder.createAndBindUi(this));

    tournamentService
        .getCurrentTournament(new AsyncCallback<TournamentCollection>() {

          @Override
          public void onFailure(Throwable caught) {
            logger.log(Level.SEVERE, "failed to get current tournament: "
                + caught.getLocalizedMessage());
            Toast
                .showErrorToast("Failed to get current tournament: see the log for more information.");
            currentTournament = null;
          }

          @Override
          public void onSuccess(TournamentCollection result) {
            currentTournament = result.getTournament();
          }

        });

    cellList.setPageSize(20);

    // Allow keyboard navigation
    cellList.setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);
    cellList
        .setKeyboardSelectionPolicy(KeyboardSelectionPolicy.BOUND_TO_SELECTION);

    // Add a selection model so we can select cells.
    final SingleSelectionModel<BraketTournament> selectionModel = new SingleSelectionModel<BraketTournament>(
        BraketTournament.KEY_PROVIDER);
    cellList.setSelectionModel(selectionModel);
    selectionModel
        .addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
          public void onSelectionChange(SelectionChangeEvent event) {
            tournamentInfoPanel.setSelectedTournament(selectionModel
                .getSelectedObject());
          }
        });

    // Put the initial data into the list
    dataProvider.addDataDisplay(cellList);
    pagerPanel.setDisplay(cellList);
  }
}
