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
    public Context context;

    public NavigationAdapter (Activity activity ) {context = activity;}



    @Override
    public int getCount() {
        return 7;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        switch (position){
            case 0:
                return NavigationFragment.NAVIGATION_PROFILE_ID;
            case 1:
                return NavigationFragment.NAVIGATION_MESSAGES_ID;

            case 2:
                return NavigationFragment.NAVIGATION_GROUPS_ID;

            case 3:
                return NavigationFragment.NAVIGATION_FRIENDS_ID;

            case 5:
                return NavigationFragment.NAVIGATION_SETTINGS_ID;

            case 6:
                return NavigationFragment.NAVIGATION_ABOUT_ID;

        }
        return -1;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        if(position==4){
            return false;
        }

        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View itemView = null;
        if(position==0){
            itemView = LayoutInflater.from(context).inflate(R.layout.navigation_profile, null);
            return itemView;
        }
        if(position==4){
            return LayoutInflater.from(context).inflate(R.layout.navigation_divider, null);
        }
        itemView = LayoutInflater.from(context).inflate(R.layout.navigation_item, null);

        TextView textView = (TextView) itemView.findViewById(R.id.text);
        switch (position){
            case 1:
                itemView.findViewById(R.id.fake_padding).setVisibility(View.VISIBLE);
                textView.setText(R.string.navigation_messages);
                break;
            case 2:
                textView.setText(R.string.navigation_groups);
                break;
            case 3:
                textView.setText(R.string.navigation_friends);
                break;
            case 5:
                textView.setText(R.string.navigation_settings);
                break;
            case 6:
                textView.setText(R.string.navigation_about);
                break;
        }


        return itemView;
    }
}
