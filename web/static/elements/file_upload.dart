/*
@license
Original code Copyright (c) 2015 Winston Howes. All rights reserved.
This code may only be used under the MIT license found at https://github.com/winhowes/file-upload/blob/master/LICENSE
Modified for use with Polymer Dart by Phil Killewald (c) 2016.
*/
@HtmlImport('file_upload.html')
library braket.file_upload;

import 'dart:html';
import 'package:polymer/polymer.dart';
import 'package:web_components/web_components.dart' show HtmlImport;
import 'package:polymer_elements/paper_progress.dart';
import 'package:polymer_elements/paper_button.dart';
import 'package:polymer_elements/iron_icons.dart';

class ExtendedFile {
    File file;
    int progress = 0;
    bool error = false;
    bool complete = false;
    String name = "";
    HttpRequest xhr = new HttpRequest();

    ExtendedFile(File this.file) {
      this.name = this.file.name;
    }
}

@PolymerRegister('file-upload')
class FileUpload extends PolymerElement {
  FileUpload.created() : super.created();
  /**
  * Fired when a response is received status code 2XX.
  *
  * @event success
  */
  /**
  * Fired when a response is received other status code.
  *
  * @event error
  */
  /**
  * Fired when a file is about to be uploaded.
  *
  * @event before-upload
  */

  /**
  * `target` is the target url to upload the files to.
  * Additionally by adding "<name>" in your url, it will be replaced by
  * the file name.
  */
  @property
  String target = "";

  /**
  * `accept` is the set of comma separated file extensions or mime types
  * to filter as accepted.
  */
  @property
  String accept = "";

  /**
  * `progressHidden` indicates whether or not the progress bar should be hidden.
  */
  @property
  bool progressHidden = false;

  /**
  * `droppable` indicates whether or not to allow file drop.
  */
  @property
  bool droppable = false;

  /**
  * `dropText` is the  text to display in the file drop area.
  */
  @property
  String dropText = "Drop Files Here";

  /**
  * `multi` indicates whether or not to allow multiple files to be uploaded.
  */
  @property
  bool multi = false;

  /**
  * `files` is the list of files to be uploaded
  */
  List<ExtendedFile> files = [];

  /**
  * `method` is the http method to be used during upload
  */
  @property
  String method = "PUT";

  /**
  * `raised` indicates whether or not the button should be raised
  */
  @property
  bool raised = false;

  /**
  * `noink` indicates that the button should not have an ink effect
  */
  @property
  bool noink = false;

  /**
  * `headers` is a key value map of header names and values
  */
  @property
  Map<String, String> headers = {};

  /**
  * `retryText` is the text for the tooltip to retry an upload
  */
  @property
  String retryText = 'Retry Upload';

  /**
  * `removeText` is the text for the tooltip to remove an upload
  */
  @property
  String removeText = 'Remove';

  /**
  * `successText` is the text for the tooltip of a successful upload
  */
  @property
  String successText = 'Success';

  /**
  * `errorText` is the text to display for a failed upload
  */
  @property
  String errorText = 'Error uploading file...';

  /**
  * `additional` object of key-pair values to send additional values along with file.
  */
  @property
  Map<String, String> additional = {};

  /**
  * `manualUpload` indiciates that when a files is selected it will <b>NOT</b> be automatically uploaded. `uploadFile` will have to be called programmatically on the files to upload.
  */
  @property
  bool manualUpload = false;

  /**
   * `shownDropText` determines whether or not the drop text should be shown.
   */
  @property
  bool shownDropText = false;

  /**
  * Clears the list of files
  */
  void clearFiles() {
    this.files.clear();
    this.showDropText();
  }

  void ready() {
    if (this.raised) {
      this.toggleAttribute("raised", true, $['button']);
    }
    if (this.noink) {
      this.toggleAttribute("noink", true, $['button']);
    }
    if (this.droppable) {
      this.showDropText();
    }
  }

  /**
  * Listen to drag-over
  */
  @Listen('dragover')
  void handleDragOver(MouseEvent event, [_]) {
    if (this.droppable) {
      event.stopPropagation();
      this.toggleClass("hover", true, $['UploadBoarder']);
    }
  }

