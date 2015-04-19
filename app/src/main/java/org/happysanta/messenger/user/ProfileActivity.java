package org.happysanta.messenger.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.methods.VKApiUsers;
import com.vk.sdk.api.methods.VKApiWall;
import com.vk.sdk.api.model.VKApiPost;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;
import com.vk.sdk.api.model.VKPostArray;

import org.happysanta.messenger.R;
import org.happysanta.messenger.core.BaseActivity;
import org.happysanta.messenger.posts.PostHolder;

public class ProfileActivity extends BaseActivity {
    int userId;
    int postId;

    private String subtitle;
    private static final String EXTRA_USERID = "extra_userid";
    private static final String EXTRA_POSTID = "extra_postid";
    private ProfilePostsAdapter adapter;
    private VKApiUserFull currentUser;
    private VKPostArray userPosts;
    // Остальное все ок? Толко вместо поста брать список постов?

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

        RecyclerView recycler = (RecyclerView) findViewById(R.id.profile_recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProfilePostsAdapter();
        recycler.setAdapter(adapter);

        userId = getIntent().getIntExtra(EXTRA_USERID, 0);
        postId = getIntent().getIntExtra(EXTRA_POSTID, 0);
        // todo show loading?
        // потом мы загружаем юзера
        new VKApiUsers().get(new VKParameters() {{
            put("user_ids", userId);
            put("fields", "photo_200,city,activity,last_seen,counters,bdate,can_write_private_message");
        }}).executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {

                VKList<VKApiUserFull> users = (VKList<VKApiUserFull>) response.parsedModel;
                currentUser = users.get(0);
                adapter.notifyItemInserted(0);

                fetchPosts();
                // давай вот отсюда начинай, только причеши там комментарии, чтобы красивенько было и... и коммит
                // удачки) я уже на странице юзера. перезайди на не\
                // потом этого юзера заносим по вьюшкам
                // лучше все же не забивать все в одном OnCreate потому что сложно будет читать, так получается более мене читаемо

                //showUser(currentUser);
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

    private void fetchPosts() {
        // не, getById  возвращает посты по айдишникам. В посты не грузятся данные
        new VKApiWall().get(new VKParameters() {{
            put(VKApiWall.EXTENDED, 1);
            put("owner_id", userId);
        }}).executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                userPosts = (VKPostArray) response.parsedModel;
                // когда загрузили пост, то говорим адаптеру, что у нас появился первый итем
                adapter.notifyItemRangeInserted(1, userPosts.size());

            }
        });
    }


    public static Intent openProfile(Context context, int userid) {
        return new Intent(context, ProfileActivity.class).putExtra(EXTRA_USERID, userid);
    }

    private class ProfilePostsAdapter extends RecyclerView.Adapter {

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return 0;
            }
            return 1;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // created profile holder
            RecyclerView.ViewHolder holder = null;
            if (viewType == 0) {
                ProfileHolder profileHolder = new ProfileHolder(LayoutInflater.from(getBaseContext()).inflate(R.layout.item_profile, parent, false));
                holder = profileHolder;
            } else {
                // created post holder
                PostHolder postHolder = new PostHolder(LayoutInflater.from(getBaseContext()).inflate(R.layout.item_post, parent, false));
                holder = postHolder;
            }
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof ProfileHolder) {
                ((ProfileHolder) holder).bind(0, currentUser);
            } else if (holder instanceof PostHolder) {
                // и заносятся данные этих коментаривев в холдеры
                VKApiPost post = userPosts.get(position - 1);
                PostHolder postHolder = (PostHolder) holder;
                postHolder.bind((position - 1), post,userPosts.owners.getById(post.from_id));// position-1 потому что на 0 индексе у нас новость, и комментарии в адаптере идут с 1, но в списке комментариев они все равно с 0
            }
        }
        // работает? нету постов

        @Override
        public int getItemCount() {
            int count = currentUser != null ? 1 + (userPosts != null ? userPosts.size() : 0) : 0;
            return count;
        }
    }
}