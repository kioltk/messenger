package org.happysanta.messenger.posts.manage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.vk.sdk.api.model.VKList;

import org.happysanta.messenger.R;
import org.happysanta.messenger.core.BaseFragment;
import org.happysanta.messenger.core.BaseViewHolder;
import org.happysanta.messenger.core.views.TintImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 27.04.2015.
 */
public class FeedManageFragment extends BaseFragment {
    private RecyclerView recycler;
    private LinearLayoutManager recyclerLayoutManager;
    private FeedManageAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_feed_manage_list, container, false);
        recycler = (RecyclerView) findViewById(R.id.recycler);

        recyclerLayoutManager = new LinearLayoutManager(activity);
        recycler.setLayoutManager(recyclerLayoutManager);
        recycler.setHasFixedSize(false);

        adapter = new FeedManageAdapter();
        adapter.setHasStableIds(true);
        recycler.setAdapter(adapter);

        return rootView;
    }

    private class FeedManageAdapter extends RecyclerView.Adapter<FeedManageHolder> {

        @Override
        public FeedManageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new FeedManageHolder(LayoutInflater.from(activity).inflate(R.layout.item_manage_tab, parent, false));
        }

        @Override
        public void onBindViewHolder(FeedManageHolder holder, int position) {
            switch (position){
                case 0:
                    holder.tabsNameView.setText("Feed");
                    break;

                case 1:
                    holder.tabsNameView.setText("Suggestions");
                    break;

                case 2:
                    holder.tabsNameView.setText("Photo");
                    break;

                case 3:
                    holder.tabsNameView.setText("Users");
                    break;

                case 4:
                    holder.tabsNameView.setText("Communities");
                    break;

                case 5:
                    holder.tabsNameView.setText("Favorites");
                    break;
            }
        }

        @Override
        public int getItemCount() { return 6; }
    }

    private class FeedManageHolder extends BaseViewHolder {
        private CheckBox tabsSelectView;
        private TintImageView tabsIcoView;
        private TextView tabsNameView;

        public FeedManageHolder(View itemView) {
            super(itemView);
            tabsIcoView = (TintImageView) findViewById(R.id.tabs_ico);
            tabsNameView = (TextView) findViewById(R.id.tabs_name);
            tabsSelectView = (CheckBox) findViewById(R.id.checkBox);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        tabsSelectView.setChecked(!tabsSelectView.isChecked());
                }
            });
        }
    }
}
