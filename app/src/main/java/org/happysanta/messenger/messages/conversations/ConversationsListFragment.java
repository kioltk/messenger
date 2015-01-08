package org.happysanta.messenger.messages.conversations;

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
import com.vk.sdk.api.model.VKList;

import org.happysanta.messenger.R;
import org.happysanta.messenger.core.BaseFragment;
import org.happysanta.messenger.core.util.BitmapUtil;
import org.happysanta.messenger.core.util.ImageUtil;
import org.happysanta.messenger.longpoll.LongpollService;
import org.happysanta.messenger.longpoll.listeners.LongpollDialogListener;
import org.happysanta.messenger.longpoll.updates.LongpollNewMessage;
import org.happysanta.messenger.longpoll.updates.LongpollTyping;
import org.happysanta.messenger.messages.DialogActivity;

import java.util.ArrayList;


/**
 * Created by Jesus Christ. Amen.
 */
public class ConversationsListFragment extends BaseFragment {
    private View rootView;
    private VKList<VKApiDialog> dialogs;
    private TextView status;
    private boolean chatsShowed = false;
    private RecyclerView recycler;
    private TextView debugView;
    private LinearLayoutManager recyclerLayoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_conversations_list, container, false);
        recycler = (RecyclerView) rootView.findViewById(R.id.recycler);
        status = (TextView) rootView.findViewById(R.id.status);
        debugView = (TextView) rootView.findViewById(R.id.debug);

        recyclerLayoutManager = new LinearLayoutManager(activity);
        recycler.setLayoutManager(recyclerLayoutManager);
        recycler.setHasFixedSize(true);

        new VKApiMessages().getDialogs().executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                VKList<VKApiDialog> messages = (VKList<VKApiDialog>) response.parsedModel;
                dialogs = messages;
                recycler.setAdapter(new ConversationsAdapter());
                status.setVisibility(View.GONE);

                LongpollService.addGlobalConversationListener(new LongpollDialogListener(0) {
                    @Override
                    public void onNewMessages(ArrayList<LongpollNewMessage> newMessages) {
                        for (LongpollNewMessage newMessage : newMessages) {
                            for (VKApiDialog dialog : dialogs) {
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
                    public void onTyping(ArrayList<LongpollTyping> typing) {

                    }
                });
                LongpollService.addGlobalChatListener(new LongpollDialogListener(0) {
                    @Override
                    public void onNewMessages(ArrayList<LongpollNewMessage> newMessages) {
                        for (LongpollNewMessage newMessage : newMessages) {
                            for (VKApiDialog dialog : dialogs) {
                                if(dialog.isChat())
                                    if(dialog.dialogId==newMessage.chat_id){
                                        dialog.setLastMessage(newMessage);
                                        moveToTop(dialog);
                                        break;
                                    }
                            }
                        }
                        //recycler.getAdapter().notifyDataSetChanged();
                    }

                    @Override
                    public void onTyping(ArrayList<LongpollTyping> typing) {

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

    private void moveToTop(VKApiDialog dialog) {
        RecyclerView.ViewHolder firstHolder = recycler.findViewHolderForPosition(0);
        int top = 0;
        if(firstHolder!=null) {
            top = (int) firstHolder.itemView.getTop();
        } else {
            top = 1;
        }
        int dialogIndex = dialogs.indexOf(dialog);
        if(dialogIndex!=0) {
            dialogs.remove(dialogIndex);
            dialogs.add(0, dialog);
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
                    // tod show and hide?

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
                item.setChecked(chatsShowed);
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
            conversationViewHolder.bindContent(dialogs.get(i));
        }

        @Override
        public int getItemCount() {
            return dialogs.size();
        }


    }

    private class ConversationViewHolder extends RecyclerView.ViewHolder{
        private final TextView titleView;
        private final TextView bodyView;
        private final TextView dateView;
        private final ImageView onlineView;
        private final ImageView photoView;

        public ConversationViewHolder(View itemView) {
            super(itemView);

            titleView = (TextView) itemView.findViewById(R.id.title);
            bodyView = (TextView) itemView.findViewById(R.id.body);
            dateView = (TextView) itemView.findViewById(R.id.date);
            onlineView = (ImageView) itemView.findViewById(R.id.online);
            photoView = (ImageView) itemView.findViewById(R.id.dialog_photo);
        }

        public void bindContent(final VKApiDialog dialog) {
            try {
                titleView.setText(dialog.title);
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


                dateView.setText("" + dialog.getDate());
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
                        Intent intent = DialogActivity.getActivityIntent(getActivity(), dialog);
                        startActivity(intent);
                    }
                });
            }catch (Exception exp){
                bodyView.setText("ERROR");
            }
        }
    }
}
