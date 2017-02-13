@HtmlImport('admin_games_page.html')
library braket.admin_games_page;

import 'package:polymer/polymer.dart';
import 'package:web_components/web_components.dart' show HtmlImport;

import '../lib/game.dart';
import 'round_scroller.dart';
import 'package:polymer_elements/iron_ajax.dart';
import 'package:polymer_elements/iron_request.dart';

@PolymerRegister('admin-games-page')
class AdminGamesPage extends PolymerElement {
  AdminGamesPage.created() : super.created();

  @property
  List<Game> games = [];

  @reflectable
  handleGames(CustomEventWrapper e, IronRequest detail) async {
    clear('games');
    List<Game> newGames = detail.response.map((Object o) => new Game()..initFromMap(convertToDart(o))).toList();
    addAll('games', newGames);
    // ($['registrations-get'] as IronAjax).generateRequest();
  }
}
