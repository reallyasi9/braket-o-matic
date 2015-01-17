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

import net.exclaimindustries.paste.braket.client.User;
import net.exclaimindustries.paste.braket.client.UserName;
import net.exclaimindustries.paste.braket.client.resources.Resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author paste
 * 
 */
public class UserDialog extends DialogBox {

    interface UserDialogUiBinder extends UiBinder<Widget, UserDialog> {
    }

    private static UserDialogUiBinder uiBinder = GWT
            .create(UserDialogUiBinder.class);

    private User user;

    private final static Resources res = GWT.create(Resources.class);

    static {
        res.style().ensureInjected();
    }

    @UiField
    Label name;

    @UiField
    TextBox firstName;

    @UiField
    TextBox lastName;

    @UiField
    TextBox nickname;

    @UiField
    Button saveButton;

    @UiField
    Button cancelButton;

    public UserDialog() {
        super(false, true);
        setWidget(uiBinder.createAndBindUi(this));
        getCaption().setText("user options");
        setAnimationEnabled(true);
        setGlassEnabled(true);
        addStyleName(res.style().dialogBox());
        setGlassStyleName(res.style().glass());

        saveButton.setEnabled(false);
    }

    public void setUser(User user) {
        this.user = user;
        name.setText(user.getName().getFullName("unnamed user"));
        firstName.setText(user.getName().getFirstName());
        lastName.setText(user.getName().getLastName());
        nickname.setText(user.getName().getNickname());
    }

    @Override
    protected void beginDragging(MouseDownEvent e) {
        e.preventDefault();
    }

    protected void updateName() {
        UserName userName = new UserName(firstName.getText(),
                lastName.getText(), nickname.getText());
        user.setName(userName);
        name.setText(user.getName().getFullName("unnamed user"));
    }

    protected void saveUser() {
//        BraketEntryPoint.userService.storeUser(user,
//                new AsyncCallback<String>() {
//
//                    @Override
//                    public void onFailure(Throwable caught) {
//                        BraketEntryPoint.displayException(caught);
//                    }
//
//                    @Override
//                    public void onSuccess(String result) {
//                        // The user ID shouldn't change with this operation, so
//                        // just
//                        // close the window.
//                        hide();
//                        BraketEntryPoint
//                                .displayToast("User successfully updated");
//                    }
//
//                });
    }

    @UiHandler("firstName")
    void firstNameKeyPress(KeyUpEvent event) {
        updateName();
        saveButton.setEnabled(true);
    }

    @UiHandler("lastName")
    void lastNameKeyPress(KeyUpEvent event) {
        updateName();
        saveButton.setEnabled(true);
    }

    @UiHandler("nickname")
    void nicknameKeyPress(KeyUpEvent event) {
        updateName();
        saveButton.setEnabled(true);
    }

    @UiHandler("saveButton")
    void saveMe(ClickEvent event) {
        saveButton.setEnabled(false);
        saveUser();
    }

    @UiHandler("cancelButton")
    void cancelMe(ClickEvent event) {
        saveButton.setEnabled(false);
        UserName userName = user.getName();
        firstName.setText(userName.getFirstName());
        lastName.setText(userName.getLastName());
        nickname.setText(userName.getNickname());
        hide();
    }
    
    public User getUser() {
        return user;
    }

}
