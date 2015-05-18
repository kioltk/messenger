package org.happysanta.messenger.friends;


import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.happysanta.messenger.R;
import org.happysanta.messenger.core.BaseFragment;
import org.happysanta.messenger.friends.views.SlidingTabLayout;

/**
 * Created by alex on 09/01/15.
 */
public class FriendsFragment extends BaseFragment {

    private static final int FRIENDS_ALL            = 0;
    private static final int FRIENDS_ONLY_ONLINE    = 1;

//    this one is yet to be implemented
//    private static final int FRIENDS_REQUESTS       = 2;

    private SlidingTabLayout    mFriendsTabs;
    private ViewPager           mViewPager;
    private View                mShadowView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_friends, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //toolbarShadow.setVisibility(View.GONE);

        mShadowView                        = view.findViewById(R.id.tab_shadow);
        mViewPager      = (ViewPager)        view.findViewById(R.id.friends_viewpager);
        mFriendsTabs    = (SlidingTabLayout) view.findViewById(R.id.friends_tabs);

        if (!(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)) {

            mShadowView.setVisibility(View.VISIBLE);
        }

        mViewPager  .setAdapter(new TabPagerAdapter(getChildFragmentManager()));

        mFriendsTabs.setViewPager(mViewPager);
        mFriendsTabs.setDividerColors(Color.TRANSPARENT);
        mFriendsTabs.setSelectedIndicatorColors(Color.WHITE);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        //toolbarShadow.setVisibility(View.VISIBLE);
    }

    private class TabPagerAdapter extends FragmentStatePagerAdapter
            implements IFragmentTitleCallback {

        public TabPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {

                case FRIENDS_ALL:           return FriendsListFragment.newInstance(false, this);
                case FRIENDS_ONLY_ONLINE:   return FriendsListFragment.newInstance(true,  this);
            }

            return new Fragment();
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {

                case FRIENDS_ALL: {

                    return "FRIENDS";
                }
                case FRIENDS_ONLY_ONLINE: {

                    return "ONLINE";
                }
            }

            return "Error";
        }

        @Override
        public void setFriendsCountTitle(int count, int position) {

            switch (position) {

                case FRIENDS_ALL: {

                    mFriendsTabs.setTitle(count + " FRIENDS", FRIENDS_ALL);
                    break;
                }
                case FRIENDS_ONLY_ONLINE: {

                    mFriendsTabs.setTitle(count + " ONLINE", FRIENDS_ONLY_ONLINE);
                    break;
                }
            }
        }
    }
}
