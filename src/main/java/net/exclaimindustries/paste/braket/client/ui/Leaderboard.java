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
package net.exclaimindustries.paste.braket.client.ui;

import java.util.Collection;
import java.util.Comparator;

import net.exclaimindustries.paste.braket.client.LeaderboardService;
import net.exclaimindustries.paste.braket.client.LeaderboardServiceAsync;
import net.exclaimindustries.paste.braket.client.UserName;
import net.exclaimindustries.paste.braket.client.resources.Resources;
import net.exclaimindustries.paste.braket.shared.SelectionInfo;

import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;

/**
 * @author paste
 * 
 */
public class Leaderboard extends SimpleLayoutPanel {

    private static LeaderboardUiBinder uiBinder = GWT
            .create(LeaderboardUiBinder.class);

    interface LeaderboardUiBinder extends UiBinder<Widget, Leaderboard> {
    }

    private static Resources res = GWT.create(Resources.class);

    static {
        res.style().ensureInjected();
    }

    private final LeaderboardServiceAsync leaderService = GWT
            .create(LeaderboardService.class);

    @UiField(provided = true)
    DataGrid<SelectionInfo> dataGrid;

    private final ListDataProvider<SelectionInfo> dataProvider =
            new ListDataProvider<SelectionInfo>();

    private NumberFormat numberFormat = NumberFormat.getFormat("0.00");

    private final TextColumn<SelectionInfo> pointsColumn;

    public Leaderboard() {
        // Make the DataGrid before binding!
        dataGrid = new DataGrid<SelectionInfo>(SelectionInfo.KEY_PROVIDER);
        // dataGrid.setWidth("600px");
        dataGrid.setMinimumTableWidth(500, Unit.PX);
        dataGrid.setHeight("100%");
        dataGrid.setAutoHeaderRefreshDisabled(true);
        dataGrid.setEmptyTableWidget(new Label("no data to show right now..."));

        // Add a ColumnSortEvent.ListHandler to connect sorting to the
        // java.util.List.
        ListHandler<SelectionInfo> columnSortHandler =
                new ListHandler<SelectionInfo>(dataProvider.getList());

        // Number (not sortable)
        TextColumn<SelectionInfo> numColumn = new TextColumn<SelectionInfo>() {

            @Override
            public String getValue(SelectionInfo object) {
                return Integer
                        .toString(dataProvider.getList().indexOf(object) + 1);
            }
        };

        // Name
        TextColumn<SelectionInfo> nameColumn = new TextColumn<SelectionInfo>() {

            @Override
            public String getValue(SelectionInfo object) {
                return object.getUser().getName().toString();
            }

        };

        columnSortHandler.setComparator(nameColumn,
                new Comparator<SelectionInfo>() {
                    public int compare(SelectionInfo o1, SelectionInfo o2) {
                        UserName n1 = o1.getUser().getName();
                        UserName n2 = o2.getUser().getName();
                        return n1.toString().compareTo(n2.toString());
                    }
                });

        pointsColumn = new TextColumn<SelectionInfo>() {

            @Override
            public String getValue(SelectionInfo object) {
                return Double.toString(object.getPoints());
            }

        };
        columnSortHandler.setComparator(pointsColumn,
                new Comparator<SelectionInfo>() {
                    public int compare(SelectionInfo o1, SelectionInfo o2) {
                        return Double.compare(o1.getPoints(), o2.getPoints());
                    }
                });

        TextColumn<SelectionInfo> possiblePointsColumn =
                new TextColumn<SelectionInfo>() {

                    @Override
                    public String getValue(SelectionInfo object) {
                        return Double.toString(object.getPointsPossible());
                    }

                };
        columnSortHandler.setComparator(possiblePointsColumn,
                new Comparator<SelectionInfo>() {
                    public int compare(SelectionInfo o1, SelectionInfo o2) {
                        return Double.compare(o1.getPointsPossible(),
                                o2.getPointsPossible());
                    }
                });

        TextColumn<SelectionInfo> expectedPayoutColumn =
                new TextColumn<SelectionInfo>() {

                    @Override
                    public String getValue(SelectionInfo object) {
                        return numberFormat.format(object.getExpectedPayout());
                    }

                };
        columnSortHandler.setComparator(expectedPayoutColumn,
                new Comparator<SelectionInfo>() {
                    public int compare(SelectionInfo o1, SelectionInfo o2) {
                        return Double.compare(o1.getExpectedPayout(),
                                o2.getExpectedPayout());
                    }
                });

        // Add columns to the grid
        dataGrid.addColumn(numColumn);
        dataGrid.setColumnWidth(numColumn, 3, Unit.EM);
        nameColumn.setSortable(true);
        dataGrid.addColumn(nameColumn, "name");
        pointsColumn.setSortable(true);
        dataGrid.addColumn(pointsColumn, "points");
        dataGrid.setColumnWidth(pointsColumn, 6, Unit.EM);
        possiblePointsColumn.setSortable(true);
        dataGrid.addColumn(possiblePointsColumn, "points possible");
        dataGrid.setColumnWidth(possiblePointsColumn, 15, Unit.EM);
        expectedPayoutColumn.setSortable(true);
        dataGrid.addColumn(expectedPayoutColumn, "Expect-O-Matic");
        dataGrid.setColumnWidth(expectedPayoutColumn, 15, Unit.EM);

        // ActionCell.
        Column<SelectionInfo, SelectionInfo> viewColumn =
                addColumn(new ActionCell<SelectionInfo>("view braket",
                        new ActionCell.Delegate<SelectionInfo>() {

                            @Override
                            public void execute(SelectionInfo object) {
//                                BraketEntryPoint.doBraketDisplay(object
//                                        .getSelection());
                            }
                        }), "", new GetValue<SelectionInfo>() {
                    @Override
                    public SelectionInfo getValue(SelectionInfo si) {
                        return si;
                    }
                }, null);
        dataGrid.setColumnWidth(viewColumn, 10, Unit.EM);

        // Attach the column sorter
        dataGrid.addColumnSortHandler(columnSortHandler);

        // Attach the data provider to the dataGrid
        dataProvider.addDataDisplay(dataGrid);

        setWidget(uiBinder.createAndBindUi(this));

    }

