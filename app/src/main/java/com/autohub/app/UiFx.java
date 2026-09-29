package com.autohub.app;

import android.animation.ValueAnimator;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

/** Motion & micro-interactions for AUTO HUB. Framework-only, zero dependencies. */
final class UiFx {
    private UiFx() {}

    /** Springy press feedback: scale down on touch, bounce back on release + haptic tick. */
    static <T extends View> T press(T v) {
        v.setOnTouchListener((view, ev) -> {
            int a = ev.getActionMasked();
            if (a == MotionEvent.ACTION_DOWN) {
                view.animate().cancel();
                view.animate().scaleX(0.93f).scaleY(0.93f)
                    .setDuration(90).setInterpolator(new DecelerateInterpolator()).start();
                view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            } else if (a == MotionEvent.ACTION_UP || a == MotionEvent.ACTION_CANCEL) {
                view.animate().cancel();
                view.animate().scaleX(1f).scaleY(1f)
                    .setDuration(280).setInterpolator(new OvershootInterpolator(2.4f)).start();
            }
            return false;
        });
        return v;
    }

    /** Staggered entrance: rise + fade for every card added to a screen. */
    static void stagger(View v, int index) {
        float dy = 18f * v.getResources().getDisplayMetrics().density;
        v.setAlpha(0f);
        v.setTranslationY(dy);
        v.animate().alpha(1f).translationY(0f)
            .setDuration(340)
            .setStartDelay(Math.min(index * 45, 430))
            .setInterpolator(new DecelerateInterpolator(1.35f))
            .start();
    }

    /** Infinite soft neon pulse for hero elements. */
    static void pulse(View v) {
        ValueAnimator a = ValueAnimator.ofFloat(1f, 0.8f);
        a.setDuration(1300);
        a.setRepeatCount(ValueAnimator.INFINITE);
        a.setRepeatMode(ValueAnimator.REVERSE);
        a.addUpdateListener(an -> v.setAlpha((Float) an.getAnimatedValue()));
        a.start();
    }
}
