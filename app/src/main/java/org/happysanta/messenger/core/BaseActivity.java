package org.happysanta.messenger.core;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.vk.sdk.VKUIHelper;

import org.happysanta.messenger.R;
import org.happysanta.messenger.core.util.Dimen;

/**
 * Created by d_great on 23.12.14.
 */
public class BaseActivity extends ActionBarActivity {

    protected Toolbar toolbar;
    //protected View toolBarShadow;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        VKUIHelper.onCreate(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolBarShadow = findViewById(R.id.toolbar_shadow);

        if (toolbar != null) {

            toolbar.setTitleTextColor(getResources().getColor(R.color.vk_white));

            setSupportActionBar(toolbar);
            int paddingTop = Dimen.getStatusBarHeight();
            toolbar.setPadding(0, paddingTop, 0, 0);
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

    /*public View getToolBarShadow() {
        return toolBarShadow;
    }*/
}