package org.happysanta.messenger.messages.core;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;

import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.methods.VKApiMessages;
import com.vk.sdk.api.model.VKApiMessage;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKAttachments;
import com.vk.sdk.api.model.VKList;

import org.happysanta.messenger.R;
import org.happysanta.messenger.longpoll.updates.LongpollNewMessage;
import org.happysanta.messenger.messages.core.holders.AudioMessageViewHolder;
import org.happysanta.messenger.messages.core.holders.ComplexMessageViewHolder;
import org.happysanta.messenger.messages.core.holders.FileMessageViewHolder;
import org.happysanta.messenger.messages.core.holders.GeoMessageViewHolder;
import org.happysanta.messenger.messages.core.holders.LoadingMessageViewHolder;
import org.happysanta.messenger.messages.core.holders.MessageViewHolder;
import org.happysanta.messenger.messages.core.holders.PhotoMessageViewHolder;
import org.happysanta.messenger.messages.core.holders.StickerMessageViewHolder;
import org.happysanta.messenger.messages.core.holders.TypingMessageViewHolder;
import org.happysanta.messenger.messages.core.holders.VideoMessageViewHolder;

import java.util.ArrayList;

/**
 * Created by Jesus Christ. Amen.
 */
public class MessagesAdapter extends RecyclerView.Adapter<MessageViewHolder> {
    private final Activity activity;
    private final VKList<VKApiMessage> messages;
    private final boolean isChat;
    private final VKList<VKApiUserFull> chatUsers;
    private final View typingViewItem1;
    private View typingView;

    public MessagesAdapter(Activity activity, VKList<VKApiMessage> messages, VKList<VKApiUserFull> participants) {
        this(activity, messages, participants, true);
    }

    public MessagesAdapter(Activity activity, VKList<VKApiMessage> messages, VKList<VKApiUserFull> participants, boolean isChat) {
        this.activity = activity;
        this.messages = messages;
        this.isChat = isChat;
        this.chatUsers = participants;
        typingView = LayoutInflater.from(activity).inflate(R.layout.item_message_typing, null);
        typingViewItem1 = typingView.findViewById(R.id.typing_view_item1);
    }

    @Override
    public int getItemCount() {
        return messages.size() + 2;
    }

