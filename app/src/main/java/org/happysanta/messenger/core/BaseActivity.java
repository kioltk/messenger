package org.happysanta.messenger.core;

import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import com.vk.sdk.VKUIHelper;

import org.happysanta.messenger.R;
import org.happysanta.messenger.core.util.Dimen;

/**
 * Created by d_great on 23.12.14.
 */
public class BaseActivity extends ActionBarActivity {
    private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        VKUIHelper.onCreate(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar!=null) {
            setSupportActionBar(toolbar);
            int paddingTop = Dimen.getStatusBarHeight();
            toolbar.setPadding(0,paddingTop, 0, 0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VKUIHelper.onDestroy(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        VKUIHelper.onResume(this);
    }

    public Toolbar getToolbar() {
        return toolbar;
    }
}
