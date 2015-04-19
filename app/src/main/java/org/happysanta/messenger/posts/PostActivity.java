package org.happysanta.messenger.posts;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.methods.VKApiWall;
import com.vk.sdk.api.model.VKApiModel;
import com.vk.sdk.api.model.VKApiPost;
import com.vk.sdk.api.model.VKCommentArray;
import com.vk.sdk.api.model.VKPostArray;

import org.happysanta.messenger.R;
import org.happysanta.messenger.core.BaseActivity;

/**
 * Created by admin on 16.04.2015.
 */
public class PostActivity extends BaseActivity {
    int ownerId;
    int postId;

    private String subtitle;
    private static final String EXTRA_USERID = "extra_userid";
    private static final String EXTRA_POSTID = "extra_postid";
    private CommentedPostAdapter adapter;
    private VKApiPost currentPost;
    private VKCommentArray postComments;
    private VKApiModel postOwner;


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

        // мы созда\м ресайклер, который будет показывать наши данные
        RecyclerView recycler = (RecyclerView) findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CommentedPostAdapter();
        recycler.setAdapter(adapter);

        ownerId = getIntent().getIntExtra(EXTRA_USERID, 0);
        postId = getIntent().getIntExtra(EXTRA_POSTID, 0);
        Log.d("PostActivity",ownerId + " "+ postId);
        new VKApiWall().getById(new VKParameters(){{
            put(VKApiWall.EXTENDED, 1);
            put("posts", ownerId +"_"+postId);
        }}).executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                VKPostArray postList = (VKPostArray) response.parsedModel;
                currentPost = postList.get(0);
                if(currentPost.from_id>0)
                postOwner = postList.usersOwners.get(0);
                else
                postOwner = postList.groupsOwners.get(0);
                // когда загрузили пост, то говорим адаптеру, что у нас появился первый итем
                adapter.notifyItemInserted(0);

                // когда адаптер отобразил новость, мы загружаем комментарии
                fetchComments();
            }
        });
    }

    private void fetchComments() {
        // настраиваем запрос
        new VKApiWall().getComments(new VKParameters(){{
            put(VKApiWall.EXTENDED, 1);
            put("owner_id", ownerId);
            put("post_id", postId);
            put("need_likes", 1);

        }})
        .executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                postComments = (VKCommentArray) response.parsedModel;
                // сохраняем комментарии
                adapter.notifyItemRangeInserted(1, postComments.size());
                // уведомляем адаптер, что у нас добавились комментарии
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
            }
        });
    }
    // надо сделать страницу юзера по такому же принципу, только первый холдер - юзер, остальные - посты
    public static Intent openPost(Context context,int ownerId, int postid){
        return new Intent(context, PostActivity.class).putExtra(EXTRA_USERID, ownerId).putExtra(EXTRA_POSTID, postid);
    }


    private class CommentedPostAdapter extends RecyclerView.Adapter {

        @Override
        public int getItemViewType(int position) {

            // адаптер говорит, что первый итем - пост
            if(position==0){
                return 0;// в нулевом будет пост
            }
            // адаптер для всех кроме 1 итема говорит, что они - комментарии
            return 1;// остальное комментарии
        }
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            // создает также холдер для поста
            RecyclerView.ViewHolder holder = null;
            if(viewType==0){
                PostHolder postHolder = new PostHolder(LayoutInflater.from(getBaseContext()).inflate(R.layout.item_post, parent, false));
                holder = postHolder;
            }else{
                // создаются холдеры комментариев
                CommentHolder commentHolder = new CommentHolder(LayoutInflater.from(getBaseContext()).inflate(R.layout.item_comment, parent, false));
                holder = commentHolder;
            }
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            // и заносит данные поста в холдер
            if(holder instanceof PostHolder){
                PostHolder postHolder = ((PostHolder) holder);
                postHolder.bind(0, currentPost);
                postHolder.bindOwner(postOwner);

            } else if(holder instanceof CommentHolder){
                // и заносятся данные этих коментаривев в холдеры
                ((CommentHolder)holder).bind(postComments.get(position-1));// position-1 потому что на 0 индексе у нас новость, и комментарии в адаптере идут с 1, но в списке комментариев они все равно с 0
            }
        }

        @Override
        public int getItemCount() {

            int count = currentPost != null ? 1 : 0; // если currentPost != null  то заносим 1, иначе заносим 0;
            // если есть пост, то смотрим есть ли комментарии. если есть, то добавляем и комментарии.

            return
                    currentPost != null ?
                            (1 + (postComments != null ? postComments.size() : 0))
                            : 0;
        }
    }
}
