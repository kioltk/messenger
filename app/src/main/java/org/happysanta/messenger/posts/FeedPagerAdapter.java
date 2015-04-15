package org.happysanta.messenger.posts;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Jesus Christ. Amen.
 */
public class FeedPagerAdapter extends FragmentStatePagerAdapter {
    public FeedPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment item = new PostsListFragment();

        return item;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0: return "News";
            case 1: return "Suggested";
            case 2: return "Friends";
            case 3: return "Communities";
            case 4: return "Photos";
        }
        return "error";
    }
}
