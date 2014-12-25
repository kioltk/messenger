package org.happysanta.messenger.messages.core;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
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
        View itemView = LayoutInflater.from(activity).inflate(R.layout.item_message, null);

        TextView textView = (TextView) itemView.findViewById(R.id.message_text);
        final TextView dateView = (TextView) itemView.findViewById(R.id.dateView);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        final VKApiMessage message = getItem(position);

        textView.setText(message.body);
        dateView.setText("" + message.date);
        dateView.setVisibility(View.GONE);

        if(message.out){
            layoutParams.gravity = Gravity.LEFT;
        } else {
            layoutParams.gravity = Gravity.RIGHT;
        }
        textView.setLayoutParams(layoutParams);
        dateView.setLayoutParams(layoutParams);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dateView.getVisibility()==View.VISIBLE){
                    dateView.setVisibility(View.GONE);
                }else{
                    dateView.setVisibility(View.VISIBLE);
                }
            }
        });

        return itemView;
    }
}
