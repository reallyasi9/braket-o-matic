@HtmlImport('team_selector.html')
library braket.team_selector;

import 'package:polymer/polymer.dart';
import 'package:web_components/web_components.dart' show HtmlImport;

import 'dart:html';
import 'dart:collection';
import 'dart:convert';
import 'package:polymer_elements/paper_input.dart';
import '../lib/team.dart';
import 'favorite_team.dart';


class AutoselectOption {
    AutoselectOption(String this.text, FavoriteTeam this.team);

    String text;
    FavoriteTeam team;
}


@PolymerRegister('team-selector')
class TeamSelector extends PolymerElement {
  TeamSelector.created() : super.created();

    /**
    * `autoValidate` Set to true to auto-validate the input value.
    */
    @property
    bool autoValidate = false;

    /**
    * `errorMessage` The error message to display when the input is invalid.
    */
    @property
    String errorMessage;

    /**
     * `label` Text to display as the input label
     */
    @property
    String label;

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
     * `source` Array of objects with the options to execute the autocomplete feature
     * TODO: Still working this out...
     */
    @property
    Map<String, FavoriteTeam> source = new SplayTreeMap<String, FavoriteTeam>();

    /**
     *
     */
    @property
    String textProperty = 'text';

    @property
    String valueProperty = 'value';

    /**
     * `value` Selected object from the suggestions
     * TODO: Still working this out...
     */
    @Property(notify: true)
    FavoriteTeam team;

    @Property(notify: true)
    String text;

    @property
    bool disableShowClear = false;

    @property
    bool remoteSource = false;

    @property
    String eventNamespace = '-';

    @property
    int minLength = 1;

    /**
     * `_suggestions` Array with the actual suggestions to display
     */
    List<FavoriteTeam> _suggestions = new List<FavoriteTeam>();

    int _currentIndex = -1;

    int _scrollIndex = 0;

    int _maxViewableItems = 7;

    double _itemHeight = 36.0;

    FavoriteTeam _team = null;

    String _text = null;


    // Element Lifecycle
    void ready() {
        this._team = this.team;
    }


    // Callback after typing something
    @reflectable
    void _onKeypress(KeyEvent event, [_]) {
        int which = event.which;
        if (which == 40) {
            // scroll down
            this._keydown();
        } else if (which==38) {
            // scroll up
            this._keyup(event);
        } else if (which==13) {
            // finalize selection
            this._keyenter();
        } else {
            // get suggestions
            this._fetchSuggestions(event);
        }
    }


    // Either ask for suggestions or wait until there are enough characters to search
    void _fetchSuggestions(KeyEvent event) {
        PaperInput target = event.target as PaperInput;
        String searchValue = target.value;

        if (searchValue.length >= this.minLength) {
            this._fireEvent(this.getOption(), 'change'); // TODO send the search?
        } else {
            $['clear'].style.display = 'none';
            this._suggestions.clear();
        }
    }


    // Ask the backend for suggestions
    @reflectable
    void _onChange(PolymerEvent event, dynamic detail) {
        String search = detail.option.text;
        try {
            HttpRequest.getString("/backend/search-teams?s=$search").then(_updateSuggestions);
        } catch (e) {
            print("Shoot!  Couldn't access the login URL! $e");
        }
    }


    // Parse the returned suggestions and build the elements to select
    _updateSuggestions(String jsonMessage) async {
        this._suggestions.clear();
        List<Team> suggestedTeams = JSON.decode(jsonMessage);
        for (Team team in suggestedTeams) {
            FavoriteTeam favTeam = document.createElement('favorite-team') as FavoriteTeam;
            favTeam.set('team', team);
            this._suggestions.add(favTeam);
        }
    }


    /**
    * Clears the input text
    */
    void _clear() {
        this.team = this._team;
        this.text = this._text;
        $['clear'].style.display = 'none';
        this._hideSuggestionsWrapper();
        this._emptyItems();
        this._fireEvent(this.getOption(), 'reset');
    }


    /**
    * Hide the suggestions wrapper
    */
    void _hideSuggestionsWrapper() {
        $['suggestionsWrapper'].style.display = 'none';
    }


    void _handleSuggestions(event) {
        if (!this.remoteSource) {
            this._createSuggestions(event);
        } else {
            this._remoteSuggestions(event);
        }
    }





    void _bindSuggestions(List<FavoriteTeam> arr) {
        if (arr.isNotEmpty) {
            this._suggestions = arr;
            this._currentIndex = -1;
            this._scrollIndex = 0;
            if (!this.disableShowClear) {
                $['clear'].style.display = 'block';
            }
            $['suggestionsWrapper'].style.display = 'block';
        } else {
            $['clear'].style.display = 'none';
            this._suggestions.clear();
        }
    }

