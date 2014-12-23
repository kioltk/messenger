package org.happysanta.messenger.messages.conversations;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.methods.VKApiMessages;
import com.vk.sdk.api.model.VKApiMessage;
import com.vk.sdk.api.model.VKList;

import org.happysanta.messenger.R;
import org.happysanta.messenger.messages.ChatActivity;
import org.happysanta.messenger.messages.core.MessagesAdapter;


/**
 * A placeholder fragment containing a simple view.
 */
public class ConversationFragment extends Fragment {

    public ConversationFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);
        final ListView messagesList = (ListView) rootView.findViewById(R.id.messages_list);
        final TextView statusView = (TextView) rootView.findViewById(R.id.status);
        final int dialogId = getArguments().getInt(ChatActivity.ARG_DIALOGID, 0);
        boolean isChat = getArguments().getBoolean(ChatActivity.ARG_ISCHAT, false);


        statusView.setText("loading");

        VKRequest request = isChat? new VKApiMessages().getChatHistory(dialogId):new VKApiMessages().getHistory(dialogId);
                request.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        VKList<VKApiMessage> messages = (VKList<VKApiMessage>) response.parsedModel;
                        VKList<VKApiMessage> messagesSort = new VKList();
                        for (VKApiMessage message : messages) {
                            messagesSort.add(0, message);
                            messages = messagesSort;
                        }
                        messagesList.setAdapter(new MessagesAdapter(getActivity(), messages.toArrayList()));
                        if (messages.isEmpty()) {
                            statusView.setText("Start the conversation");
                        } else {
                            statusView.setVisibility(View.GONE);
                        }
                        ((BaseAdapter) messagesList.getAdapter()).notifyDataSetChanged();
                    }
                    @Override
                    public void onError(VKError error) {
                        statusView.setText(error.toString());
                    }
                });


        return rootView;
    }



    public static ConversationFragment getInstance(Bundle extras) {
        ConversationFragment fragment = new ConversationFragment();
        fragment.setArguments(extras);
        return fragment;
    }
}