package org.happysanta.messenger.posts.manage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.happysanta.messenger.R;
import org.happysanta.messenger.core.BaseActivity;
import org.happysanta.messenger.friends.views.SlidingTabLayout;
import org.happysanta.messenger.posts.PostsListFragment;

/**
 * Created by admin on 26.04.2015.
 */
public class ManageActivity extends BaseActivity {
    private static final String EXTRA_USER_ID = "extra_user_id";
    private static final String ARG_LIST_TYPE = "list_type";
    private static final int LIST_TYPE_FEED = 0;
    private static final int LIST_TYPE_USERS = 1;
    private static final int LIST_TYPE_GROUPS = 2;

    private SlidingTabLayout tabsView;
    private ViewPager viewPager;
    private PagerAdapter fragmentsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);


        tabsView = (SlidingTabLayout) findViewById(R.id.tabs);

        viewPager = (ViewPager) findViewById(R.id.pager);
        fragmentsAdapter = new ManageFragmentsAdapter(getSupportFragmentManager());
        viewPager.setAdapter(fragmentsAdapter);

        tabsView.setViewPager(viewPager);
        tabsView.setDividerColors(Color.TRANSPARENT);
        tabsView.setSelectedIndicatorColors(Color.WHITE);

        android.support.v7.app.ActionBar actionbar = getSupportActionBar();
        if (null != actionbar) {
            actionbar.setHomeButtonEnabled(true);
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
            final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

    }

    public static Intent openManage(Context context, int userid) {
        return new Intent(context, ManageActivity.class).putExtra(EXTRA_USER_ID, userid);
    }

    public static Fragment getFeedInstance() {
        FeedManageFragment fragment = new FeedManageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_LIST_TYPE, LIST_TYPE_FEED);
        fragment.setArguments(args);
        return fragment;
    }

    public static Fragment getUsersInstance() {
        FeedUsersFilterFragment fragment = new FeedUsersFilterFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_LIST_TYPE, LIST_TYPE_USERS);
        fragment.setArguments(args);
        return fragment;
    }

    public static Fragment getGroupInstance() {
        FeedGroupsFilterFragment fragment = new FeedGroupsFilterFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_LIST_TYPE, LIST_TYPE_GROUPS);
        fragment.setArguments(args);
        return fragment;
    }


}
