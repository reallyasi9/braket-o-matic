package net.exclaimindustries.paste.braket.client.ui;

import java.util.Comparator;

import net.exclaimindustries.paste.braket.client.BraketTeam;
import net.exclaimindustries.paste.braket.client.TeamName;
import net.exclaimindustries.paste.braket.client.resources.Resources;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;

public class EditTeamDialog extends SimpleLayoutPanel {

    private static EditTeamDialogUiBinder uiBinder = GWT
            .create(EditTeamDialogUiBinder.class);

    interface EditTeamDialogUiBinder extends UiBinder<Widget, EditTeamDialog> {
    }

    private final static Resources res = GWT.create(Resources.class);

    static {
        res.style().ensureInjected();
    }

    // TODO make this async
    private final ListDataProvider<BraketTeam> dataProvider =
            new ListDataProvider<BraketTeam>();

    @UiField(provided = true)
    DataGrid<BraketTeam> dataGrid;

    public EditTeamDialog() {
        // Make the DataGrid before binding!
        dataGrid = new DataGrid<BraketTeam>(25);
        // dataGrid.setWidth("600px");
        dataGrid.setMinimumTableWidth(500, Unit.PX);
        dataGrid.setHeight("100%");
        dataGrid.setAutoHeaderRefreshDisabled(true);
        dataGrid.setEmptyTableWidget(new Label("no data to show right now..."));

        // Add a ColumnSortEvent.ListHandler to connect sorting to the
        // java.util.List.
        ListHandler<BraketTeam> columnSortHandler =
                new ListHandler<BraketTeam>(dataProvider.getList());

        // Index
        Column<BraketTeam, String> indexColumn =
                addColumn(new EditTextCell(), "index", new GetValue<String>() {

                    @Override
                    public String getValue(BraketTeam team) {
                        return Integer.toString(team.getIndex());
                    }

                }, new FieldUpdater<BraketTeam, String>() {

                    @Override
                    public void update(int index, BraketTeam object, String value) {
                        object.setIndex(Integer.valueOf(value));
                        doTeamUpdate(index, object);
                    }
                });
        columnSortHandler.setComparator(indexColumn, new Comparator<BraketTeam>() {
            public int compare(BraketTeam o1, BraketTeam o2) {
                return Integer.valueOf(o1.getIndex()).compareTo(o2.getIndex());
            }
        });
        indexColumn.setSortable(true);
        dataGrid.setColumnWidth(indexColumn, 5, Unit.EM);

        // Seed
        Column<BraketTeam, String> seedColumn =
                addColumn(new EditTextCell(), "seed", new GetValue<String>() {

                    @Override
                    public String getValue(BraketTeam team) {
                        return Integer.toString(team.getSeed());
                    }

                }, new FieldUpdater<BraketTeam, String>() {

                    @Override
                    public void update(int index, BraketTeam object, String value) {
                        object.setSeed(Integer.valueOf(value));
                        doTeamUpdate(index, object);
                    }
                });
        columnSortHandler.setComparator(seedColumn, new Comparator<BraketTeam>() {
            public int compare(BraketTeam o1, BraketTeam o2) {
                return Integer.valueOf(o1.getSeed()).compareTo(o2.getSeed());
            }
        });
        seedColumn.setSortable(true);
        dataGrid.setColumnWidth(seedColumn, 5, Unit.EM);

        // School name
        Column<BraketTeam, String> schoolNameColumn =
                addColumn(new EditTextCell(), "school name", new GetValue<String>() {

                    @Override
                    public String getValue(BraketTeam team) {
                        return team.getName().getSchoolName();
                    }

                }, new FieldUpdater<BraketTeam, String>() {

                    @Override
                    public void update(int index, BraketTeam object, String value) {
                        TeamName oldName = object.getName();
                        object.setName(new TeamName(value, oldName.getTeamName(),
                                oldName.getDisplayName(), oldName.getShortName(),
                                oldName.getAbbreviation()));
                        doTeamUpdate(index, object);
                    }
                });
        columnSortHandler.setComparator(schoolNameColumn,
                new Comparator<BraketTeam>() {
                    public int compare(BraketTeam o1, BraketTeam o2) {
                        return o1.getName().getSchoolName()
                                .compareTo(o2.getName().getSchoolName());
                    }
                });
        schoolNameColumn.setSortable(true);
        dataGrid.setColumnWidth(schoolNameColumn, 12, Unit.EM);

        // Team name
        Column<BraketTeam, String> teamNameColumn =
                addColumn(new EditTextCell(), "team name", new GetValue<String>() {

                    @Override
                    public String getValue(BraketTeam team) {
                        return team.getName().getTeamName();
                    }

                }, new FieldUpdater<BraketTeam, String>() {

                    @Override
                    public void update(int index, BraketTeam object, String value) {
                        TeamName oldName = object.getName();
                        object.setName(new TeamName(oldName.getSchoolName(), value,
                                oldName.getDisplayName(), oldName.getShortName(),
                                oldName.getAbbreviation()));
                        doTeamUpdate(index, object);
                    }
                });
        columnSortHandler.setComparator(teamNameColumn,
                new Comparator<BraketTeam>() {
                    public int compare(BraketTeam o1, BraketTeam o2) {
                        return o1.getName().getTeamName()
                                .compareTo(o2.getName().getTeamName());
                    }
                });
        teamNameColumn.setSortable(true);
        dataGrid.setColumnWidth(teamNameColumn, 12, Unit.EM);

        // Display name
        Column<BraketTeam, String> displayNameColumn =
                addColumn(new EditTextCell(), "display name",
                        new GetValue<String>() {

                            @Override
                            public String getValue(BraketTeam team) {
                                return team.getName().getDisplayName();
                            }

                        }, new FieldUpdater<BraketTeam, String>() {

                            @Override
                            public void update(int index, BraketTeam object,
                                    String value) {
                                TeamName oldName = object.getName();
                                object.setName(new TeamName(oldName.getSchoolName(),
                                        oldName.getTeamName(), value, oldName
                                                .getShortName(), oldName
                                                .getAbbreviation()));
                                doTeamUpdate(index, object);
                            }
                        });
        columnSortHandler.setComparator(displayNameColumn,
                new Comparator<BraketTeam>() {
                    public int compare(BraketTeam o1, BraketTeam o2) {
                        return o1.getName().getDisplayName()
                                .compareTo(o2.getName().getDisplayName());
                    }
                });
        dataGrid.setColumnWidth(displayNameColumn, 24, Unit.EM);
        displayNameColumn.setSortable(true);

        // Short name
        Column<BraketTeam, String> shortNameColumn =
                addColumn(new EditTextCell(), "short name", new GetValue<String>() {

                    @Override
                    public String getValue(BraketTeam team) {
                        return team.getName().getShortName();
                    }

                }, new FieldUpdater<BraketTeam, String>() {

                    @Override
                    public void update(int index, BraketTeam object, String value) {
                        TeamName oldName = object.getName();
                        object.setName(new TeamName(oldName.getSchoolName(), oldName
                                .getTeamName(), oldName.getDisplayName(), value,
                                oldName.getAbbreviation()));
                        doTeamUpdate(index, object);
                    }
                });
        columnSortHandler.setComparator(shortNameColumn,
                new Comparator<BraketTeam>() {
                    public int compare(BraketTeam o1, BraketTeam o2) {
                        return o1.getName().getShortName()
                                .compareTo(o2.getName().getShortName());
                    }
                });
        shortNameColumn.setSortable(true);
        dataGrid.setColumnWidth(shortNameColumn, 15, Unit.EM);

        // Abbreviation name
        Column<BraketTeam, String> abbreviationColumn =
                addColumn(new EditTextCell(), "abbrv.", new GetValue<String>() {

                    @Override
                    public String getValue(BraketTeam team) {
                        return team.getName().getAbbreviation();
                    }

                }, new FieldUpdater<BraketTeam, String>() {

                    @Override
                    public void update(int index, BraketTeam object, String value) {
                        TeamName oldName = object.getName();
                        object.setName(new TeamName(oldName.getSchoolName(), oldName
                                .getTeamName(), oldName.getDisplayName(), oldName
                                .getShortName(), value));
                        doTeamUpdate(index, object);
                    }
                });
        columnSortHandler.setComparator(abbreviationColumn,
                new Comparator<BraketTeam>() {
                    public int compare(BraketTeam o1, BraketTeam o2) {
                        return o1.getName().getAbbreviation()
                                .compareTo(o2.getName().getAbbreviation());
                    }
                });
        abbreviationColumn.setSortable(true);
        dataGrid.setColumnWidth(abbreviationColumn, 5, Unit.EM);

        // Kenpom Pyth
        Column<BraketTeam, String> pythColumn =
                addColumn(new EditTextCell(), "Kenpom Pyth.",
                        new GetValue<String>() {

                            @Override
                            public String getValue(BraketTeam team) {
                                return Double.toString(team.getKenpomScore());
                            }

                        }, new FieldUpdater<BraketTeam, String>() {

                            @Override
                            public void update(int index, BraketTeam object,
                                    String value) {
                                object.setKenpomScore(Double.valueOf(value));
                                doTeamUpdate(index, object);
                            }
                        });
        columnSortHandler.setComparator(pythColumn, new Comparator<BraketTeam>() {
            public int compare(BraketTeam o1, BraketTeam o2) {
                return Double.compare(o1.getKenpomScore(), o2.getKenpomScore());
            }
        });
        pythColumn.setSortable(true);
        dataGrid.setColumnWidth(pythColumn, 5, Unit.EM);

        // Win pct
        Column<BraketTeam, String> winColumn =
                addColumn(new EditTextCell(), "Win Pct.", new GetValue<String>() {

                    @Override
                    public String getValue(BraketTeam team) {
                        return Double.toString(team.getWinPercentage());
                    }

                }, new FieldUpdater<BraketTeam, String>() {

                    @Override
                    public void update(int index, BraketTeam object, String value) {
                        object.setWinPercentage(Double.valueOf(value));
                        doTeamUpdate(index, object);
                    }
                });
        columnSortHandler.setComparator(winColumn, new Comparator<BraketTeam>() {
            public int compare(BraketTeam o1, BraketTeam o2) {
                return Double.compare(o1.getWinPercentage(), o2.getWinPercentage());
            }
        });
        winColumn.setSortable(true);
        dataGrid.setColumnWidth(winColumn, 5, Unit.EM);

        dataGrid.addColumnSortHandler(columnSortHandler);

        // Attach the data provider to the dataGrid
        dataProvider.addDataDisplay(dataGrid);

        setWidget(uiBinder.createAndBindUi(this));
    }

