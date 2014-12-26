package org.happysanta.messenger.messages.core;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.vk.sdk.api.model.VKApiGeo;
import com.vk.sdk.api.model.VKApiMessage;
import com.vk.sdk.api.model.VKList;

import org.happysanta.messenger.R;
import org.happysanta.messenger.core.util.MapUtil;
import org.happysanta.messenger.longpoll.updates.LongpollNewMessage;

import java.util.ArrayList;

/**
 * Created by Jesus Christ. Amen.
 */
public class MessagesAdapter extends BaseAdapter {
    private final Activity activity;
    private final VKList<VKApiMessage> messages;
    private View typingView;

    public MessagesAdapter(Activity activity, VKList<VKApiMessage> messages) {
        this.activity = activity;
        this.messages = messages;
        typingView = LayoutInflater.from(activity).inflate(R.layout.item_typing, null);
    }

    @Override
    public int getCount() {
        return messages.size()+1;
    }

    @Override
    public VKApiMessage getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(position==messages.size()){
            return typingView;
        }



        View itemView = LayoutInflater.from(activity).inflate(R.layout.item_message, null);
        View bodyView;
        TextView textView = (TextView) itemView.findViewById(R.id.text);
        ImageView emojiView = (ImageView) itemView.findViewById(R.id.emoji);
        ImageView stickerView = (ImageView) itemView.findViewById(R.id.sticker);
        final ImageView mapView = (ImageView) itemView.findViewById(R.id.map);
        final TextView dateView = (TextView) itemView.findViewById(R.id.dateView);

        LinearLayout.LayoutParams mapLayoutParams = (LinearLayout.LayoutParams) mapView.getLayoutParams();
        LinearLayout.LayoutParams dateLayoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        final VKApiMessage message = getItem(position);

        if(message.body!=null&&!message.body.equals("")) {
            if(message.emoji&&message.body.length()==2){
                textView.setVisibility(View.GONE);
                emojiView.setVisibility(View.VISIBLE);
                bodyView = emojiView;
                // todo set emoji?
            }else {
                textView.setText(message.body);
                bodyView = textView;
            }
        }else{
            if(message.sticker!=null){
                stickerView.setVisibility(View.VISIBLE);
                textView.setVisibility(View.GONE);
                bodyView = stickerView;
                // todo sticker
            } else {
                if(message.geo!=null){
                    textView.setVisibility(View.GONE);
                    bodyView = mapView;
                }else {
                    textView.setText("ERROR");
                    bodyView = textView;
                }
            }
        }
        if(message.geo!=null){
            mapView.setVisibility(View.VISIBLE);
            VKApiGeo geo = message.geo;
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
        dateView.setText("" + message.date);
        dateView.setVisibility(View.GONE);


        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) bodyView.getLayoutParams();
        if(message.out){
            layoutParams.gravity = Gravity.RIGHT;
            dateLayoutParams.gravity = Gravity.RIGHT;
            mapLayoutParams.gravity = Gravity.RIGHT;
        } else {
            layoutParams.gravity = Gravity.LEFT;
            dateLayoutParams.gravity = Gravity.LEFT;
            mapLayoutParams.gravity = Gravity.LEFT;
        }
        bodyView.setLayoutParams(layoutParams);
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
        });

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
        },4000);
    }
}
