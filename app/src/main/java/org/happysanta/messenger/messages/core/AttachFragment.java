package org.happysanta.messenger.messages.core;

import android.os.Bundle;

import org.happysanta.messenger.core.BaseFragment;

/**
 * Created by Jesus Christ. Amen.
 */
public class AttachFragment extends BaseFragment {


    // attaches here

    public static AttachFragment getInstance() {
        AttachFragment fragment = new AttachFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

}