    public VKApiMessage getItem(int position) {
        return messages.get(position - 1);
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = MessageViewType.Unknown;
        if (position == 0) {
            return MessageViewType.Loading;
        }
        if (position == getItemCount() - 1) {
            return MessageViewType.Typing;
        }
        VKApiMessage currentMessage = getItem(position);

        if (currentMessage.sticker != null) {
            return MessageViewType.Sticker;
        }

        // todo complex checking
        if (currentMessage.body != null && !currentMessage.body.equals("")) {
            if (currentMessage.emoji && currentMessage.body.length() == 2) {
                return MessageViewType.Emoji;
            } else {
                return MessageViewType.Complex;
            }
        } else {
            if (currentMessage.geo != null) {
                return MessageViewType.Geo;
            } else {
                if (currentMessage.attachments != null && !currentMessage.attachments.isEmpty()) {
                    String attachType = "";
                    attachType = currentMessage.attachments.get(0).getType();
                    switch (attachType) {
                        case VKAttachments.TYPE_PHOTO:
                            return MessageViewType.Photo;
                        case VKAttachments.TYPE_AUDIO:
                            return MessageViewType.Audio;
                        case VKAttachments.TYPE_DOC:
                            return MessageViewType.Doc;
                        case VKAttachments.TYPE_VIDEO:
                            return MessageViewType.Video;
                    }
                }
            }
        }
        return viewType;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case MessageViewType.Typing:
                return new TypingMessageViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_message_typing, null));
            case MessageViewType.Loading:
                return new LoadingMessageViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_message_loading, null));
            case MessageViewType.Photo:
                return new PhotoMessageViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_message_photo, null));
            case MessageViewType.Doc:
                return new FileMessageViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_message_file, null));
            case MessageViewType.Video:
                return new VideoMessageViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_message_video, null));
            case MessageViewType.Audio:
                return new AudioMessageViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_message_audio, null));
            case MessageViewType.Sticker:
                return new StickerMessageViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_message_sticker, null));
            case MessageViewType.Geo:
                return new GeoMessageViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_message_geo, null));
            case MessageViewType.Complex:
                return new ComplexMessageViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_message_complex, null));
        }
        return new MessageViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_message_empty, null));
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        if (itemViewType < 0) {
            return;
        }
        VKApiMessage prevMessage = position > 1 ? getItem(position - 1) : null;
        VKApiMessage currentMessage = getItem(position);
        VKApiMessage nextMessage = position < getItemCount() - 2 ? getItem(position + 1) : null;
        // getView(position, holder.itemView);
        switch (itemViewType) {
            case MessageViewType.Complex:
                holder.bindData(currentMessage);
                break;
            case MessageViewType.Video:
            case MessageViewType.Photo:
                holder.bindData(currentMessage);
                if (!currentMessage.out) {
                    holder.showOwner(chatUsers.getById(currentMessage.user_id));
                } else {
                    holder.hideOwner();
                }
                break;
            case MessageViewType.Audio:
                holder.bindData(currentMessage);
                if (!currentMessage.out) {
                    holder.showOwner(chatUsers.getById(currentMessage.user_id));
                } else {
                    holder.hideOwner();
                }
            case MessageViewType.Emoji:
            case MessageViewType.Sticker:
                holder.bindData(currentMessage);
                break;
            case MessageViewType.Geo:
                holder.bindData(currentMessage);
            case MessageViewType.Unknown:
            default:
                holder.bindData(currentMessage);
        }
        if (isChat) {
            if (prevMessage != null && currentMessage.user_id == prevMessage.user_id) {
                holder.groupTop();
            } else {
                holder.ungroupTop();
                if (!currentMessage.out) {
                    holder.showOwner(chatUsers.getById(currentMessage.user_id));
                } else {
                    holder.hideOwner();
                }
            }
            if (nextMessage != null && currentMessage.user_id == nextMessage.user_id) {
                holder.groupBottom();
            } else {
                holder.ungroupBottom();
            }


        } else {
            if (prevMessage != null && currentMessage.out == prevMessage.out) {
                holder.groupTop();
            } else {
                holder.ungroupTop();
            }
            if (nextMessage != null && currentMessage.out == nextMessage.out) {
                holder.groupBottom();
            } else {
                holder.ungroupBottom();
            }
        }
        holder.itemView.setAlpha(currentMessage.id==0?0.5f:1);
    }


    @Override
    public long getItemId(int position) {
        if(position==0){
            return -1;
        }
        if(position==messages.size()){
            return -2;
        }
        return messages.get(position).id;

    }


    public void newMessages(ArrayList<LongpollNewMessage> newMessages) {
        ArrayList<LongpollNewMessage> filteredMessages = new ArrayList<>();
        for (LongpollNewMessage newMessage: newMessages) {

            if (!newMessage.out) {
                messages.add(newMessage);// todo add sorted by time
                notifyItemInserted(messages.indexOf(newMessage)+1);
            } else{
                messages.add(newMessage);// todo add by sending query
                notifyItemInserted(messages.indexOf(newMessage)+1);
            }
            // update to full?
        }
    }

    public void typing() {
        typingView.setVisibility(View.VISIBLE);
        AnimationSet set = new AnimationSet(true);
        set.setRepeatMode(Animation.RESTART);
        set.setDuration(600);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.addAnimation(new TranslateAnimation(0, 0, 0, 100));
        typingViewItem1.startAnimation(set);
        typingView.postDelayed(new Runnable() {
            @Override
            public void run() {
                typingView.setVisibility(View.INVISIBLE);
            }
        }, 5500);
    }

    boolean sending = false;
    public void send(final VKApiMessage message) {
        sending = true;
        // sendingQuery.add(message);
        VKRequest request = new VKApiMessages().send(message);
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                message.id = (int) response.parsedModel;
                sending = false;
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
                sending = false;
            }
        });
        /* messages.add(message);
        notifyItemInserted(messages.size()); */
    }
}