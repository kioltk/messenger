package org.happysanta.messenger.core.util;

import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

import static org.happysanta.messenger.MessengerApplication.app;


/**
 * Created by Jesus Christ. Amen.
 */
public class Screen {
    private static float density;
    private static float scaledDensity;
    private static DisplayMetrics metrics;

    public static DisplayMetrics displayMetrics(){
        if(metrics==null){
            metrics = app().getResources().getDisplayMetrics();
        }
        return metrics;
    }

    public static int dp(float dp) {
        if (density == 0f)
            density = app().getResources().getDisplayMetrics().density;

        return (int) (dp * density + .5f);
    }

    public static int sp(float sp) {
        if (scaledDensity == 0f)
            scaledDensity = app().getResources().getDisplayMetrics().scaledDensity;

        return (int) (sp * scaledDensity + .5f);
    }

    public static int getWidth() {
        return app().getResources().getDisplayMetrics().widthPixels;
    }

    public static int getHeight() {
        return app().getResources().getDisplayMetrics().heightPixels;
    }

    public static boolean isTablet() {
        return (app().getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static boolean isLandscape() {
        Display display = ((WindowManager) app().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int rotation = display.getRotation();
        return (getWidth()>getHeight()) == (rotation== Surface.ROTATION_0||rotation==Surface.ROTATION_180);
    }
}
