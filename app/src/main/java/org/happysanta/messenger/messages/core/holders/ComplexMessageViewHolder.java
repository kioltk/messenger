package org.happysanta.messenger.messages.core.holders;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.vk.sdk.api.model.VKApiGeo;
import com.vk.sdk.api.model.VKApiMessage;
import com.vk.sdk.api.model.VKApiUserFull;

import org.happysanta.messenger.R;
import org.happysanta.messenger.core.util.BitmapUtil;
import org.happysanta.messenger.core.util.Dimen;
import org.happysanta.messenger.core.util.ImageUtil;
import org.happysanta.messenger.core.util.MapUtil;
import org.happysanta.messenger.core.util.TimeUtils;
import org.happysanta.messenger.messages.core.holders.MessageViewHolder;

/**
 * Created by Jesus Christ. Amen.
 */
public class ComplexMessageViewHolder extends MessageViewHolder {
    private final View bodyView;
    private final TextView textView;
    private final ImageView mapView;
    private final TextView dateView;
    private final ImageView ownerView;
    private int currentGravity = Gravity.RIGHT;
    private int itemDefaultPadding;
    private int itemLeftPadding;
    private int itemTopPadding;
    private int itemRightPadding;
    private int itemBottomPadding;

    public ComplexMessageViewHolder(View itemView) {
        super(itemView);

        bodyView = itemView.findViewById(R.id.message_simple_content);
        textView = (TextView) itemView.findViewById(R.id.text);
        // emojiView = (ImageView) itemView.findViewById(R.id.emoji);
        // stickerView = (ImageView) itemView.findViewById(R.id.sticker);
        mapView = (ImageView) itemView.findViewById(R.id.map);
        dateView = (TextView) itemView.findViewById(R.id.dateView);
        ownerView = (ImageView) itemView.findViewById(R.id.owner);
        // ownerView = (ImageView) itemView.findViewById(R.id.owner);
    }

    @Override
    public void bindData(VKApiMessage currentMessage) {

        LinearLayout.LayoutParams textLayoutParams = (LinearLayout.LayoutParams) textView.getLayoutParams();
        LinearLayout.LayoutParams mapLayoutParams = (LinearLayout.LayoutParams) mapView.getLayoutParams();
        LinearLayout.LayoutParams dateLayoutParams = (LinearLayout.LayoutParams) dateView.getLayoutParams();


        if (!currentMessage.read_state) {
            itemView.setBackgroundColor(getColor(R.color.dialog_unread_background));
        } else {
            itemView.setBackgroundDrawable(null);
        }

        //ownerView.setVisibility(View.GONE);


        textView.setText(currentMessage.body);
        //bodyView = textView;

        if (currentMessage.geo != null) {
            final VKApiGeo geo = currentMessage.geo;
            mapView.setVisibility(View.VISIBLE);
            mapView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(getContext())
                            .setTitle(R.string.map_show_title)
                            .setMessage(R.string.map_show_message)
                            .setPositiveButton(R.string.map_show_ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    getContext().startActivity(
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

        dateView.setText(TimeUtils.format(currentMessage.date*1000, getContext()));
        dateView.setVisibility(View.GONE);

        itemDefaultPadding = Dimen.get(R.dimen.default_message_padding);
        itemLeftPadding = itemDefaultPadding;
        itemTopPadding = itemDefaultPadding;
        itemRightPadding = itemDefaultPadding;
        itemBottomPadding = itemDefaultPadding;
        if (currentMessage.out) {
            currentGravity = Gravity.RIGHT;
            itemLeftPadding = itemDefaultPadding * 4;
        } else {
            currentGravity = Gravity.LEFT;
            itemRightPadding = itemDefaultPadding * 4;
        }
        textLayoutParams.gravity = currentGravity;
        dateLayoutParams.gravity = currentGravity;
        mapLayoutParams.gravity = currentGravity;

        bodyView.setPadding(itemLeftPadding, itemTopPadding, itemRightPadding, itemBottomPadding);


        textView.setLayoutParams(textLayoutParams);
        dateView.setLayoutParams(dateLayoutParams);
        mapView.setLayoutParams(mapLayoutParams);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dateView.getVisibility() == View.VISIBLE) {
                    dateView.setVisibility(View.GONE);
                } else {
                    dateView.setVisibility(View.VISIBLE);
                }
            }
        });
        /*

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


        bodyView.setPadding(itemLeftPadding, itemTopPadding, itemRightPadding, itemBottomPadding);
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


    }

    @Override
    public void groupTop() {
        itemTopPadding = itemDefaultPadding / 4;
        bodyView.setPadding(itemLeftPadding,itemTopPadding,itemRightPadding,itemBottomPadding);
    }

    @Override
    public void ungroupTop() {
    }

    @Override
    public void groupBottom() {
        itemBottomPadding = itemDefaultPadding / 4;
        bodyView.setPadding(itemLeftPadding,itemTopPadding,itemRightPadding,itemBottomPadding);
    }

    @Override
    public void ungroupBottom() {
    }

    @Override
    public void showOwner(VKApiUserFull owner) {
        ownerView.setVisibility(View.VISIBLE);
        ownerView.setImageBitmap(BitmapUtil.circle(R.drawable.user_placeholder));

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

    @Override
    public void hideOwner() {
        ownerView.setVisibility(View.GONE);
    }
}