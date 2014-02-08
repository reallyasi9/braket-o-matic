package net.exclaimindustries.paste.braket.client;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * A class to store colors in hue [0,360 degrees), saturation [0,1], and
 * lightness [0,1] values.
 * 
 * @author paste
 * 
 */
public final class HSLColor implements IsSerializable {
    public final static HSLColor WHITE = new HSLColor(0, 0, 1);
    public final static HSLColor LIGHT_GRAY = new HSLColor(0, 0, .75);
    public final static HSLColor GRAY = new HSLColor(0, 0, .5);
    public final static HSLColor DARK_GRAY = new HSLColor(0, 0, .25);
    public final static HSLColor BLACK = new HSLColor(0, 0, 0);
    public final static HSLColor RED = new HSLColor(0, 1, .5);
    public final static HSLColor PINK = new HSLColor(0, 1, .84);
    public final static HSLColor ORANGE = new HSLColor(39, 1, .5);
    public final static HSLColor YELLOW = new HSLColor(60, 1, .5);
    public final static HSLColor GREEN = new HSLColor(120, 1, .5);
    public final static HSLColor MAGENTA = new HSLColor(300, 1, .5);
    public final static HSLColor CYAN = new HSLColor(180, 1, .5);
    public final static HSLColor BLUE = new HSLColor(240, 1, .5);

    /**
     * The hue [0,360)
     */
    double hue;

    /**
     * The saturation [0,1]
     */
    double saturation;

    /**
     * The lightness [0,1]
     */
    double lightness;

    /**
     * Constructor with HSL values.
     * 
     * @param h
     *            The hue value, from 0 to 360 (degrees).
     * @param s
     *            The saturation value, from 0 to 1.
     * @param l
     *            The lightness value, from 0 to 1.
     */
    public HSLColor(double h, double s, double l) {
        hue = h % 360.;
        saturation = (s < 0.) ? 0. : (s > 1) ? 1. : s;
        lightness = (l < 0.) ? 0. : (l > 1) ? 1. : l;
    }

    /**
     * Default constructor (defaults to black)
     */
    public HSLColor() {
        super();
        hue = 0;
        saturation = 0;
        lightness = 0;
    }

    public double getHue() {
        return hue;
    }

    public double getSaturation() {
        return saturation;
    }

    public double getLightness() {
        return lightness;
    }

    public String toString() {
        return "hsl(" + getHue() + "," + (getSaturation() * 100) + "%,"
                + (getLightness() * 100) + "%)";
    }

    public static HSLColor fromRGB(RGBColor rgb) {
        // Get r, g, b in decimal
        double r = (double) rgb.getRed() / 255.;
        double g = (double) rgb.getGreen() / 255.;
        double b = (double) rgb.getBlue() / 255.;

        // Calculate chroma
        double max = Math.max(r, Math.max(g, b));
        double min = Math.min(r, Math.min(g, b));
        double c = max - min;

        // Calculate piecewise hue
        double h =
                60 * ((c == 0.) ? 0. : (max == r) ? ((g - b) / c) % 6.
                        : (max == g) ? (b - r) / c + 2. : (r - g) / c + 4.);

        // Calculate lightness
        double l = .5 * (max + min);

        // Calculate saturation
        double s = (c == 0.) ? 0. : c / (1 - Math.abs(2. * l - 1.));

        return new HSLColor(h, s, l);
    }
}
