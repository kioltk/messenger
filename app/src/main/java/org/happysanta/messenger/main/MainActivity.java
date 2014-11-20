package org.happysanta.messenger.main;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;

import org.happysanta.messenger.R;
import org.happysanta.messenger.about.AboutFragment;
import org.happysanta.messenger.friends.FriendsFragment;
import org.happysanta.messenger.messages.ConversationsFragment;
import org.happysanta.messenger.messages.GroupsFragment;
import org.happysanta.messenger.profile.ProfileFragment;
import org.happysanta.messenger.settings.SettingsFragment;

public class MainActivity extends ActionBarActivity
        implements NavigationFragment.NavigationDrawerCallbacks {

     private NavigationFragment mNavigationFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationFragment = (NavigationFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mNavigationFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (position) {
            case 0:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new ProfileFragment())
                        .commit();

            break;
            case 1:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new ConversationsFragment())
                        .commit();

            break;
            case 2:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new GroupsFragment())
                        .commit();
                break;
            case 3:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new FriendsFragment())
                        .commit();
                break;
            case 4:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new SettingsFragment())
                        .commit();
                break;
            case 5:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new AboutFragment())
                        .commit();
                break;
            default:
                break;
        }
    }







}
