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

import com.google.gwt.user.client.rpc.IsSerializable;
import com.googlecode.objectify.annotation.Embed;

/**
 * A class to store 24-bit RGB colors.
 * 
 * @author paste
 * 
 */
@Embed
public final class Color implements IsSerializable {
        
    public final static Color WHITE = new Color(255, 255, 255);
    public final static Color LIGHT_GRAY = new Color(192, 192, 192);
    public final static Color GRAY = new Color(128, 128, 128);
    public final static Color DARK_GRAY = new Color(64, 64, 64);
    public final static Color BLACK = new Color(0, 0, 0);
    public final static Color RED = new Color(255, 0, 0);
    public final static Color PINK = new Color(255, 175, 175);
    public final static Color ORANGE = new Color(255, 200, 0);
    public final static Color YELLOW = new Color(255, 255, 0);
    public final static Color GREEN = new Color(0, 255, 0);
    public final static Color MAGENTA = new Color(255, 0, 255);
    public final static Color CYAN = new Color(0, 255, 255);
    public final static Color BLUE = new Color(0, 0, 255);

    /**
     * The color, stored as a 24-bit integer.
     */
    private int color;

    /**
     * Constructor with RGB values.
     * 
     * @param r
     *            The red value, from 0 to 255.
     * @param g
     *            The green value, from 0 to 255.
     * @param b
     *            The blue value, from 0 to 255.
     */
    public Color(int r, int g, int b) {
        this.color =
                ((r << 16) & 0x00ff0000) | ((g << 8) & 0x0000ff00)
                        | (b & 0x000000ff);
    }

    /**
     * Constructor with fractional RGB values.
     * 
     * @param dr
     *            The red value, from 0 to 1.
     * @param dg
     *            The green value, from 0 to 1.
     * @param db
     *            The blue value, from 0 to 1.
     */
    public Color(double dr, double dg, double db) {
        this((int) ((dr > 1) ? 255 : (dr < 0) ? 0 : dr * 255),
                (int) ((dg > 1) ? 255 : (dg < 0) ? 0 : dg * 255),
                (int) ((db > 1) ? 255 : (db < 0) ? 0 : db * 255));
    }

    /**
     * Constructor with single RGB value.
     * 
     * @param color
     *            RGB value stored as a single 24-bit integer.
     */
    public Color(int color) {
        this.color = color;
    }

    /**
     * Default constructor (defaults to black)
     */
    public Color() {
        super();
        this.color = 0;
    }

    /**
     * Constructor from a hex string.
     * 
     * @param hexColor
     *            RGB value as a 6-digit hex string.
     */
    public Color(String hexColor) {
        this(Integer.valueOf(hexColor, 16));
    }

    public int getRed() {
        return (color >> 16) & 0x000000ff;
    }

    public int getGreen() {
        return (color >> 8) & 0x000000ff;
    }

    public int getBlue() {
        return color & 0x000000ff;
    }

    public String getHexValue() {
        StringBuilder sb = new StringBuilder();
        sb.append(Integer.toHexString(color));
        while (sb.length() < 6) {
            sb.append("0");
        }
        return sb.toString();
    }

    public String toString() {
        return "red=" + getRed() + ", green=" + getGreen() + ", blue="
                + getBlue();
    }
}