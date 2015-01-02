package org.happysanta.messenger.friends;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.methods.VKApiFriends;
import com.vk.sdk.api.model.VKUsersArray;

import org.happysanta.messenger.R;
import org.happysanta.messenger.user.UserDialog;

/**
 * Created by Jesus Christ. Amen.
 */
public class FriendsFragment extends Fragment {
    private VKUsersArray friends;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_friends, container, false);
        final ListView friendsList = (ListView) rootView.findViewById(R.id.friends_list);
        final TextView statusView = (TextView) rootView.findViewById(R.id.status);
        final ProgressBar progressBar = (ProgressBar) rootView.findViewById(R.id.progress);

        new VKApiFriends().get().executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {

                friends = (VKUsersArray) response.parsedModel;
                friendsList.setAdapter(new FriendsAdapter(getActivity(), friends.toArrayList()));
                progressBar.setVisibility(View.GONE);

                if(friends.isEmpty()){
                    statusView.setVisibility(View.VISIBLE);

            }}

            @Override
            public void onError(VKError error) {
                super.onError(error);
                statusView.setText(error.toString());
                progressBar.setVisibility(View.GONE);

            }
        });

        friendsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                new UserDialog(getActivity(), friends.get(position)).show();
            }
        });
        return rootView;
    }
}
