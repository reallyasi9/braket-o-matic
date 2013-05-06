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

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author paste
 * 
 */
public class BracketCopyright extends Composite {

    private static BracketCopyrightUiBinder uiBinder = GWT
            .create(BracketCopyrightUiBinder.class);

    interface BracketCopyrightUiBinder extends
            UiBinder<Widget, BracketCopyright> {
    }

    public BracketCopyright() {
        initWidget(uiBinder.createAndBindUi(this));

        StringBuilder copyrightTextBuilder = new StringBuilder();
        copyrightTextBuilder
                .append("copyright ")
                .append(DateTimeFormat.getFormat(
                        DateTimeFormat.PredefinedFormat.YEAR)
                        .format(new Date()))
                .append(" &lt; a. carter | a. killewald &otimes; n. killewald | p. killewald &gt;");

        copyright.setHTML(copyrightTextBuilder.toString());
    }

    @UiField
    InlineHTML copyright;

}
