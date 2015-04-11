package org.happysanta.messenger.user;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.happysanta.messenger.R;
import org.happysanta.messenger.core.BaseActivity;

public class ProfileActivity extends BaseActivity {

    private static final String EXTRA_USERID = "extra_userid";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        int userid = getIntent().getIntExtra(EXTRA_USERID, 0);
        // todo show loading?

    }

    public static Intent openProfile(Context context, int userid){
        return new Intent(context, ProfileActivity.class).putExtra(EXTRA_USERID, userid);
    }

}
