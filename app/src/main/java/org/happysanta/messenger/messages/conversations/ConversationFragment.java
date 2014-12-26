package org.happysanta.messenger.messages.conversations;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.methods.VKApiMessages;
import com.vk.sdk.api.model.VKApiMessage;
import com.vk.sdk.api.model.VKList;

import org.happysanta.messenger.R;
import org.happysanta.messenger.longpoll.LongpollService;
import org.happysanta.messenger.longpoll.listeners.LongpollDialogListener;
import org.happysanta.messenger.longpoll.updates.LongpollNewMessage;
import org.happysanta.messenger.longpoll.updates.LongpollTyping;
import org.happysanta.messenger.messages.ChatActivity;
import org.happysanta.messenger.messages.core.MessagesAdapter;

import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class ConversationFragment extends Fragment {

    // core
    private VKList<VKApiMessage> messages;

    // ui
    private ListView messagesList;
    private Button sendButton;
    private EditText editMessageText;
    private int dialogId;
    private boolean isChat;
    private MessagesAdapter messagesAdapter;

    public ConversationFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);
        messagesList = (ListView) rootView.findViewById(R.id.messages_list);
        final TextView statusView = (TextView) rootView.findViewById(R.id.status);
        sendButton = (Button) rootView.findViewById(R.id.send_button);
        editMessageText = (EditText) rootView.findViewById(R.id.message_box);
        dialogId = getArguments().getInt(ChatActivity.ARG_DIALOGID, 0);
        isChat = getArguments().getBoolean(ChatActivity.ARG_ISCHAT, false);


        statusView.setText("loading");

        VKRequest request = isChat? new VKApiMessages().getChatHistory(dialogId):new VKApiMessages().getHistory(dialogId);
                request.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        messages = (VKList<VKApiMessage>) response.parsedModel;
                        VKList<VKApiMessage> messagesSort = new VKList();
                        for (VKApiMessage message : messages) {
                            messagesSort.add(0, message);
                            messages = messagesSort;
                        }
                        messagesAdapter = new MessagesAdapter(getActivity(), messages);
                        messagesList.setAdapter(messagesAdapter);
                        if (messages.isEmpty()) {
                            statusView.setText("Start the conversation");
                        } else {
                            statusView.setVisibility(View.GONE);
                        }
                        messagesList.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                    }
                    @Override
                    public void onError(VKError error) {
                        statusView.setText(error.toString());
                    }
                });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        LongpollService.addConversationListener(new LongpollDialogListener(dialogId) {
            @Override
            public void onNewMessages(ArrayList<LongpollNewMessage> newMessages) {
                messagesAdapter.newMessages(newMessages);
                tryScrollToDown();
            }

            @Override
            public void onTyping(ArrayList<LongpollTyping> typing) {
                messagesAdapter.typing();

            }
        });
        return rootView;
    }

    private void sendMessage() {
        String messageText = editMessageText.getText().toString();
        if(messageText==null || messageText.equals("")){
            return;
        }
        VKApiMessage message = new VKApiMessage();
        if (isChat) {
            message.chat_id = dialogId;
        }else {
            message.user_id = dialogId;
        }
        message.body = messageText;
        message.out = true;
        message.read_state = false;
        VKRequest request = new VKApiMessages().send(message);
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                editMessageText.setText(null);
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
            }
        });
        messages.add(message);
        messagesAdapter.notifyDataSetChanged();
        tryScrollToDown();
    }

    private void tryScrollToDown() {
        // todo проскроливать вниз
    }


    public static ConversationFragment getInstance(Bundle extras) {
        ConversationFragment fragment = new ConversationFragment();
        fragment.setArguments(extras);
        return fragment;
    }
}