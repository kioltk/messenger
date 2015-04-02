package org.happysanta.messenger.messages.conversations;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.methods.VKApiMessages;
import com.vk.sdk.api.model.VKApiDialog;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;

import org.happysanta.messenger.R;
import org.happysanta.messenger.core.BaseFragment;
import org.happysanta.messenger.core.util.BitmapUtil;
import org.happysanta.messenger.core.util.ImageUtil;
import org.happysanta.messenger.core.util.TimeUtils;
import org.happysanta.messenger.core.views.SuperViewHolder;
import org.happysanta.messenger.friends.adapter.FriendsAdapter;
import org.happysanta.messenger.longpoll.LongpollService;
import org.happysanta.messenger.longpoll.listeners.LongpollDialogListener;
import org.happysanta.messenger.longpoll.updates.LongpollNewMessage;
import org.happysanta.messenger.longpoll.updates.LongpollTyping;
import org.happysanta.messenger.messages.ChatActivity;
import org.happysanta.messenger.messages.core.DialogUtil;

import java.util.ArrayList;


/**
 * Created by Jesus Christ. Amen.
 */
public class ConversationsListFragment extends BaseFragment {
    private View rootView;
    private VKList<VKApiDialog> allDialogs = new VKList<>();
    private VKList<VKApiDialog> showingDialogs = new VKList<>();
    private TextView status;
    private boolean chatsShowed = false;
    private RecyclerView recycler;
    private TextView debugView;
    private LinearLayoutManager recyclerLayoutManager;
    private ConversationsAdapter recyclerAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_conversations_list, container, false);
        recycler = (RecyclerView) rootView.findViewById(R.id.recycler);
        status = (TextView) rootView.findViewById(R.id.status);
        debugView = (TextView) rootView.findViewById(R.id.debug);

        recyclerLayoutManager = new LinearLayoutManager(activity);
        recycler.setLayoutManager(recyclerLayoutManager);
        recycler.setHasFixedSize(false);

        chatsShowed = DialogUtil.isChatsShowed();

        new VKApiMessages().getDialogs().executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {

                allDialogs.addAll((VKList<VKApiDialog>) response.parsedModel);
                if(chatsShowed) {
                    showingDialogs.addAll(allDialogs);
                } else {
                    for (VKApiDialog dialog : allDialogs) {
                        if (!dialog.isChat()) {
                            showingDialogs.add(dialog);
                        }
                    }
                }
                recyclerAdapter = new ConversationsAdapter();
                recyclerAdapter.setHasStableIds(true);
                recycler.setAdapter(recyclerAdapter);
                status.setVisibility(View.GONE);

                LongpollService.addGlobalConversationListener(new LongpollDialogListener(0) {
                    @Override
                    public void onNewMessages(ArrayList<LongpollNewMessage> newMessages) {
                        for (LongpollNewMessage newMessage : newMessages) {
                            for (VKApiDialog dialog : showingDialogs) {
                                if(!dialog.isChat())
                                    if(dialog.dialogId==newMessage.user_id){
                                        dialog.setLastMessage(newMessage);
                                        moveToTop(dialog);
                                        break;
                                    }
                            }
                        }
                        //(recycler.getAdapter()).notifyDataSetChanged();
                    }

                    @Override
                    public void onTyping(ArrayList<LongpollTyping> typings) {
                        for (LongpollTyping typing : typings) {
                            for (VKApiDialog dialog : showingDialogs) {
                                if(!dialog.isChat())
                                    if(dialog.dialogId==typing.dialogId){
                                        showTyping(dialog, typing);
                                        break;
                                    }
                            }
                        }
                    }
                });
                LongpollService.addGlobalChatListener(new LongpollDialogListener(0) {
                    @Override
                    public void onNewMessages(ArrayList<LongpollNewMessage> newMessages) {
                        if(chatsShowed) {
                            for (LongpollNewMessage newMessage : newMessages) {
                                for (VKApiDialog dialog : showingDialogs) {
                                    if (dialog.isChat())
                                        if (dialog.dialogId == newMessage.chat_id) {
                                            dialog.setLastMessage(newMessage);
                                            moveToTop(dialog);
                                            break;
                                        }
                                }
                            }
                        } else {
                            for (LongpollNewMessage newMessage : newMessages) {
                                for (VKApiDialog dialog : allDialogs) {
                                    if (dialog.isChat())
                                        if (dialog.dialogId == newMessage.chat_id) {
                                            dialog.setLastMessage(newMessage);
                                            allDialogs.remove(dialog);
                                            allDialogs.add(0, dialog);
                                            break;
                                        }
                                }
                            }
                        }
                        //recycler.getAdapter().notifyDataSetChanged();
                    }

                    @Override
                    public void onTyping(ArrayList<LongpollTyping> typings) {
                        for (LongpollTyping typing : typings) {
                            for (VKApiDialog dialog : showingDialogs) {
                                if(dialog.isChat())
                                    if(dialog.dialogId==typing.dialogId){
                                        showTyping(dialog, typing);
                                        break;
                                    }
                            }
                        }
                    }
                });
            }


            @Override
            public void onError(VKError error) {
                status.setText(error.toString());
            }
        });


        return rootView;
    }

    private void showTyping(VKApiDialog dialog, LongpollTyping typing) {
        ConversationViewHolder viewHolder = (ConversationViewHolder) recycler.findViewHolderForItemId(dialog.isChat() ? -dialog.getId() : dialog.getId());
        if(viewHolder!=null)
            viewHolder.showTyping(typing);
    }

    private void moveToTop(VKApiDialog dialog) {
        RecyclerView.ViewHolder firstHolder = recycler.findViewHolderForPosition(0);
        int top = 0;
        if(firstHolder!=null) {
            top = (int) firstHolder.itemView.getTop();
        } else {
            top = 1;
        }
        int dialogIndex = showingDialogs.indexOf(dialog);
        if(dialogIndex!=0) {
            allDialogs.remove(dialog);
            allDialogs.add(0, dialog);
            showingDialogs.remove(dialogIndex);
            showingDialogs.add(0, dialog);
            recycler.getAdapter().notifyItemMoved(dialogIndex, 0);
        }
        recycler.getAdapter().notifyItemChanged(0);
        if(top==0){
            recyclerLayoutManager.scrollToPositionWithOffset(0, 0);
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_dialogs, menu);
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            switch (item.getItemId()) {
                case R.id.action_dialogs_showchats:{
                    item.setChecked(chatsShowed);
                }break;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_dialogs_showchats: {
                //ыц
                chatsShowed = !item.isChecked();
                DialogUtil.setChatsShowed(chatsShowed);
                item.setChecked(chatsShowed);

                if(chatsShowed){
                    for (int i = 0; i < allDialogs.size(); i++) {
                        VKApiDialog dialog = allDialogs.get(i);
                        if (dialog.isChat()) {
                            showingDialogs.add(i, dialog);
                            recyclerAdapter.notifyItemInserted(i);
                        }
                    }
                } else {
                    for (int i = 0; i < showingDialogs.size(); i++) {
                        VKApiDialog dialog = showingDialogs.get(i);
                        if (dialog.isChat()) {
                            showingDialogs.remove(i);
                            recyclerAdapter.notifyItemRemoved(i);
                            i--;
                        }
                    }
                }


            }
            break;

        }


        return super.onOptionsItemSelected(item);
    }

    private class ConversationsAdapter extends RecyclerView.Adapter<ConversationViewHolder> {


        @Override
        public ConversationViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new ConversationViewHolder(View.inflate(activity,R.layout.item_dialog, null));
        }

        @Override
        public void onBindViewHolder(ConversationViewHolder conversationViewHolder, int i) {
            conversationViewHolder.bindContent(showingDialogs.get(i));
        }


        @Override
        public int getItemCount() {
            return showingDialogs.size();
        }

        @Override
        public long getItemId(int position) {
            VKApiDialog dialog = showingDialogs.get(position);
            return dialog.isChat()? -dialog.getId():dialog.getId();
        }

    }

    private class ConversationViewHolder extends SuperViewHolder {
        private final TextView titleView;
        private final TextView bodyView;
        private final TextView dateView;
        private final ImageView onlineView;
        private final ImageView photoView;
        private final TextView typingView;
        private VKApiDialog dialog;

        public ConversationViewHolder(View itemView) {
            super(itemView);

            titleView = (TextView) itemView.findViewById(R.id.title);
            bodyView = (TextView) itemView.findViewById(R.id.body);
            typingView = (TextView) itemView.findViewById(R.id.typing);
            dateView = (TextView) itemView.findViewById(R.id.date);
            onlineView = (ImageView) itemView.findViewById(R.id.online);
            photoView = (ImageView) itemView.findViewById(R.id.dialog_photo);
        }

        public void bindContent(final VKApiDialog dialog) {
            this.dialog = dialog;
            try {
                titleView.setText(dialog.getTitle());
                bodyView.clearAnimation();
                typingView.clearAnimation();
                bodyView.setAlpha(1);
                typingView.setAlpha(0);
                if (dialog.lastMessage.out) {
                    bodyView.setText(getString(R.string.conversation_body, dialog.getBody()));
                } else {
                    bodyView.setText(dialog.getBody());
                }

                if (!dialog.lastMessage.read_state) {
                    itemView.setBackgroundColor(getResources().getColor(R.color.conversation_unread_background));
                    titleView.setTypeface(null, Typeface.BOLD);
                    bodyView.setTypeface(null, Typeface.BOLD);
                } else {
                    itemView.setBackgroundDrawable(null);
                    titleView.setTypeface(null);
                    bodyView.setTypeface(null);
                }

                onlineView.setVisibility(dialog.isOnline() ? View.VISIBLE : View.GONE);


                dateView.setText(TimeUtils.format(dialog.getDate()*1000, getContext()));



                photoView.setImageBitmap(BitmapUtil.circle(R.drawable.user_placeholder));
                ImageUtil.showFromCache(dialog.getPhoto(), new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        photoView.setImageBitmap(BitmapUtil.circle(loadedImage));
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {

                    }
                });
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = ChatActivity.getActivityIntent(getActivity(), dialog);
                        startActivity(intent);
                    }
                });
            }catch (Exception exp){
                bodyView.setText("ERROR");
            }
        }
        ArrayList<VKApiUserFull> typingUsers = new ArrayList<>();
        public void showTyping(final LongpollTyping typing) {
            if (typing.isChat) {
                // few typings at the same time?
                VKApiUserFull user = dialog.getParticipant(typing.userId);
                if(typingUsers.size()==1){

                } else {
                    typingView.setText(typingUsers.toString() + " are typing.");
                }
            }
            bodyView.animate().alpha(0).setDuration(500).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    bodyView.animate().alpha(1).setDuration(500).setStartDelay(5500).setListener(null).start();
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    bodyView.animate().alpha(1).setDuration(500).setStartDelay(5500).setListener(null).start();
                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            }).start();
            typingView.animate().alpha(1).setDuration(500).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    typingView.animate().alpha(0).setDuration(500).setStartDelay(5500).setListener(null).start();
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    typingView.animate().alpha(0).setDuration(500).setStartDelay(5500).setListener(null).start();
                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            }).start();
        }
    }
}
