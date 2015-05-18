package org.happysanta.messenger.posts;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.text.Spannable;
import android.text.util.Linkify;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.methods.VKApiWall;
import com.vk.sdk.api.model.VKApiAudio;
import com.vk.sdk.api.model.VKApiCommunity;
import com.vk.sdk.api.model.VKApiDocument;
import com.vk.sdk.api.model.VKApiGeo;
import com.vk.sdk.api.model.VKApiModel;
import com.vk.sdk.api.model.VKApiPhoto;
import com.vk.sdk.api.model.VKApiPost;
import com.vk.sdk.api.model.VKApiPost.VKApiPostSourceData;
import com.vk.sdk.api.model.VKApiPost.VKApiPostSourcePlatform;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKApiVideo;
import com.vk.sdk.api.model.VKAttachments;
import com.vk.sdk.api.model.VKList;

import org.happysanta.messenger.R;
import org.happysanta.messenger.core.BaseViewHolder;
import org.happysanta.messenger.core.util.BitmapUtil;
import org.happysanta.messenger.core.util.MapUtil;
import org.happysanta.messenger.core.util.TimeUtils;
import org.happysanta.messenger.core.views.TintImageView;
import org.happysanta.messenger.user.ProfileActivity;

/**
 * Created by Jesus Christ. Amen.
 */
public class PostHolder extends BaseViewHolder {
    private static final String TAG = "PostHolder";
    private final TintImageView pinIcoView;
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

    //Attaches
    private ImageView photoAttachView;
    private final View mapAttachView;
    private final ImageView mapView;
    private final TextView mapTitleView;

    //Post menu
    int ownerId;
    int postId;
    private boolean canPin;
    private boolean isPinned;


    public PostHolder(View itemView) {
        super(itemView);
        postCardView = (CardView) itemView.findViewById(R.id.card_view);

        photoView = (ImageView) itemView.findViewById(R.id.user_photo);
        nameView = (TextView) itemView.findViewById(R.id.user_name);
        dateView = (TextView) itemView.findViewById(R.id.news_date);
        commentMenu = itemView.findViewById(R.id.btn_menu);
        platformIcoView = (ImageView) itemView.findViewById(R.id.platform_ico);
        pinIcoView = (TintImageView) itemView.findViewById(R.id.pin);

        //Post attaches
        textView = (TextView) itemView.findViewById(R.id.news_body);
        sourceDataView = (TextView) itemView.findViewById(R.id.sourceData);
        photoAttachView = (ImageView) itemView.findViewById(R.id.photo_attach);
        //Post attaches | Map
        mapAttachView = itemView.findViewById(R.id.attach_map);
        mapView = (ImageView) itemView.findViewById(R.id.map);
        mapTitleView = (TextView) itemView.findViewById(R.id.map_title);

        //Counts and views
        repostView = (TintImageView) itemView.findViewById(R.id.news_repost);
        likeView = (TintImageView) itemView.findViewById(R.id.news_like);
        commentsCountView = (TextView) itemView.findViewById(R.id.news_comments_count);
        repostsCountView = (TextView) itemView.findViewById(R.id.news_reposts_count);
        likesCountView = (TextView) itemView.findViewById(R.id.news_likes_count);

        //Buttons
        commentsButton = itemView.findViewById(R.id.btn_comments);
        shareButton = itemView.findViewById(R.id.btn_share);
        likeButton = itemView.findViewById(R.id.btn_like);
    }

    public void bind(final int position, final VKApiPost post) {
        //Actions
        postId = post.getId();
        ownerId = post.to_id;
        canPin = post.can_pin;
        isPinned = post.is_pinned;

        // потом платформу
        if (post.sourcePlatform != null) {

            platformIcoView.setVisibility(View.VISIBLE);
            switch (post.sourcePlatform) {
                case VKApiPostSourcePlatform.ANDROID:
                    platformIcoView.setImageResource(R.drawable.ico_android);
                    break;
                case VKApiPostSourcePlatform.IPAD:
                case VKApiPostSourcePlatform.IPHONE:
                    platformIcoView.setImageResource(R.drawable.ico_ios);
                    break;
                case VKApiPostSourcePlatform.WIN:
                    platformIcoView.setImageResource(R.drawable.ico_win);
                    break;
                case VKApiPostSourcePlatform.MOBILE:
                    platformIcoView.setImageResource(R.drawable.ico_mobile);
                    break;
            }

        }else {
            platformIcoView.setVisibility(View.GONE);
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
            }
        }

        //Post pinned status
        if (isPinned){
            pinIcoView.setVisibility(View.VISIBLE);
            Log.v(TAG, "Pin status: " + isPinned);
        } else {
            pinIcoView.setVisibility(View.GONE);
        }

        if (canPin) {
            Log.v(TAG, "User can pin? " + canPin);
        }

