package org.happysanta.messenger.main;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.happysanta.messenger.BuildConfig;
import org.happysanta.messenger.R;
import org.happysanta.messenger.core.util.BitmapUtil;
import org.happysanta.messenger.core.util.Dimen;
import org.happysanta.messenger.core.util.ImageUtil;
import org.happysanta.messenger.core.util.ProfileUtil;

/**
 * Created by Jesus Christ. Amen.
 */
public class NavigationAdapter extends BaseAdapter {
    private static final int DIVIDER = -1;
    private static final int DIVIDER_SMALL = -2;
    public Context context;

    public NavigationAdapter (Activity activity ) {context = activity;}



    @Override
    public int getCount() {
        if(BuildConfig.DEBUG){
            return 9;
        }
        return 8;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        if(position==1){
            return DIVIDER_SMALL;
        }
        if(BuildConfig.DEBUG) {
            switch (position) {
                case 0:
                    return NavigationFragment.NAVIGATION_PROFILE_ID;
                case 2:
                    return NavigationFragment.NAVIGATION_NEWS_ID;
                case 3:
                    return NavigationFragment.NAVIGATION_MESSAGES_ID;
                case 4:
                    return NavigationFragment.NAVIGATION_GROUPS_ID;
                case 5:
                    return NavigationFragment.NAVIGATION_FRIENDS_ID;
                case 6:
                    return DIVIDER;
                case 7:
                    return NavigationFragment.NAVIGATION_SETTINGS_ID;
                case 8:
                    return NavigationFragment.NAVIGATION_ABOUT_ID;

            }
        }else{

            switch (position) {
                case 0:
                    return NavigationFragment.NAVIGATION_PROFILE_ID;
                case 2:
                    return NavigationFragment.NAVIGATION_MESSAGES_ID;

                case 3:
                    return NavigationFragment.NAVIGATION_GROUPS_ID;

                case 4:
                    return NavigationFragment.NAVIGATION_FRIENDS_ID;
                case 5:
                    return DIVIDER;
                case 6:
                    return NavigationFragment.NAVIGATION_SETTINGS_ID;
                case 7:
                    return NavigationFragment.NAVIGATION_ABOUT_ID;

            }
        }
        return -1;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return getItemId(position)>0 ;


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View itemView = null;
        itemView = LayoutInflater.from(context).inflate(R.layout.navigation_item, null);

        TextView textView = (TextView) itemView.findViewById(R.id.text);
        switch ((int) getItemId(position)){
            case NavigationFragment.NAVIGATION_PROFILE_ID:
                itemView = LayoutInflater.from(context).inflate(R.layout.navigation_profile, null);

                final ImageView userPhoto = (ImageView) itemView.findViewById(R.id.user_photo);
                TextView userName = (TextView) itemView.findViewById(R.id.user_name);
                TextView userStatus = (TextView) itemView.findViewById(R.id.user_status);
                View searchButton = itemView.findViewById(R.id.search);


                ImageUtil.showFromCache(ProfileUtil.getUserPhoto(), new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        loadedImage = BitmapUtil.circle(loadedImage);
                        userPhoto.setImageBitmap(loadedImage);
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {

                    }
                });
                userName.setText(ProfileUtil.getUserName());
                userStatus.setText(ProfileUtil.getUserStatus());
                int paddingBottom = itemView.getPaddingBottom();
                int paddingTop = Dimen.getStatusBarHeight();
                itemView.setPadding(0,paddingTop, 0,paddingBottom);

                searchButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "Search", Toast.LENGTH_SHORT).show();
                    }
                });

                break;
            case NavigationFragment.NAVIGATION_NEWS_ID:
                textView.setText(R.string.navigation_news);
                break;
            case NavigationFragment.NAVIGATION_MESSAGES_ID:
                textView.setText(R.string.navigation_messages);
                break;
            case NavigationFragment.NAVIGATION_GROUPS_ID:
                textView.setText(R.string.navigation_groups);
                break;
            case NavigationFragment.NAVIGATION_FRIENDS_ID:
                textView.setText(R.string.navigation_friends);
                break;
            case NavigationFragment.NAVIGATION_SETTINGS_ID:
                textView.setText(R.string.navigation_settings);
                break;
            case NavigationFragment.NAVIGATION_ABOUT_ID:
                textView.setText(R.string.navigation_about);
                break;
            case DIVIDER:
                itemView = LayoutInflater.from(context).inflate(R.layout.navigation_divider, null);
                break;
            case DIVIDER_SMALL:
                itemView = LayoutInflater.from(context).inflate(R.layout.navigation_divider_small, null);
                break;
        }


        return itemView;
    }
}
