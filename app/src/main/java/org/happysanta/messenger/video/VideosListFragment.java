package org.happysanta.messenger.video;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.methods.VKApiVideos;
import com.vk.sdk.api.model.VKApiVideo;
import com.vk.sdk.api.model.VKList;

import org.happysanta.messenger.R;
import org.happysanta.messenger.core.BaseFragment;
import org.happysanta.messenger.core.BaseViewHolder;
import org.happysanta.messenger.core.util.TimeUtils;

/**
 * Created by Jesus Christ. Amen.
 */
public class VideosListFragment extends BaseFragment {
    private View rootView;
    private VKList<VKApiVideo> videos;
    private TextView statusView;
    private RecyclerView recycler;
    private GridLayoutManager recyclerLayoutManager;
    private VideosAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_video_list, container, false);
        recycler = (RecyclerView) rootView.findViewById(R.id.recycler);
        statusView = (TextView) rootView.findViewById(R.id.status);

        recyclerLayoutManager = new GridLayoutManager(activity, 2);
        recycler.setLayoutManager(recyclerLayoutManager);
        recycler.setHasFixedSize(false);

        new VKApiVideos().get(new VKParameters()).executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                videos = (VKList<VKApiVideo>) response.parsedModel;
                VideosListFragment.this.videos = videos;
                adapter = new VideosAdapter();
                recycler.setAdapter(adapter);
                statusView.setVisibility(View.GONE);
            }

            @Override
            public void onError(VKError error) { statusView.setText(error.toString()); }
        });

        return rootView;
    }

    private class VideosAdapter extends RecyclerView.Adapter<VideosHolder>{
        @Override
        public VideosHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new VideosHolder(LayoutInflater.from(activity).inflate(R.layout.item_video, null));
        }

        @Override
        public void onBindViewHolder(VideosHolder holder, int position) {
            holder.bind(0, videos.get(position));

        }

        @Override
        public int getItemCount() { return videos.size(); }
    }

    private class VideosHolder extends BaseViewHolder {

        private final ImageView bgView;
        private final TextView titleView;
        private final TextView durationView;
        private final TextView countView;
        private final TextView ownerIdView;

        public VideosHolder(View itemView) {
            super(itemView);
            bgView = (ImageView) findViewById(R.id.videoPreview);
            titleView = (TextView) findViewById(R.id.title);
            durationView = (TextView) findViewById(R.id.duration);
            countView = (TextView) findViewById(R.id.view_count);
            ownerIdView = (TextView) findViewById(R.id.owner_id);
        }

        public void bind(final int position, final VKApiVideo video) {

            ImageLoader.getInstance().displayImage(video.photo_320, bgView, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {

                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    bgView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {

                }
            });

            bgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.startActivity(VideoPlayerActivity.openVideo(activity, video.link, video.id, video.photo_640, video.duration));
                }
            });

            titleView.setText(video.title);
            durationView.setText(TimeUtils.formatDuration(video.duration));
            ownerIdView.setText("" + video.owner_id);
            countView.setText(video.views + " views");
        }
    }
}
