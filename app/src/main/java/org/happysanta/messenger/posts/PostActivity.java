package org.happysanta.messenger.posts;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.methods.VKApiUsers;
import com.vk.sdk.api.methods.VKApiWall;
import com.vk.sdk.api.model.VKApiPost;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;

import org.happysanta.messenger.R;
import org.happysanta.messenger.core.BaseActivity;

/**
 * Created by admin on 16.04.2015.
 */
public class PostActivity extends BaseActivity {
    int userId;
    int postId;

    private String subtitle;
    private static final String EXTRA_USERID = "extra_userid";
    private static final String EXTRA_POSTID = "extra_postid";


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

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
        postId = getIntent().getIntExtra(EXTRA_POSTID, 0);
        new VKApiWall().getById(new VKParameters(){{
            put(VKApiWall.EXTENDED, 1);
            put("posts", userId+"_"+postId);
        }}).executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                VKList<VKApiPost> postList = (VKList<VKApiPost>) response.parsedModel;

            }
        });
    }

    public static Intent openPost(Context context,int userid, int postid){
        return new Intent(context, PostActivity.class).putExtra(EXTRA_USERID, userid).putExtra(EXTRA_POSTID, postid);
    }

}
