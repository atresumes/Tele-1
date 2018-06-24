package de.keyboardsurfer.android.widget.crouton;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

final class DefaultAnimationsBuilder {
    private static final long DURATION = 400;
    private static int lastInAnimationHeight;
    private static int lastOutAnimationHeight;
    private static Animation slideInDownAnimation;
    private static Animation slideOutUpAnimation;

    private DefaultAnimationsBuilder() {
    }

    static Animation buildDefaultSlideInDownAnimation(View croutonView) {
        if (!areLastMeasuredInAnimationHeightAndCurrentEqual(croutonView) || slideInDownAnimation == null) {
            slideInDownAnimation = new TranslateAnimation(0.0f, 0.0f, (float) (-croutonView.getMeasuredHeight()), 0.0f);
            slideInDownAnimation.setDuration(DURATION);
            setLastInAnimationHeight(croutonView.getMeasuredHeight());
        }
        return slideInDownAnimation;
    }

    static Animation buildDefaultSlideOutUpAnimation(View croutonView) {
        if (!areLastMeasuredOutAnimationHeightAndCurrentEqual(croutonView) || slideOutUpAnimation == null) {
            slideOutUpAnimation = new TranslateAnimation(0.0f, 0.0f, 0.0f, (float) (-croutonView.getMeasuredHeight()));
            slideOutUpAnimation.setDuration(DURATION);
            setLastOutAnimationHeight(croutonView.getMeasuredHeight());
        }
        return slideOutUpAnimation;
    }

    private static boolean areLastMeasuredInAnimationHeightAndCurrentEqual(View croutonView) {
        return areLastMeasuredAnimationHeightAndCurrentEqual(lastInAnimationHeight, croutonView);
    }

    private static boolean areLastMeasuredOutAnimationHeightAndCurrentEqual(View croutonView) {
        return areLastMeasuredAnimationHeightAndCurrentEqual(lastOutAnimationHeight, croutonView);
    }

    private static boolean areLastMeasuredAnimationHeightAndCurrentEqual(int lastHeight, View croutonView) {
        return lastHeight == croutonView.getMeasuredHeight();
    }

    private static void setLastInAnimationHeight(int lastInAnimationHeight) {
        lastInAnimationHeight = lastInAnimationHeight;
    }

    private static void setLastOutAnimationHeight(int lastOutAnimationHeight) {
        lastOutAnimationHeight = lastOutAnimationHeight;
    }
}
