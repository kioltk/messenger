package org.happysanta.messenger.audio;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import org.happysanta.messenger.R;
import org.happysanta.messenger.core.BaseFragment;
import org.happysanta.messenger.friends.views.SlidingTabLayout;

/**
 * Created by admin on 09.05.2015
 */
public class AudioFragment extends BaseFragment {
    ViewPager viewPager;
    private PagerAdapter audioAdapter;
    private SlidingTabLayout tabsView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_feed, null);
        tabsView = (SlidingTabLayout) rootView.findViewById(R.id.friends_tabs);

        viewPager = (ViewPager) rootView.findViewById(R.id.pager);
        audioAdapter = new AudioPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(audioAdapter);

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
