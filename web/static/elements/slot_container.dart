@HtmlImport('slot_container.html')
library braket.slot_container;

import 'package:polymer/polymer.dart';
import 'package:web_components/web_components.dart' show HtmlImport;

import '../lib/team.dart';

@PolymerRegister('slot-container')
class SlotContainer extends PolymerElement {
  SlotContainer.created() : super.created();

  // Team that occupies this slot (can be null) FIXME: should be a Team!
  @property
  int team;

  // The seed of the team in this slot (only for display purposes)
  @property
  int seed = 0;

  // Score for this slot
  @property
  int score = 0;

  // Whether or not this slot has been selected
  @property
  bool selected = false;

  // Whether or not this slot contains the winner of the game
  @property
  bool winner = false;

  // Whether or not this slot is eliminated
  @property
  bool eliminated = false;

}
