package com.droidkit.pickers.map.util;

import android.content.Context;
import android.os.Build;

/**
 * Created by kioltk on 10/30/14.
 */
public class Dimen {
    private static Context context;

    public static int getStatusBarHeight() {
        int result = 0;
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.KITKAT){
            return result;
        }
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static void initialize(Context context){
        Dimen.context = context;
    }

    public static int getPX(int resourceId) {

        return context.getResources().getDimensionPixelSize(resourceId);
    }


}
