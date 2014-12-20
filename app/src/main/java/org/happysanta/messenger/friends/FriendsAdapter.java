package org.happysanta.messenger.friends;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.model.VKApiUser;
import com.vk.sdk.api.model.VKApiUserFull;

import org.happysanta.messenger.R;

import java.util.ArrayList;

/**
 * Created by Jesus Christ. Amen.
 */
public class FriendsAdapter extends BaseAdapter {

    private final ArrayList<VKApiUserFull> friends;
    private final Context context;

    public FriendsAdapter(Context context, ArrayList<VKApiUserFull> friends) {
        this.context = context;
        this.friends = friends;
    }

    @Override
    public int getCount() {
        return friends.size();
    }

    @Override
    public VKApiUserFull getItem(int position) {

        return friends.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View itemView = LayoutInflater.from(context).inflate(R.layout.item_friend, null);

        ImageView photoView = (ImageView) itemView.findViewById(R.id.user_photo);
        TextView nameView = (TextView) itemView.findViewById(R.id.user_name);

        VKApiUserFull user = getItem(position);


        nameView.setText(user.toString());
        ImageLoader.getInstance().displayImage(user.photo_50, photoView);

        return itemView;
    }
}
