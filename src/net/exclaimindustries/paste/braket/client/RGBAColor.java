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
public final class RGBAColor implements IsSerializable {

    public final static RGBAColor WHITE = new RGBAColor(255, 255, 255, 1);
    public final static RGBAColor LIGHT_GRAY = new RGBAColor(192, 192, 192, 1);
    public final static RGBAColor GRAY = new RGBAColor(128, 128, 128, 1);
    public final static RGBAColor DARK_GRAY = new RGBAColor(64, 64, 64, 1);
    public final static RGBAColor BLACK = new RGBAColor(0, 0, 0, 1);
    public final static RGBAColor RED = new RGBAColor(255, 0, 0, 1);
    public final static RGBAColor PINK = new RGBAColor(255, 175, 175, 1);
    public final static RGBAColor ORANGE = new RGBAColor(255, 165, 0, 1);
    public final static RGBAColor YELLOW = new RGBAColor(255, 255, 0, 1);
    public final static RGBAColor GREEN = new RGBAColor(0, 255, 0, 1);
    public final static RGBAColor MAGENTA = new RGBAColor(255, 0, 255, 1);
    public final static RGBAColor CYAN = new RGBAColor(0, 255, 255, 1);
    public final static RGBAColor BLUE = new RGBAColor(0, 0, 255, 1);

    protected final static RegExp hexPattern =
            RegExp.compile(
                    "^#(?:(\\p{XDigit}{2})(\\p{XDigit}{2})(\\p{XDigit}{2})|(\\p{XDigit})(\\p{XDigit})(\\p{XDigit}))$",
                    "i");
    protected final static RegExp rgbPattern = RegExp.compile(
            "^rgb\\(([0-9]{1,3}),([0-9]{1,3}),([0-9]{1,3})\\)$", "i");
    
    protected final static RegExp rgbaPattern = RegExp.compile(
            "^rgba\\(([0-9]{1,3}),([0-9]{1,3}),([0-9]{1,3}),([0-9\\.]+)\\)$", "i");

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
     * The opacity [0,1]
     */
    private double alpha;

    /**
     * Constructor with RGB values.
     * 
     * @param r
     *            The red value, from 0 to 255.
     * @param g
     *            The green value, from 0 to 255.
     * @param b
     *            The blue value, from 0 to 255.
     * @param a
     *            The alpha value, from 0 to 1.
     */
    public RGBAColor(int r, int g, int b, double a) {
        red = (r < 0) ? 0 : (r > 255) ? 255 : r;
        green = (g < 0) ? 0 : (g > 255) ? 255 : g;
        blue = (b < 0) ? 0 : (b > 255) ? 255 : b;
        alpha = (a < 0) ? 0 : (a > 1) ? 1 : a;
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
     * @param a
     *            The alpha value, from 0 to 1.
     */
    public RGBAColor(double dr, double dg, double db, double a) {
        red = (int) ((dr > 1) ? 255 : (dr < 0) ? 0 : dr * 255);
        green = (int) ((dg > 1) ? 255 : (dg < 0) ? 0 : dg * 255);
        blue = (int) ((db > 1) ? 255 : (db < 0) ? 0 : db * 255);
        alpha = (a < 0) ? 0 : (a > 1) ? 1 : a;
    }

    /**
     * Constructor with single RGBA value.
     * 
     * @param color
     *            RGB value stored as a single 24-bit integer.
     */
    public RGBAColor(int color) {
        red = (color >> 16) & 0xff;
        green = (color >> 8) & 0xff;
        blue = color & 0xff;
        alpha = 1;
    }

    /**
     * Default constructor (defaults to black)
     */
    public RGBAColor() {
        super();
        red = 0;
        green = 0;
        blue = 0;
        alpha = 1;
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

    public double getAlpha() {
        return alpha;
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
        return "rgba(" + getRed() + "," + getGreen() + "," + getBlue() + ","
                + getAlpha() + ")";
    }

    public static RGBAColor fromHSL(HSLAColor hsl) {

        double h = hsl.getHue();
        double s = hsl.getSaturation();
        double l = hsl.getLightness();
        double a = hsl.getAlpha();

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

        return new RGBAColor(r, g, b, a);
    }

    public static RGBAColor fromCSSString(String css) throws ParseException {
        // Is this a hex string starting with a '#'?

        if (hexPattern.test(css)) {
            MatchResult hexMatcher = hexPattern.exec(css);
            int r = Integer.valueOf(hexMatcher.getGroup(1), 16);
            int g = Integer.valueOf(hexMatcher.getGroup(2), 16);
            int b = Integer.valueOf(hexMatcher.getGroup(3), 16);
            return new RGBAColor(r, g, b, 1);
        }

        if (rgbPattern.test(css)) {
            MatchResult rgbMatcher = rgbPattern.exec(css);
            int r = Integer.valueOf(rgbMatcher.getGroup(1), 10);
            int g = Integer.valueOf(rgbMatcher.getGroup(2), 10);
            int b = Integer.valueOf(rgbMatcher.getGroup(3), 10);
            return new RGBAColor(r, g, b, 1);
        }

        if (rgbaPattern.test(css)) {
            MatchResult rgbaMatcher = rgbaPattern.exec(css);
            int r = Integer.valueOf(rgbaMatcher.getGroup(1), 10);
            int g = Integer.valueOf(rgbaMatcher.getGroup(2), 10);
            int b = Integer.valueOf(rgbaMatcher.getGroup(3), 10);
            int a = Integer.valueOf(rgbaMatcher.getGroup(4), 10);
            return new RGBAColor(r, g, b, a);
        }

        throw new ParseException("unable to parse string [" + css + "] to RGBA value");
    }
}