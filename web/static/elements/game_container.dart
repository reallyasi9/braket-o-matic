@HtmlImport('game_container.html')
library braket.game_container;

import 'package:polymer/polymer.dart';
import 'package:web_components/web_components.dart' show HtmlImport;

import 'slot_container.dart';
import '../lib/game.dart';

@PolymerRegister('game-container')
class GameContainer extends PolymerElement {
  GameContainer.created() : super.created();

  // Game displayed
  @property
  Game game;

}
