@HtmlImport('round_scroller.html')
library braket.round_scroller;

import 'package:polymer/polymer.dart';
import 'package:web_components/web_components.dart' show HtmlImport;

import 'game_container.dart';
import '../lib/game.dart';

@PolymerRegister('round-scroller')
class RoundScroller extends PolymerElement {
  RoundScroller.created() : super.created();

  // Collection of games to display, sorted by round/game (hopefully)
  @property
  List<Game> games = [];

  @property
  int currentRound = 0;

  // Return the games in a given round, in round order.  Rounds begin at 0.
  @reflectable
  bool inCurrentRound(Game g) => g.round == currentRound;

}
