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
import org.happysanta.messenger.core.util.BitmapUtil;
import org.happysanta.messenger.core.util.TimeUtils;

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
    public VKApiPost getItem(int position) { return newsList.get(position); }

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

        View commentsView =  itemView.findViewById(R.id.news_comments);
        View repostView   =  itemView.findViewById(R.id.news_repost);
        View likeView     =  itemView.findViewById(R.id.news_like);

        TextView commentsCountView = (TextView) itemView.findViewById(R.id.news_comments_count);
        TextView repostsCountView  = (TextView) itemView.findViewById(R.id.news_reposts_count);
        TextView likesCountView    = (TextView) itemView.findViewById(R.id.news_likes_count);

        final VKApiPost post = (VKApiPost) getItem(position);

        photoView.setImageBitmap(BitmapUtil.circle(R.drawable.user_placeholder));
        nameView.setText("" + post.from_id);
        textView.setText(post.text);
        dateView.setText(TimeUtils.format(post.date*1000, activity));

        commentsCountView.setText("" + post.comments_count);
        repostsCountView.setText("" + post.reposts_count);
        likesCountView.setText("" + post.likes_count);

        return itemView;
    }


}
