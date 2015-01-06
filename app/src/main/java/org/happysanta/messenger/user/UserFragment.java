package org.happysanta.messenger.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.methods.VKApiUsers;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;

import org.happysanta.messenger.R;
import org.happysanta.messenger.core.BaseFragment;
import org.happysanta.messenger.longpoll.LongpollService;
import org.happysanta.messenger.longpoll.listeners.LongpollListener;

import java.util.ArrayList;

/**
 * Created by Jesus Christ. Amen.
 */
public class UserFragment extends BaseFragment {
    private View rootView;
    private ArrayList<Object> updates = new ArrayList<Object>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        final TextView usernameView = (TextView) rootView.findViewById(R.id.username);

        TextView statusView = (TextView) rootView.findViewById(R.id.status);

        ImageView imageView = (ImageView) rootView.findViewById(R.id.user_photo);

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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_profile, menu);
    }


    private class UpdatesAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return updates.size();
        }

        @Override
        public Object getItem(int position) {
            return updates.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = new TextView(getActivity());
            textView.setText(getItem(position).toString());
            return textView;
        }
    }
}
