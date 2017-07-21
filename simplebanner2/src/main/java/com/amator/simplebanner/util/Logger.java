package com.amator.simplebanner.util;

import android.util.Log;

import static com.amator.simplebanner.widget.SimpleBanner.TAG;

/**
 * Created by AmatorLee on 2017/7/21.
 */

public class Logger {

    public static boolean DEBUG = true;


    public static void d(String TAG,String message){
        if (DEBUG){
            Log.d(TAG,message);
        }
    }


}
