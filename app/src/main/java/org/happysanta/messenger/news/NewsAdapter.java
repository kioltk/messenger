package org.happysanta.messenger.news;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vk.sdk.api.model.VKApiPost;
import com.vk.sdk.api.model.VKList;

import org.happysanta.messenger.R;

/**
 * Created by insidefun on 01.01.2015.
 */
public class NewsAdapter extends BaseAdapter {
    private final Activity activity;
    private VKList<VKApiPost> newsList;

    public NewsAdapter(Activity activity, VKList<VKApiPost> newsList) {
        this.activity = activity;
        this.newsList = newsList;
    }


    @Override
    public int getCount() {
        return newsList.size();
    }

    @Override
    public Object getItem(int position) {
        return newsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View itemView = LayoutInflater.from(activity).inflate(R.layout.item_news, null);
        CardView cardView = (CardView) itemView.findViewById(R.id.card_view);
        ImageView photoView = (ImageView) itemView.findViewById(R.id.user_photo);
        ImageView menuView = (ImageView) itemView.findViewById(R.id.news_menu);
        TextView nameView = (TextView) itemView.findViewById(R.id.user_name);
        TextView textView = (TextView) itemView.findViewById(R.id.news_body);
        final TextView dateView = (TextView) itemView.findViewById(R.id.news_date);

        final VKApiPost post = (VKApiPost) getItem(position);

        nameView.setText(""+post.from_id);
        textView.setText(post.text);
        dateView.setText("" + post.date);

        return itemView;
    }


}
