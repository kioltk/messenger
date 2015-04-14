package org.happysanta.messenger.news;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vk.sdk.api.model.VKApiPost;
import com.vk.sdk.api.model.VKList;

import org.happysanta.messenger.R;
import org.happysanta.messenger.core.util.BitmapUtil;
import org.happysanta.messenger.core.util.TimeUtils;
import org.happysanta.messenger.core.views.TintImageView;
import org.happysanta.messenger.user.ProfileActivity;

/**
 * Created by insidefun on 01.01.2015.
 */
public class NewsAdapter extends BaseAdapter {
    private final Activity activity;
    private VKList<VKApiPost> newsList;

    private ImageView photoView;
    private TextView nameView;
    private TextView textView;
    private View commentsView;
    private View btnMenu;
    private TintImageView repostView;
    private TintImageView likeView;
    private TextView commentsCountView;
    private TextView repostsCountView;
    private TextView likesCountView;
    private View btnShare;
    private View btnLike;

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
    public View getView(final int position, View convertView, ViewGroup parent) {
        View itemView = LayoutInflater.from(activity).inflate(R.layout.item_news, null);

        photoView = (ImageView) itemView.findViewById(R.id.user_photo);
        nameView = (TextView) itemView.findViewById(R.id.user_name);
        final TextView dateView = (TextView) itemView.findViewById(R.id.news_date);
        btnMenu =  itemView.findViewById(R.id.btn_menu);

        textView = (TextView) itemView.findViewById(R.id.news_body);

        commentsView =  itemView.findViewById(R.id.news_comments);
        repostView = (TintImageView) itemView.findViewById(R.id.news_repost);
        likeView = (TintImageView) itemView.findViewById(R.id.news_like);
        commentsCountView = (TextView) itemView.findViewById(R.id.news_comments_count);
        repostsCountView = (TextView) itemView.findViewById(R.id.news_reposts_count);
        likesCountView = (TextView) itemView.findViewById(R.id.news_likes_count);

        btnShare = itemView.findViewById(R.id.btn_share);
        btnLike = itemView.findViewById(R.id.btn_like);

        final VKApiPost post = (VKApiPost) getItem(position);

        photoView.setImageBitmap(BitmapUtil.circle(R.drawable.user_placeholder));
        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.startActivity(ProfileActivity.openProfile(activity, post.from_id));
            }
        });

        nameView.setText("" + post.from_id);
        textView.setText(post.text);
        dateView.setText(TimeUtils.format(post.date*1000, activity));
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity, "Share button", Toast.LENGTH_SHORT).show();
            }
        });
        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity, "Like button", Toast.LENGTH_SHORT).show();
            }
        });

        commentsCountView.setText("" + post.comments_count);
        repostsCountView.setText("" + post.reposts_count);
        likesCountView.setText("" + post.likes_count);

        if (post.user_likes){
            likesCountView.setTextColor(Color.parseColor("#619de2"));
            likeView.setTint(0xff619de2);
        } else{
            likeView.setTint(0xffb5b9bd);
        }

        if (post.user_reposted){
            repostsCountView.setTextColor(Color.parseColor("#619de2"));
            repostView.setTint(0xff619de2);
        } else{
            repostView.setTint(0xffb5b9bd);
        }

        return itemView;
    }

    private void showPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(activity, v);
        popupMenu.inflate(R.menu.menu_news);

        popupMenu
                .setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.action_edit_post:
                                Toast.makeText(activity, "Edit post", Toast.LENGTH_SHORT).show();
                                return true;

                            case R.id.action_delete_post:
                                Toast.makeText(activity, "Delete post", Toast.LENGTH_SHORT).show();
                                return true;

                            default:
                                return false;
                        }
                    }
                });

        popupMenu.show();
    }
}
