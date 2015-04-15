package org.happysanta.messenger.news;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.vk.sdk.api.model.VKApiPhoto;
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
    private View btnComments;
    private View btnMenu;
    private TintImageView repostView;
    private TintImageView likeView;
    private TextView commentsCountView;
    private TextView repostsCountView;
    private TextView likesCountView;
    private View btnShare;
    private View btnLike;

    private View attach;
    private ImageView photoAttachView;


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
        attach = itemView.findViewById(R.id.attach);
        photoAttachView = (ImageView) itemView.findViewById(R.id.photo_attach);

        repostView = (TintImageView) itemView.findViewById(R.id.news_repost);
        likeView = (TintImageView) itemView.findViewById(R.id.news_like);
        commentsCountView = (TextView) itemView.findViewById(R.id.news_comments_count);
        repostsCountView = (TextView) itemView.findViewById(R.id.news_reposts_count);
        likesCountView = (TextView) itemView.findViewById(R.id.news_likes_count);

        btnComments =  itemView.findViewById(R.id.btn_comments);
        btnShare = itemView.findViewById(R.id.btn_share);
        btnLike = itemView.findViewById(R.id.btn_like);

        final VKApiPost post = (VKApiPost) getItem(position);
        if (post.attachments.size() > 0) {
            VKApiPhoto photoAttach = (VKApiPhoto) post.attachments.get(0);

            if(photoAttach instanceof VKApiPhoto){
                ImageLoader.getInstance().displayImage(photoAttach.photo_604, photoAttachView, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        photoAttachView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {

                    }
                });
            }
        }

        photoView.setImageBitmap(BitmapUtil.circle(R.drawable.user_placeholder));
        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.startActivity(ProfileActivity.openProfile(activity, post.from_id));
            }
        });

        nameView.setText("" + post.from_id);

        if(post.text == null){
            textView.setVisibility(View.GONE);
        } else {
            textView.setText(post.text);
        }

        dateView.setText(TimeUtils.format(post.date * 1000, activity));
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });

        btnComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity, "Comments button", Toast.LENGTH_SHORT).show();
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
            likesCountView.setTextColor(activity.getResources().getColor(R.color.post_item_blue));
            likeView.setTint(activity.getResources().getColor(R.color.post_item_blue));
        } else{
            likeView.setTint(activity.getResources().getColor(R.color.post_item_grey));
        }

        if (post.user_reposted){
            repostsCountView.setTextColor(activity.getResources().getColor(R.color.post_item_blue));
            repostView.setTint(activity.getResources().getColor(R.color.post_item_blue));
        } else{
            repostView.setTint(activity.getResources().getColor(R.color.post_item_grey));
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
