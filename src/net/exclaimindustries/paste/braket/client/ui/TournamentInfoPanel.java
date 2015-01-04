package net.exclaimindustries.paste.braket.client.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.exclaimindustries.paste.braket.client.BraketTournament;
import net.exclaimindustries.paste.braket.client.TournamentService;
import net.exclaimindustries.paste.braket.client.TournamentService.TournamentCollection;
import net.exclaimindustries.paste.braket.client.TournamentServiceAsync;

import com.google.common.base.Joiner;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DoubleBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.view.client.SelectionModel;
import com.summatech.gwt.client.HourMinutePicker;

public class TournamentInfoPanel extends Composite {

  // UiBinder stuff -----------------------------------------------------------

  private static TournamentInfoPanelUiBinder uiBinder = GWT
      .create(TournamentInfoPanelUiBinder.class);

  interface TournamentInfoPanelUiBinder extends
      UiBinder<Widget, TournamentInfoPanel> {
  }

  // UiFields -----------------------------------------------------------------

  @UiField
  TextBox nameBox;

  @UiField
  Label nameStatus;

  @UiField
  DateBox startDateBox;

  @UiField
  Label startDateStatus;

  @UiField(provided = true)
  HourMinutePicker startTimeBox;

  @UiField
  Label startTimeStatus;

  @UiField
  Label currentLabel;

  @UiField
  DoubleBox buyInBox;

  @UiField
  Label buyInStatus;

  @UiField
  TextBox payOutValuesBox;

  @UiField
  CheckBox payOutValuesRemainderCheckBox;

  @UiField
  Label payOutValuesRemainderLabel;

  @UiField
  Label payOutValuesStatus;

  @UiField
  DoubleBox upsetBox;

  @UiField
  Label upsetStatus;

  @UiField
  Button createButton;

  @UiField
  Button updateButton;

  @UiField
  Button deleteButton;

  @UiField
  Button selectButton;

  // Members ------------------------------------------------------------------

  private BraketTournament tournament;

  private BraketTournament currentTournament;

  private CellList<?> cellList;

  /**
   * Logging
   */
  private Logger logger = Logger.getLogger(this.getClass().getSimpleName());

  /**
   * Service for reading and editing Tournament data
   */
  private TournamentServiceAsync tournamentService = GWT
      .create(TournamentService.class);

  // Callbacks ----------------------------------------------------------------

  private AsyncCallback<TournamentCollection> getCurrentTournamentCallback = new AsyncCallback<TournamentCollection>() {
    @Override
    public void onFailure(Throwable caught) {
      logger.log(Level.SEVERE,
          "failed to get current tournament: " + caught.getLocalizedMessage());
      Toast
          .showErrorToast("Failed to get current tournament: see the log for more information.");
      currentTournament = null;
    }

    @Override
    public void onSuccess(TournamentCollection result) {
      currentTournament = result.getTournament();
    }
  };

  private AsyncCallback<Long> storeTournamentCallback = new AsyncCallback<Long>() {

    @Override
    public void onFailure(Throwable caught) {
      logger.log(Level.SEVERE,
          "failed to save tournament: " + caught.getLocalizedMessage());
      Toast
          .showErrorToast("Failed to save tournament: see the log for more information.");
      setSelectedTournament(tournament);
    }

    @Override
    public void onSuccess(Long result) {
      tournament.setId(result);

      // Update the visible elements
      // FIXME For some reason, this isn't making an entry in the list.
      cellList.setVisibleRangeAndClearData(cellList.getVisibleRange(), true);
      cellList.redraw();

      setSelectedTournament(tournament);
    }
  };

  private AsyncCallback<BraketTournament> updateCurrentTournamentCallback = new AsyncCallback<BraketTournament>() {
    @Override
    public void onFailure(Throwable caught) {
      logger.log(Level.SEVERE,
          "failed to set active tournament: " + caught.getLocalizedMessage());
      Toast
          .showErrorToast("Failed to set active tournament: see the log for more information.");
    }

    @Override
    public void onSuccess(BraketTournament result) {
      currentTournament = result;

      cellList.setVisibleRangeAndClearData(cellList.getVisibleRange(), true);
      cellList.redraw();

      setSelectedTournament(currentTournament);
    }

  };

