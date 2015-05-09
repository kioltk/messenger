package org.happysanta.messenger.audio;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by insidefun on 09.05.2015
 */
public class AudioPagerAdapter extends FragmentStatePagerAdapter {
    public AudioPagerAdapter(FragmentManager fm) { super(fm); }

    @Override
    public Fragment getItem(int position) {
        Fragment item = null;
        switch (position){
            case 0:
                item = AudiosListFragment.getMyAudiosInstance();
                break;
            case 1:
                item = AudiosListFragment.getSuggestedInstance();
                break;
            case 2:
                item = AudiosListFragment.getPopularInstance();
                break;
            case 3:
                item = AudiosListFragment.getAlbumsInstance();
                break;
        }
        return item;
    }

    @Override
    public int getCount() { return 4; }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0: return "MY MUSIC";
            case 1: return "SUGGESTED";
            case 2: return "POPULAR";
            case 3: return "ALBUMS";
        }
        return "error";
    }
}
