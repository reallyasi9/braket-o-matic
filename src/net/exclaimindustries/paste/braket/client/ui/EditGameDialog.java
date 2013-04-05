package net.exclaimindustries.paste.braket.client.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import net.exclaimindustries.paste.braket.client.BraketEntryPoint;
import net.exclaimindustries.paste.braket.client.BraketGame;
import net.exclaimindustries.paste.braket.client.BraketTeam;
import net.exclaimindustries.paste.braket.client.TeamService;
import net.exclaimindustries.paste.braket.client.TeamServiceAsync;
import net.exclaimindustries.paste.braket.client.resources.Resources;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.DatePickerCell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SelectionCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;

public class EditGameDialog extends SimpleLayoutPanel {

    private static EditGameDialogUiBinder uiBinder = GWT
            .create(EditGameDialogUiBinder.class);

    interface EditGameDialogUiBinder extends UiBinder<Widget, EditGameDialog> {
    }

    private final static Resources RES = GWT.create(Resources.class);
    private final static TeamServiceAsync TEAM_SERV = GWT
            .create(TeamService.class);
    private final static HashMap<Long, BraketTeam> TEAMS =
            new HashMap<Long, BraketTeam>();
    private final static HashMap<String, Long> IDS_BY_NAME =
            new HashMap<String, Long>();
    private final static HashMap<Long, String> NAMES_BY_ID =
            new HashMap<Long, String>();
    private final static ArrayList<String> NAME_LIST = new ArrayList<String>();

    static {
        RES.style().ensureInjected();

    }

    // TODO make this async
    private final ListDataProvider<BraketGame> dataProvider =
            new ListDataProvider<BraketGame>();

    @UiField(provided = true)
    DataGrid<BraketGame> dataGrid;

    public EditGameDialog() {
        // Make the DataGrid before binding!
        dataGrid = new DataGrid<BraketGame>(25);
        // dataGrid.setWidth("600px");
        dataGrid.setMinimumTableWidth(500, Unit.PX);
        dataGrid.setHeight("100%");
        dataGrid.setAutoHeaderRefreshDisabled(true);
        dataGrid.setEmptyTableWidget(new Label("no data to show right now..."));

        TEAM_SERV.getTeams(new AsyncCallback<List<BraketTeam>>() {

            @Override
            public void onFailure(Throwable caught) {
                BraketEntryPoint.displayException(caught);
            }

            @Override
            public void onSuccess(List<BraketTeam> result) {
                TEAMS.clear();
                for (BraketTeam t : result) {
                    TEAMS.put(t.getId(), t);
                }
                IDS_BY_NAME.clear();
                NAMES_BY_ID.clear();
                for (BraketTeam t : TEAMS.values()) {
                    IDS_BY_NAME.put(t.getName().getAbbreviation(), t.getId());
                    NAMES_BY_ID.put(t.getId(), t.getName().getAbbreviation());
                }
                NAME_LIST.clear();
                NAME_LIST.addAll(IDS_BY_NAME.keySet());
                NAME_LIST.add("none");

                finishInit();

            }

        });

        setWidget(uiBinder.createAndBindUi(this));
    }

