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

import java.util.LinkedList;

import net.exclaimindustries.paste.braket.client.Team;
import net.exclaimindustries.paste.braket.client.RGBAColor;
import net.exclaimindustries.paste.braket.client.TeamName;
import net.exclaimindustries.paste.braket.client.resources.Resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author paste
 * 
 */
public class DownloadTeamsDialog extends DialogBox {

    private static DownloadTeamsDialogUiBinder uiBinder = GWT
            .create(DownloadTeamsDialogUiBinder.class);

    interface DownloadTeamsDialogUiBinder extends
            UiBinder<Widget, DownloadTeamsDialog> {
    }

    private Team downloadedTeam;

    @UiField
    TextBox espnId;

    @UiField
    TextBox espnIdFixed;

    @UiField
    TextBox schoolName;

    @UiField
    TextBox teamName;

    @UiField
    TextBox abbreviation;

    @UiField
    TextBox seed;

    @UiField
    TextBox index;

    @UiField
    TextBox wpct;

    @UiField
    TextBox kenpom;

    @UiField
    TextBox color;

    @UiField
    Button downloadButton;

    @UiField
    Button saveButton;

    @UiField
    Button cancelButton;

    @UiField
    Image teamImage;

    private final static Resources res = GWT.create(Resources.class);

    static {
        res.style().ensureInjected();
    }

    public DownloadTeamsDialog() {
        super(false, true);
        setWidget(uiBinder.createAndBindUi(this));
        getCaption().setText("download and save team");
        setAnimationEnabled(true);
        setGlassEnabled(true);
        addStyleName(res.style().dialogBox());
        setGlassStyleName(res.style().glass());
    }

    public void reset() {
        downloadedTeam = null;
        espnId.setText("");
        teamImage.setVisible(false);
        espnIdFixed.setText("");
        schoolName.setText("");
        schoolName.setEnabled(false);
        abbreviation.setText("");
        abbreviation.setEnabled(false);
        teamName.setText("");
        teamName.setEnabled(false);
        seed.setText("");
        seed.setEnabled(false);
        index.setText("");
        index.setEnabled(false);
        wpct.setText("");
        wpct.setEnabled(false);
        kenpom.setText("");
        kenpom.setEnabled(false);
        color.setText("");
        color.setEnabled(false);
        saveButton.setEnabled(false);
    }

    // private void updateForm() {
    // espnIdFixed.setText(Long.toString(downloadedTeam.getId()));
    // teamImage.setUrl(downloadedTeam.getPicture());
    // teamImage.setVisible(true);
    // schoolName.setText(downloadedTeam.getName().getSchoolName());
    // schoolName.setEnabled(true);
    // abbreviation.setText(downloadedTeam.getName().getAbbreviation());
    // abbreviation.setEnabled(true);
    // teamName.setText(downloadedTeam.getName().getTeamName());
    // teamName.setEnabled(true);
    // // Seed doesn't come with the download
    // seed.setText(Integer.toString(downloadedTeam.getSeed()));
    // seed.setEnabled(true);
    // // Index doesn't come with download
    // // Force fill in order
    // if (BraketEntryPoint.currTournament.getTeams().size() <
    // BraketEntryPoint.currTournament
    // .getNumberOfTeams()) {
    // index.setText(Integer.toString(BraketEntryPoint.currTournament
    // .getTeams().size()));
    // index.setEnabled(false);
    // } else {
    // index.setText("0");
    // index.setEnabled(true);
    // }
    // wpct.setText(Double.toString(downloadedTeam.getWinPercentage()));
    // wpct.setEnabled(true);
    // kenpom.setText(Double.toString(downloadedTeam.getKenpomScore()));
    // kenpom.setEnabled(true);
    // color.setText(downloadedTeam.getColor().getHexValue());
    // color.setEnabled(true);
    // saveButton.setEnabled(true);
    // }

    private void saveFormData() {
        try {
            TeamName name = downloadedTeam.getName();
            name.setSchoolName(schoolName.getText());
            name.setTeamName(teamName.getText());
            name.setAbbreviation(abbreviation.getText());
            downloadedTeam.setName(name);
            downloadedTeam.setSeed(Integer.valueOf(seed.getText()));
            downloadedTeam.setIndex(Integer.valueOf(index.getText()));
            downloadedTeam.setWinPercentage(Double.valueOf(wpct.getText()));
            downloadedTeam.setKenpomScore(Double.valueOf(kenpom.getText()));
            downloadedTeam.setColor(new RGBAColor(Integer.valueOf(color.getText(), 16)));
            color.setText(downloadedTeam.getColor().getHexValue());
        } catch (Throwable caught) {
            // BraketEntryPoint.displayException(caught);
        }
    }

    @UiHandler("downloadButton")
    void downloadClick(ClickEvent e) {
        doDownload();
    }

    @UiHandler({ "schoolName", "teamName", "abbreviation", "seed", "index", "wpct",
            "kenpom", "color" })
    void saveEnter(KeyUpEvent e) {
        if (e.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
            doSave();
        }
    }

    private void doDownload() {
        LinkedList<Long> teamIds = new LinkedList<Long>();
        try {
            teamIds.add(Long.valueOf(espnId.getText()));
        } catch (Throwable caught) {
            // BraketEntryPoint.displayException(caught);
        }
        reset();
        // BraketEntryPoint.teamService.downloadTeams(teamIds,
        // new AsyncCallback<Collection<BraketTeam>>() {
        //
        // @Override
        // public void onFailure(Throwable caught) {
        // BraketEntryPoint.displayException(caught);
        // }
        //
        // @Override
        // public void onSuccess(Collection<BraketTeam> result) {
        // if (result.isEmpty()) {
        // BraketEntryPoint
        // .displayToast("Unable to download team");
        // return;
        // }
        // downloadedTeam = result.iterator().next();
        // BraketEntryPoint
        // .displayToast("Team downloaded successfully.");
        // updateForm();
        // seed.selectAll();
        // seed.setFocus(true);
        // }
        //
        // });
    }

    @UiHandler("espnId")
    void downloadEnter(KeyUpEvent e) {
        if (e.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
            doDownload();
        }
    }

    @UiHandler("saveButton")
    void saveClick(ClickEvent e) {
        doSave();
    }

    private void doSave() {
        saveButton.setEnabled(false);
        saveFormData();

        // BraketEntryPoint.tournaService.addTeam(downloadedTeam,
        // new AsyncCallback<Long>() {
        //
        // @Override
        // public void onFailure(Throwable caught) {
        // BraketEntryPoint.displayException(caught);
        // }
        //
        // @Override
        // public void onSuccess(Long result) {
        //
        // BraketEntryPoint
        // .displayToast("Team saved to the tournament.");
        //
        // BraketEntryPoint.currTournament.addTeam(downloadedTeam);
        //
        // reset();
        // // Keep the dialog open
        // espnId.setFocus(true);
        // }
        //
        // });
    }

    @UiHandler("cancelButton")
    void cancelMe(ClickEvent event) {
        hide();
    }

}
