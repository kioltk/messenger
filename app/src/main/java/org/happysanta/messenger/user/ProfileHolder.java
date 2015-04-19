package org.happysanta.messenger.user;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.vk.sdk.api.model.VKApiCity;
import com.vk.sdk.api.model.VKApiUserFull;

import org.happysanta.messenger.R;
import org.happysanta.messenger.core.BaseViewHolder;
import org.happysanta.messenger.core.util.BitmapUtil;
import org.happysanta.messenger.core.util.ImageUtil;
import org.happysanta.messenger.messages.ChatActivity;

/**
 * Created by admin on 18.04.2015.
 */
public class ProfileHolder extends BaseViewHolder {
    int userId;
    // это вьюшки из первой карточки
    private TextView nameView;
    private ImageView photoView;
    private TextView cityView;
    private TextView statusView;
    private final View messageButton;

    // вторая карточка
    private View friendCardView;
    private TextView friendsView;
    private TextView mutualFriendsView;
    private TextView onlineFriendsView;
    private TextView followersView;
    private View btnFriends;
    private View btnMutual;
    private View btnOnline;
    private View btnFollowers;
    private TextView photosCountView;
    private View btnPhoto;

    public ProfileHolder(View itemView) {
        super(itemView);
        nameView = (TextView) findViewById(R.id.text_name);
        photoView = (ImageView) findViewById(R.id.user_photo);
        cityView = (TextView) findViewById(R.id.city);
        statusView = (TextView) findViewById(R.id.status);


        friendCardView = findViewById(R.id.content_holder);

        friendsView = (TextView)  findViewById(R.id.friends);
        mutualFriendsView = (TextView)  findViewById(R.id.mutual);
        onlineFriendsView = (TextView)  findViewById(R.id.online_friend);
        followersView = (TextView)  findViewById(R.id.followers);


        btnFriends =  findViewById(R.id.btn_friends);
        btnMutual =  findViewById(R.id.btn_mutual);
        btnOnline =  findViewById(R.id.btn_online);
        btnFollowers =  findViewById(R.id.btn_followers);

        photosCountView = (TextView) findViewById(R.id.photos_counter);
        btnPhoto = findViewById(R.id.btn_photos);

        messageButton = findViewById(R.id.button_message);

    }


    public void bind(final int position, final VKApiUserFull user) {
        setUserName(user.toString());
        setUserId(user.id);
        setOnline(user.online);
        setPhoto(user.getPhoto());
        setCounters(user.counters);
        setCity(user.city);
        findViewById(R.id.loading).setVisibility(View.GONE);
        findViewById(R.id.content_holder).setVisibility(View.VISIBLE);
        if (user.can_write_private_message) {
            messageButton.setVisibility(View.VISIBLE);
            messageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getContext().startActivity(ChatActivity.openChat(getContext(), user));
                }
            });
        } else{
            messageButton.setVisibility(View.GONE);
        }
    }

    private void setCity(VKApiCity city) {
        if(city==null){
            findViewById(R.id.city_holder).setVisibility(View.GONE);
        }else{
            cityView.setText(city.title);
        }
    }

    private void setCounters(final VKApiUserFull.Counters counters) {

        if (counters == null ||
                (counters.friends == 0 &&
                        counters.followers == 0 &&
                        counters.online_friends == 0 &&
                        counters.mutual_friends == 0
                )) {
            friendCardView.setVisibility(View.GONE);
            return;
        }


        if (counters.friends > 0) {
            friendsView.setText(Integer.toString(counters.friends));
        } else {
            btnFriends.setVisibility(View.GONE);
        }

        if (counters.mutual_friends > 0) {
            mutualFriendsView.setText(Integer.toString(counters.mutual_friends));
        } else {
            btnMutual.setVisibility(View.GONE);
        }

        if (counters.online_friends > 0) {
            onlineFriendsView.setText(Integer.toString(counters.online_friends));
        } else {
            btnOnline.setVisibility(View.GONE);
        }

        if (counters.followers > 0) {
            followersView.setText(Integer.toString(counters.followers));
        } else {
            btnFollowers.setVisibility(View.GONE);
        }

        if (counters.photos > 1){
            photosCountView.setText(counters.photos + " photos");
        } else if (counters.photos > 0){
            photosCountView.setText(counters.photos + " photo");
        } else {
            btnPhoto.setVisibility(View.GONE);
        }

        btnFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Friends button", Toast.LENGTH_SHORT).show();
            }
        });

        btnMutual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Mutual button", Toast.LENGTH_SHORT).show();
            }
        });

        btnOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Online button", Toast.LENGTH_SHORT).show();
            }
        });

        btnFollowers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Followers button", Toast.LENGTH_SHORT).show();
            }
        });

        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Photos button", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setPhoto(String photo) {
        photoView.setImageBitmap(BitmapUtil.circle(R.id.user_photo));
        ImageUtil.showFromCache(photo, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                photoView.setImageBitmap(BitmapUtil.circle(loadedImage));
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
    }

    public void setUserName(String userName) {
        nameView.setText(userName);
    }


    public void setUserId(int id) {
        this.userId = id;
    }

    public void setOnline(boolean online) {
        String onlineText;
        if(online){
            onlineText = "online";
        }else{
            onlineText = "offline";
        }
        statusView.setText(onlineText);
    }
}
