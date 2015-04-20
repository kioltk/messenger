package org.happysanta.messenger.messages;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.methods.VKApiMessages;
import com.vk.sdk.api.model.VKApiMessage;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKAttachments;
import com.vk.sdk.api.model.VKList;

import org.happysanta.messenger.R;
import org.happysanta.messenger.core.ActivityResultCatcher;
import org.happysanta.messenger.core.BaseFragment;
import org.happysanta.messenger.core.util.Dimen;
import org.happysanta.messenger.core.util.ProfileUtil;
import org.happysanta.messenger.longpoll.LongpollService;
import org.happysanta.messenger.longpoll.listeners.LongpollDialogListener;
import org.happysanta.messenger.longpoll.updates.LongpollNewMessage;
import org.happysanta.messenger.longpoll.updates.LongpollTyping;
import org.happysanta.messenger.messages.attach.AttachAdapter;
import org.happysanta.messenger.messages.attach.AttachCountListener;
import org.happysanta.messenger.messages.attach.AttachDialog;
import org.happysanta.messenger.messages.attach.AttachRequestCode;
import org.happysanta.messenger.messages.groupchats.GroupChatInfoActivity;
import org.happysanta.messenger.messages.core.DialogUtil;
import org.happysanta.messenger.messages.core.MessagesAdapter;
import org.happysanta.messenger.user.ProfileActivity;

