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
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
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

  @UiField(provided = true)
  HourMinutePicker startTimeBox;

  @UiField
  Label currentLabel;

  @UiField
  DoubleBox buyInBox;

  // @UiField
  // TextBox payOutBox;

  @UiField
  DoubleBox upsetBox;

  @UiField
  Button createButton;

  @UiField
  Button updateButton;

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

  private AsyncCallback<Long> storeTournamentCallback = new AsyncCallback<Long>() {

    @Override
    public void onFailure(Throwable caught) {
      setSelectedTournament(tournament);
      updateButton.setEnabled(tournament != null);
      createButton.setEnabled(true);
      selectButton.setEnabled(tournament != currentTournament);
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
      selectButton.setEnabled(true);
    }
  };

  private AsyncCallback<BraketTournament> updateCurrentTournamentCallback = new AsyncCallback<BraketTournament>() {
    @Override
    public void onFailure(Throwable caught) {
      // TODO Auto-generated method stub
      selectButton.setEnabled(currentTournament != tournament);
      updateButton.setEnabled(true);
      createButton.setEnabled(true);
    }

    @Override
    public void onSuccess(BraketTournament result) {
      // TODO add a badge
      selectButton.setEnabled(false);
      currentTournament = result;
      setSelectedTournament(currentTournament);
    }

  };

  private ClickHandler createTournamentHandler = new ClickHandler() {
    @Override
    public void onClick(ClickEvent event) {

      updateButton.setEnabled(false);
      createButton.setEnabled(false);
      selectButton.setEnabled(false);

      tournament = new BraketTournament();
      updateTournamentFromInput();

      // Validate first
      // TODO use a real validation engine
      nameStatus.setText("");
      boolean invalid = false;
      if (tournament.getName().isEmpty()) {
        nameStatus.setText("Tournament name cannot be empty");
        invalid = true;
      }
      
      if (!invalid) {
        tournamentService.storeTournament(tournament, storeTournamentCallback);
      } else {
        updateButton.setEnabled(true);
        createButton.setEnabled(true);
        selectButton.setEnabled(true);
      }
    }
  };

  private ClickHandler updateTournamentHandler = new ClickHandler() {
    @Override
    public void onClick(ClickEvent event) {
      if (tournament == null) {
        return;
      }

      updateButton.setEnabled(false);
      createButton.setEnabled(false);
      selectButton.setEnabled(false);
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
      if (!Window.confirm("TODO: Yes/No Update?")) {
        return;
      }
      selectButton.setEnabled(false);
      tournamentService.setCurrentTournament(tournament,
          updateCurrentTournamentCallback);
    }

  };

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

  private ChangeHandler changeHandler = new ChangeHandler() {

    @Override
    public void onChange(ChangeEvent event) {
      if (tournament != null) {
        updateButton.setEnabled(true);
      }
      createButton.setEnabled(true);
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
    initWidget(uiBinder.createAndBindUi(this));

    DateTimeFormat dateFormat = DateTimeFormat.getFormat("yyyy.MM.dd");
    startDateBox.setFormat(new DateBox.DefaultFormat(dateFormat));

    nameBox.addChangeHandler(changeHandler);
    startDateBox.getTextBox().addChangeHandler(changeHandler);
    startTimeBox.addHandler(changeHandler, ChangeEvent.getType());
    buyInBox.addChangeHandler(changeHandler);
    upsetBox.addChangeHandler(changeHandler);

    // Initialize the tournament to null.
    setSelectedTournament(null);

    // Define buttons
    createButton.addClickHandler(createTournamentHandler);

    updateButton.addClickHandler(updateTournamentHandler);

    selectButton.addClickHandler(selectCurrentTournamentHandler);

    // Lookup the current tournament
    tournamentService.getCurrentTournament(getCurrentTournamentCallback);
  }

  public void setSelectedTournament(BraketTournament tourn) {
    this.tournament = tourn;
    updateButton.setEnabled(tourn != null);
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
