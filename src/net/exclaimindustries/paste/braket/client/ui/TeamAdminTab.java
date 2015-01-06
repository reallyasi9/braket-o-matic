package net.exclaimindustries.paste.braket.client.ui;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.exclaimindustries.paste.braket.client.BraketTeam;
import net.exclaimindustries.paste.braket.client.TeamService;
import net.exclaimindustries.paste.braket.client.TeamServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;

public class TeamAdminTab extends Composite {

  /**
   * UIBinder boilerplate
   */
  private static TournamentAdminTabUiBinder uiBinder = GWT
      .create(TournamentAdminTabUiBinder.class);

  /**
   * UIBinder boilerplate
   *
   */
  interface TournamentAdminTabUiBinder extends UiBinder<Widget, TeamAdminTab> {
  }

  /**
   * Logging
   */
  private Logger logger = Logger.getLogger(this.getClass().getSimpleName());

  /**
   * Service for reading and editing Team data
   */
  private TeamServiceAsync teamService = GWT.create(TeamService.class);

  // UI Fields ----------------------------------------------------------------

  @UiField(provided = true)
  DataGrid<BraketTeam> dataGrid;

  // Members ------------------------------------------------------------------

  private final AsyncDataProvider<BraketTeam> dataProvider = new AsyncDataProvider<BraketTeam>(
      BraketTeam.KEY_PROVIDER) {

    private BraketTeam.IndexName sortCondition = BraketTeam.IndexName.schoolName;

    @Override
    protected void onRangeChanged(HasData<BraketTeam> display) {
      final int offset = display.getVisibleRange().getStart();
      int limit = display.getVisibleRange().getLength();
      // RPC call to get more tournaments
      teamService.getTeams(sortCondition, offset, limit,
          new AsyncCallback<List<BraketTeam>>() {

            @Override
            public void onFailure(Throwable caught) {
              logger.log(Level.SEVERE,
                  "failed to get team list: " + caught.getLocalizedMessage());
              Toast
                  .showErrorToast("Failed to load team list: see the log for more information.");
              updateRowCount(0, true);
            }

            @Override
            public void onSuccess(List<BraketTeam> result) {
              updateRowCount(result.size(), true);
              updateRowData(offset, result);
            }

          });
    }

  };

  //
  // private final AsyncDataProvider<BraketTeam> dataProvider = new
  // AsyncDataProvider<BraketTeam>(
  // BraketTeam.KEY_PROVIDER) {
  //
  // private String sortCondition = "startTime";
  //
  // public void setSortCondition(String condition) {
  // sortCondition = condition;
  // }
  //
  // @Override
  // protected void onRangeChanged(HasData<BraketTeam> display) {
  // final int offset = display.getVisibleRange().getStart();
  // int limit = display.getVisibleRange().getLength();
  // // RPC call to get more tournaments
  // teamService.getTeams(sortCondition, offset, limit,
  // new AsyncCallback<List<BraketTeam>>() {
  //
  // @Override
  // public void onFailure(Throwable caught) {
  // // TODO Auto-generated method stub
  //
  // }
  //
  // @Override
  // public void onSuccess(List<BraketTeam> result) {
  // updateRowData(offset, result);
  // }
  //
  // });
  // }
  //
  // };
  //
  // /**
  // * The list of pending changes.
  // */
  // private List<PendingChange<?, ?>> pendingChanges = new
  // ArrayList<PendingChange<?, ?>>();
  //
  // public TeamAdminTab() {
  //
  // dataGrid = new DataGrid<BraketTournament>(20,
  // BraketTournament.KEY_PROVIDER);
  //
  // // Create columns
  // // Name of the tournament
  // Column<BraketTournament, String> nameColumn = new Column<BraketTournament,
  // String>(
  // new TextCell()) {
  // @Override
  // public String getValue(BraketTournament object) {
  // return object.getName();
  // }
  // };
  // nameColumn
  // .setFieldUpdater(new FieldUpdater<BraketTournament, String>() {
  // @Override
  // public void update(int index, BraketTournament object,
  // String value) {
  // pendingChanges
  // .add(new PendingChange<BraketTournament, String>(
  // object, value) {
  // @Override
  // protected void doCommit(
  // BraketTournament cell, String value) {
  // cell.setName(value);
  // }
  // });
  // }
  // });
  // dataGrid.addColumn(nameColumn, "Name");
  //
  // // Date and time the tournament begins
  // Column<BraketTournament, Date> startTimeColumn = new
  // Column<BraketTournament, Date>(
  // new DateCell(
  // DateTimeFormat.getFormat(PredefinedFormat.RFC_2822))) {
  // @Override
  // public Date getValue(BraketTournament object) {
  // return object.getStartTime();
  // }
  // };
  // startTimeColumn
  // .setFieldUpdater(new FieldUpdater<BraketTournament, Date>() {
  // @Override
  // public void update(int index, BraketTournament object,
  // Date value) {
  // pendingChanges
  // .add(new PendingChange<BraketTournament, Date>(
  // object, value) {
  // @Override
  // protected void doCommit(
  // BraketTournament cell, Date value) {
  // cell.setStartTime(value);
  // }
  // });
  // }
  // });
  // dataGrid.addColumn(startTimeColumn, "Starting Time");
  // startTimeColumn.setSortable(true);
  //
  // // Buy-in Value
  // Column<BraketTournament, Number> buyInColumn = new Column<BraketTournament,
  // Number>(
  // new NumberCell(NumberFormat.getCurrencyFormat())) {
  // @Override
  // public Number getValue(BraketTournament object) {
  // return object.getBuyInValue();
  // }
  // };
  // buyInColumn
  // .setFieldUpdater(new FieldUpdater<BraketTournament, Number>() {
  // @Override
  // public void update(int index, BraketTournament object,
  // Number value) {
  // pendingChanges
  // .add(new PendingChange<BraketTournament, Number>(
  // object, value) {
  // @Override
  // protected void doCommit(
  // BraketTournament cell, Number value) {
  // cell.setBuyInValue((Double) value);
  // }
  // });
  // }
  // });
  // dataGrid.addColumn(buyInColumn, "Buy-In");
  //
  // initWidget(uiBinder.createAndBindUi(this));
  // }
  //
  // /*
  // * The following is from the GWT CellSampler showcase
  // * http://gwt.googleusercontent
  // * .com/samples/Showcase/Showcase.html#!CwCellSampler
  // */
  //
  // /**
  // * A pending change to a column. Changes aren't committed immediately to
  // * illustrate that cells can remember their pending changes.
  // *
  // * @param <DataType>
  // * the type stored in the table row
  // * @param <T>
  // * the data type being changed
  // */
  // private abstract static class PendingChange<DataType, T> {
  // private final DataType cell;
  // private final T value;
  //
  // public PendingChange(DataType cell, T value) {
  // this.cell = cell;
  // this.value = value;
  // }
  //
  // /**
  // * Commit the change to the cell.
  // */
  // public void commit() {
  // doCommit(cell, value);
  // }
  //
  // /**
  // * Update the appropriate field in the {@link ContactInfo}.
  // *
  // * @param cell
  // * the cell to update
  // * @param value
  // * the new value
  // */
  // protected abstract void doCommit(DataType cell, T value);
  // }

}