  private AsyncCallback<Void> deleteTournamentCallback = new AsyncCallback<Void>() {
    @Override
    public void onFailure(Throwable caught) {
      logger.log(Level.SEVERE,
          "failed to delete tournament: " + caught.getLocalizedMessage());
      Toast
          .showErrorToast("Failed to delete tournament: see the log for more information.");
    }

    @Override
    public void onSuccess(Void result) {
      if (tournament.getId() == currentTournament.getId()) {
        currentTournament = null;
      }

      // Unselect the tournament
      @SuppressWarnings("unchecked")
      SelectionModel<BraketTournament> selectionModel = (SelectionModel<BraketTournament>) cellList
          .getSelectionModel();
      selectionModel.setSelected(tournament, false);

      // Update the visible elements
      cellList.setVisibleRangeAndClearData(cellList.getVisibleRange(), true);
      cellList.redraw();

      setSelectedTournament(null);

    }
  };

  private ClickHandler createTournamentHandler = new ClickHandler() {
    @Override
    public void onClick(ClickEvent event) {
      tournament = new BraketTournament();
      if (validateFields()) {
        updateTournamentFromInput();
        tournamentService.storeTournament(tournament, storeTournamentCallback);
      }
    }
  };

  private ClickHandler updateTournamentHandler = new ClickHandler() {
    @Override
    public void onClick(ClickEvent event) {
      if (tournament == null) {
        return;
      }
      if (validateFields()) {
        updateTournamentFromInput();
        tournamentService.storeTournament(tournament, storeTournamentCallback);
      }
    }
  };

  private ClickHandler selectCurrentTournamentHandler = new ClickHandler() {

    @Override
    public void onClick(ClickEvent event) {
      if (tournament == null) {
        return;
      }
      if (!Window
          .confirm("Changing the active tournament could result in unwanted changes to the website for logged-in users.\n\nClick \"OK\" to proceed with changing the active tournament.")) {
        return;
      }
      tournamentService.setCurrentTournament(tournament,
          updateCurrentTournamentCallback);
    }

  };

  private ClickHandler deleteTournamentHandler = new ClickHandler() {

    @Override
    public void onClick(ClickEvent event) {
      if (tournament == null) {
        return;
      }
      if (!Window
          .confirm("Deleting a tournament could result in permanently lost data.  THIS OPERATION CANNOT BE UNDONE.\n\nClick \"OK\" to proceed with deleting this tournament.")) {
        return;
      }
      tournamentService.deleteTournament(tournament, deleteTournamentCallback);
    }

  };

  private ClickHandler payOutValuesRemainderHandler = new ClickHandler() {

    @Override
    public void onClick(ClickEvent event) {
      payOutValuesRemainderCheckBox.setValue(
          !payOutValuesRemainderCheckBox.getValue(), true);
    }

  };

  // Methods ------------------------------------------------------------------

  /**
   * Constructor
   * 
   * Builds the UI and
   */
  public TournamentInfoPanel(CellList<BraketTournament> cl) {
    cellList = cl;

    startTimeBox = new HourMinutePicker(HourMinutePicker.PickerFormat._24_HOUR);

    // Initialize the UI
    initWidget(uiBinder.createAndBindUi(this));

    DateTimeFormat dateFormat = DateTimeFormat.getFormat("yyyy.MM.dd");
    startDateBox.setFormat(new DateBox.DefaultFormat(dateFormat));

    // Add a bunch of change handlers
    // nameBox.addChangeHandler(changeHandler);
    // startDateBox.getTextBox().addChangeHandler(changeHandler);
    // startTimeBox.addHandler(changeHandler, ChangeEvent.getType());
    // buyInBox.addChangeHandler(changeHandler);
    // upsetBox.addChangeHandler(changeHandler);
    // payOutValuesRemainderCheckBox.addHandler(changeHandler,
    // ChangeEvent.getType());

    // Initialize the tournament to null.
    setSelectedTournament(null);

    // Define buttons and click handlers
    createButton.addClickHandler(createTournamentHandler);
    updateButton.addClickHandler(updateTournamentHandler);
    selectButton.addClickHandler(selectCurrentTournamentHandler);
    deleteButton.addClickHandler(deleteTournamentHandler);
    payOutValuesRemainderLabel.addClickHandler(payOutValuesRemainderHandler);

    // Lookup the current tournament
    tournamentService.getCurrentTournament(getCurrentTournamentCallback);
  }

