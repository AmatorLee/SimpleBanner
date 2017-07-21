package com.amator.simplebanner.util;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by AmatorLee on 2017/7/21.
 */

public class DisplayUtil {

    public static int getScreenWidth(Context context) {

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.widthPixels;
    }

    public static int dp2px(Context context, int def) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (density * def + 0.5f);
    }


}