import java.io.File;
import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class ChatFragment extends BaseFragment {

    // core
    private VKList<VKApiMessage> messages = new VKList<>();
    private MessagesAdapter messagesAdapter;
    private DialogUtil dialogUtil;
    private int dialogId;
    private boolean isChat;

    // ui
    private RecyclerView messagesRecycler;
    private View sendButton;
    private EditText editMessageText;
    private TextView statusView;
    private ImageView attachButton;

    // attach
    private static final int REQUEST_CODE_FILE = 5;
    private boolean attachWindowOpened = false;
    private AttachDialog attachDialog;
    private ArrayList<VKAttachments.VKApiAttachment> attaches = new ArrayList<>(10);
    private VKList<VKApiUserFull> participants;
    private RecyclerView attachRecycler;
    private AttachAdapter attachAdapter;
    private ActivityResultCatcher attachResultCatcher = new ActivityResultCatcher() {
        @Override
        public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
            return false;
        }
    };

    public ChatFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_chat, container, false);


        attachButton = (ImageView) findViewById(R.id.attach);
        attachRecycler = (RecyclerView) findViewById(R.id.attach_recycler);

        sendButton = findViewById(R.id.send_button);
        editMessageText = (EditText) findViewById(R.id.message_box);
        messagesRecycler = (RecyclerView) findViewById(R.id.recycler);

        statusView = (TextView) findViewById(R.id.status);

        dialogId = getArguments().getInt(ChatActivity.ARG_DIALOGID, 0);
        isChat = getArguments().getBoolean(ChatActivity.ARG_ISCHAT, false);
        participants = new VKList<>();
        SparseArray<VKApiUserFull> usersSparseArray = getArguments().getSparseParcelableArray(ChatActivity.ARG_CHAT_PARTICIPANTS);
        for (int i = 0, sparseArray = usersSparseArray.size(); i < sparseArray; i++) {
            VKApiUserFull user = usersSparseArray.valueAt(i);
            participants.add(user);
        }
        if(participants.getById(ProfileUtil.getUserId())==null){
            participants.add(ProfileUtil.getUser());
        }
        dialogUtil = new DialogUtil(isChat, dialogId);

        statusView.setText("loading");
        editMessageText.setText(dialogUtil.getBody());
        messagesRecycler.setHasFixedSize(false);
        messagesRecycler.setLayoutManager(new LinearLayoutManager(activity));
        messagesAdapter = new MessagesAdapter(activity, messages, participants, isChat);
        messagesRecycler.setAdapter(messagesAdapter);
        VKRequest request = isChat ? new VKApiMessages().getChatHistory(dialogId) : new VKApiMessages().getHistory(dialogId);
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                VKList<VKApiMessage> messagesSort = new VKList();
                for (VKApiMessage message : (VKList<VKApiMessage>) response.parsedModel) {
                    messagesSort.add(0, message);
                }
                messages.addAll(messagesSort);
                if (messages.isEmpty()) {
                    statusView.setText("Start the conversation");
                } else {
                    statusView.setVisibility(View.GONE);
                }
                messagesAdapter.notifyDataSetChanged();
                tryScrollToBottom();
                //messagesRecycler.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
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
                sendMessage();
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
        editMessageText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // showAttach(false);
                    tryScrollToBottom();
                }
            }
        });

        attachButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleAttach();
            }
        });
        attachAdapter = new AttachAdapter(attaches);
        attachAdapter.setCountListener(new AttachCountListener() {

            ValueAnimator animator;
            int attachRecyclerHeight = Dimen.get(R.dimen.attachRecyclerHeight);
            ValueAnimator.AnimatorUpdateListener animationListener = new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    ViewGroup.LayoutParams params = attachRecycler.getLayoutParams();
                    params.height = (int) (attachRecyclerHeight*((Float)animation.getAnimatedValue()));
                    attachRecycler.setLayoutParams(params);
                    attachRecycler.invalidate();
                }
            };

            @Override
            public void onCountChanged(final int newCount) {
                attachRecycler.post(new Runnable() {

                    @Override
                    public void run() {
                        animator = null;
                        if (newCount == 0) {
                            if(attachRecycler.getLayoutParams().height>1){
                                animator = ValueAnimator.ofFloat(1f, 0f);
                                animator.addUpdateListener(animationListener);
                                animator.addListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        //attachRecycler.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animation) {

                                    }
                                });
                            }
                        } else {
                            if(attachRecycler.getLayoutParams().height<1) {
                                animator = ValueAnimator.ofFloat(0f, 1f);
                                animator.addUpdateListener(animationListener);


                            }
                        }
                        if(animator!=null) {
                            if (newCount > 0){
                                animator.setInterpolator(new OvershootInterpolator());
                            } else {
                                // todo ios animation?
                                animator.setInterpolator(new BounceInterpolator());
                            }

                            animator.setDuration(500);
                            animator.setStartDelay(100);
                            animator.start();
                        }

                    }
                });
            }
        });
        attachRecycler.setHasFixedSize(false);
        attachRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        attachRecycler.setAdapter(attachAdapter);

        return rootView;
    }

    private void toggleAttach() {
        showAttach(!attachWindowOpened);
    }

    private void showAttach(boolean show) {
        if(attaches.size()>=10){
            Toast.makeText(activity, "Too many attaches", Toast.LENGTH_SHORT).show();
            return;
        }
        attachButton.setImageResource(R.drawable.ic_header_important);
        attachDialog = new AttachDialog(activity);
        attachDialog.show();
        /*attachDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                attachDialog = null;
            }
        });*/
    }


    public void sendMessage() {
        String messageText = editMessageText.getText().toString();
        if (messageText == null || messageText.equals("")) {
            return;
        }
        VKApiMessage message = new VKApiMessage();
        if (isChat) {
            message.chat_id = dialogId;
        } else {
            message.user_id = dialogId;
        }
        message.body = messageText;
        message.out = true;
        message.read_state = false;
        message.guid = (int) (System.currentTimeMillis() / 1000L * dialogId);

        messagesAdapter.send(message);
        tryScrollToBottom();

        editMessageText.setText(null);
    }

    private void tryScrollToBottom() {
        messagesRecycler.post(new Runnable() {
            @Override
            public void run() {
                messagesRecycler.smoothScrollToPosition(messagesAdapter.getItemCount() - 1);
                //messagesRecycler.scrollTo(0,messagesRecycler.getHeight());
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {

        if (isChat) {
            menuInflater.inflate(R.menu.menu_chat, menu);
        } else {
            menuInflater.inflate(R.menu.menu_conversation, menu);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_profile: {
                startActivity(ProfileActivity.openProfile(getActivity(), dialogId));
            }
            break;
            case R.id.action_chat_participants: {
                startActivity(GroupChatInfoActivity.openDialogInfo(getActivity(), dialogId));
            }
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public static ChatFragment getInstance(Bundle extras) {
        ChatFragment fragment = new ChatFragment();
        fragment.setArguments(extras);
        return fragment;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case AttachRequestCode.FILE:
                if(resultCode== Activity.RESULT_OK){
                    ArrayList<String> list = data.getStringArrayListExtra("data");
                    File[] files = new File[list.size()];
                    for (int i = 0; i < list.size(); i++) {
                        files[i] = new File(list.get(i));
                    }
                    attachAdapter.onFileAttached(files);
                }
                break;
            case AttachRequestCode.MAP:
                if(resultCode == Activity.RESULT_OK){
                    //attachAdapter.onGeoAttached(new AttachGeo());
                }
                break;
        }
    }


}