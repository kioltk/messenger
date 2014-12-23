package org.happysanta.messenger.core.util;

import android.content.Context;
import android.os.Build;

/**
 * Created by d_great on 23.12.14.
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

    public static void init(Context context){
        Dimen.context = context;
    }
}
