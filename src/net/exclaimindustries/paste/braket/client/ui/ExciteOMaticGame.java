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

import net.exclaimindustries.paste.braket.client.Game;
import net.exclaimindustries.paste.braket.client.Team;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author paste
 * 
 */
public class ExciteOMaticGame extends ResizeComposite {

    interface MyStyle extends CssResource {
        String positive();

        String negative();

        String leftArrow();

        String rightArrow();

        String headerCell();
    }

    private static ExciteOMaticGameUiBinder uiBinder = GWT
            .create(ExciteOMaticGameUiBinder.class);

    interface ExciteOMaticGameUiBinder extends
            UiBinder<Widget, ExciteOMaticGame> {
    }

    @UiField
    Label topTeamName;

    @UiField
    Label bottomTeamName;

    @UiField
    Image topTeamImage;

    @UiField
    Image bottomTeamImage;

    @UiField
    Label arrow;

    @UiField
    Label topTeamChange;

    @UiField
    Label bottomTeamChange;

    private final NumberFormat changeFormat = NumberFormat
            .getFormat("+0.00;-0.00");

    @UiField
    MyStyle style;

    public ExciteOMaticGame(Game game, Team topTeam,
            Team bottomTeam, double topChange, double bottomChange) {
        initWidget(uiBinder.createAndBindUi(this));

        setHeight("180px");
        setWidth("360px");

        topTeamName.setText(topTeam.getName().getDisplayName());
        bottomTeamName.setText(bottomTeam.getName().getDisplayName());
        topTeamImage.setUrl(topTeam.getPicture());
        bottomTeamImage.setUrl(bottomTeam.getPicture());
        topTeamChange.setText(changeFormat.format(topChange));
        bottomTeamChange.setText(changeFormat.format(bottomChange));
        if (topChange > bottomChange) {
            topTeamChange.addStyleName(style.positive());
            bottomTeamChange.addStyleName(style.negative());
            arrow.addStyleName(style.leftArrow());
        } else {
            bottomTeamChange.addStyleName(style.positive());
            topTeamChange.addStyleName(style.negative());
            arrow.addStyleName(style.rightArrow());
        }

    }

}
