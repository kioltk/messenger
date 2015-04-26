package org.happysanta.messenger.audio;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.methods.VKApiAudios;
import com.vk.sdk.api.model.VKApiAudio;
import com.vk.sdk.api.model.VKList;

import org.happysanta.messenger.R;
import org.happysanta.messenger.core.BaseFragment;
import org.happysanta.messenger.core.BaseViewHolder;
import org.happysanta.messenger.core.util.TimeUtils;

/**
 * Created by Jesus Christ. Amen.
 */
public class AudiosListFragment extends BaseFragment {
    int audioId;
    private View rootView;
    private VKList<VKApiAudio> audios;
    private RecyclerView recycler;
    private TextView statusView;
    private LinearLayoutManager recyclerLayoutManager;
    private AudiosAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_audio_list, container, false);
        recycler = (RecyclerView) rootView.findViewById(R.id.recycler);
        statusView = (TextView) rootView.findViewById(R.id.status);

        recyclerLayoutManager = new LinearLayoutManager(activity);
        recycler.setLayoutManager(recyclerLayoutManager);
        recycler.setHasFixedSize(false);

        new VKApiAudios().get(new VKParameters()).executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                audios = (VKList<VKApiAudio>) response.parsedModel;
                AudiosListFragment.this.audios = audios;
                adapter = new AudiosAdapter();
                adapter.setHasStableIds(true);
                recycler.setAdapter(adapter);
                statusView.setVisibility(View.GONE);
            }

            @Override
            public void onError(VKError error) { statusView.setText(error.toString()); }
        });

        return rootView;
    }

    private class AudiosAdapter extends RecyclerView.Adapter<AudiosHolder> {
        @Override
        public AudiosHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new AudiosHolder(View.inflate(activity, R.layout.item_audio, null));
        }

        @Override
        public void onBindViewHolder(AudiosHolder holder, int position) {
            holder.bind(0, audios.get(position));
        }

        @Override
        public int getItemCount() { return audios.size(); }
    }

    private class AudiosHolder extends BaseViewHolder {
        private ImageView playView;
        private TextView titleView;
        private TextView subtitleView;
        private final TextView durationView;

        public AudiosHolder(View itemView) {
            super(itemView);
            playView = (ImageView) findViewById(R.id.play);
            titleView = (TextView) findViewById(R.id.title);
            subtitleView = (TextView) findViewById(R.id.subtitle);
            durationView = (TextView) findViewById(R.id.duration);
        }
        public void bind(final int position, final VKApiAudio audio){
            playView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(activity, "Play audio", Toast.LENGTH_SHORT).show();
                    activity.startActivity(PlayerActivity.openAudio(getContext(), audio.id, audio.url, audio.title, audio.artist, audio.duration));
                }
            });


            titleView.setText(audio.title);
            subtitleView.setText(audio.artist);
            durationView.setText(TimeUtils.formatDuration(audio.duration));
        }
    }
}
