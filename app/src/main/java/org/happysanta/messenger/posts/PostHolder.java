package org.happysanta.messenger.posts;

import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.vk.sdk.api.model.VKApiPhoto;
import com.vk.sdk.api.model.VKApiPost;
import com.vk.sdk.api.model.VKApiPost.VKApiPostSourceData;
import com.vk.sdk.api.model.VKApiPost.VKApiPostSourcePlatform;
import com.vk.sdk.api.model.VKApiUserFull;

import org.happysanta.messenger.R;
import org.happysanta.messenger.core.BaseViewHolder;
import org.happysanta.messenger.core.util.BitmapUtil;
import org.happysanta.messenger.core.util.TimeUtils;
import org.happysanta.messenger.core.views.TintImageView;
import org.happysanta.messenger.user.ProfileActivity;

/**
 * Created by Jesus Christ. Amen.
 */
public class PostHolder extends BaseViewHolder {
    private CardView postCardView;
    private ImageView photoView;
    private TextView nameView;
    private ImageView platformIcoView;
    private TextView textView;
    private TextView dateView;
    private View commentsButton;
    private View commentMenu;
    private TintImageView repostView;
    private TintImageView likeView;
    private TextView commentsCountView;
    private TextView repostsCountView;
    private TextView likesCountView;
    private View shareButton;
    private View likeButton;
    private TextView sourceDataView;

    private View attach;
    private ImageView photoAttachView;

    public PostHolder(View itemView) {
        super(itemView);
        postCardView = (CardView) itemView.findViewById(R.id.card_view);

        photoView = (ImageView) itemView.findViewById(R.id.user_photo);
        nameView = (TextView) itemView.findViewById(R.id.user_name);
        dateView = (TextView) itemView.findViewById(R.id.news_date);
        commentMenu = itemView.findViewById(R.id.btn_menu);
        platformIcoView = (ImageView) itemView.findViewById(R.id.android_ico);
        commentMenu =  itemView.findViewById(R.id.btn_menu);

        textView = (TextView) itemView.findViewById(R.id.news_body);
        sourceDataView = (TextView) itemView.findViewById(R.id.sourceData);
        attach = itemView.findViewById(R.id.attach);
        photoAttachView = (ImageView) itemView.findViewById(R.id.photo_attach);


        repostView = (TintImageView) itemView.findViewById(R.id.news_repost);
        likeView = (TintImageView) itemView.findViewById(R.id.news_like);
        commentsCountView = (TextView) itemView.findViewById(R.id.news_comments_count);
        repostsCountView = (TextView) itemView.findViewById(R.id.news_reposts_count);
        likesCountView = (TextView) itemView.findViewById(R.id.news_likes_count);

        commentsButton = itemView.findViewById(R.id.btn_comments);
        shareButton = itemView.findViewById(R.id.btn_share);
        likeButton = itemView.findViewById(R.id.btn_like);
    }

    public void bind(final int position, final VKApiPost post, final VKApiUserFull owner) {

        // сначала заполняем всю информацию о пользователе
        photoView.setImageBitmap(BitmapUtil.circle(R.drawable.user_placeholder));
        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContext().startActivity(ProfileActivity.openProfile(getContext(), owner.id));
            }
        });
        ImageLoader.getInstance().loadImage(owner.getPhoto(), new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if(loadedImage!=null)
                    photoView.setImageBitmap(BitmapUtil.circle(loadedImage));
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
        nameView.setText(owner.toString());

        // потом платформу
        if (post.sourcePlatform !=null) {
            if (post.sourcePlatform.equals(VKApiPostSourcePlatform.ANDROID)) {
                platformIcoView.setVisibility(View.VISIBLE);
            } else {
                platformIcoView.setVisibility(View.GONE);
            }
        }

        // заполняем дату
        dateView.setText(TimeUtils.format(post.date * 1000, getContext()));


        // кнопочки
        postCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContext().startActivity(PostActivity.openPost(getContext(), post.from_id, post.id));
            }
        });

        commentMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });

        commentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Comments button", Toast.LENGTH_SHORT).show();
                getContext().startActivity(PostActivity.openPost(getContext(), post.from_id, post.id));
            }
        });
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Share button", Toast.LENGTH_SHORT).show();
            }
        });
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Like button", Toast.LENGTH_SHORT).show();
            }
        });

        commentsCountView.setText("" + post.comments_count);
        repostsCountView.setText("" + post.reposts_count);
        likesCountView.setText("" + post.likes_count);

        if (post.user_likes) {
            likesCountView.setTextColor(getContext().getResources().getColor(R.color.post_item_blue));
            likeView.setTint(getContext().getResources().getColor(R.color.post_item_blue));
        } else {
            likeView.setTint(getContext().getResources().getColor(R.color.post_item_grey));
        }

        if (post.user_reposted) {
            repostsCountView.setTextColor(getContext().getResources().getColor(R.color.post_item_blue));
            repostView.setTint(getContext().getResources().getColor(R.color.post_item_blue));
        } else {
            repostView.setTint(getContext().getResources().getColor(R.color.post_item_grey));
        }



        // ТОЛЬКО ПОТОМ ДАННЫЕ



        // если есть источник, значиит проверяем какой
        if (post.sourceData != null) {
            // если источник - изменение фотки, то мы
            if (post.sourceData.equals(VKApiPostSourceData.PROFILE_PHOTO_CHANGE)) {
                sourceDataView.setText("user updated profile picture:");
                sourceDataView.setVisibility(View.VISIBLE);
                VKApiPhoto photoAttach = (VKApiPhoto) post.attachments.get(0);
                if (photoAttach instanceof VKApiPhoto) {
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
            } // ну или проверяем другие источники
        }

        if (post.text.isEmpty()) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.VISIBLE);
            textView.setText(post.text);
        }

    }
    private void showPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(getContext(), v);
        popupMenu.inflate(R.menu.menu_news);

        popupMenu
                .setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.action_edit_post:
                                Toast.makeText(getContext(), "Edit post", Toast.LENGTH_SHORT).show();
                                return true;

                            case R.id.action_delete_post:
                                Toast.makeText(getContext(), "Delete post", Toast.LENGTH_SHORT).show();
                                return true;

                            default:
                                return false;
                        }
                    }
                });

        popupMenu.show();
    }

    public void bindOwner(VKApiUserFull owner) {

    }
}
