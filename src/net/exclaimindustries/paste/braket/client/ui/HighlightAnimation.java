package net.exclaimindustries.paste.braket.client.ui;

import net.exclaimindustries.paste.braket.client.HSLAColor;
import net.exclaimindustries.paste.braket.client.RGBAColor;
import net.exclaimindustries.paste.braket.shared.ParseException;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.dom.client.Element;

public class HighlightAnimation extends Animation {

    protected Element element;

    protected HSLAColor highlightColor;

    protected HSLAColor targetColor;

    protected String originalColor;

    protected String originalBackgroundImage;

    protected double originalOpacity;

    public HighlightAnimation(Element ele, RGBAColor color) {
        element = ele;
        highlightColor = HSLAColor.fromRGBA(color);
    }

    public HighlightAnimation(Element ele, HSLAColor color) {
        element = ele;
        highlightColor = color;
    }

    /**
     * Default color is yellow.
     * 
     * @param ele
     *            The element to highlight
     */
    public HighlightAnimation(Element ele) {
        element = ele;
        highlightColor = new HSLAColor(60, 1, .8, 1);
    }

    public void highlight(int durationMilliSec) {
        cancel();
        originalColor = element.getStyle().getBackgroundColor();
        originalBackgroundImage = element.getStyle().getBackgroundImage();
        // I hope this works...
        try {
            targetColor = HSLAColor.fromRGBA(RGBAColor.fromCSSString(originalColor));
        } catch (ParseException e) {
            // Target color can't be parsed, so instead, use opacity
            targetColor =
                    new HSLAColor(highlightColor.getHue(),
                            highlightColor.getSaturation(),
                            highlightColor.getLightness(), 0);
        }
        element.getStyle().setBackgroundImage("none");
        run(durationMilliSec);
    }

    @Override
    protected void onUpdate(double progress) {
        // The distance between the hues is the shortest distance on the circle
        double dh = highlightColor.getHue() - targetColor.getHue();
        if (Math.abs(dh) > 180) {
            dh = -Math.signum(dh) * (360 - Math.abs(dh));
        }
        double ds = highlightColor.getSaturation() - targetColor.getSaturation();
        double dl = highlightColor.getLightness() - targetColor.getLightness();
        double da = highlightColor.getAlpha() - targetColor.getAlpha();

        double h = targetColor.getHue() + progress * dh;
        double s = targetColor.getSaturation() + progress * ds;
        double l = targetColor.getLightness() + progress * dl;
        double a = targetColor.getAlpha() + progress * da;

        HSLAColor currentColor = new HSLAColor(h, s, l, a);
        element.getStyle().setBackgroundColor(currentColor.toString());
    }

    @Override
    protected void onComplete() {
        element.getStyle().setBackgroundColor(originalColor);
        element.getStyle().setBackgroundImage(originalBackgroundImage);
    }

    @Override
    protected void onCancel() {
        onComplete();
    }

    @Override
    protected double interpolate(double x) {
        // Start at 0, ping up to 1 quickly, then fall back to 0 slowly.
        double q = 0.25;
        double r = 0.5;
        if (x < q) {
            return 1 - Math.cos(Math.PI * x / (2 * q));
        } else if (x < r) {
            return 1;
        } else {
            return Math.cos(Math.PI * (x - r) / (2 * (1 - r)));
        }
    }

}