    private void finishInit() {

        // Add a ColumnSortEvent.ListHandler to connect sorting to the
        // java.util.List.
        ListHandler<BraketGame> columnSortHandler =
                new ListHandler<BraketGame>(dataProvider.getList());

        // Index
        Column<BraketGame, String> indexColumn =
                addColumn(new EditTextCell(), "index", new GetValue<String>() {

                    @Override
                    public String getValue(BraketGame game) {
                        return Integer.toString(game.getIndex());
                    }

                }, new FieldUpdater<BraketGame, String>() {

                    @Override
                    public void update(int index, BraketGame object,
                            String value) {
                        object.setIndex(Integer.valueOf(value));
                        doGameUpdate(index, object);
                    }
                });
        columnSortHandler.setComparator(indexColumn,
                new Comparator<BraketGame>() {
                    public int compare(BraketGame o1, BraketGame o2) {
                        return Integer.valueOf(o1.getIndex()).compareTo(
                                o2.getIndex());
                    }
                });
        indexColumn.setSortable(true);
        dataGrid.setColumnWidth(indexColumn, 5, Unit.EM);

        // Top team
        Column<BraketGame, String> topTeamColumn =
                addColumn(new SelectionCell(NAME_LIST), "top team",
                        new GetValue<String>() {

                            @Override
                            public String getValue(BraketGame game) {
                                if (!TEAMS.containsKey(game.getTopTeamId())) {
                                    return "none";
                                }
                                return NAMES_BY_ID.get(game.getTopTeamId());
                            }

                        }, new FieldUpdater<BraketGame, String>() {

                            @Override
                            public void update(int index, BraketGame object,
                                    String value) {
                                if (value.equals("none")) {
                                    object.setTopTeamId(null);
                                } else {
                                    object.setTopTeamId(IDS_BY_NAME.get(value));
                                }

                                doGameUpdate(index, object);

                            }
                        });
        dataGrid.setColumnWidth(topTeamColumn, 12, Unit.EM);

        // Top score
        Column<BraketGame, String> topScoreColumn =
                addColumn(new EditTextCell(), "top score",
                        new GetValue<String>() {

                            @Override
                            public String getValue(BraketGame game) {
                                if (game.getTopScore() == null) {
                                    return "";
                                }
                                return Integer.toString(game.getTopScore());
                            }

                        }, new FieldUpdater<BraketGame, String>() {

                            @Override
                            public void update(int index, BraketGame object,
                                    String value) {
                                object.setTopScore(Integer.valueOf(value));
                                doGameUpdate(index, object);
                            }
                        });
        dataGrid.setColumnWidth(topScoreColumn, 7, Unit.EM);

        // Bottom team
        Column<BraketGame, String> bottomTeamColumn =
                addColumn(new SelectionCell(NAME_LIST), "bottom team",
                        new GetValue<String>() {

                            @Override
                            public String getValue(BraketGame game) {
                                if (!TEAMS.containsKey(game.getBottomTeamId())) {
                                    return "none";
                                }
                                return NAMES_BY_ID.get(game.getBottomTeamId());
                            }

                        }, new FieldUpdater<BraketGame, String>() {

                            @Override
                            public void update(int index, BraketGame object,
                                    String value) {
                                if (value.equals("none")) {
                                    object.setBottomTeamId(null);
                                } else {
                                    object.setBottomTeamId(IDS_BY_NAME
                                            .get(value));
                                }

                                doGameUpdate(index, object);
                            }
                        });
        dataGrid.setColumnWidth(bottomTeamColumn, 12, Unit.EM);

        // Bottom score
        Column<BraketGame, String> bottomScoreColumn =
                addColumn(new EditTextCell(), "bottom score",
                        new GetValue<String>() {

                            @Override
                            public String getValue(BraketGame game) {
                                if (game.getBottomScore() == null) {
                                    return "";
                                }
                                return Integer.toString(game.getBottomScore());
                            }

                        }, new FieldUpdater<BraketGame, String>() {

                            @Override
                            public void update(int index, BraketGame object,
                                    String value) {
                                object.setBottomScore(Integer.valueOf(value));
                                doGameUpdate(index, object);
                            }
                        });
        dataGrid.setColumnWidth(bottomScoreColumn, 7, Unit.EM);

        // Winner
        List<String> options = Arrays.asList("none", "top", "bottom");
        Column<BraketGame, String> winnerColumn =
                addColumn(new SelectionCell(options), "winner",
                        new GetValue<String>() {

                            @Override
                            public String getValue(BraketGame game) {
                                if (game.getWinner() == null) {
                                    return "none";
                                } else {
                                    return (game.getWinner()) ? "bottom" : "top";
                                }
                            }

                        }, new FieldUpdater<BraketGame, String>() {

                            @Override
                            public void update(int index, BraketGame object,
                                    String value) {
                                if (value.equals("none")) {
                                    object.setWinner(null);
                                } else {
                                    object.setWinner(value.equals("bottom"));
                                }

                                doGameUpdate(index, object);

                            }
                        });
        dataGrid.setColumnWidth(winnerColumn, 7, Unit.EM);

        // Location
        Column<BraketGame, String> locationColumn =
                addColumn(new EditTextCell(), "location",
                        new GetValue<String>() {

                            @Override
                            public String getValue(BraketGame game) {
                                return game.getLocation();
                            }

                        }, new FieldUpdater<BraketGame, String>() {

                            @Override
                            public void update(int index, BraketGame object,
                                    String value) {
                                object.setLocation(value);
                                doGameUpdate(index, object);
                            }
                        });
        dataGrid.setColumnWidth(locationColumn, 14, Unit.EM);

        // Status
        Column<BraketGame, String> statusColumn =
                addColumn(new EditTextCell(), "status", new GetValue<String>() {

                    @Override
                    public String getValue(BraketGame game) {
                        return game.getGameStatus();
                    }

                }, new FieldUpdater<BraketGame, String>() {

                    @Override
                    public void update(int index, BraketGame object,
                            String value) {
                        object.setGameStatus(value);
                        doGameUpdate(index, object);
                    }
                });
        dataGrid.setColumnWidth(statusColumn, 14, Unit.EM);

        // Date
        DateTimeFormat dateFormat =
                DateTimeFormat.getFormat(PredefinedFormat.RFC_2822);
        Column<BraketGame, Date> dateColumn =
                addColumn(new DatePickerCell(dateFormat), "date",
                        new GetValue<Date>() {

                            @Override
                            public Date getValue(BraketGame game) {
                                return game.getScheduledDate();
                            }

                        }, new FieldUpdater<BraketGame, Date>() {

                            @Override
                            public void update(int index, BraketGame object,
                                    Date value) {
                                object.setScheduledDate(value);
                                doGameUpdate(index, object);
                            }
                        });
        dataGrid.setColumnWidth(dateColumn, 14, Unit.EM);

        // ESPN ID
        Column<BraketGame, String> espnColumn =
                addColumn(new EditTextCell(), "ESPN ID",
                        new GetValue<String>() {

                            @Override
                            public String getValue(BraketGame game) {
                                if (game.getEspnId() == null) {
                                    return "";
                                }
                                return Long.toString(game.getEspnId());
                            }

                        }, new FieldUpdater<BraketGame, String>() {

                            @Override
                            public void update(int index, BraketGame object,
                                    String value) {
                                object.setEspnId(Long.valueOf(value));
                                doGameUpdate(index, object);
                            }
                        });
        dataGrid.setColumnWidth(espnColumn, 7, Unit.EM);

        dataGrid.addColumnSortHandler(columnSortHandler);

        // Attach the data provider to the dataGrid
        dataProvider.addDataDisplay(dataGrid);
    }

