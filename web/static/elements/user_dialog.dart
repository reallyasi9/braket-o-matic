@HtmlImport('user_dialog.html')
library braket.user_dialog;

import 'dart:html';

import 'package:polymer/polymer.dart';
import 'package:web_components/web_components.dart' show HtmlImport;

import 'package:polymer_elements/paper_dialog.dart';
import 'file_upload.dart';
import 'package:polymer_elements/paper_input.dart';
import 'package:polymer_elements/paper_icon_button.dart';
import 'package:polymer_elements/paper_dropdown_menu.dart';
import 'package:polymer_elements/paper_listbox.dart';
import 'package:polymer_elements/paper_item.dart';
import '../lib/user.dart';

@PolymerRegister('user-dialog')
class UserDialog extends PolymerElement {
    UserDialog.created() : super.created();

    @property
    String surname;

    @property
    String givenName;

    @property
    String nickname;

    @property
    String pictureURL;

    @property
    String favoriteTeam;

    @property
    bool withBackdrop = false;

    @reflectable
    openDialog() async {
        PaperDialog pd = $["dialog"];
        pd.open();
    }

    // Modal dialogs that are not direct children of the <body> element are broken.
    @reflectable
    patchOverlay(CustomEventWrapper e, [_]) async {
        PaperDialog d = e.target;
        if (d.withBackdrop) {
            d.parentNode.insertBefore(d.backdropElement, d);
        }
    }

    @reflectable
    clearInput(CustomEventWrapper e, [_]) async {
        PaperIconButton b = e.target;
        PaperInput i = b.parentNode;
        i.updateValueAndPreserveCaret("");
    }

    @reflectable
    clearPicture(CustomEventWrapper e, [_]) async {
        // TODO
    }

    @reflectable
    handleChange(CustomEventWrapper e, [_]) async {
        // I guess this works, but it doesn't propogate up...
        User u = new User(this.surname, this.givenName, this.nickname, this.favoriteTeam);
        try {
            HttpRequest.request("/backend/user", method: "PUT", sendData: u);
        } catch (e) {
            print("Shoot!  Couldn't write user data!");
        }
    }

}
