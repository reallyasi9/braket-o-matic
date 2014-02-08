package net.exclaimindustries.paste.braket.client.ui;

import net.exclaimindustries.paste.braket.client.HSLColor;
import net.exclaimindustries.paste.braket.client.RGBColor;
import net.exclaimindustries.paste.braket.shared.ParseException;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.dom.client.Element;

public class HighlightAnimation extends Animation {

    protected Element element;

    protected HSLColor highlightColor;

    protected HSLColor targetColor;

    protected String originalColor;

    protected String originalBackgroundImage;

    protected double originalOpacity;

    public HighlightAnimation(Element ele, RGBColor color) {
        element = ele;
        highlightColor = HSLColor.fromRGB(color);
    }

    public HighlightAnimation(Element ele, HSLColor color) {
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
        highlightColor = new HSLColor(60, 1, .8);
    }

    public void highlight(int durationMilliSec) {
        cancel();
        originalColor = element.getStyle().getBackgroundColor();
        originalBackgroundImage = element.getStyle().getBackgroundImage();
        // I hope this works...
        try {
            targetColor = HSLColor.fromRGB(RGBColor.fromCSSString(originalColor));
        } catch (ParseException e) {
            // Target color can't be parsed, so make it white?
            targetColor = HSLColor.WHITE;
        }
        element.getStyle().setBackgroundImage("none");
        element.getStyle().setBackgroundColor(
                RGBColor.fromHSL(highlightColor).getHexValue());
        run(durationMilliSec);
    }

    @Override
    protected void onUpdate(double progress) {
        // Interpolate between highlight and target
        double p1 = interpolate(progress);
        // The distance between the hues is the shortest distance on the circle
        double dh = targetColor.getHue() - highlightColor.getHue();
        if (Math.abs(dh) > 180) {
            dh = -Math.signum(dh) * (360 - Math.abs(dh));
        }
        double ds = targetColor.getSaturation() - highlightColor.getSaturation();
        double dl = targetColor.getLightness() - highlightColor.getLightness();

        double h = highlightColor.getHue() + p1 * dh;
        double s = highlightColor.getSaturation() + p1 * ds;
        double l = highlightColor.getLightness() + p1 * dl;

        HSLColor currentColor = new HSLColor(h, s, l);
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

}
