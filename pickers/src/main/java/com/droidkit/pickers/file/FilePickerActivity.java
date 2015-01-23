package com.droidkit.pickers.file;

import android.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;


import com.droidkit.file.R;
import com.droidkit.pickers.file.items.BackItem;
import com.droidkit.pickers.file.items.ExplorerItem;
import com.droidkit.pickers.file.util.HistoryDatabase;

public class FilePickerActivity extends BasePickerActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //searchEnable();
        findViewById(R.id.controllers).setVisibility(View.GONE);

        /*
        final int actionBarTitle = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
        final TextView title = (TextView)getWindow().findViewById(actionBarTitle);
        if ( title != null ) {
            title.setEllipsize(TextUtils.TruncateAt.START);
        }*/
        // getActionBar().setIcon(null);
    }

    @Override
    protected Fragment getWelcomeFragment() {
        return new ExplorerFragment();
    }

    @Override
    protected void save() {
        HistoryDatabase.save(this, selectedItems);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View itemView, int position, long id) {

        ExplorerItem item = (ExplorerItem) parent.getItemAtPosition(position);

        if (item instanceof BackItem) {
            onBackPressed();
            return;
        }

        if (item.isDirectory()) {
            String path = item.getPath();
            Bundle bundle = new Bundle();
            bundle.putString("path", path);

            Fragment fragment = new ExplorerFragment();
            fragment.setArguments(bundle);
            getFragmentManager().beginTransaction()
                    .setCustomAnimations(R.animator.picker_fragment_explorer_enter, R.animator.picker_fragment_explorer_exit,
                            R.animator.picker_fragment_explorer_return, R.animator.picker_fragment_explorer_out)
                    .replace(R.id.container, fragment)
                    .addToBackStack(path)
                    .commit();
        } else {

            selectItem(item, itemView);
            returnResult();
        }
    }


}