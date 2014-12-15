package net.exclaimindustries.paste.braket.client.ui;

import java.util.List;

import net.exclaimindustries.paste.braket.client.BraketTournament;
import net.exclaimindustries.paste.braket.client.TournamentService;
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
   * Scrolling pager containing list of Tournaments
   */
  @UiField
  ShowMorePagerPanel pagerPanel;

  /**
   * Form for editing tournaments
   */
  @UiField
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
      // Value can be null, so do a null check..
      if (value == null) {
        return;
      }

      sb.appendHtmlConstant("<table>");

      // Add the tournament name
      sb.appendHtmlConstant("<tr><td>");
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
              // TODO Auto-generated method stub

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

  /**
   * An instance of the constants.
   */
  private final MyConstants constants = GWT.create(MyConstants.class);

  public TournamentAdminTab() {

    // Initialize the widget
    initWidget(uiBinder.createAndBindUi(this));

    // Initial TournamentCell
    TournamentCell tournamentCell = new TournamentCell();

    // CellList with a key provider
    cellList = new CellList<BraketTournament>(tournamentCell,
        BraketTournament.KEY_PROVIDER);
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

    // dataGrid = new DataGrid<BraketTournament>(BraketTournament.KEY_PROVIDER);
    // dataGrid.setWidth("100%");
    //
    // // The header and footer are static
    // dataGrid.setAutoHeaderRefreshDisabled(true);
    // dataGrid.setAutoFooterRefreshDisabled(true);
    //
    // // Set the message to display when the table is empty.
    // dataGrid.setEmptyTableWidget(new Label(constants.myDataGridEmpty()));
    //
    // // Create columns
    // // Name of the tournament
    // Column<BraketTournament, String> nameColumn = new
    // Column<BraketTournament, String>(
    // new TextCell()) {
    // @Override
    // public String getValue(BraketTournament object) {
    // return object.getName();
    // }
    // };
    // nameColumn.setFieldUpdater(new FieldUpdater<BraketTournament, String>() {
    // @Override
    // public void update(int index, BraketTournament object, String value) {
    // pendingChanges.add(new PendingChange<BraketTournament, String>(object,
    // value) {
    // @Override
    // protected void doCommit(BraketTournament cell, String value) {
    // cell.setName(value);
    // }
    // });
    // }
    // });
    // dataGrid.addColumn(nameColumn, constants.myDataGridColumnName());
    //
    // // Date and time the tournament begins
    // Column<BraketTournament, Date> startTimeColumn = new
    // Column<BraketTournament, Date>(
    // new DateCell(DateTimeFormat.getFormat(PredefinedFormat.RFC_2822))) {
    // @Override
    // public Date getValue(BraketTournament object) {
    // return object.getStartTime();
    // }
    // };
    // startTimeColumn.setFieldUpdater(new FieldUpdater<BraketTournament,
    // Date>() {
    // @Override
    // public void update(int index, BraketTournament object, Date value) {
    // pendingChanges.add(new PendingChange<BraketTournament, Date>(object,
    // value) {
    // @Override
    // protected void doCommit(BraketTournament cell, Date value) {
    // cell.setStartTime(value);
    // }
    // });
    // }
    // });
    // dataGrid.addColumn(startTimeColumn,
    // constants.myDataGridColumnStartingTime());
    // startTimeColumn.setSortable(true);
    //
    // // Buy-in Value
    // Column<BraketTournament, Number> buyInColumn = new
    // Column<BraketTournament, Number>(
    // new NumberCell(NumberFormat.getCurrencyFormat())) {
    // @Override
    // public Number getValue(BraketTournament object) {
    // return object.getBuyInValue();
    // }
    // };
    // buyInColumn.setFieldUpdater(new FieldUpdater<BraketTournament, Number>()
    // {
    // @Override
    // public void update(int index, BraketTournament object, Number value) {
    // pendingChanges.add(new PendingChange<BraketTournament, Number>(object,
    // value) {
    // @Override
    // protected void doCommit(BraketTournament cell, Number value) {
    // cell.setBuyInValue((Double) value);
    // }
    // });
    // }
    // });
    // dataGrid.addColumn(buyInColumn, constants.myDataGridColumnBuyIn());

    // Put the initial data into the list
    dataProvider.addDataDisplay(cellList);
    pagerPanel.setDisplay(cellList);
  }

}
