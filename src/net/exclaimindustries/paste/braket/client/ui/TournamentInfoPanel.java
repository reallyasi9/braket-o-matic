package net.exclaimindustries.paste.braket.client.ui;

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
  
  //@UiField
  //TimeBox startTimeBox;

  @UiField
  Button createButton;

  // @UiField
  // Button updateButton;

  private BraketTournament tournament;

  /**
   * Service for reading and editing Tournament data
   */
  private TournamentServiceAsync tournamentService = GWT
      .create(TournamentService.class);

  public TournamentInfoPanel() {
    initWidget(uiBinder.createAndBindUi(this));
    
    DateTimeFormat dateFormat = DateTimeFormat.getFormat("yyyy.MM.dd");
    startDateBox.setFormat(new DateBox.DefaultFormat(dateFormat));

    // Initialize the tournament to null.
    setTournament(null);

    // Define buttons
    createButton.addClickHandler(new ClickHandler() {

      @Override
      public void onClick(ClickEvent event) {
        tournament = new BraketTournament();
        tournament.setName(nameBox.getText());
        // TODO more stuff here
        tournamentService.storeTournament(tournament,
            new AsyncCallback<Long>() {

              @Override
              public void onFailure(Throwable caught) {
                // TODO Auto-generated method stub
                
              }

              @Override
              public void onSuccess(Long result) {
                tournament.setId(result);
                setTournament(tournament);
              }

            });
      }

    });
  }

  public void setTournament(BraketTournament tourn) {
    this.tournament = tourn;
    // updateButton.setEnabled(tourn != null);
    if (tourn != null) {
      nameBox.setText(tourn.getName());
      startDateBox.setValue(tourn.getStartTime());
    }
  }

}
