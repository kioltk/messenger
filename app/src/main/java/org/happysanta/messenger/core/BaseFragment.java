package org.happysanta.messenger.core;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * Created by d_great on 27.12.14.
 */
public class BaseFragment extends Fragment {

    protected View          rootView;
    protected BaseActivity  activity;
    protected Toolbar       toolbar;
    //protected View          toolbarShadow;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View findViewById(int layoutId){
        return rootView.findViewById(layoutId);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        this.activity   = (BaseActivity) activity;

        toolbar         = this.activity.getToolbar();
        /*toolbarShadow   = this.activity.getToolBarShadow();*/
    }
}
