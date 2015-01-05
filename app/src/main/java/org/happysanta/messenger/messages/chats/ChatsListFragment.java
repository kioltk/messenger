package org.happysanta.messenger.messages.chats;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.methods.VKApiMessages;
import com.vk.sdk.api.model.VKApiDialog;
import com.vk.sdk.api.model.VKList;

import org.happysanta.messenger.R;
import org.happysanta.messenger.messages.DialogActivity;

/**
 * Created by Jesus Christ. Amen.
 */
public class ChatsListFragment extends Fragment {
    private View rootView;
    private VKList<VKApiDialog> dialogs;
    private TextView status;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_chats_list, container, false);
        final GridView list = (GridView) rootView.findViewById(R.id.grid);
        status = (TextView) rootView.findViewById(R.id.status);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                VKApiDialog dialog = dialogs.get(position);
                Intent intent = DialogActivity.getActivityIntent(getActivity(), dialog);
                startActivity(intent);
            }
        });

        new VKApiMessages().getChats().executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                VKList<VKApiDialog> dialogs = (VKList<VKApiDialog>) response.parsedModel;
                ChatsListFragment.this.dialogs = dialogs;
                list.setAdapter(new ConversationsAdapter());
                status.setVisibility(View.GONE);
            }

            @Override
            public void onError(VKError error) {
                status.setText(error.toString());
            }
        });

        return rootView;
    }


    private class ConversationsAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return dialogs.size();
        }

        @Override
        public VKApiDialog getItem(int position) {
            return dialogs.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View dialogView = getActivity().getLayoutInflater().inflate(R.layout.item_chat, null);
            TextView titleView = (TextView) dialogView.findViewById(R.id.title);
            TextView bodyView = (TextView) dialogView.findViewById(R.id.body);
            ImageView photoView = (ImageView) dialogView.findViewById(R.id.chat_photo);
            VKApiDialog dialog = getItem(position);


            titleView.setText(dialog.title);
            bodyView.setText(dialog.usersCount+ " в чате");
            ImageLoader.getInstance().displayImage(dialog.getPhoto(), photoView);

            return dialogView;
        }
    }
}
