package org.happysanta.messenger.sandbox;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.happysanta.messenger.R;
import org.happysanta.messenger.core.BaseFragment;
import org.happysanta.messenger.news.NewsListFragment;

/**
 * Created by Jesus Christ. Amen.
 */
public class SandboxFragment extends BaseFragment {
    ViewPager pagerView;
    private PagerAdapter feedAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_sandbox, null);

        pagerView = (ViewPager) rootView.findViewById(R.id.pager);
        feedAdapter = new FragmentStatePagerAdapter(getChildFragmentManager()) {
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


        return rootView;
    }
}
