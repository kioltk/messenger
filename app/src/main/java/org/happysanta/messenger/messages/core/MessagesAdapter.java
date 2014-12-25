package org.happysanta.messenger.messages.core;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vk.sdk.api.model.VKApiMessage;
import com.vk.sdk.api.model.VKList;

import org.happysanta.messenger.R;

/**
 * Created by Jesus Christ. Amen.
 */
public class MessagesAdapter extends BaseAdapter {
    private final Activity activity;
    private final VKList<VKApiMessage> messages;

    public MessagesAdapter(Activity activity, VKList<VKApiMessage> messages) {
        this.activity = activity;
        this.messages = messages;
    }

    @Override
    public int getCount() {
        return messages.size();
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
        View itemView = LayoutInflater.from(activity).inflate(R.layout.item_message, null);
        View bodyView;
        TextView textView = (TextView) itemView.findViewById(R.id.text);
        ImageView emojiView = (ImageView) itemView.findViewById(R.id.emoji);
        ImageView stickerView = (ImageView) itemView.findViewById(R.id.sticker);
        final TextView dateView = (TextView) itemView.findViewById(R.id.dateView);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
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
                textView.setText("ERROR");
                bodyView = textView;
            }
        }
        dateView.setText("" + message.date);
        dateView.setVisibility(View.GONE);

        if(message.out){
            layoutParams.gravity = Gravity.RIGHT;
        } else {
            layoutParams.gravity = Gravity.LEFT;
        }
        bodyView.setLayoutParams(layoutParams);
        dateView.setLayoutParams(layoutParams);

        bodyView.setOnClickListener(new View.OnClickListener() {
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
}
