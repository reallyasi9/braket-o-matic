package net.exclaimindustries.paste.braket.client;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * A class to store colors in hue [0,360 degrees), saturation [0,1], and
 * lightness [0,1] values.
 * 
 * @author paste
 * 
 */
public final class HSLAColor implements IsSerializable {
    public final static HSLAColor WHITE = new HSLAColor(0, 0, 1, 1);
    public final static HSLAColor LIGHT_GRAY = new HSLAColor(0, 0, .75, 1);
    public final static HSLAColor GRAY = new HSLAColor(0, 0, .5, 1);
    public final static HSLAColor DARK_GRAY = new HSLAColor(0, 0, .25, 1);
    public final static HSLAColor BLACK = new HSLAColor(0, 0, 0, 1);
    public final static HSLAColor RED = new HSLAColor(0, 1, .5, 1);
    public final static HSLAColor PINK = new HSLAColor(0, 1, .84, 1);
    public final static HSLAColor ORANGE = new HSLAColor(39, 1, .5, 1);
    public final static HSLAColor YELLOW = new HSLAColor(60, 1, .5, 1);
    public final static HSLAColor GREEN = new HSLAColor(120, 1, .5, 1);
    public final static HSLAColor MAGENTA = new HSLAColor(300, 1, .5, 1);
    public final static HSLAColor CYAN = new HSLAColor(180, 1, .5, 1);
    public final static HSLAColor BLUE = new HSLAColor(240, 1, .5, 1);
    public final static HSLAColor HIGHLIGHTER_YELLOW = new HSLAColor(60, 1, .8, 1);

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
     * The opacity [0,1]
     */
    double alpha;

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
    public HSLAColor(double h, double s, double l, double a) {
        hue = h % 360.;
        saturation = (s < 0.) ? 0. : (s > 1.) ? 1. : s;
        lightness = (l < 0.) ? 0. : (l > 1.) ? 1. : l;
        alpha = (a < 0.) ? 0. : (a > 1.) ? 1. : a;
    }

    /**
     * Default constructor (defaults to black)
     */
    public HSLAColor() {
        super();
        hue = 0;
        saturation = 0;
        lightness = 0;
        alpha = 1;
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

    public double getAlpha() {
        return alpha;
    }

    public String toString() {
        return "hsla(" + getHue() + "," + (getSaturation() * 100) + "%,"
                + (getLightness() * 100) + "%," + getAlpha() + ")";
    }

    public static HSLAColor fromRGBA(RGBAColor rgb) {
        // Get r, g, b in decimal
        double r = (double) rgb.getRed() / 255.;
        double g = (double) rgb.getGreen() / 255.;
        double b = (double) rgb.getBlue() / 255.;
        double a = rgb.getAlpha();

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

        return new HSLAColor(h, s, l, a);
    }
}
