package org.happysanta.messenger.about;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.happysanta.messenger.R;
import org.happysanta.messenger.core.BaseFragment;
import org.happysanta.messenger.posts.ComposeActivity;

/**
 * Created by Jesus Christ. Amen.
 */
public class AboutFragment extends BaseFragment {
    private Button btnCompose;
    int userId;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_about, container, false);

        btnCompose = (Button) rootView.findViewById(R.id.compose_post);
        btnCompose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(ComposeActivity.openCompose(activity, userId));
            }
        });

        return rootView;
    }
}
