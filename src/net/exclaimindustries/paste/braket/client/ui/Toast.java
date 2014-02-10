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

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author paste
 * 
 */
public class Toast extends PopupPanel implements HasText {

    private static ToastUiBinder uiBinder = GWT.create(ToastUiBinder.class);

    interface ToastUiBinder extends UiBinder<Widget, Toast> {
    }

    interface Style extends CssResource {

        String toastPopup();

        String messageToast();

        String errorToast();
    }

    @UiField
    Label toastText;

    @UiField
    FlowPanel toast;

    @UiField
    Style style;

    private Toast() {
        super(true, false);
        setWidget(uiBinder.createAndBindUi(this));
        setStyleName(style.toastPopup());
    }

    private void setErrorStyle() {
        toast.setStyleName(style.errorToast());
    }

    private void setMessageStyle() {
        toast.setStyleName(style.messageToast());
    }

    public static Toast showErrorToast(String message) {
        Toast errorToast = new Toast();
        errorToast.setErrorStyle();
        errorToast.setText(message);
        errorToast.show();
        return errorToast;
    }

    public static Toast showMessageToast(String message) {
        Toast messageToast = new Toast();
        messageToast.setMessageStyle();
        messageToast.setText(message);
        messageToast.show();
        return messageToast;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.google.gwt.user.client.ui.HasText#getText()
     */
    @Override
    public String getText() {
        return toastText.getText();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.google.gwt.user.client.ui.HasText#setText(java.lang.String)
     */
    @Override
    public void setText(String text) {
        toastText.setText(text);
    }

}
