package org.happysanta.messenger.messages.conversations;

import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.methods.VKApiMessages;
import com.vk.sdk.api.model.VKApiMessage;
import com.vk.sdk.api.model.VKList;

import org.happysanta.messenger.R;
import org.happysanta.messenger.core.BaseFragment;
import org.happysanta.messenger.longpoll.LongpollService;
import org.happysanta.messenger.longpoll.listeners.LongpollDialogListener;
import org.happysanta.messenger.longpoll.updates.LongpollNewMessage;
import org.happysanta.messenger.longpoll.updates.LongpollTyping;
import org.happysanta.messenger.messages.DialogActivity;
import org.happysanta.messenger.messages.chats.ChatDialog;
import org.happysanta.messenger.messages.core.AttachFragment;
import org.happysanta.messenger.messages.core.DialogUtil;
import org.happysanta.messenger.messages.core.MessagesAdapter;
import org.happysanta.messenger.user.UserDialog;

import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class ConversationFragment extends BaseFragment {

    // core
    private VKList<VKApiMessage> messages;
    private MessagesAdapter messagesAdapter;
    private DialogUtil dialogUtil;
    private int dialogId;
    private boolean isChat;

    // ui
    private ListView messagesList;
    private View sendButton;
    private EditText editMessageText;
    private TextView statusView;
    private ImageView attachButton;

    // attach
    private boolean attachWindowOpened;
    private AttachFragment attachFragment;

    public ConversationFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_chat, container, false);
        messagesList = (ListView) findViewById(R.id.messages_list);
        statusView = (TextView) findViewById(R.id.status);
        attachButton = (ImageView) findViewById(R.id.attach);
        sendButton = findViewById(R.id.send_button);
        editMessageText = (EditText) findViewById(R.id.message_box);
        dialogId = getArguments().getInt(DialogActivity.ARG_DIALOGID, 0);
        isChat = getArguments().getBoolean(DialogActivity.ARG_ISCHAT, false);

        dialogUtil = new DialogUtil(isChat, dialogId);

        statusView.setText("loading");
        editMessageText.setText(dialogUtil.getBody());

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
                        messagesAdapter = new MessagesAdapter(activity, messages);
                        messagesList.setAdapter(messagesAdapter);
                        if (messages.isEmpty()) {
                            statusView.setText("Start the conversation");
                        } else {
                            statusView.setVisibility(View.GONE);
                        }
                        //messagesList.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                    }
                    @Override
                    public void onError(VKError error) {
                        statusView.setText(error.toString());
                    }
                });


        LongpollService.addConversationListener(new LongpollDialogListener(dialogId) {
            @Override
            public void onNewMessages(ArrayList<LongpollNewMessage> newMessages) {
                messagesAdapter.newMessages(newMessages);
                tryScrollToBottom();
            }

            @Override
            public void onTyping(ArrayList<LongpollTyping> typing) {
                messagesAdapter.typing();

            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                message.guid = (int) (System.currentTimeMillis()/1000L * dialogId);
                sendMessage(message);
                editMessageText.setText(null);
            }
        });

        editMessageText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dialogUtil.saveBody(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        attachButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleAttach();
            }
        });

        return rootView;
    }

    private void toggleAttach() {
        if(!attachWindowOpened) {
            // closing
            attachButton.setImageResource(R.drawable.ic_header_important);
            attachFragment = AttachFragment.getInstance();
            getChildFragmentManager().beginTransaction().replace(R.id.attach_window, attachFragment).commit();
        } else {
            // opening
            attachButton.setImageResource(R.drawable.ic_drawer);
            getChildFragmentManager().beginTransaction().remove(attachFragment).commit();
        }
        attachWindowOpened = !attachWindowOpened;
    }

    public void sendMessage(VKApiMessage message) {
        messagesAdapter.send(message);
        tryScrollToBottom();
    }

    private void tryScrollToBottom() {
        messagesList.post(new Runnable() {
            @Override
            public void run() {
                messagesList.smoothScrollToPosition(messagesAdapter.getCount() - 1);
                //messagesList.scrollTo(0,messagesList.getHeight());
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater menuInflater) {

        if(isChat) {
            menuInflater.inflate(R.menu.menu_chat, menu);
        } else {
            menuInflater.inflate(R.menu.menu_conversation, menu);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_profile: {
                UserDialog profileDialog = new UserDialog(activity, dialogId);
                profileDialog.show();
            }break;
            case R.id.action_chat_participants: {
                ChatDialog chatDialog = new ChatDialog(activity, dialogId);
                chatDialog.show();
            }break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public static ConversationFragment getInstance(Bundle extras) {
        ConversationFragment fragment = new ConversationFragment();
        fragment.setArguments(extras);
        return fragment;
    }
}