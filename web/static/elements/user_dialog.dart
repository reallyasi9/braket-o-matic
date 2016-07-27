@HtmlImport('user_dialog.html')
library braket.user_dialog;

import 'dart:html';
import 'dart:async';

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

const Duration _TIMEOUT = const Duration(seconds: 1);

@PolymerRegister('user-dialog')
class UserDialog extends PolymerElement {
    UserDialog.created() : super.created();

    @Property(notify: true)
    String givenName;

    @Property(notify: true)
    String surname;

    @Property(notify: true)
    String nickname;

    @Property(notify: true)
    String picture;

    @property
    bool withBackdrop = false;

    Timer _debounceTimer = new Timer(_TIMEOUT, () => {});

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
        // For now, handle all values
        this.set("givenName", ($["given-name-input"] as PaperInput).value);
        this.set("surname", ($["surname-input"] as PaperInput).value);
        this.set("nickname", ($["nickname-input"] as PaperInput).value);
        // I guess this works, but it doesn't propogate up...

        if (_debounceTimer.isActive) {
            this._debounceTimer.cancel();
        }
        _debounceTimer = new Timer(_TIMEOUT, () {
            this._upload();
        });
    }

    _upload() async {
        User u = new User(this.surname, this.givenName, this.nickname, null /* for now */);
        try {
            HttpRequest.request("/backend/user", method: "PUT", sendData: u);
        } catch (err) {
            print("Shoot!  Couldn't write user data!");
        }
    }

}
