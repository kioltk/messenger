package org.happysanta.messenger.main;

import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;

import org.happysanta.messenger.R;
import org.happysanta.messenger.about.AboutFragment;
import org.happysanta.messenger.core.BaseActivity;
import org.happysanta.messenger.friends.FriendsFragment;
import org.happysanta.messenger.longpoll.LongpollService;
import org.happysanta.messenger.messages.chats.ChatsListFragment;
import org.happysanta.messenger.messages.conversations.ConversationsListFragment;
import org.happysanta.messenger.news.NewsListFragment;
import org.happysanta.messenger.user.UserFragment;
import org.happysanta.messenger.settings.SettingsFragment;

public class MainActivity extends BaseActivity
        implements NavigationFragment.NavigationDrawerCallbacks {

     private NavigationFragment mNavigationFragment;
    private DrawerLayout mDrawerLayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        LongpollService.toggle(this, "param1", "param2");

        mNavigationFragment = (NavigationFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationFragment.setUp(
                R.id.navigation_drawer,
                mDrawerLayout);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, new ConversationsListFragment())
                .commit();
    }

    @Override
    public void onNavigationDrawerItemSelected(int itemdId) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (itemdId) {
            case (int) NavigationFragment.NAVIGATION_PROFILE_ID:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new UserFragment())
                        .commit();

                break;
            case NavigationFragment.NAVIGATION_NEWS_ID:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new NewsListFragment())
                        .commit();
                break;
            case (int) NavigationFragment.NAVIGATION_MESSAGES_ID:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new ConversationsListFragment())
                        .commit();
                break;
            case (int) NavigationFragment.NAVIGATION_GROUPS_ID:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new ChatsListFragment())
                        .commit();
                break;
            case (int) NavigationFragment.NAVIGATION_FRIENDS_ID:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new FriendsFragment())
                        .commit();
                break;
            case (int) NavigationFragment.NAVIGATION_SETTINGS_ID:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new SettingsFragment())
                        .commit();
                break;
            case (int) NavigationFragment.NAVIGATION_ABOUT_ID:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new AboutFragment())
                        .commit();
                break;
            default:
                break;
        }
    }






}
