package net.exclaimindustries.paste.braket.client.ui;

import java.util.Comparator;

import net.exclaimindustries.paste.braket.client.BraketUser;
import net.exclaimindustries.paste.braket.client.UserName;
import net.exclaimindustries.paste.braket.client.resources.Resources;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextCell;
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

public class EditUserDialog extends SimpleLayoutPanel {

    private static EditUserDialogUiBinder uiBinder = GWT
            .create(EditUserDialogUiBinder.class);

    interface EditUserDialogUiBinder extends UiBinder<Widget, EditUserDialog> {
    }

    private final static Resources res = GWT.create(Resources.class);

    static {
        res.style().ensureInjected();
    }

    // TODO make this async
    private final ListDataProvider<BraketUser> dataProvider =
            new ListDataProvider<BraketUser>();

    @UiField(provided = true)
    DataGrid<BraketUser> dataGrid;

    public EditUserDialog() {
        setHeight("100%");
        // Make the DataGrid before binding!
        dataGrid = new DataGrid<BraketUser>();
        // dataGrid.setWidth("600px");
        dataGrid.setMinimumTableWidth(500, Unit.PX);
        // dataGrid.setHeight("500px");
        dataGrid.setAutoHeaderRefreshDisabled(true);
        dataGrid.setEmptyTableWidget(new Label("no data to show right now..."));

        // First name
        Column<BraketUser, String> firstNameColumn =
                addColumn(new EditTextCell(), "first name", new GetValue<String>() {
                    @Override
                    public String getValue(BraketUser user) {
                        return user.getName().getFirstName();
                    }
                }, new FieldUpdater<BraketUser, String>() {
                    @Override
                    public void update(int index, BraketUser object, String value) {
                        UserName name = object.getName();
                        UserName newName =
                                new UserName(value, name.getLastName(), name
                                        .getNickname());
                        object.setName(newName);
                        doUserUpdate(index, object);
                    }
                });
        // Add a ColumnSortEvent.ListHandler to connect sorting to the
        // java.util.List.
        ListHandler<BraketUser> columnSortHandler =
                new ListHandler<BraketUser>(dataProvider.getList());
        columnSortHandler.setComparator(firstNameColumn,
                new Comparator<BraketUser>() {
                    public int compare(BraketUser o1, BraketUser o2) {
                        UserName n1 = o1.getName();
                        UserName n2 = o2.getName();
                        return n1.getFirstName().compareTo(n2.getFirstName());
                    }
                });

        firstNameColumn.setSortable(true);

        // Last name
        Column<BraketUser, String> lastNameColumn =
                addColumn(new EditTextCell(), "last name", new GetValue<String>() {
                    @Override
                    public String getValue(BraketUser user) {
                        return user.getName().getLastName();
                    }
                }, new FieldUpdater<BraketUser, String>() {
                    @Override
                    public void update(int index, BraketUser object, String value) {
                        UserName name = object.getName();
                        UserName newName =
                                new UserName(name.getFirstName(), value, name
                                        .getNickname());
                        object.setName(newName);
                        doUserUpdate(index, object);
                    }
                });
        columnSortHandler.setComparator(lastNameColumn,
                new Comparator<BraketUser>() {
                    public int compare(BraketUser o1, BraketUser o2) {
                        UserName n1 = o1.getName();
                        UserName n2 = o2.getName();
                        return n1.getLastName().compareTo(n2.getLastName());
                    }
                });
        lastNameColumn.setSortable(true);

        // Nickname
        Column<BraketUser, String> nicknameColumn =
                addColumn(new EditTextCell(), "nickname", new GetValue<String>() {
                    @Override
                    public String getValue(BraketUser user) {
                        return user.getName().getNickname();
                    }
                }, new FieldUpdater<BraketUser, String>() {
                    @Override
                    public void update(int index, BraketUser object, String value) {
                        UserName name = object.getName();
                        UserName newName =
                                new UserName(name.getFirstName(),
                                        name.getLastName(), value);
                        object.setName(newName);
                        doUserUpdate(index, object);
                    }
                });

        columnSortHandler.setComparator(nicknameColumn,
                new Comparator<BraketUser>() {
                    public int compare(BraketUser o1, BraketUser o2) {
                        UserName n1 = o1.getName();
                        UserName n2 = o2.getName();
                        return n1.getNickname().compareTo(n2.getNickname());
                    }
                });

        nicknameColumn.setSortable(true);

        // Email
        Column<BraketUser, String> emailColumn =
                addColumn(new TextCell(), "email", new GetValue<String>() {
                    @Override
                    public String getValue(BraketUser user) {
                        return user.getEmail();
                    }
                }, null);
        columnSortHandler.setComparator(emailColumn, new Comparator<BraketUser>() {
            public int compare(BraketUser o1, BraketUser o2) {
                return (o1.getEmail().compareToIgnoreCase(o2.getEmail()));
            }
        });
        emailColumn.setSortable(true);

        // Column<BraketUser, Boolean> registerColumn =
        // addColumn(new CheckboxCell(), "registered",
        // new GetValue<Boolean>() {
        // @Override
        // public Boolean getValue(BraketUser user) {
        // return BraketEntryPoint.currTournament
        // .getRegisteredSelectionId(user.getId()) != null;
        // }
        // }, new FieldUpdater<BraketUser, Boolean>() {
        // @Override
        // public void update(int index,
        // final BraketUser object, Boolean value) {
        // if (value) {
        // // register the user with the tournament
        // // updater
        // BraketEntryPoint.userService.registerUser(
        // object, new AsyncCallback<Long>() {
        //
        // @Override
        // public void onFailure(
        // Throwable caught) {
        // BraketEntryPoint
        // .displayException(caught);
        // }
        //
        // @Override
        // public void onSuccess(
        // Long result) {
        // BraketEntryPoint
        // .displayToast("User updated");
        // BraketEntryPoint.currTournament.addRegistration(
        // object.getId(),
        // result);
        // }
        //
        // });
        // } else {
        // BraketEntryPoint.userService
        // .unregisterUser(object,
        // new AsyncCallback<Void>() {
        //
        // @Override
        // public
        // void
        // onFailure(
        // Throwable caught) {
        // BraketEntryPoint
        // .displayException(caught);
        // }
        //
        // @Override
        // public void onSuccess(
        // Void result) {
        // BraketEntryPoint
        // .displayToast("User updated");
        // BraketEntryPoint.currTournament
        // .removeRegistration(object
        // .getId());
        // }
        //
        // });
        // }
        // }
        // });
        // dataGrid.setColumnWidth(registerColumn, 10, Unit.EM);

        // ActionCell.
        // Column<BraketUser, BraketUser> viewColumn =
        // addColumn(new ActionCell<BraketUser>("edit braket",
        // new ActionCell.Delegate<BraketUser>() {
        //
        // @Override
        // public void execute(BraketUser object) {
        // BraketEntryPoint.doEditUserSelection(object);
        // }
        // }), "", new GetValue<BraketUser>() {
        // @Override
        // public BraketUser getValue(BraketUser si) {
        // return si;
        // }
        // }, null);
        // dataGrid.setColumnWidth(viewColumn, 10, Unit.EM);

        dataGrid.addColumnSortHandler(columnSortHandler);

        // Attach the data provider to the dataGrid
        dataProvider.addDataDisplay(dataGrid);

        // Make a pager and attach
        // SimplePager.Resources pagerResources =
        // GWT.create(SimplePager.Resources.class);
        // pager =
        // new SimplePager(TextLocation.CENTER, pagerResources, false, 0,
        // true);
        // pager.setDisplay(dataGrid);

        setWidget(uiBinder.createAndBindUi(this));
    }

