package org.happysanta.messenger.main;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.happysanta.messenger.BuildConfig;
import org.happysanta.messenger.R;

/**
 * Fragment used for managing interactions for and presentation of a navigation drawer.
 * See the <a href="https://developer.android.com/design/patterns/navigation-drawer.html#Interaction">
 * design guidelines</a> for a complete explanation of the behaviors implemented here.
 */
public class NavigationFragment extends Fragment {

    public static final int DIVIDER = -1;
    public static final int DIVIDER_SMALL = -2;
    public static final int NAVIGATION_PROFILE_ID = 11;
    public static final int NAVIGATION_NEWS_ID = 10;
    public static final int NAVIGATION_MESSAGES_ID = 1;
    public static final int NAVIGATION_GROUPSCHAT_ID = 2;
    public static final int NAVIGATION_VIDEOS_ID = 7;
    public static final int NAVIGATION_AUDIOS_ID = 8;
    public static final int NAVIGATION_PHOTOS_ID = 9;
    public static final int NAVIGATION_COMMUNITIES_ID = 6;
    public static final int NAVIGATION_FRIENDS_ID = 3;
    public static final int NAVIGATION_SETTINGS_ID = 4;
    public static final int NAVIGATION_ABOUT_ID = 5;
    public static final int NAVIGATION_SANDBOX_ID = 105;

    public static int getItemId(int position){
        if(position==1){
            return DIVIDER_SMALL;
        }
        if(BuildConfig.DEBUG) {
            switch (position) {
                case 0:
                    return NavigationFragment.NAVIGATION_PROFILE_ID;
                case 2:
                    return NavigationFragment.NAVIGATION_NEWS_ID;
                case 3:
                    return NavigationFragment.NAVIGATION_MESSAGES_ID;
                case 4:
                    return NavigationFragment.NAVIGATION_GROUPSCHAT_ID;
                case 5:
                    return NavigationFragment.NAVIGATION_FRIENDS_ID;
                case 6:
                    return DIVIDER;
                case 7:
                    return NavigationFragment.NAVIGATION_VIDEOS_ID;
                case 8:
                    return NavigationFragment.NAVIGATION_AUDIOS_ID;
                case 9:
                    return NavigationFragment.NAVIGATION_PHOTOS_ID;
                case 10:
                    return NavigationFragment.NAVIGATION_COMMUNITIES_ID;
                case 11:
                    return DIVIDER;
                case 12:
                    return NavigationFragment.NAVIGATION_SETTINGS_ID;
                case 13:
                    return NavigationFragment.NAVIGATION_ABOUT_ID;

            }
        }else{

            switch (position) {
                case 0:
                    return NavigationFragment.NAVIGATION_PROFILE_ID;
                case 2:
                    return NavigationFragment.NAVIGATION_MESSAGES_ID;
                case 3:
                    return NavigationFragment.NAVIGATION_GROUPSCHAT_ID;
                case 4:
                    return NavigationFragment.NAVIGATION_FRIENDS_ID;
                case 5:
                    return DIVIDER;
                case 6:
                    return NavigationFragment.NAVIGATION_SETTINGS_ID;
                case 7:
                    return NavigationFragment.NAVIGATION_ABOUT_ID;

            }
        }
        return -1;
    }

    /**
     * Remember the position of the selected item.
     */
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    /**
     * Per the design guidelines, you should show the drawer on launch until the user manually
     * expands it. This shared preference tracks this.
     */
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private NavigationDrawerCallbacks mCallbacks;

    /**
     * Helper component that ties the action bar to the navigation drawer.
     */
    private ActionBarDrawerToggle mDrawerToggle;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;
    private View mFragmentContainerView;

    private int mCurrentSelectedPosition = 3;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;

    public NavigationFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Read in the flag indicating whether or not the user has demonstrated awareness of the
        // drawer. See PREF_USER_LEARNED_DRAWER for details.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }

        // Select either the default item (0) or the last selected item.
        selectItem(mCurrentSelectedPosition);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_navigation, container, false);
        mDrawerListView = (ListView) rootView.findViewById(R.id.navigation_list);
        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });
        mDrawerListView.setAdapter(new NavigationAdapter(getActivity()));
        mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);

        rootView.findViewById(R.id.sandbox).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDrawerLayout != null) {
                    mDrawerLayout.closeDrawer(mFragmentContainerView);
                }
                if (mCallbacks != null) {
                    mCallbacks.onNavigationDrawerItemSelected(NavigationFragment.NAVIGATION_SANDBOX_ID);
                }
            }
        });

        return rootView;
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     * @param fragmentId   The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.
        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),                    /* host Activity */
                mDrawerLayout,                    /* DrawerLayout object */
                R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
                R.string.navigation_drawer_close  /* "close drawer" description for accessibility */
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }

                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }

                if (!mUserLearnedDrawer) {
                    // The user manually opened the drawer; store this flag to prevent auto-showing
                    // the navigation drawer automatically in the future.
                    mUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
                }

                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }
        };

        // If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
        // per the navigation drawer design guidelines.
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }

        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void selectItem(int position) {
        mCurrentSelectedPosition = position;
        if (mDrawerListView != null) {
            mDrawerListView.setItemChecked(position, true);
        }
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null) {

            mCallbacks.onNavigationDrawerItemSelected(NavigationFragment.getItemId(position));
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private ActionBar getActionBar() {
        return ((ActionBarActivity) getActivity()).getSupportActionBar();
    }

    public void restore() {
        mCurrentSelectedPosition = 3;
        selectItem(mCurrentSelectedPosition);
    }

    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public static interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onNavigationDrawerItemSelected(int itemId);
    }
}
