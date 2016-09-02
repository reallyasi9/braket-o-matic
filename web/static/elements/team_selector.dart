@HtmlImport('team_selector.html')
library braket.team_selector;

import 'package:polymer/polymer.dart';
import 'package:web_components/web_components.dart' show HtmlImport;

import 'dart:html';
import 'dart:collection';
import 'dart:convert';
import 'dart:async';

import 'package:polymer_elements/paper_input.dart';
import 'package:polymer_elements/paper_material.dart';
import 'package:polymer_elements/paper_item.dart';
import 'package:polymer_elements/paper_ripple.dart';
import 'package:polymer_elements/paper_icon_button.dart';
import 'package:polymer_elements/iron_icons.dart';
import '../lib/team.dart';
import 'favorite_team.dart';


const Duration _TIMEOUT = const Duration(milliseconds: 300);


@PolymerRegister('team-selector')
class TeamSelector extends PolymerElement {
    TeamSelector.created() : super.created();

    /**
    * `errorMessage` The error message to display when the input is invalid.
    */
    @property
    String errorMessage = "Please select a team";

    /**
    * `label` Text to display as the input label
    */
    @property
    String label = "Favorite team";

    /**
    * `noLabelFloat` Set to true to disable the floating label.
    */
    @property
    bool noLabelFloat = false;

    /**
    * `required` Set to true to mark the input as required.
    */
    @property
    bool required = false;

    /**
    * `value` Selected object from the suggestions
    */
    @Property(notify: true)
    Team team;

    @property
    int minLength = 3;

    /**
    * `suggestions` Array with the actual suggestions to display
    */
    @Property(notify: true)
    List<Team> suggestions = new List<Team>();

    int _currentIndex = -1;

    int _scrollIndex = 0;

    int _maxViewableItems = 5;

    // In pixels
    // TODO: get this from the displayed element instead of fixing the value here.
    int _itemHeight = 36;

    // The actual selected team
    Team _team = null;

    // A timer to clear the selection window
    Timer _hideTimer = new Timer(_TIMEOUT, () => {});


    // Element Lifecycle
    void ready() {
        this._team = this.team;
    }


    // Callback after typing something
    @Listen('keyup')
    void handleKeypress(CustomEventWrapper event, [_]) {
        KeyboardEvent ke = event.original as KeyboardEvent;
        int which = ke.which;
        if (which == 40) {
            // scroll down
            _keydown();
        } else if (which==38) {
            // scroll up
            _keyup();
        } else if (which==13) {
            // finalize selection
            _keyenter();
        } else {
            // get suggestions
            _fetchSuggestions();
        }
    }


    // Either ask for suggestions or wait until there are enough characters to search
    void _fetchSuggestions() {
        String searchValue = $['input'].value;

        if (searchValue.length >= this.minLength) {
            _handleChange(searchValue);
        } else {
            $['clear'].style.display = 'none';
            this.suggestions.clear();
        }
    }


    // Ask the backend for suggestions
    _handleChange(String search) async {
        try {
            HttpRequest.getString("/backend/search-teams?s=$search").then(_updateSuggestions);
        } catch (e) {
            print("Shoot!  Couldn't access the login URL! $e");
        }
    }


    // Parse the returned suggestions and build the elements to select
    _updateSuggestions(String jsonMessage) async {
        this.suggestions.clear();
        List<Team> suggestedTeams = JSON.decode(jsonMessage);
        _bindSuggestions(suggestedTeams);
    }


    /**
    * Clears the input text
    */
    @reflectable
    void handleClear(CustomEventWrapper event, [_]) {
        this.team = this._team;
        $['clear'].style.display = 'none';
        _hideSuggestionsWrapper();
        _emptyItems();
        _fireEvent('reset');
        print("CLEAR HAPPENED?");
    }


    /**
    * Hide the suggestions wrapper
    */
    void _hideSuggestionsWrapper() {
        $['suggestionsWrapper'].style.display = 'none';
    }

    /**
    * Set the given list of teams as the suggested teams, and reset the
    * displayed list (called after the suggested teams have been determined).
    */
    void _bindSuggestions(List<Team> arr) {
        if (arr != null && arr.isNotEmpty) {
            this.suggestions = arr;
            this.notifyPath('suggestions', this.suggestions);
            this._currentIndex = -1;
            this._scrollIndex = 0;
            $['clear'].style.display = 'block';
            $['suggestionsWrapper'].style.display = 'block';
        } else {
            $['clear'].style.display = 'none';
            this.suggestions.clear();
            this.notifyPath('suggestions', this.suggestions);
        }
    }


