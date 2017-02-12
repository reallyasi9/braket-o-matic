import 'package:polymer/polymer.dart';
import 'package:exportable/exportable.dart';

@export
class Game extends JsProxy with Exportable {

  @reflectable
  @export
  int id;

  // Do I need the parent?
  @reflectable
  @export
  int tournamentID;

  @reflectable
  @export
  int topTeam;

  @reflectable
  @export
  int bottomTeam;

  @reflectable
  @export
  bool topTeamDefined;

  @reflectable
  @export
  bool bottomTeamDefined;

  @reflectable
  @export
  int winner;

  @reflectable
  @export
  int winnerTopBottom;

  @reflectable
  @export
  int game;

  @reflectable
  @export
  int round;

  @reflectable
  @export
  int gameInRound;
}