  protected boolean validateFields() {
    // Validate first
    // TODO use a real validation engine
    nameStatus.setText("");
    startDateStatus.setText("");
    startTimeStatus.setText("");
    buyInStatus.setText("");
    payOutValuesStatus.setText("");
    upsetStatus.setText("");
    boolean invalid = false;
    if (nameBox.getText().isEmpty()) {
      nameStatus.setText("Tournament name cannot be empty");
      invalid = true;
    }
    if (startDateBox.getValue() == null) {
      startDateStatus.setText("Start date must be in the format "
          + startDateBox.getFormat().toString());
      invalid = true;
    }
    if (startTimeBox.getMinutes() == null) {
      startTimeStatus.setText("Start time must be in the format HH:mm");
      invalid = true;
    }
    if (buyInBox.getValue() == null) {
      buyInStatus.setText("Buy-in must be a number");
      invalid = true;
    }
    for (String strVal : payOutValuesBox.getValue().split(",[ ]*")) {
      try {
        Double val = Double.valueOf(strVal);
        if (val == null) {
          payOutValuesStatus
              .setText("Pay-out must be a comma-separated list of numbers");
          invalid = true;
        }
      } catch (NumberFormatException | NullPointerException e) {
        payOutValuesStatus
            .setText("Pay-out must be a comma-separated list of numbers");
        invalid = true;
      }
    }
    if (upsetBox.getValue() == null) {
      upsetStatus.setText("Upset value must be a number");
      invalid = true;
    }

    return (!invalid);
  }

  public void setSelectedTournament(BraketTournament tourn) {
    tournament = tourn;
    updateInputFromTournament();
  }

  private void updateTournamentFromInput() {
    if (tournament != null) {
      tournament.setName(nameBox.getValue());

      Date startDate = startDateBox.getValue();
      Long startDateTimeMillisec = startDate.getTime()
          + startTimeBox.getMinutes() * 60000;
      Date startDateTime = new Date(startDateTimeMillisec);
      tournament.setStartTime(startDateTime);

      Double buyIn = Double.valueOf(buyInBox.getValue());
      tournament.setBuyInValue(buyIn);

      Double upsetValue = Double.valueOf(upsetBox.getValue());
      tournament.setUpsetValue(upsetValue);

      List<Double> payOuts = new ArrayList<Double>();
      for (String strVal : payOutValuesBox.getValue().split(",[ ]*")) {
        payOuts.add(Double.valueOf(strVal));
      }
      if (payOutValuesRemainderCheckBox.getValue()) {
        payOuts.add(0, null);
      }
      tournament.setPayOutValues(payOuts);
    }
  }

  private void updateInputFromTournament() {
    if (tournament != null) {
      nameBox.setValue(tournament.getName());

      Date startDate = tournament.getStartTime();
      // Have to use string manipulation because GWT does not emulate Calendar
      // client-side
      DateTimeFormat dtfHourMinute = DateTimeFormat.getFormat("HH:mm");
      String hmStrings[] = dtfHourMinute.format(startDate).split(":");
      int hours = Integer.parseInt(hmStrings[0]);
      int minutes = Integer.parseInt(hmStrings[1]);
      startDateBox.setValue(startDate);
      startTimeBox.setTime("", hours, minutes);

      buyInBox.setValue(tournament.getBuyInValue());

      upsetBox.setValue(tournament.getUpsetValue());

      List<Double> payOuts = tournament.getRawPayOutValues();
      payOutValuesRemainderCheckBox.setValue(payOuts.get(0) == null);
      payOutValuesBox.setValue(Joiner.on(", ").skipNulls().join(payOuts));

      if (currentTournament != null
          && tournament.getId() == currentTournament.getId()) {
        currentLabel.setText("Yes");
        selectButton.setEnabled(false);
      } else {
        currentLabel.setText("No");
        selectButton.setEnabled(true);
      }

      // Turn on buttons
      payOutValuesRemainderCheckBox.setEnabled(true);
      updateButton.setEnabled(true);
      deleteButton.setEnabled(true);

    } else {
      nameBox.setValue(null);
      startDateBox.setValue(null);
      startTimeBox.setTime("", 0, 0);
      buyInBox.setValue(null);
      upsetBox.setValue(null);
      payOutValuesBox.setValue(null);
      payOutValuesRemainderCheckBox.setValue(null);
      currentLabel.setText(null);

      // Turn off buttons
      selectButton.setEnabled(false);
      updateButton.setEnabled(false);
      deleteButton.setEnabled(false);

    }
  }

}
