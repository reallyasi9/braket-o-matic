@HtmlImport('favorite_team.html')
library braket.favorite_team;

import 'package:polymer/polymer.dart';
import 'package:web_components/web_components.dart' show HtmlImport;

import 'dart:html';
import '../lib/team.dart';

@PolymerRegister('favorite-team')
class FavoriteTeam extends PolymerElement {
    FavoriteTeam.created() : super.created();

    @property
    Team team;

    @property
    String background;

    @Observe("team")
    handleTeam(Team newTeam) {
        if (newTeam.colors.isEmpty) {
            this.setAttribute('style', "background: #888888");
        } else if (newTeam.colors.length == 1) {
            this.setAttribute('style', "background: ${newTeam.colors[0]}");
        } else {
            String colors = newTeam.colors.join(",");
            this.setAttribute('style', "background: linear-gradient(100deg,$colors)");
        }
    }
}
