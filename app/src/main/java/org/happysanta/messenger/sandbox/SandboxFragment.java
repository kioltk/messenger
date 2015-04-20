package org.happysanta.messenger.sandbox;

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
public class SandboxFragment extends BaseFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_sandbox, null);

        

        return rootView;
    }
}
