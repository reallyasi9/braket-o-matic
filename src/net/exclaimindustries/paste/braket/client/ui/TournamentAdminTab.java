package net.exclaimindustries.paste.braket.client.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.exclaimindustries.paste.braket.client.BraketTournament;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.i18n.shared.DateTimeFormat.PredefinedFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class TournamentAdminTab extends Composite {

    private static TournamentAdminTabUiBinder uiBinder = GWT
            .create(TournamentAdminTabUiBinder.class);

    interface TournamentAdminTabUiBinder extends
            UiBinder<Widget, TournamentAdminTab> {
    }

    @UiField(provided = true)
    DataGrid<BraketTournament> dataGrid;

    /**
     * The list of pending changes.
     */
    private List<PendingChange<?, ?>> pendingChanges = new ArrayList<PendingChange<?, ?>>();

    public TournamentAdminTab() {

        dataGrid = new DataGrid<BraketTournament>(20,
                BraketTournament.KEY_PROVIDER);

        initWidget(uiBinder.createAndBindUi(this));

        // Create columns
        // Name of the tournament
        addColumn(new TextCell(), "Name",
                new GetValue<BraketTournament, String>() {
                    @Override
                    public String getValue(BraketTournament row) {
                        return row.getName();
                    }
                }, new FieldUpdater<BraketTournament, String>() {
                    @Override
                    public void update(int index, BraketTournament object,
                            String value) {
                        pendingChanges.add(new NameChange(object, value));
                    }
                });

        // Date and time the tournament begins
        DateTimeFormat dateFormat = DateTimeFormat
                .getFormat(PredefinedFormat.DATE_TIME_MEDIUM);
        addColumn(new DateCell(dateFormat), "Start Time",
                new GetValue<BraketTournament, Date>() {
                    @Override
                    public Date getValue(BraketTournament row) {
                        return row.getStartTime();
                    }
                }, new FieldUpdater<BraketTournament, Date>() {
                    @Override
                    public void update(int index, BraketTournament object,
                            Date value) {
                        pendingChanges.add(new DateChange(object, value));
                    }
                });
    }

    /*
     * The following is from the GWT CellSampler showcase
     * http://gwt.googleusercontent
     * .com/samples/Showcase/Showcase.html#!CwCellSampler
     */

    /**
     * Get a cell value from a record.
     * 
     * @param <DataType>
     *            the datatype stored in the table row
     * @param <C>
     *            the cell type
     */
    private static interface GetValue<DataType, C> {
        C getValue(DataType row);
    }

    /**
     * A pending change to a column. Changes aren't committed immediately to
     * illustrate that cells can remember their pending changes.
     * 
     * @param <DataType>
     *            the type stored in the table row
     * @param <T>
     *            the data type being changed
     */
    private abstract static class PendingChange<DataType, T> {
        private final DataType cell;
        private final T value;

        public PendingChange(DataType cell, T value) {
            this.cell = cell;
            this.value = value;
        }

        /**
         * Commit the change to the cell.
         */
        public void commit() {
            doCommit(cell, value);
        }

        /**
         * Update the appropriate field in the {@link ContactInfo}.
         * 
         * @param cell
         *            the cell to update
         * @param value
         *            the new value
         */
        protected abstract void doCommit(DataType cell, T value);
    }

    /**
     * Update the name of the tournament
     */
    private static class NameChange extends
            PendingChange<BraketTournament, String> {
        public NameChange(BraketTournament cell, String value) {
            super(cell, value);
        }

        @Override
        protected void doCommit(BraketTournament cell, String value) {
            cell.setName(value);
        }
    }

    /**
     * Update the start time of the tournament
     */
    private static class DateChange extends
            PendingChange<BraketTournament, Date> {
        public DateChange(BraketTournament cell, Date value) {
            super(cell, value);
        }

        @Override
        protected void doCommit(BraketTournament cell, Date value) {
            cell.setStartTime(value);
        }
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
    private <C> Column<BraketTournament, C> addColumn(Cell<C> cell,
            String headerText, final GetValue<BraketTournament, C> getter,
            FieldUpdater<BraketTournament, C> fieldUpdater) {
        Column<BraketTournament, C> column = new Column<BraketTournament, C>(
                cell) {
            @Override
            public C getValue(BraketTournament object) {
                return getter.getValue(object);
            }
        };
        column.setFieldUpdater(fieldUpdater);
        // if (cell instanceof AbstractEditableCell<?, ?>) {
        // editableCells.add((AbstractEditableCell<?, ?>) cell);
        // }
        dataGrid.addColumn(column, headerText);
        return column;
    }

}
