package org.happysanta.messenger.messages.conversations;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.methods.VKApiMessages;
import com.vk.sdk.api.model.VKApiDialog;
import com.vk.sdk.api.model.VKList;

import org.happysanta.messenger.R;
import org.happysanta.messenger.messages.ChatActivity;

/**
 * Created by Jesus Christ. Amen.
 */
public class ConversathionsListFragment extends Fragment {
    private View rootView;
    private VKList<VKApiDialog> dialogs;
    private TextView status;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        final ListView list = (ListView) rootView.findViewById(R.id.list);
        status = (TextView) rootView.findViewById(R.id.status);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                VKApiDialog dialog = dialogs.get(position);
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(ChatActivity.ARG_USERID, dialog.getId() );
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        new VKApiMessages().getDialogs().executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                VKList<VKApiDialog> messages = (VKList<VKApiDialog>) response.parsedModel;
                dialogs = messages;
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
            View dialogView = getActivity().getLayoutInflater().inflate(R.layout.item_dialog, null);
            TextView bodyView = (TextView) dialogView.findViewById(R.id.body);

            VKApiDialog dialog = getItem(position);

            bodyView.setText(dialog.message.body);

            return dialogView;
        }
    }
}
