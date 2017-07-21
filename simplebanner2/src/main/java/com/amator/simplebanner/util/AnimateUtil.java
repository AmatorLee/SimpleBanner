package com.amator.simplebanner.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.Log;
import android.view.View;

import static com.amator.simplebanner.widget.SimpleBanner.TAG;

/**
 * Created by AmatorLee on 2017/7/21.
 */

public class AnimateUtil {
    public static final String TAG = "AnimateUtil";


    public static void showView(final View view) {
        Log.d(TAG, "showView excute====");
        view.animate()
                .translationY(0)
                .setInterpolator(new FastOutSlowInInterpolator())
                .setDuration(400)
                .setListener(new AnimatorListenerAdapter() {

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        Log.d(TAG, "show onAnimationEnd: ");
                        view.setVisibility(View.VISIBLE);
                    }
                }).start();
    }

    public static void hideView(final View view){
        Log.d(TAG, "hideView excute====");
        view.animate()
                .translationY(view.getHeight())
                .setInterpolator(new FastOutSlowInInterpolator())
                .setDuration(400)
                .setListener(new AnimatorListenerAdapter() {

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        Log.d(TAG, "hideonAnimationEnd");
                        view.setVisibility(View.GONE);
                    }
                }).start();
    }


}
