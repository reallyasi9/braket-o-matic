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

package net.exclaimindustries.paste.braket.client;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Embed;

/**
 * A class that stores the name of the user and allows a simple way to display
 * it in First "Nick" Last format.
 * 
 * @author paste
 */
@Embed
public class UserName implements Serializable, Cloneable {

	/**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * The first (given) name of the user.
	 */
	private String firstName = null;

	/**
	 * The last (family) name of the user.
	 */
	private String lastName = null;

	/**
	 * A clever nickname the user gives himself.
	 */
	private String nickname = null;

	/**
	 * Default constructor.
	 */
	public UserName() {
	}

	/**
	 * Constructor with fields.
	 * 
	 * @param firstName
	 *            The first (given) name of the user
	 * @param lastName
	 *            The last (family) name of the user
	 * @param nickname
	 *            A clever nickname for the user
	 */
	public UserName(String firstName, String lastName, String nickname) {
		super();
		this.firstName = (firstName == null) ? "" : firstName.trim();
		this.lastName = (lastName == null) ? "" : lastName.trim();
		this.nickname = (nickname == null) ? "" : nickname.trim();
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName.trim();
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName.trim();
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname.trim();
	}

	/**
	 * Get the full name of the user in First "Nick" Last format.
	 * 
	 * @param fallback
	 *            A string to return if
	 *            {@link net.exclaimindustries.paste.braket.client.UserName#toString()
	 *            toString} would return null or an empty string.
	 * @return The output of toString if that output is not null or an empty
	 *         string, else the fallback string given.
	 */
	public String getFullName(String fallback) {
		if ((firstName == null || firstName.length() == 0)
				&& (nickname == null || nickname.length() == 0)
				&& (lastName == null || lastName.length() == 0)) {
			return fallback;
		}
		return this.toString();
	}

	public String toString() {
		StringBuilder fullName = new StringBuilder();
		if (firstName != null && firstName.length() > 0) {
			fullName.append(firstName);
		}
		if (nickname != null && nickname.length() > 0) {
			if (fullName.length() > 0) {
				fullName.append(' ');
			}
			fullName.append('"').append(nickname).append('"');
		}
		if (lastName != null && lastName.length() > 0) {
			if (fullName.length() > 0) {
				fullName.append(' ');
			}
			fullName.append(lastName);
		}
		return fullName.toString();
	}
	
	public Object clone() {
	    return new UserName(firstName, lastName, nickname);
	}
	
	public boolean hasFirstName() {
	    return firstName != null && firstName.length() > 0;
	}
	
	public boolean hasLastName() {
        return lastName != null && lastName.length() > 0;
    }
	
	public boolean hasNickname() {
        return nickname != null && nickname.length() > 0;
    }
	
	public boolean hasReadableName() {
	    return hasFirstName() && hasLastName();
	}

}