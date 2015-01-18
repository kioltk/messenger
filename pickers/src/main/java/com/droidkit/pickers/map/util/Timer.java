package com.droidkit.pickers.map.util;

import android.util.Log;

/**
 * Created by kioltk on 10/30/14.
 */
public class Timer {
    private static long time;

    public static void finish(String message) {
        Log.d("Timer", (System.currentTimeMillis() - time) + " " + message);
    }

    public static void start() {
        time = System.currentTimeMillis();
    }
}
