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

import net.exclaimindustries.paste.braket.client.resources.Resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author paste
 * 
 */
public class ExceptionDialog extends DialogBox implements HasText {

    private static final Binder uiBinder = GWT.create(Binder.class);

    interface Binder extends UiBinder<Widget, ExceptionDialog> {
    }

    private static Resources res = GWT.create(Resources.class);

    static {
        res.style().ensureInjected();
    }

    @UiField
    Button closeButton;
    @UiField
    Label errorText;

    public ExceptionDialog() {
        super(false, true);
        setWidget(uiBinder.createAndBindUi(this));
        getCaption().setText("dag nabbit!");
        setAnimationEnabled(true);
        setGlassEnabled(true);
        addStyleName(res.style().dialogBox());
        setGlassStyleName(res.style().glass());
    }

    @UiHandler("closeButton")
    void onClick(ClickEvent e) {
        hide();
    }

    @Override
    protected void beginDragging(MouseDownEvent e) {
        e.preventDefault();
    }

    @Override
    public void setText(String text) {
        errorText.setText(text);
    }

    @Override
    public String getText() {
        return errorText.getText();
    }

    public void setException(Throwable caught) {
        errorText.setText(caught.getLocalizedMessage());
    }

}
