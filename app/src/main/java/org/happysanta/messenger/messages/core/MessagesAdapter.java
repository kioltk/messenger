package org.happysanta.messenger.messages.core;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vk.sdk.api.model.VKApiMessage;

import org.happysanta.messenger.R;

import java.util.ArrayList;

/**
 * Created by Jesus Christ. Amen.
 */
public class MessagesAdapter extends BaseAdapter {
    private final Activity activity;
    private final ArrayList<VKApiMessage> messages;

    public MessagesAdapter(Activity activity, ArrayList<VKApiMessage> messages) {
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
        RelativeLayout itemView = (RelativeLayout) LayoutInflater.from(activity).inflate(R.layout.item_message, null);
        TextView textView = (TextView) itemView.findViewById(R.id.message_text);

        VKApiMessage message = getItem(position);

        textView.setText(message.body);

        if(message.out){
            itemView.setGravity(Gravity.RIGHT);
        } else {
            itemView.setGravity(Gravity.LEFT);
        }

        return itemView;
    }
}
