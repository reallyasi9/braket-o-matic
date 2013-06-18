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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.exclaimindustries.paste.braket.client.BraketGame;
import net.exclaimindustries.paste.braket.client.BraketTeam;
import net.exclaimindustries.paste.braket.client.BraketUser;
import net.exclaimindustries.paste.braket.client.ExpectedValueService;
import net.exclaimindustries.paste.braket.client.ExpectedValueServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author paste
 * 
 */
public class ExciteOMatic extends ScrollPanel {

    private static ExciteOMaticUiBinder uiBinder = GWT
            .create(ExciteOMaticUiBinder.class);

    interface ExciteOMaticUiBinder extends UiBinder<Widget, ExciteOMatic> {
    }

    private static final ExpectedValueServiceAsync EXPECTO_SERVICE = GWT
            .create(ExpectedValueService.class);

    @UiField
    VerticalPanel panel;

    private final BraketUser user;
    private final List<BraketGame> games;
    private final Map<Long, BraketTeam> teamMap =
            new HashMap<Long, BraketTeam>();

    public ExciteOMatic(BraketUser u, List<BraketGame> g, List<BraketTeam> t) {
        user = u;
        games = g;
        for (BraketTeam team : t) {
            teamMap.put(team.getId(), team);
        }

        setWidget(uiBinder.createAndBindUi(this));

        panel.setWidth("100%");
        // setHeight("100%");
        panel.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        panel.setSpacing(12);

    }

    public void reset() {

        // Get the expected values
        EXPECTO_SERVICE.getConditionalExpectedValues(user.getId(),
                new AsyncCallback<Map<Long, Double>>() {

                    @Override
                    public void onFailure(Throwable caught) {
//                        BraketEntryPoint.displayException(caught);
                    }

                    @Override
                    public void onSuccess(Map<Long, Double> result) {
                        panel.clear();
                        if (!result.containsKey(Long.valueOf(0l))) {
//                            BraketEntryPoint
//                                    .displayToast("Expect-o-Matic is hashing results.  Try again in a few minutes.");
                            return;
                        }
                        double totalValue = result.get(Long.valueOf(0l));
                        for (BraketGame game : games) {
                            Long topTeamId = game.getTopTeamId();
                            Long bottomTeamId = game.getBottomTeamId();

                            double topValue = 0;
                            if (result.containsKey(topTeamId)) {
                                topValue = result.get(topTeamId);
                                if (topValue != topValue) {
                                    // NaNs all over the place...
                                    topValue = totalValue;
                                }
                            }
                            double bottomValue = 0;
                            if (result.containsKey(bottomTeamId)) {
                                bottomValue = result.get(bottomTeamId);
                                if (bottomValue != bottomValue) {
                                    bottomValue = totalValue;
                                }
                            }

                            ExciteOMaticGame eomGame =
                                    new ExciteOMaticGame(game, teamMap
                                            .get(topTeamId), teamMap
                                            .get(bottomTeamId), topValue
                                            - totalValue, bottomValue
                                            - totalValue);
                            panel.add(eomGame);
                        }
                    }
                });

    }
}