    public void reset() {
        // Do not trust pusher robot.
        BraketEntryPoint.gameService
                .getGames(new AsyncCallback<List<BraketGame>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        BraketEntryPoint.displayException(caught);
                    }

                    @Override
                    public void onSuccess(final List<BraketGame> resGames) {

                        dataGrid.setPageSize(resGames.size());
                        dataProvider.getList().clear();
                        dataProvider.getList().addAll(resGames);
                        dataProvider.refresh();

                    }
                });

    }

    private void doGameUpdate(final int index, final BraketGame game) {

        BraketEntryPoint.gameService.storeGame(game, new AsyncCallback<Long>() {

            @Override
            public void onFailure(Throwable caught) {
                BraketEntryPoint.displayException(caught);
            }

            @Override
            public void onSuccess(Long result) {

                BraketEntryPoint.displayToast("Game updated.");
                dataProvider.getList().set(index, game);
            }

        });
    }

    /**
     * Get a cell value from a record.
     * 
     * @param <C>
     *            the cell type
     */
    private static interface GetValue<C> {
        C getValue(BraketGame team);
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
    private <C> Column<BraketGame, C> addColumn(Cell<C> cell,
            String headerText, final GetValue<C> getter,
            FieldUpdater<BraketGame, C> fieldUpdater) {
        Column<BraketGame, C> column = new Column<BraketGame, C>(cell) {
            @Override
            public C getValue(BraketGame object) {
                return getter.getValue(object);
            }
        };
        column.setFieldUpdater(fieldUpdater);

        dataGrid.addColumn(column, headerText);
        return column;
    }

}
