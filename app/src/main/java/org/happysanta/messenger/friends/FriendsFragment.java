package org.happysanta.messenger.friends;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.model.VKApiUser;

import org.happysanta.messenger.R;

import java.util.ArrayList;

/**
 * Created by Jesus Christ. Amen.
 */
public class FriendsFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_friends, container, false);
        ListView friendsList = (ListView) rootView.findViewById(R.id.friends_list);
        ArrayList<VKApiUser> friends = new ArrayList<VKApiUser>();

        friends.add(new VKApiUser());
        friends.add(new VKApiUser());
        friends.add(new VKApiUser());
        friends.add(new VKApiUser());
        friends.add(new VKApiUser());

        friendsList.setAdapter(new FriendsAdapter(getActivity(), friends));
        friendsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Dialog friendDialog = new AlertDialog.Builder(getActivity())
                        .setView(getActivity().getLayoutInflater().inflate(R.layout.dialog_friend, null))
                        .show();
                friendDialog.setCanceledOnTouchOutside(true);




            }
        });
        return rootView;
    }
}
