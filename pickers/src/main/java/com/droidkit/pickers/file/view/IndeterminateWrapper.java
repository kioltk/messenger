package com.droidkit.pickers.file.view;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;


/**
 * Created by Jesus Christ. Amen.
 */
public class IndeterminateWrapper {


    ProgressBar indeterminateView;


    public IndeterminateWrapper(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        indeterminateView = new ProgressBar(activity);
        indeterminateView.setVisibility(View.GONE);

        // get the action bar layout
        int actionBarId = activity.getResources().getIdentifier("action_bar_container", "id", "android");
        FrameLayout f = (FrameLayout) decorView.findViewById(actionBarId);
        if (f == null) {
            return;
            //f = (FrameLayout) decorView.findViewById(android.R.id.action_bar_container);
        }

        // add the view to that layout
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.BOTTOM;
        f.addView(indeterminateView, params);
    }

    public void show() {
        indeterminateView.setVisibility(View.VISIBLE);
    }

    public void hide() {
        indeterminateView.setVisibility(View.GONE);
    }
}
