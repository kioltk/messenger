package org.happysanta.messenger.posts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.happysanta.messenger.R;
import org.happysanta.messenger.core.BaseActivity;
import org.happysanta.messenger.core.BaseViewHolder;
import org.happysanta.messenger.core.views.TintImageView;

/**
 * Created by admin on 01.05.2015
 */
public class ComposeActivity extends BaseActivity {
    private static final String EXTRA_USER_ID = "extra_user_id";
    private TextView titleView;
    private TextView bodyView;
    private TintImageView photoView;
    private TintImageView mapView;
    private TintImageView smileView;
    private PhotoListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        //removeView = (TintImageView) findViewById(R.id.remove);
        titleView = (TextView) findViewById(R.id.title);
        bodyView = (TextView) findViewById(R.id.body);
        photoView = (TintImageView) findViewById(R.id.photo);
        mapView = (TintImageView) findViewById(R.id.map);
        smileView = (TintImageView) findViewById(R.id.smile);

        //Added photos recycler
        RecyclerView recycler = (RecyclerView) findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter = new PhotoListAdapter();
        recycler.setAdapter(adapter);

        //Toolbar
        android.support.v7.app.ActionBar actionbar = getSupportActionBar();
        if (null != actionbar) {
            actionbar.setHomeButtonEnabled(true);
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
            final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }


    private class PhotoListAdapter extends RecyclerView.Adapter<PhotoHolder> {
        @Override
        public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new PhotoHolder(View.inflate(getBaseContext(), R.layout.item_compose_photo, null));
        }

        @Override
        public void onBindViewHolder(PhotoHolder holder, int position) {
            holder.bind(0);
        }

        @Override
        public int getItemCount() {
            return 10;
        }
    }

    private class PhotoHolder extends BaseViewHolder {
        private final ImageView uploadPhotoView;
        private final ProgressBar progressView;
        private final ImageView removePhotoView;

        public PhotoHolder(View itemView) {
            super(itemView);
            uploadPhotoView = (ImageView) itemView.findViewById(R.id.photo);
            removePhotoView = (ImageView) itemView.findViewById(R.id.remove_photo);
            progressView = (ProgressBar) itemView.findViewById(R.id.progress_bar);
        }

        public void bind(final int position){
        }
    }


    public static Intent openCompose(Context context, int userId) {
        return new Intent(context, ComposeActivity.class)
                .putExtra(EXTRA_USER_ID, userId);
    }
}
