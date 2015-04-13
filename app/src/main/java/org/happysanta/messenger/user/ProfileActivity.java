package org.happysanta.messenger.user;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.methods.VKApiUsers;
import com.vk.sdk.api.model.VKApiCity;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;

import org.happysanta.messenger.R;
import org.happysanta.messenger.core.BaseActivity;
import org.happysanta.messenger.core.util.BitmapUtil;
import org.happysanta.messenger.core.util.Dimen;
import org.happysanta.messenger.core.util.ImageUtil;

public class ProfileActivity extends BaseActivity {
    int userId;
    private String subtitle;

    private static final String EXTRA_USERID = "extra_userid";

    // это вьюшки из первой карточки
    private TextView nameView;
    private ImageView photoView;
    private TextView cityView;
    private TextView statusView;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


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


        // потом мы загружаем юзера

        android.support.v7.app.ActionBar actionbar = getSupportActionBar();
        if (null != actionbar) {
            actionbar.setHomeButtonEnabled(true);
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
            final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

            toolbar.setSubtitle(subtitle);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });


        }

        userId = getIntent().getIntExtra(EXTRA_USERID, 0);
        // todo show loading?
        new VKApiUsers().get(new VKParameters(){{
            put("user_ids", userId);
            put("fields","photo_200,city,activity,last_seen,counters,bdate");
        }}).executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {

                VKList<VKApiUserFull> users = (VKList<VKApiUserFull>) response.parsedModel;
                VKApiUserFull currentUser = users.get(0);

                // потом этого юзера заносим по вьюшкам
                // лучше все же не забивать все в одном OnCreate потому что сложно будет читать, так получается более мене читаемо
                showUser(currentUser);
            }
        });
        /*
        pagerView = (ViewPager) findViewById(R.id.pager);
        feedAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return new NewsListFragment();
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
        pagerView.setAdapter(feedAdapter);
        */
    }

    private void showUser(VKApiUserFull user) {
        setUserName(user.toString());
        setUserId(user.id);
        setOnline(user.online);
        setPhoto(user.getPhoto());
        setCounters(user.counters);
        setCity(user.city);
        findViewById(R.id.loading).setVisibility(View.GONE);
        findViewById(R.id.content_holder).setVisibility(View.VISIBLE);
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
                Toast.makeText(ProfileActivity.this, "Friends button", Toast.LENGTH_SHORT).show();
            }
        });

        btnMutual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProfileActivity.this, "Mutual button", Toast.LENGTH_SHORT).show();
            }
        });

        btnOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProfileActivity.this, "Online button", Toast.LENGTH_SHORT).show();
            }
        });

        btnFollowers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProfileActivity.this, "Followers button", Toast.LENGTH_SHORT).show();
            }
        });

        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ProfileActivity.this, "Photos button", Toast.LENGTH_SHORT).show();
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

    public static Intent openProfile(Context context, int userid){
        return new Intent(context, ProfileActivity.class).putExtra(EXTRA_USERID, userid);
    }

}