    public void reset() {

        // Do not trust pusher robot.
        // BraketEntryPoint.teamService
        // .getTeams(new AsyncCallback<List<BraketTeam>>() {
        //
        // @Override
        // public void onFailure(Throwable caught) {
        // BraketEntryPoint.displayException(caught);
        // }
        //
        // @Override
        // public void onSuccess(List<BraketTeam> result) {
        // dataGrid.setPageSize(result.size());
        // dataProvider.getList().clear();
        // dataProvider.getList().addAll(result);
        // dataProvider.refresh();
        // }
        // });

    }

    private void doTeamUpdate(final int index, final BraketTeam team) {

        // BraketEntryPoint.teamService.storeTeam(team, new
        // AsyncCallback<Long>() {
        //
        // @Override
        // public void onFailure(Throwable caught) {
        // BraketEntryPoint.displayException(caught);
        // }
        //
        // @Override
        // public void onSuccess(Long result) {
        //
        // BraketEntryPoint.displayToast("Team updated.");
        // dataProvider.getList().set(index, team);
        //
        // }
        //
        // });
    }

    /**
     * Get a cell value from a record.
     * 
     * @param <C>
     *            the cell type
     */
    private static interface GetValue<C> {
        C getValue(BraketTeam team);
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
    private <C> Column<BraketTeam, C> addColumn(Cell<C> cell, String headerText,
            final GetValue<C> getter, FieldUpdater<BraketTeam, C> fieldUpdater) {
        Column<BraketTeam, C> column = new Column<BraketTeam, C>(cell) {
            @Override
            public C getValue(BraketTeam object) {
                return getter.getValue(object);
            }
        };
        column.setFieldUpdater(fieldUpdater);

        dataGrid.addColumn(column, headerText);
        return column;
    }

}