    // Reload the user info from the datastore and rebuild the list
    public void reset() {

        leaderService
                .getLeaderboard(new AsyncCallback<Collection<SelectionInfo>>() {

                    @Override
                    public void onFailure(Throwable caught) {
//                        BraketEntryPoint.displayException(caught);
                    }

                    @Override
                    public void onSuccess(Collection<SelectionInfo> result) {

                        dataGrid.setPageSize(result.size());
                        // I think that's it...
                        dataProvider.getList().clear();
                        dataProvider.getList().addAll(result);
                        dataProvider.refresh();

                        // Sort by points, descending
                        dataGrid.getColumnSortList().push(pointsColumn);
                        dataGrid.getColumnSortList().push(pointsColumn);
                    }

                });
    }

    // TODO Put these in an abstract object!
    /**
     * Get a cell value from a record.
     * 
     * @param <C>
     *            the cell type
     */
    private static interface GetValue<C> {
        C getValue(SelectionInfo user);
    }

    /**
     * Add a column with a header.
     * 
     * @param <C>
     *            the cell type
     * @param cell
     *            the cell used to render the column
     * @param headerText
     *            the header string
     * @param getter
     *            the value getter for the cell
     */
    private <C> Column<SelectionInfo, C> addColumn(Cell<C> cell,
            String headerText, final GetValue<C> getter,
            FieldUpdater<SelectionInfo, C> fieldUpdater) {
        Column<SelectionInfo, C> column = new Column<SelectionInfo, C>(cell) {
            @Override
            public C getValue(SelectionInfo object) {
                return getter.getValue(object);
            }
        };
        column.setFieldUpdater(fieldUpdater);

        dataGrid.addColumn(column, headerText);
        return column;
    }

}