        // когда я прошу сделать из одного массива, в котором данные не рассортированы по типу, сделать несколько массивов с конкретными данными,
        // это значет, что нужно сделать несколько массивов, и запихнуть из одно в несколько
        VKList<VKApiPhoto> photos = new VKList<>();
        VKList<VKApiAudio> audios = new VKList<>();
        VKList<VKApiVideo> videos = new VKList<>();
        VKList<VKApiDocument> docs = new VKList<>();
        Log.d(TAG, "PostId :" + post.id);

        VKAttachments attachments = post.attachments;
        for (VKAttachments.VKApiAttachment attachment : attachments) {
            Log.d(TAG, "Attach :" + attachment.toAttachmentString());
            switch (attachment.getType()) {
                case VKAttachments.TYPE_PHOTO:
                    photos.add((VKApiPhoto) attachment);
                    break;
                case VKAttachments.TYPE_AUDIO:
                    audios.add((VKApiAudio) attachment);
                    break;
                case VKAttachments.TYPE_VIDEO:
                    videos.add((VKApiVideo) attachment);
                    break;
                case VKAttachments.TYPE_DOC:
                    docs.add((VKApiDocument) attachment);
                    break;
            }
        }

        // расскидали по массивам

        for (VKApiPhoto photo : photos) {
            // ну вот, утебя есть массив фоточек, придумай как их запихнуть в холдер
            // каждую фотку запихнуть в вьюшку
        }

        for (VKApiAudio audio : audios) {
            // каждую аудио запихнуть в вьюшку
        }

        for (VKApiVideo video : videos) {
            // каждую видюшку
        }

        for (VKApiDocument doc : docs) {
            // каждый док
        }

        if (post.text.isEmpty()) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.VISIBLE);
            Spannable formattedText = Spannable.Factory.getInstance().newSpannable(post.text);
            Linkify.addLinks(formattedText, Linkify.ALL);
            textView.setText(formattedText);
        }

        //Map
        if(post.geo != null){
            Log.v(TAG, "Map :" + post.geo);
            mapAttachView.setVisibility(View.VISIBLE);

            final VKApiGeo geo = post.geo;
            mapView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(getContext())
                            .setTitle(R.string.map_show_title)
                            .setMessage(R.string.map_show_message)
                            .setPositiveButton(R.string.map_show_ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    getContext().startActivity(
                                            new Intent(Intent.ACTION_VIEW, Uri.parse("geo:" + geo.lat + "," + geo.lon
                                                    + "?q=" + geo.lat + "," + geo.lon + "&z=" + 10)));
                                }
                            })
                            .setNegativeButton(R.string.map_show_cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setCancelable(true).show();
                }
            });
            mapTitleView.setText(geo.place.title);

            ImageLoader.getInstance().displayImage(MapUtil.getMap(geo.lat, geo.lon, 600, 400, true, "").toString(), mapView, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {

                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    mapView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {

                }
            });
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

                            case R.id.action_pin_post:
                                    pinPost();
                                    Toast.makeText(getContext(), "Post was pinned", Toast.LENGTH_SHORT).show();
                                    return true;

                            case R.id.action_unpin_post:
                                if (pinIcoView.getVisibility() == View.VISIBLE) {
                                    unpinPost();
                                    Toast.makeText(getContext(), "Post was unpinned", Toast.LENGTH_SHORT).show();
                                    return true;
                                } else {
                                    return false;
                                }

                            default:
                                return false;
                        }
                    }
                });

        popupMenu.show();
    }


    private void pinPost() {
        new VKApiWall().pin(new VKParameters() {{
            put("owner_id", ownerId);
            put("post_id", postId);
        }}).executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                    pinIcoView.setVisibility(View.VISIBLE);
            }
        });
    }
    private void unpinPost() {
        new VKApiWall().unPin(new VKParameters() {{
            put("owner_id", ownerId);
            put("post_id", postId);
        }}).executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                    pinIcoView.setVisibility(View.GONE);

            }
        });
    }

    public void bindOwner(final VKApiUserFull owner) {
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
    }
    public void bindOwner(final VKApiCommunity community) {
        // сначала заполняем всю информацию о пользователе
        photoView.setImageBitmap(BitmapUtil.circle(R.drawable.user_placeholder));
        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContext().startActivity(ProfileActivity.openProfile(getContext(), community.id));
            }
        });
        ImageLoader.getInstance().loadImage(community.getPhoto(), new ImageLoadingListener() {
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
        nameView.setText(community.toString());
    }

    public void bindOwner(VKApiModel owner) {
        if (owner instanceof VKApiUserFull) {
            bindOwner((VKApiUserFull) owner);
        } else {
            if (owner instanceof VKApiCommunity)
                bindOwner((VKApiCommunity) owner);
        }
    }
}
