package org.happysanta.messenger.posts;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.happysanta.messenger.R;
import org.happysanta.messenger.core.BaseFragment;
import org.happysanta.messenger.friends.views.SlidingTabLayout;
import org.happysanta.messenger.posts.manage.ManageActivity;

/**
 * Created by Jesus Christ. Amen.
 */
public class FeedFragment extends BaseFragment {
    int userId;
    ViewPager viewPager;
    private PagerAdapter feedAdapter;
    private SlidingTabLayout tabsView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_feed, null);
        tabsView    = (SlidingTabLayout) rootView.findViewById(R.id.friends_tabs);

        viewPager = (ViewPager) rootView.findViewById(R.id.pager);
        feedAdapter = new FeedPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(feedAdapter);

        tabsView.setViewPager(viewPager);
        tabsView.setDividerColors(Color.TRANSPARENT);
        tabsView.setSelectedIndicatorColors(Color.WHITE);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_feed, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit_feed: {
                startActivity(ManageActivity.openManage(activity, userId));
            }
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //toolbarShadow.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        //toolbarShadow.setVisibility(View.VISIBLE);
    }
}
