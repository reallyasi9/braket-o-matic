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
 * Informs the recipient that an action cannot be performed because the user is
 * logged in, but does not have administrative privileges.
 * 
 * @author paste
 * 
 */
public class UserNotAdminException extends Exception {

  public UserNotAdminException(String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  private static final long serialVersionUID = 1L;

  public UserNotAdminException() {
  }

  public UserNotAdminException(String message) {
    super(message);
  }

  public UserNotAdminException(Throwable cause) {
    super(cause);
  }

  public UserNotAdminException(String message, Throwable cause) {
    super(message, cause);
  }

}
