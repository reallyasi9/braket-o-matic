package net.exclaimindustries.paste.braket.client.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.exclaimindustries.paste.braket.client.BraketTournament;
import net.exclaimindustries.paste.braket.client.TournamentService;
import net.exclaimindustries.paste.braket.client.TournamentService.TournamentCollection;
import net.exclaimindustries.paste.braket.client.TournamentServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.thirdparty.guava.common.base.Joiner;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
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
import com.google.gwt.view.client.AsyncDataProvider;
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

  private AsyncDataProvider<BraketTournament> dataProvider;

  /**
   * Service for reading and editing Tournament data
   */
  private TournamentServiceAsync tournamentService = GWT
      .create(TournamentService.class);

  // Callbacks ----------------------------------------------------------------

  private AsyncCallback<TournamentCollection> getCurrentTournamentCallback = new AsyncCallback<TournamentCollection>() {
    @Override
    public void onFailure(Throwable caught) {
      // TODO Auto-generated method stub

    }

    @Override
    public void onSuccess(TournamentCollection result) {
      // TODO put badge on list?
      currentTournament = result.getTournament();
      setSelectedTournament(currentTournament);

    }
  };

  private AsyncCallback<Long> storeTournamentCallback = new AsyncCallback<Long>() {

    @Override
    public void onFailure(Throwable caught) {
      setSelectedTournament(tournament);
    }

    @Override
    public void onSuccess(Long result) {
      // TODO add to list
      tournament.setId(result);
      dataProvider.getKey(tournament);
      List<BraketTournament> tournaments = new ArrayList<BraketTournament>();
      tournaments.add(tournament);
      dataProvider.updateRowData(Integer.MAX_VALUE, tournaments);
      setSelectedTournament(tournament);
    }
  };

  private AsyncCallback<BraketTournament> updateCurrentTournamentCallback = new AsyncCallback<BraketTournament>() {
    @Override
    public void onFailure(Throwable caught) {
      // TODO Auto-generated method stub
    }

    @Override
    public void onSuccess(BraketTournament result) {
      // TODO add a badge
      currentTournament = result;
      setSelectedTournament(currentTournament);
    }

  };

  private AsyncCallback<Void> deleteTournamentCallback = new AsyncCallback<Void>() {
    @Override
    public void onFailure(Throwable caught) {
      // TODO Auto-generated method stub

    }

    @Override
    public void onSuccess(Void result) {
      // TODO put badge on list?
      if (tournament == currentTournament) {
        currentTournament = null;
      }
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
      updateTournamentFromInput();

      // TODO more stuff here (like validation?)

      tournamentService.storeTournament(tournament, storeTournamentCallback);
    }
  };

  private ClickHandler selectCurrentTournamentHandler = new ClickHandler() {

    @Override
    public void onClick(ClickEvent event) {
      if (tournament == null) {
        return;
      }
      // TODO
      if (!Window.confirm("TODO: Yes/No Update?")) {
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
      // TODO
      if (!Window.confirm("TODO: Yes/No Delete?")) {
        return;
      }
      tournamentService.deleteTournament(tournament, deleteTournamentCallback);
    }

  };

  private ChangeHandler changeHandler = new ChangeHandler() {

    @Override
    public void onChange(ChangeEvent event) {
      if (tournament != null) {
        updateButton.setEnabled(true);
      }
      createButton.setEnabled(true);
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
  public TournamentInfoPanel(AsyncDataProvider<BraketTournament> dp) {
    dataProvider = dp;

    startTimeBox = new HourMinutePicker(HourMinutePicker.PickerFormat._24_HOUR);

    DateTimeFormat dateFormat = DateTimeFormat.getFormat("yyyy.MM.dd");
    startDateBox.setFormat(new DateBox.DefaultFormat(dateFormat));

    // Add a bunch of change handlers
    nameBox.addChangeHandler(changeHandler);
    startDateBox.getTextBox().addChangeHandler(changeHandler);
    startTimeBox.addHandler(changeHandler, ChangeEvent.getType());
    buyInBox.addChangeHandler(changeHandler);
    upsetBox.addChangeHandler(changeHandler);
    payOutValuesRemainderCheckBox.addHandler(changeHandler,
        ChangeEvent.getType());

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

    // Initialize the UI
    initWidget(uiBinder.createAndBindUi(this));
  }

  protected boolean validateFields() {
    // Validate first
    // TODO use a real validation engine
    nameStatus.setText("");
    startDateStatus.setText("");
    startTimeStatus.setText("");
    buyInStatus.setText("");
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

    return (!invalid);
  }

  public void setSelectedTournament(BraketTournament tourn) {
    this.tournament = tourn;
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

      List<Double> payOuts = tournament.getPayOutValues();
      if (payOuts.get(0) == null) {
        payOutValuesRemainderCheckBox.setValue(true);
        payOutValuesBox.setValue(Joiner.on(", ").join(
            payOuts.subList(1, payOuts.size())));
      } else {
        payOutValuesRemainderCheckBox.setValue(false);
        payOutValuesBox.setValue(Joiner.on(", ").join(payOuts));
      }

      if (currentTournament != null) {
        currentLabel
            .setText(tournament.getId() == currentTournament.getId() ? "Yes"
                : "No");
      } else {
        currentLabel.setText("No");
      }
    }
  }

}
