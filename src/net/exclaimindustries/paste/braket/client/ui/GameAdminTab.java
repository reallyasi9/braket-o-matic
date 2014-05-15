package net.exclaimindustries.paste.braket.client.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.exclaimindustries.paste.braket.client.BraketGame;
import net.exclaimindustries.paste.braket.client.BraketTournament;
import net.exclaimindustries.paste.braket.client.GameServiceAsync;
import net.exclaimindustries.paste.braket.client.TournamentService;
import net.exclaimindustries.paste.braket.client.TournamentServiceAsync;

import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.i18n.shared.DateTimeFormat.PredefinedFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;

public class GameAdminTab extends Composite {

	private static GameAdminTabUiBinder uiBinder = GWT
			.create(GameAdminTabUiBinder.class);

	interface GameAdminTabUiBinder extends UiBinder<Widget, GameAdminTab> {
	}
//
//	@UiField(provided = true)
//	DataGrid<BraketGame> dataGrid;
//
//	private GameServiceAsync gameService = GWT.create(TournamentService.class);
//
//	private final AsyncDataProvider<BraketGame> dataProvider = new AsyncDataProvider<BraketGame>(
//			BraketGame.KEY_PROVIDER) {
//
//		private String sortCondition = "startTime";
//
//		public void setSortCondition(String condition) {
//			sortCondition = condition;
//		}
//
//		@Override
//		protected void onRangeChanged(HasData<BraketGame> display) {
//			final int offset = display.getVisibleRange().getStart();
//			int limit = display.getVisibleRange().getLength();
//			// RPC call to get more tournaments
//			gameService.getGames(sortCondition, offset, limit,
//					new AsyncCallback<List<BraketTournament>>() {
//
//						@Override
//						public void onFailure(Throwable caught) {
//							// TODO Auto-generated method stub
//
//						}
//
//						@Override
//						public void onSuccess(List<BraketTournament> result) {
//							updateRowData(offset, result);
//						}
//
//					});
//		}
//
//	};
//
//	/**
//	 * The list of pending changes.
//	 */
//	private List<PendingChange<?, ?>> pendingChanges = new ArrayList<PendingChange<?, ?>>();
//
//	public GameAdminTab() {
//
//		dataGrid = new DataGrid<BraketGame>(20, BraketGame.KEY_PROVIDER);
//
//		// Create columns
//		// Name of the tournament
//		Column<BraketTournament, String> nameColumn = new Column<BraketTournament, String>(
//				new TextCell()) {
//			@Override
//			public String getValue(BraketTournament object) {
//				return object.getName();
//			}
//		};
//		nameColumn
//				.setFieldUpdater(new FieldUpdater<BraketTournament, String>() {
//					@Override
//					public void update(int index, BraketTournament object,
//							String value) {
//						pendingChanges
//								.add(new PendingChange<BraketTournament, String>(
//										object, value) {
//									@Override
//									protected void doCommit(
//											BraketTournament cell, String value) {
//										cell.setName(value);
//									}
//								});
//					}
//				});
//		dataGrid.addColumn(nameColumn, "Name");
//
//		// Date and time the tournament begins
//		Column<BraketTournament, Date> startTimeColumn = new Column<BraketTournament, Date>(
//				new DateCell(
//						DateTimeFormat.getFormat(PredefinedFormat.RFC_2822))) {
//			@Override
//			public Date getValue(BraketTournament object) {
//				return object.getStartTime();
//			}
//		};
//		startTimeColumn
//				.setFieldUpdater(new FieldUpdater<BraketTournament, Date>() {
//					@Override
//					public void update(int index, BraketTournament object,
//							Date value) {
//						pendingChanges
//								.add(new PendingChange<BraketTournament, Date>(
//										object, value) {
//									@Override
//									protected void doCommit(
//											BraketTournament cell, Date value) {
//										cell.setStartTime(value);
//									}
//								});
//					}
//				});
//		dataGrid.addColumn(startTimeColumn, "Starting Time");
//		startTimeColumn.setSortable(true);
//
//		// Buy-in Value
//		Column<BraketTournament, Number> buyInColumn = new Column<BraketTournament, Number>(
//				new NumberCell(NumberFormat.getCurrencyFormat())) {
//			@Override
//			public Number getValue(BraketTournament object) {
//				return object.getBuyInValue();
//			}
//		};
//		buyInColumn
//				.setFieldUpdater(new FieldUpdater<BraketTournament, Number>() {
//					@Override
//					public void update(int index, BraketTournament object,
//							Number value) {
//						pendingChanges
//								.add(new PendingChange<BraketTournament, Number>(
//										object, value) {
//									@Override
//									protected void doCommit(
//											BraketTournament cell, Number value) {
//										cell.setBuyInValue((Double) value);
//									}
//								});
//					}
//				});
//		dataGrid.addColumn(buyInColumn, "Buy-In");
//
//		initWidget(uiBinder.createAndBindUi(this));
//	}
//
//	/*
//	 * The following is from the GWT CellSampler showcase
//	 * http://gwt.googleusercontent
//	 * .com/samples/Showcase/Showcase.html#!CwCellSampler
//	 */
//
//	/**
//	 * A pending change to a column. Changes aren't committed immediately to
//	 * illustrate that cells can remember their pending changes.
//	 * 
//	 * @param <DataType>
//	 *            the type stored in the table row
//	 * @param <T>
//	 *            the data type being changed
//	 */
//	private abstract static class PendingChange<DataType, T> {
//		private final DataType cell;
//		private final T value;
//
//		public PendingChange(DataType cell, T value) {
//			this.cell = cell;
//			this.value = value;
//		}
//
//		/**
//		 * Commit the change to the cell.
//		 */
//		public void commit() {
//			doCommit(cell, value);
//		}
//
//		/**
//		 * Update the appropriate field in the {@link ContactInfo}.
//		 * 
//		 * @param cell
//		 *            the cell to update
//		 * @param value
//		 *            the new value
//		 */
//		protected abstract void doCommit(DataType cell, T value);
//	}

}
