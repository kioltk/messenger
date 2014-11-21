package org.happysanta.messenger.main;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.happysanta.messenger.R;

/**
 * Created by Jesus Christ. Amen.
 */
public class NavigationAdapter extends BaseAdapter {
    private Context context;

    public NavigationAdapter (Activity activity ) {context = activity;}



    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View itemView = null;
        if(position==0){
            itemView = LayoutInflater.from(context).inflate(R.layout.navigation_profile, null);
            return itemView;
        }
        itemView = LayoutInflater.from(context).inflate(R.layout.navigation_item, null);

        TextView textView = (TextView) itemView.findViewById(R.id.text);
        switch (position){
            case 1:
                textView.setText(R.string.navigation_messages);
                break;
            case 2:
                textView.setText(R.string.navigation_groups);
                break;
            case 3:
                textView.setText(R.string.navigation_friends);
                break;
            case 4:
                textView.setText(R.string.navigation_settings);
                break;
            case 5:
                textView.setText(R.string.navigation_about);
                break;
        }


        return itemView;
    }
}
