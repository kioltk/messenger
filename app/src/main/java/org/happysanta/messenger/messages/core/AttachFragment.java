package org.happysanta.messenger.messages.core;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.happysanta.messenger.R;
import org.happysanta.messenger.core.BaseFragment;

/**
 * Created by Jesus Christ. Amen.
 */
public class AttachFragment extends BaseFragment {
    private AttachListener attachListener;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_attach, null);

        return rootView;
    }

    // attaches here

    public static AttachFragment getInstance() {
        AttachFragment fragment = new AttachFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    public void setAttachListener(AttachListener attachListener) {
        this.attachListener = attachListener;
    }
}
