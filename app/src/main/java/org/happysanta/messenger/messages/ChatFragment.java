package org.happysanta.messenger.messages;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.methods.VKApiMessages;
import com.vk.sdk.api.model.VKApiMessage;
import com.vk.sdk.api.model.VKList;

import org.happysanta.messenger.R;
import org.happysanta.messenger.messages.core.MessagesAdapter;

/**
 * A placeholder fragment containing a simple view.
 */
public class ChatFragment extends Fragment {

    public ChatFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);
        final ListView messagesList = (ListView) rootView.findViewById(R.id.messages_list);
        final TextView statusView = (TextView) rootView.findViewById(R.id.status);
        int userId = getArguments().getInt(ChatActivity.ARG_CHAT, 0);

        statusView.setText("loading");

        new VKApiMessages().getChatHistory(userId).executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                VKList<VKApiMessage> messages = (VKList<VKApiMessage>) response.parsedModel;
                messagesList.setAdapter(new MessagesAdapter(getActivity(), messages.toArrayList()));
                if(messages.isEmpty()){
                    statusView.setText("Start the conversation");
                }else{
                    statusView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(VKError error) {
                statusView.setText(error.toString());

            }
        });




        return rootView;
    }

    public static ChatFragment getInstance(Bundle extras) {
        ChatFragment fragment = new ChatFragment();
        fragment.setArguments(extras);
        return fragment;
    }
}
