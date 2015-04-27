package org.happysanta.messenger.posts.manage;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by admin on 26.04.2015.
 */
public class ManageFragmentsAdapter extends FragmentStatePagerAdapter {
    public ManageFragmentsAdapter(FragmentManager fm) { super(fm); }

    @Override
    public Fragment getItem(int position) {
        Fragment item = null;
        switch (position) {
            case 0:
                item = ManageActivity.getFeedInstance();
                break;
            case 1:
                item = ManageActivity.getUsersInstance();
                break;
            case 2:
                item = ManageActivity.getGroupInstance();
                break;
        }
        return item;
    }

    @Override
    public int getCount() { return 3; }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0: return "Feed";
            case 1: return "Users";
            case 2: return "Group";
        }
        return "error";
    }
}
