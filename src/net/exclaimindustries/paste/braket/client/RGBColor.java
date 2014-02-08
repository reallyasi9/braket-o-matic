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

import net.exclaimindustries.paste.braket.shared.ParseException;

import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.googlecode.objectify.annotation.Embed;

/**
 * A class to store 24-bit RGB colors.
 * 
 * @author paste
 * 
 */
@Embed
public final class RGBColor implements IsSerializable {

    public final static RGBColor WHITE = new RGBColor(255, 255, 255);
    public final static RGBColor LIGHT_GRAY = new RGBColor(192, 192, 192);
    public final static RGBColor GRAY = new RGBColor(128, 128, 128);
    public final static RGBColor DARK_GRAY = new RGBColor(64, 64, 64);
    public final static RGBColor BLACK = new RGBColor(0, 0, 0);
    public final static RGBColor RED = new RGBColor(255, 0, 0);
    public final static RGBColor PINK = new RGBColor(255, 175, 175);
    public final static RGBColor ORANGE = new RGBColor(255, 165, 0);
    public final static RGBColor YELLOW = new RGBColor(255, 255, 0);
    public final static RGBColor GREEN = new RGBColor(0, 255, 0);
    public final static RGBColor MAGENTA = new RGBColor(255, 0, 255);
    public final static RGBColor CYAN = new RGBColor(0, 255, 255);
    public final static RGBColor BLUE = new RGBColor(0, 0, 255);

    protected final static RegExp hexPattern =
            RegExp.compile(
                    "^#(?:(\\p{XDigit}{2})(\\p{XDigit}{2})(\\p{XDigit}{2})|(\\p{XDigit})(\\p{XDigit})(\\p{XDigit}))$",
                    "i");
    protected final static RegExp rgbPattern = RegExp.compile(
            "^rgb\\(([0-9]{1,3}),([0-9]{1,3}),([0-9]{1,3})\\)$", "i");
    protected final static RegExp rgbaPattern = RegExp.compile(
            "^rgba\\(([0-9]{1,3}),([0-9]{1,3}),([0-9]{1,3})\\)$", "i");

    /**
     * The red color [0,255]
     */
    private int red;

    /**
     * The green color [0,255]
     */
    private int green;

    /**
     * The blue color [0,255]
     */
    private int blue;

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
    public RGBColor(int r, int g, int b) {
        red = r;
        green = g;
        blue = b;
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
    public RGBColor(double dr, double dg, double db) {
        red = (int) ((dr > 1) ? 255 : (dr < 0) ? 0 : dr * 255);
        green = (int) ((dg > 1) ? 255 : (dg < 0) ? 0 : dg * 255);
        blue = (int) ((db > 1) ? 255 : (db < 0) ? 0 : db * 255);
    }

    /**
     * Constructor with single RGB value.
     * 
     * @param color
     *            RGB value stored as a single 24-bit integer.
     */
    public RGBColor(int color) {
        red = (color >> 16) & 0xff;
        green = (color >> 8) & 0xff;
        blue = color & 0xff;
    }

    /**
     * Default constructor (defaults to black)
     */
    public RGBColor() {
        super();
        red = 0;
        green = 0;
        blue = 0;
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }

    public int getInt() {
        return (red << 16) + (green << 8) + blue;
    }

    public String getHexValue() {
        // String.format is not allowed on the client side, so use a string
        // builder
        StringBuilder sb = new StringBuilder();
        sb.append('#');

        // Repeated digits are simplified
        if (red % 17 == 0 && green % 17 == 0 && blue % 17 == 0) {
            sb.append(Integer.toHexString(red / 17))
                    .append(Integer.toHexString(green / 17))
                    .append(Integer.toHexString(blue / 17));
            return sb.toString();
        } else {
            if (red < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(red));
            if (green < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(green));
            if (blue < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(blue));
            return sb.toString();
        }
    }

    public String toString() {
        return "rgb(" + getRed() + "," + getGreen() + "," + getBlue() + ")";
    }

    public static RGBColor fromHSL(HSLColor hsl) {

        double h = hsl.getHue();
        double s = hsl.getSaturation();
        double l = hsl.getLightness();

        double c = (1 - Math.abs(2. * l - 1)) * s;
        double hp = h / 60.;
        double x = c * (1 - Math.abs(hp % 2. - 1));

        double m = l - 0.5 * c;

        double r = m;
        double g = m;
        double b = m;

        if (0 < h && h < 1) {
            r += c;
            g += x;
        } else if (1 <= h && h < 2) {
            r += x;
            g += c;
        } else if (2 <= h && h < 3) {
            g += c;
            b += x;
        } else if (3 <= h && h < 4) {
            g += x;
            b += c;
        } else if (4 <= h && h < 5) {
            r += x;
            b += c;
        } else {
            r += c;
            b += x;
        }

        return new RGBColor(r, g, b);
    }

    public static RGBColor fromCSSString(String css) throws ParseException {
        // Is this a hex string starting with a '#'?

        if (hexPattern.test(css)) {
            MatchResult hexMatcher = hexPattern.exec(css);
            int r = Integer.valueOf(hexMatcher.getGroup(1), 16);
            int g = Integer.valueOf(hexMatcher.getGroup(2), 16);
            int b = Integer.valueOf(hexMatcher.getGroup(3), 16);
            return new RGBColor(r, g, b);
        }

        if (rgbPattern.test(css)) {
            MatchResult rgbMatcher = rgbPattern.exec(css);
            int r = Integer.valueOf(rgbMatcher.getGroup(1), 10);
            int g = Integer.valueOf(rgbMatcher.getGroup(2), 10);
            int b = Integer.valueOf(rgbMatcher.getGroup(3), 10);
            return new RGBColor(r, g, b);
        }

        if (rgbaPattern.test(css)) {
            MatchResult rgbaMatcher = rgbaPattern.exec(css);
            int r = Integer.valueOf(rgbaMatcher.getGroup(1), 10);
            int g = Integer.valueOf(rgbaMatcher.getGroup(2), 10);
            int b = Integer.valueOf(rgbaMatcher.getGroup(3), 10);
            return new RGBColor(r, g, b);
        }

        throw new ParseException("unable to parse string [" + css + "] to RGB value");
    }
}