    void _selection(int index) {
        this._team = this.suggestions[index];
        this.team = this._team;
        //this.text = selectedTeam.schoolShortName;
        $['clear'].style.display = 'none';
        _emptyItems();
        _fireEvent('selected');
        hideSuggestions();
    }


    // Get all of the suggestions as elements
    ElementList<Element> _getItems() {
        return querySelectorAll('paper-item');
    }


    // Clear all the suggestions.
    void _emptyItems() {
        this.suggestions.clear();
    }


    // The the ID of this element (why?)
    String _getId(){
        String id = getAttribute('id');
        if (id == null || id.isEmpty) {
            // Allows access to custom elements of object?
            id = this.dataset['id'];
        }
        return id;
    }


    // Remove the "active" class from the given elements.
    void _removeActive(ElementList<Element> items) {
        items.forEach((Element e) {
            e.classes.remove('active');
        });
    }


    // Pressing down arrow will select the next suggestion
    void _keydown() {
        ElementList<Element> items = _getItems();
        int length = items.length - 1;
        if (this._currentIndex < length) {
            _removeActive(items);
            this._currentIndex++;
            items[this._currentIndex].classes.add('active');
            _scrollDown();
        }
    }


    // Pressing up arrow will select the previous suggestion
    void _keyup() {
        ElementList<Element> items = _getItems();
        if (this._currentIndex > 0) {
            _removeActive(items);
            this._currentIndex--;
            items[this._currentIndex].classes.add('active');
            _scrollUp();
        }
    }


    // Pressing enter will confirm your selection.
    void _keyenter() {
        if ($['suggestionsWrapper'].style.display == 'block' && this._currentIndex > -1) {
            this._selection(this._currentIndex);
        }
    }


    // Scroll to the next item, if we are doing that scroll thing.
    void _scrollDown() {
        int viewIndex = this._currentIndex - this._scrollIndex;
        if (viewIndex >= this._maxViewableItems) {
            this._scrollIndex++;
            int scrollTop = (this._scrollIndex * this._itemHeight);
            PaperMaterial pm = this.querySelector('paper-material');
            pm.scrollTop = scrollTop;
        }
    }


    // Scroll to the previous item, if we are doing that scroll thing.
    void _scrollUp() {
        int viewIndex = this._currentIndex - this._scrollIndex;
        if (viewIndex < 0) {
            this._scrollIndex--;
            int scrollTop = (this._scrollIndex * this._itemHeight);
            PaperMaterial pm = this.querySelector('paper-material');
            pm.scrollTop = scrollTop;
        }
    }


    // A custom way to fire an event (why?)
    void _fireEvent(String evt) {
        String eventString = 'autocomplete-$evt';
        this.fire(eventString, detail: {
            "id": this._getId(),
            "option": this._team,
            "target": this
        });
    }


    // When the selection changes
    @Listen('select')
    void handleSelect(CustomEventWrapper event, [_]) {
        // I really hope this is there...
        int index = event.detail.index;
        this._selection(index);
    }


    // When you click out of the input
    @Listen('blur')
    void handleBlur(CustomEventWrapper event, [_]) {
        _fireEvent('blur');
        hideSuggestions();
    }


    // When you focus on the input
    @Listen('focus')
    void handleFocus(CustomEventWrapper event, [_]) {
        _fireEvent('focus');
    }


    // Disable the input and hide the suggestions wrapper
    void disable() {
        _hideSuggestionsWrapper();
        $['input'].disabled = true;
    }


    // Enable the input
    void enable() {
        $['input'].disabled = false;
    }


    // In case validation is a thing
    bool validate() {
        return $['input'].validate();
    }


    // Deselect the team
    void reset() {
        this.team = this._team;
    }

    // Hide the suggestion window after a timeout
    void hideSuggestions() {
        if (_hideTimer.isActive) {
            _hideTimer.cancel();
        }
        _hideTimer = new Timer(_TIMEOUT, () {
            _hideSuggestionsWrapper();
        });
    }

}
