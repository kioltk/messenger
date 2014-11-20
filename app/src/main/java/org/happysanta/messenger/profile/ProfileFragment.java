package org.happysanta.messenger.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.methods.VKApiUsers;
import com.vk.sdk.api.model.VKApiUser;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;

import org.happysanta.messenger.R;

/**
 * Created by Jesus Christ. Amen.
 */
public class ProfileFragment extends Fragment {
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        final TextView usernameView = (TextView) rootView.findViewById(R.id.username);
        // todo это всё нужно делать заранее на экране загрузки
        new VKApiUsers().get().executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onError(VKError error) {
                super.onError(error);

                usernameView.setText(error.toString());

            }

            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                try {
                    VKList<VKApiUserFull> users = (VKList<VKApiUserFull>) response.parsedModel;
                    VKApiUserFull currentUser = users.get(0);
                    usernameView.setText(currentUser.toString());
                }catch (Exception exp){
                    usernameView.setText(exp.getMessage());
                }
            }
        });

        return rootView;
    }
}
