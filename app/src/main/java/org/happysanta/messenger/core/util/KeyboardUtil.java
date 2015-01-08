package org.happysanta.messenger.core.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Jesus Christ. Amen.
 */
public class KeyboardUtil {
    public static void hide(View probablyFocusedView, Activity activity) {
        if (probablyFocusedView != null)
            probablyFocusedView.clearFocus();
        if (activity != null) {
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            View focusedView = activity.getCurrentFocus();
            if (focusedView != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(focusedView.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        }
    }
}