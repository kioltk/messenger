package org.happysanta.messenger.messages.conversations;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.methods.VKApiUsers;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;

import org.happysanta.messenger.R;
import org.happysanta.messenger.core.BaseActivity;
import org.happysanta.messenger.core.util.BitmapUtil;
import org.happysanta.messenger.core.util.ImageUtil;

/**
 * Created by admin on 13.04.2015.
 */
public class GroupInfoActivity extends BaseActivity {
    int userId;
    int chatParticipants;
    private String subtitle;
    private static final String EXTRA_DIALOG_ID = "extra_chat_participants";

    private ImageView dialogPhotoView;
    private TextView dialogTitleView;
    private TextView dialogMembersView;


    private TextView nameView;
    private ImageView photoView;
    private TextView statusView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);

        dialogPhotoView = (ImageView) findViewById(R.id.dialog_photo);
        dialogTitleView = (TextView) findViewById(R.id.dialog_title);
        dialogMembersView = (TextView) findViewById(R.id.participant_counter);


        nameView = (TextView) findViewById(R.id.text_name);
        photoView = (ImageView) findViewById(R.id.user_photo);
        statusView = (TextView) findViewById(R.id.status);

        dialogPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(GroupInfoActivity.this, "Edit photo", Toast.LENGTH_SHORT).show();
            }
        });

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

        chatParticipants = getIntent().getIntExtra(EXTRA_DIALOG_ID, 0);

        new VKApiUsers().get(new VKParameters(){{
            put("chat_participants", chatParticipants);
            put("fields","photo_200,activity,last_seen,counters,bdate");
        }}).executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                VKList<VKApiUserFull> participants = (VKList<VKApiUserFull>) response.parsedModel;
                VKApiUserFull chatParticipants = participants.get(0);

                showParticipant(chatParticipants);
            }
        });
    }

    private void showParticipant(VKApiUserFull participant){
        setUserId(participant.id);
        setUserName(participant.toString());
        setPhoto(participant.getPhoto());
        setOnline(participant.online);
    }

    public void setUserId(int id) {
        this.userId = id;
    }

    public void setUserName(String userName) {
        nameView.setText(userName);
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

    public void setOnline(boolean online) {
        String onlineText;
        if(online){
            onlineText = "online";
        }else{
            onlineText = "offline";
        }
        statusView.setText(onlineText);
    }

    public static Intent openGroupInfo(Context context, int chatId){
        return new Intent(context, GroupInfoActivity.class).putExtra(EXTRA_DIALOG_ID, chatId);
    }
}
