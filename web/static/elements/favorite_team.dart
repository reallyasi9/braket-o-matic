@HtmlImport('favorite_team.html')
library braket.favorite_team;

import 'package:polymer/polymer.dart';
import 'package:web_components/web_components.dart' show HtmlImport;

import 'dart:html';
import '../lib/team.dart';

@PolymerRegister('favorite-team')
class FavoriteTeam extends PolymerElement {
    FavoriteTeam.created() : super.created();

    factory FavoriteTeam(Team team) {
        FavoriteTeam ft = document.createElement('favorite-team');
        ft.set('team', team);
    }

    Team team;
}