    void _createSuggestions(event) {
        this._currentIndex = -1;
        this._scrollIndex = 0;
        String value = event.target.value.toLowerCase(); // TODO
        var minLength = this.minLength;

        if (value.length >= minLength) {
            // Shows the clear button.
            if (!this.disableShowClear) {
                $['clear'].style.display = 'block';
            }

            // Search for the word in the source properties.
            var length = this.source.length;
            if (length > 0) {
                this._suggestions.clear();

                String objText = '';
                String objValue = '';

                for (T item in this.source) { // TODO
                    objText = item[this.textProperty];
                    objValue = item[this.valueProperty];
                    if(objText.toLowerCase().startsWith(value)){
                        // Adds the item to the suggestions list.
                        this.push('_suggestions', {text : objText , value : objValue}); // TODO
                    }
                }
                if (this._suggestions.length > 0) {
                    $['suggestionsWrapper'].style.display = 'block';
                }else{
                    this._hideSuggestionsWrapper();
                }
            }
        } else {
            $['clear'].style.display = 'none';
            this._suggestions.clear();
        }
    }

  _selection : function(index) {
    var selectedOption = this._suggestions[index];
    var self = this;
    this.text = selectedOption.text;
    this.value = selectedOption.value;
    this._value=this.value;
    this._text=this.text;
    this.$.clear.style.display = 'none';
    this._emptyItems();
    this._fireEvent(selectedOption,'selected');
    setTimeout(function() {
      self._hideSuggestionsWrapper();
    }, 300);
  },

  _getItems:function(){
    return this.querySelectorAll('paper-item');
  },

  _emptyItems:function(){
    this._suggestions = [];
  },

  String _getId(){
    String id = this.getAttribute('id');
    if (id.isEmpty) {
        // Allows access to custom elements of object?
        id = this.dataset['id'];
    }
    return id;
  }

  _removeActive:function(items){
    for(var i=0;i<items.length;i++){
      items[i].classList.remove('active');
    }
  },

  _keydown:function(){
    var items=this._getItems();
    var length=items.length;
    length--;
    if(this._currentIndex < length){
      this._removeActive(items);
      this._currentIndex++;
      items[this._currentIndex].classList.add('active');
      this._scrollDown();
    }
  },

  _keyup:function(){
    var items=this._getItems();
    if(this._currentIndex >0){
      this._removeActive(items);
      this._currentIndex--;
      items[this._currentIndex].classList.add('active');
      this._scrollUp();
    }
  },

  _keyenter:function(){
    if(this.$.suggestionsWrapper.style.display == 'block' && this._currentIndex > -1){
      var index=this._currentIndex;
      this._selection(index);
    }
  },

  _scrollDown:function(){
    var viewIndex=this._currentIndex-this._scrollIndex;
    if(viewIndex >= this._maxViewableItems){
      this._scrollIndex++;
      var scrollTop=(this._scrollIndex * this._itemHeight);
      var paperMaterial=this.querySelector('paper-material');
      paperMaterial.scrollTop=scrollTop;
    }
  },

  _scrollUp:function(){
    var viewIndex=this._currentIndex-this._scrollIndex;
    if(viewIndex < 0){
      this._scrollIndex--;
      var scrollTop=(this._scrollIndex * this._itemHeight);
      var paperMaterial=this.querySelector('paper-material');
      paperMaterial.scrollTop=scrollTop;
    }
  },

  void _fireEvent(AutocompleteItem option, String evt) {
    String id = this._getId();
    String eventString = 'autocomplete' + this.eventNamespace + evt;
    this.fire(eventString, detail: {
        "id": id,
        "option": option,
        "target": this
    });
  }



  @reflectable
  void _onSelect(event, [_]) {
    var index = event.model.index;
    this._selection(index);
  }

  @reflectable
  void _onBlur(event, [_]) {
    var self = this;

    this._fireEvent(this.getOption(), 'blur');
    setTimeout(function() { // TODO
      self.$.clear.style.display = 'none';
      self._hideSuggestionsWrapper();
    }, 300);
  }

  @reflectable
  void _onFocus(event, [_]) {
    this._fireEvent(this.getOption(), 'focus');
  }


  AutocompleteItem getOption() {
    return new AutocompleteItem(this.text, this.value);
  }


  void setOption(AutocompleteItem option) {
     this.text=option.text;
     this.value=option.element;
  }


  void disable() {
    //this.disabled = true; // TODO ?
    $['input'].disabled=true;
  }

  void enable() {
    //this.disabled = false; // TODO ?
    $['input'].disabled=false;
  }

  void suggestions(List<FavoriteTeam> arr) {
    this._bindSuggestions(arr);
  }

  bool validate() {
    return $['input'].validate();
  }

  void reset() {
    this._value = null;
    this._text = '';
  }

  void hideSuggestions() {
    var self=this;
    setTimeout(function() { // TODO
      self.$.clear.style.display = 'none';
      self._hideSuggestionsWrapper();
    }, 300);
  }

}
