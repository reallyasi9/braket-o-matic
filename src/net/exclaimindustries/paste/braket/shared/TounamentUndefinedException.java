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

package net.exclaimindustries.paste.braket.shared;

/**
 * This exception notifies the recipient that a given tournament doesn't exist
 * in the object store.
 * 
 * @author paste
 * 
 */
public class TounamentUndefinedException extends Exception {

  public TounamentUndefinedException(String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  private static final long serialVersionUID = 1L;

  public TounamentUndefinedException() {
    super();
  }

  public TounamentUndefinedException(String message) {
    super(message);
  }

  public TounamentUndefinedException(Throwable cause) {
    super(cause);
  }

  public TounamentUndefinedException(String message, Throwable cause) {
    super(message, cause);
  }

}
