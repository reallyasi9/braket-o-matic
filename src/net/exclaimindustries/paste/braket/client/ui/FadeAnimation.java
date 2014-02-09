package net.exclaimindustries.paste.braket.client.ui;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.dom.client.Element;

public class FadeAnimation extends Animation {

    private enum Operation {
        FADE_IN, FADE_OUT
    }

    protected Element element;

    private Operation currentOperation = Operation.FADE_IN;

    public FadeAnimation(Element ele) {
        element = ele;
    }

    public void fadeIn(int durationMilliSec) {
        cancel();
        currentOperation = Operation.FADE_IN;
        run(durationMilliSec);
    }

    public void fadeOut(int durationMilliSec) {
        cancel();
        currentOperation = Operation.FADE_OUT;
        run(durationMilliSec);
    }

    @Override
    protected void onUpdate(double progress) {
        switch (currentOperation) {
        case FADE_IN:
            element.getStyle().setOpacity(progress);
            break;
        case FADE_OUT:
            element.getStyle().setOpacity(1. - progress);
            break;
        }
    }

    @Override
    protected void onCancel() {
        switch (currentOperation) {
        case FADE_IN:
            element.getStyle().setOpacity(0.);
            break;
        case FADE_OUT:
            element.getStyle().setOpacity(1.);
            break;
        }
    }

}
