package org.happysanta.messenger.messages.core;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.methods.VKApiMessages;
import com.vk.sdk.api.model.VKApiMessage;
import com.vk.sdk.api.model.VKApiUserFull;
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

import java.util.ArrayList;

/**
 * Created by Jesus Christ. Amen.
 */
public class MessagesAdapter extends RecyclerView.Adapter<MessageViewHolder> {
    private final Activity activity;
    private final VKList<VKApiMessage> messages;
    private final boolean isChat;
    private final VKList<VKApiUserFull> chatUsers;
    private View typingView;

    public MessagesAdapter(Activity activity, VKList<VKApiMessage> messages, VKList<VKApiUserFull> participants) {
        this(activity, messages, participants, true);
    }
    public MessagesAdapter(Activity activity, VKList<VKApiMessage> messages, VKList<VKApiUserFull> participants, boolean isChat){
        this.activity = activity;
        this.messages = messages;
        this.isChat = isChat;
        this.chatUsers = participants;
        typingView = LayoutInflater.from(activity).inflate(R.layout.item_message_typing, null);
    }

    @Override
    public int getItemCount() {
        return messages.size() + 2;
    }

    public VKApiMessage getItem(int position) {
        return messages.get(position-1);
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = MessageViewType.Unknown;
        if (position==0) {
            return MessageViewType.Loading;
        }
        if(position==getItemCount()-1){
            return MessageViewType.Typing;
        }
        VKApiMessage currentMessage = getItem(position);
        if (currentMessage.body != null && !currentMessage.body.equals("")) {
            if (currentMessage.emoji && currentMessage.body.length() == 2) {
                return MessageViewType.Emoji;
            } else {
                return MessageViewType.Complex;
            }
        } else {
            if (currentMessage.sticker != null) {
                return MessageViewType.Sticker;
            } else {
                if (currentMessage.geo != null) {
                    return MessageViewType.Geo;
                } else {

                    // todo another attaches?

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
            case MessageViewType.File:
                return new FileMessageViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_message_file, null));
            case MessageViewType.Video:
                return new PhotoMessageViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_message_video, null));
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
        if(itemViewType<0){
            return;
        }
        VKApiMessage prevMessage = position > 1 ? getItem(position-1) : null;
        VKApiMessage currentMessage = getItem(position);
        VKApiMessage nextMessage = position < getItemCount() - 2 ? getItem(position + 1) : null;
        // getView(position, holder.itemView);
        switch (itemViewType){
            case MessageViewType.Complex:
                holder.bindData(currentMessage);
                break;
            case MessageViewType.Photo:
                holder.bindData(currentMessage);
                break;
            case MessageViewType.Sticker:
                holder.bindData(currentMessage);
                break;
            case MessageViewType.Geo:
                holder.bindData(currentMessage);
                if(!currentMessage.out) {
                    holder.showOwner(chatUsers.getById(currentMessage.user_id));
                } else {
                    holder.hideOwner();
                }
                return;
            case MessageViewType.Unknown:
            default:
                holder.bindData(currentMessage);
                // holder.showOwner(chatUsers.getById(currentMessage.user_id));
                return;
        }
        if (isChat) {
            if (prevMessage!=null && currentMessage.user_id == prevMessage.user_id) {
                holder.groupTop();
            } else {
                holder.ungroupTop();
                if(!currentMessage.out){
                    holder.showOwner(chatUsers.getById(currentMessage.user_id));
                } else {
                    holder.hideOwner();
                }
            }
            if(nextMessage!=null && currentMessage.user_id == nextMessage.user_id){
                holder.groupBottom();
            } else {
                holder.ungroupBottom();
            }


        } else {
            if (prevMessage!=null && currentMessage.out == prevMessage.out) {
                holder.groupTop();
            } else {
                holder.ungroupTop();
            }
            if(nextMessage!=null && currentMessage.out == nextMessage.out){
                holder.groupBottom();
            } else {
                holder.ungroupBottom();
            }
        }
    }



    @Override
    public long getItemId(int position) {
        return 0;
    }


    public View getView(int position, View convertView) {
        View itemView = convertView;
/*
        if (position == messages.size()) {
            return typingView;
        }


        View bodyView;
        View simpleContentView = itemView.findViewById(R.id.message_simple_content);
        TextView textView = (TextView) itemView.findViewById(R.id.text);
        //ImageView emojiView = (ImageView) itemView.findViewById(R.id.emoji);
        //ImageView stickerView = (ImageView) itemView.findViewById(R.id.sticker);
        final ImageView mapView = (ImageView) itemView.findViewById(R.id.map);
        final TextView dateView = (TextView) itemView.findViewById(R.id.dateView);
        final ImageView ownerView = (ImageView) itemView.findViewById(R.id.owner);

        LinearLayout.LayoutParams mapLayoutParams = (LinearLayout.LayoutParams) mapView.getLayoutParams();
        LinearLayout.LayoutParams dateLayoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        final VKApiMessage currentMessage = getItem(position);

        if (!currentMessage.read_state) {
            itemView.setBackgroundColor(activity.getResources().getColor(R.color.dialog_unread_background));
        } else {
            itemView.setBackgroundDrawable(null);
        }

        ownerView.setVisibility(View.GONE);

        if (currentMessage.body != null && !currentMessage.body.equals("")) {
            if (currentMessage.emoji && currentMessage.body.length() == 2) {
                textView.setVisibility(View.GONE);
                emojiView.setVisibility(View.VISIBLE);
                bodyView = emojiView;
                // todo set emoji?
            } else {
                textView.setText(currentMessage.body);
                bodyView = textView;
            }
        } else {
            if (currentMessage.sticker != null) {
                stickerView.setVisibility(View.VISIBLE);
                textView.setVisibility(View.GONE);
                bodyView = stickerView;
                // todo sticker
            } else {
                if (currentMessage.geo != null) {
                    textView.setVisibility(View.GONE);
                    bodyView = mapView;
                } else {
                    textView.setText("ERROR");
                    bodyView = textView;
                }
            }
        }
        if (currentMessage.geo != null) {
            final VKApiGeo geo = currentMessage.geo;
            mapView.setVisibility(View.VISIBLE);
            mapView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(activity)
                            .setTitle(R.string.map_show_title)
                            .setMessage(R.string.map_show_message)
                            .setPositiveButton(R.string.map_show_ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    activity.startActivity(
                                            new Intent(Intent.ACTION_VIEW, Uri.parse("geo:" + geo.lat + "," + geo.lon
                                                    + "?q=" + geo.lat + "," + geo.lon + "&z=" + 10)));
                                }
                            })
                            .setNegativeButton(R.string.map_show_cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setCancelable(true).show();
                }
            });
            ImageLoader.getInstance().displayImage(MapUtil.getMap(geo.lat, geo.lon, mapLayoutParams.width, mapLayoutParams.height, true, "").toString(), mapView, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {

                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    mapView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {

                }
            });
        }
        dateView.setText("" + currentMessage.date);
        dateView.setVisibility(View.GONE);


        LinearLayout.LayoutParams itemLayoutParams = (LinearLayout.LayoutParams) bodyView.getLayoutParams();
        int itemDefaultPadding = Dimen.get(R.dimen.default_message_padding);
        int itemLeftPadding = itemDefaultPadding;
        int itemTopPadding = itemDefaultPadding;
        int itemRightPadding = itemDefaultPadding;
        int itemBottomPadding = itemDefaultPadding;

        VKApiMessage prevMessage = null;
        VKApiMessage nextMessage = null;
        if (position != 0) {
            prevMessage = messages.get(position - 1);
        }
        if (position < messages.size() - 1) {
            nextMessage = messages.get(position + 1);
        }

        if (currentMessage.out) {
            itemLayoutParams.gravity = Gravity.RIGHT;
            dateLayoutParams.gravity = Gravity.RIGHT;
            mapLayoutParams.gravity = Gravity.RIGHT;
            itemLeftPadding = itemDefaultPadding * 4;
        } else {
            itemLayoutParams.gravity = Gravity.LEFT;
            dateLayoutParams.gravity = Gravity.LEFT;
            mapLayoutParams.gravity = Gravity.LEFT;
            itemRightPadding = itemDefaultPadding * 4;
        }

        if(isChat) {
            itemLeftPadding = itemDefaultPadding * 8;
            if (prevMessage != null && prevMessage.user_id == currentMessage.user_id) {
                itemTopPadding = itemDefaultPadding / 4;
            }else{
                if(!currentMessage.out){
                    ownerView.setVisibility(View.VISIBLE);
                    ownerView.setImageBitmap(BitmapUtil.circle(R.drawable.user_placeholder));
                    VKApiUserFull owner = chatUsers.getById(currentMessage.user_id);
                    ImageUtil.showFromCache(owner.getPhoto(), new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {

                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            ownerView.setImageBitmap(BitmapUtil.circle(loadedImage));
                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {

                        }
                    });
                }
            }
            if (nextMessage != null && nextMessage.user_id == currentMessage.user_id) {
                itemBottomPadding = itemDefaultPadding / 4;
            }
        } else {
            if (prevMessage != null && prevMessage.out == currentMessage.out) {
                itemTopPadding = itemDefaultPadding / 4;
            }
            if (nextMessage != null && nextMessage.out == currentMessage.out) {
                itemBottomPadding = itemDefaultPadding / 4;
            }
        }


        simpleContentView.setPadding(itemLeftPadding, itemTopPadding, itemRightPadding, itemBottomPadding);
        bodyView.setLayoutParams(itemLayoutParams);
        mapView.setLayoutParams(mapLayoutParams);
        dateView.setLayoutParams(dateLayoutParams);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dateView.getVisibility() == View.VISIBLE) {
                    dateView.setVisibility(View.GONE);
                } else {
                    dateView.setVisibility(View.VISIBLE);
                }
            }
        });*/

        return itemView;
    }

    public void newMessages(ArrayList<LongpollNewMessage> newMessages) {
        messages.addAll(newMessages);
        notifyDataSetChanged();
    }

    public void typing() {
        typingView.setVisibility(View.VISIBLE);
        typingView.postDelayed(new Runnable() {
            @Override
            public void run() {
                typingView.setVisibility(View.INVISIBLE);
            }
        }, 5500);
    }

    public void send(final VKApiMessage message) {
        VKRequest request = new VKApiMessages().send(message);
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                //message.id = (int) response.parsedModel;
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
            }
        });
        messages.add(message);
        notifyDataSetChanged();
    }
}