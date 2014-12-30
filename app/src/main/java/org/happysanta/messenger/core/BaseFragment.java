package org.happysanta.messenger.core;

import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by d_great on 27.12.14.
 */
public class BaseFragment extends Fragment {

    protected View rootView;
    public View findViewById(int layoutId){
        return rootView.findViewById(layoutId);
    }
}
