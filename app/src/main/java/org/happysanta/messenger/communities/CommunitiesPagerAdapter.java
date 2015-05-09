package org.happysanta.messenger.communities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by insidefun on 09.05.2015
 */
public class CommunitiesPagerAdapter extends FragmentStatePagerAdapter {
    public CommunitiesPagerAdapter(FragmentManager fm) { super(fm); }

    @Override
    public Fragment getItem(int position) {
        Fragment item = null;
        switch (position) {
            case 0:
                item = CommunitiesListFragment.getCommunitiesInstance();
                break;
            case 1:
                item = CommunitiesListFragment.getCommunitiesManageInstance();
                break;
            case 2:
                item = CommunitiesListFragment.getEventInstance();
                break;
        }
        return item;
    }

    @Override
    public int getCount() { return 3; }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0: return "Communities";
            case 1: return "Manage";
            case 2: return "Events";
        }
        return "error";
    }
}