    // Reload the user info from the datastore and rebuild the list
    public void reset() {

        // BraketEntryPoint.userService
        // .getUsers(new AsyncCallback<Collection<BraketUser>>() {
        //
        // @Override
        // public void onFailure(Throwable caught) {
        // BraketEntryPoint.displayException(caught);
        // }
        //
        // @Override
        // public void onSuccess(Collection<BraketUser> result) {
        //
        // // I think that's it...
        // dataGrid.setPageSize(result.size());
        // dataGrid.setHeight("100%");
        // dataProvider.getList().clear();
        // dataProvider.getList().addAll(result);
        // dataProvider.refresh();
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
        C getValue(BraketUser user);
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
    private <C> Column<BraketUser, C> addColumn(Cell<C> cell, String headerText,
            final GetValue<C> getter, FieldUpdater<BraketUser, C> fieldUpdater) {
        Column<BraketUser, C> column = new Column<BraketUser, C>(cell) {
            @Override
            public C getValue(BraketUser object) {
                return getter.getValue(object);
            }
        };
        column.setFieldUpdater(fieldUpdater);

        dataGrid.addColumn(column, headerText);
        return column;
    }

    private void doUserUpdate(final int index, final BraketUser object) {
        // BraketEntryPoint.userService.storeUser(object,
        // new AsyncCallback<String>() {
        //
        // @Override
        // public void onFailure(Throwable caught) {
        // BraketEntryPoint.displayException(caught);
        // }
        //
        // @Override
        // public void onSuccess(String result) {
        // BraketEntryPoint.displayToast("User updated");
        // dataProvider.getList().set(index, object);
        // }
        //
        // });
    }

}
