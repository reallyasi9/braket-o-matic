/**
 * This file is part of braket-o-matic.
 *
 * braket-o-matic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * braket-o-matic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with braket-o-matic.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.exclaimindustries.paste.braket.server;

import net.exclaimindustries.paste.braket.client.BraketPrediction;
import net.exclaimindustries.paste.braket.client.Team;
import net.exclaimindustries.paste.braket.client.Tournament;
import net.exclaimindustries.paste.braket.client.BraketUser;
import net.exclaimindustries.paste.braket.client.Game;
import net.exclaimindustries.paste.braket.client.HeadToHeadGame;
import net.exclaimindustries.paste.braket.client.Selectable;
import net.exclaimindustries.paste.braket.server.backends.ExpectoValues;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;

public class OfyService {

  static {
    factory().register(Selectable.class);
    factory().register(Tournament.class);
    factory().register(BraketUser.class);
    factory().register(BraketPrediction.class);
    factory().register(Team.class);
    factory().register(Game.class);
    factory().register(HeadToHeadGame.class);
    factory().register(ExpectoValues.class);
    factory().register(CurrentTournament.class);
    factory().register(CurrentExpectOMatic.class);
  }

  public static Objectify ofy() {
    return ObjectifyService.ofy();
  }

  public static ObjectifyFactory factory() {
    return ObjectifyService.factory();
  }
}
