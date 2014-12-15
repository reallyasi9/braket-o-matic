package net.exclaimindustries.paste.braket.client.ui;

import java.util.Date;

import net.exclaimindustries.paste.braket.client.BraketTournament;
import net.exclaimindustries.paste.braket.client.TournamentService;
import net.exclaimindustries.paste.braket.client.TournamentServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.summatech.gwt.client.HourMinutePicker;

public class TournamentInfoPanel extends Composite {

  private static TournamentInfoPanelUiBinder uiBinder = GWT
      .create(TournamentInfoPanelUiBinder.class);

  interface TournamentInfoPanelUiBinder extends
      UiBinder<Widget, TournamentInfoPanel> {
  }

  @UiField
  TextBox nameBox;

  @UiField
  DateBox startDateBox;

  @UiField(provided = true)
  HourMinutePicker startTimeBox;

  @UiField
  Button createButton;

  @UiField
  Button updateButton;

  private BraketTournament tournament;

  /**
   * Service for reading and editing Tournament data
   */
  private TournamentServiceAsync tournamentService = GWT
      .create(TournamentService.class);

  public TournamentInfoPanel() {
    startTimeBox = new HourMinutePicker(HourMinutePicker.PickerFormat._24_HOUR);
    initWidget(uiBinder.createAndBindUi(this));

    DateTimeFormat dateFormat = DateTimeFormat.getFormat("yyyy.MM.dd");
    startDateBox.setFormat(new DateBox.DefaultFormat(dateFormat));

    // Initialize the tournament to null.
    setSelectedTournament(null);

    // Define buttons
    createButton.addClickHandler(new ClickHandler() {

      @Override
      public void onClick(ClickEvent event) {
        tournament = new BraketTournament();
        updateTournamentFromInput();
        // TODO more stuff here
        tournamentService.storeTournament(tournament,
            new AsyncCallback<Long>() {

              @Override
              public void onFailure(Throwable caught) {
                // TODO Auto-generated method stub

              }

              @Override
              public void onSuccess(Long result) {
                // TODO add to list
                tournament.setId(result);
                setSelectedTournament(tournament);
              }

            });
      }

    });
    
    updateButton.addClickHandler(new ClickHandler() {

      @Override
      public void onClick(ClickEvent event) {
        if (tournament == null) {
          return;
        }
        updateTournamentFromInput();
        // TODO more stuff here
        tournamentService.storeTournament(tournament,
            new AsyncCallback<Long>() {

              @Override
              public void onFailure(Throwable caught) {
                // TODO Auto-generated method stub

              }

              @Override
              public void onSuccess(Long result) {
                // Notify that things went well?
              }

            });
      }

    });
  }

  public void setSelectedTournament(BraketTournament tourn) {
    this.tournament = tourn;
    updateButton.setEnabled(tourn != null);
    if (tournament != null) {
      updateInputFromTournament();
    }
  }

  private void updateTournamentFromInput() {
    if (tournament != null) {
      tournament.setName(nameBox.getText());
      Date startDate = startDateBox.getValue();
      Long startDateTimeMillisec = startDate.getTime() + startTimeBox.getMinutes() * 60000;
      Date startDateTime = new Date(startDateTimeMillisec);
      tournament.setStartTime(startDateTime);
    }
  }

  private void updateInputFromTournament() {
    if (tournament != null) {
      nameBox.setText(tournament.getName());
      Date startDate = tournament.getStartTime();
      // Have to use string manipulation because GWT does not emulate Calendar client-side
      DateTimeFormat dtfHourMinute = DateTimeFormat.getFormat("HH:mm");
      String hmStrings[] = dtfHourMinute.format(startDate).split(":");
      int hours = Integer.parseInt(hmStrings[0]);
      int minutes = Integer.parseInt(hmStrings[1]);
      startDateBox.setValue(startDate);
      startTimeBox.setTime("", hours, minutes);
    }
  }

}
