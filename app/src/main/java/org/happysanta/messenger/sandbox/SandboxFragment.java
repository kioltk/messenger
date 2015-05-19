package org.happysanta.messenger.sandbox;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringSystem;

import org.happysanta.messenger.R;
import org.happysanta.messenger.chatheads.ChatHeadsManager;
import org.happysanta.messenger.core.BaseFragment;
/**
 * Created by Jesus Christ. Amen.
 */
public class SandboxFragment extends BaseFragment {

    private static final String TAG = "SANDBOX";
    private SpringSystem springSystem;
    private Spring scaleAnimationSpring;
    private View layer;
    private Spring xAnimationSpring;
    private Spring yAnimationSpring;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_sandbox, null);

        findViewById(R.id.enable_chathead).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatHeadsManager.activateMainHead();
            }
        });
        return rootView;
    }

}