  /**
  * Listen to drag-leave
  */
  @Listen('dragleave')
  void handleDragLeave(MouseEvent event, [_]) {
    if (this.droppable) {
      event.stopPropagation();
      this.toggleClass("hover", false, $['UploadBoarder']);
    }
  }

  /**
  * A function to set up a drop area for drag-and-drop file uploads
  */
  @Listen('ondrop')
  void handleDrop(MouseEvent event, [_]) {
    this.toggleClass("hover", false, $['UploadBoarder']);
    event.preventDefault();
    for (File file in event.dataTransfer.files) {

      // Check if multiple upload is allowed
      if (!this.multi && this.files.isNotEmpty) {
        return;
      }

      // Check if filetype is accepted
      String mimeType = (file.type.isNotEmpty ? file.type.split("/").first : null);

      String fileType = file.name.split(".").last;
      if (this.accept.isNotEmpty && !(this.accept.contains(mimeType) || this.accept.contains(fileType))) {
        return;
      }

      ExtendedFile eFile = new ExtendedFile(file);
      this.files.add(eFile);
      if (!this.manualUpload) {
        this.uploadFile(eFile);
      }
    }
  }


  /**
  * Clicks the invisible file input
  */
  @reflectable
  void fileClick(CustomEventWrapper e, [_]) {
    Element elem = $['fileInput'];
    MouseEvent evt = new MouseEvent("click");
    elem.dispatchEvent(evt);
  }

  /**
  * Called whenever the list of selected files changes
  */
  @reflectable
  void filesChange(CustomEventWrapper e, [_]) {
    InputElement ele = e.target;
    ele.files.forEach((File f) => this.files.add(new ExtendedFile(f)));

    if (this.manualUpload) {
      return;
    }
    this.files.forEach(this.uploadFile);
  }

  /**
  * Cancels the file upload for a specific file
  *
  * @param {object} a file, an element of the files array
  */
  void cancel() {
    for (ExtendedFile file in this.files.where((ExtendedFile f) => !f.complete)) {
      file.xhr.abort();
      this.files.remove(file);
    }
    this.showDropText();
  }

  /**
  * Cancels the file upload
  *
  * @param {object}, an event object
  */
  @reflectable
  void cancelUpload(Event e, [_]) {
    this.cancel();
  }

  /**
  * Retries to upload the file
  *
  * @param {object}, an event object
  */
  @reflectable
  void retryUpload(Event e, [_]) {
    for (ExtendedFile file in this.files.where((ExtendedFile f) => !f.complete)) {
      file.progress = 0;
      file.error = false;
      this.uploadFile(file);
    }
  }

  /**
  * Whether or not to display the drop text
  */
  void showDropText() {
    this.shownDropText = (this.files.isNotEmpty && this.droppable);
  }

  /**
  * Uploads a file
  *
  * @param {object} a file, an element of the files array
  */
  void uploadFile(ExtendedFile eFile) {

    this.fire('before-upload', detail: eFile);
    this.showDropText();

    FormData formData = new FormData();

    // Add additional data to send with the POST variable
    this.additional.forEach(formData.append);

    // Add the file data last to support POSTing to Amazon Web Services S3.
    formData.appendBlob("file", eFile.file, eFile.file.name);

    // Set up the request URL and headers
    String url = this.target.replaceAll("<name>", eFile.file.name);
    eFile.xhr.open(this.method, url);
    this.headers.forEach(eFile.xhr.setRequestHeader);

    // Add progress indicator (with closure)
    FileUpload self = this;
    eFile.xhr.onProgress.listen((ProgressEvent e) {
      int done = e.loaded;
      int total = e.total;
      eFile.progress = ((done/total*1000.0)/10.0).floor();
    });

    // Set up what to do when load is complete
    eFile.xhr.onLoad.listen((ProgressEvent e) {
      self.fire("success", detail: eFile);
      eFile.complete = true;
    }, onError: () {
      eFile.error = true;
      eFile.complete = false;
      eFile.progress = 100;
      self.updateStyles();
      self.fire("error", detail: eFile);
    });

    // Fire away.
    eFile.xhr.send(formData);
  }
}
