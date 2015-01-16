package org.happysanta.messenger.messages;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.model.VKApiDialog;

import org.happysanta.messenger.R;
import org.happysanta.messenger.core.BaseActivity;
import org.happysanta.messenger.core.util.BitmapUtil;
import org.happysanta.messenger.core.util.Dimen;
import org.happysanta.messenger.messages.conversations.ConversationFragment;

public class ChatActivity extends BaseActivity {

    public static final String ARG_DIALOGID = "arg_dialogid";
    public static final String ARG_ISCHAT = "arg_ischat";
    public static final String ARG_TITLE = "arg_title";
    public static final String ARG_LOGO = "arg_logo";
    public static final String ARG_SUBTITLE = "arg_subtitle";
    public static final String ARG_CHAT_PARTICIPANTS = "arg_chat_participants";

    private ConversationFragment conversationFragment;
    private String title;
    private String subtitle;
    private String logo;
    private boolean isChat;
    private int dialogId;

    private EditText editMessageText;
    private Button sendButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VKUIHelper.onCreate(this);
        setContentView(R.layout.activity_chat);


        Bundle bundle = getIntent().getExtras();
        title = bundle.getString(ARG_TITLE, "Dialog");
        subtitle = bundle.getString(ARG_SUBTITLE, null);
        logo = bundle.getString(ARG_LOGO, null);
        dialogId = bundle.getInt(ChatActivity.ARG_DIALOGID, 0);
        isChat = bundle.getBoolean(ChatActivity.ARG_ISCHAT, false);

        setTitle(title);


        android.support.v7.app.ActionBar actionbar = getSupportActionBar();
        if (null != actionbar) {
            actionbar.setHomeButtonEnabled(true);
            actionbar.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
            final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

            toolbar.setLogo(R.drawable.ab_logo_placeholder);
            ImageLoader.getInstance().loadImage(logo, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {

                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap logoBitmap) {

                    int size = Dimen.get(R.dimen.ab_logo_size);
                    logoBitmap = BitmapUtil.resize(logoBitmap, size, size);
                    logoBitmap = BitmapUtil.circle(logoBitmap);
                    Drawable logoDrawable = new BitmapDrawable(getResources(), logoBitmap);
                    toolbar.setLogo(logoDrawable);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {

                }
            });
            toolbar.setSubtitle(subtitle);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }


        if (savedInstanceState == null) {
            conversationFragment = ConversationFragment.getInstance(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, conversationFragment)
                    .commit();
        }

    }

//    @Override
//    public void onBackPressed() {
//        if (conversationFragment.onBackPressed())
//            super.onBackPressed();
//    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public static Intent getActivityIntent(Context context, VKApiDialog dialog) {
        Intent intent = new Intent(context, ChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_DIALOGID, dialog.getId());
        bundle.putBoolean(ARG_ISCHAT, dialog.isChat());
        bundle.putString(ARG_TITLE, dialog.getTitle());
        bundle.putString(ARG_SUBTITLE, dialog.getSubtitle());
        bundle.putString(ARG_LOGO, dialog.photo_200);
        if(dialog.isChat())
            bundle.putSparseParcelableArray(ARG_CHAT_PARTICIPANTS, dialog.getParticipants());
        intent.putExtras(bundle);
        return intent;
    }


}