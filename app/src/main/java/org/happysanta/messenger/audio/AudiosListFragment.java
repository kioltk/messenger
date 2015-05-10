package org.happysanta.messenger.audio;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
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
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.methods.VKApiAudios;
import com.vk.sdk.api.model.VKApiAudio;
import com.vk.sdk.api.model.VKAudioArray;
import com.vk.sdk.api.model.VKList;

import org.happysanta.messenger.R;
import org.happysanta.messenger.core.BaseFragment;
import org.happysanta.messenger.core.BaseViewHolder;
import org.happysanta.messenger.core.util.TimeUtils;

/**
 * Created by Jesus Christ. Amen.
 */
public class AudiosListFragment extends BaseFragment {
    private static final String ARG_LIST_TYPE = "list_type";
    private static final int LIST_TYPE_MY_MUSIC = 0;
    private static final int LIST_TYPE_SUGGESTED = 1;
    private static final int LIST_TYPE_POPULAR = 2;
    private static final int LIST_TYPE_ALBUMS = 3;
    private int listType;

    int audioId;
    private View rootView;
    private VKList<VKApiAudio> audios;
    private RecyclerView recycler;
    private TextView statusView;
    private LinearLayoutManager recyclerLayoutManager;
    private AudiosAdapter adapter;
    private GridLayoutManager recyclerGridLayoutManager;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_audio_list, container, false);
        recycler = (RecyclerView) rootView.findViewById(R.id.recycler);
        statusView = (TextView) rootView.findViewById(R.id.status);

        listType = getArguments().getInt(ARG_LIST_TYPE);
        if(listType == LIST_TYPE_ALBUMS){
            recyclerGridLayoutManager = new GridLayoutManager(activity, 2);
            recycler.setLayoutManager(recyclerGridLayoutManager);
            recycler.setHasFixedSize(false);
        } else {
            recyclerLayoutManager = new LinearLayoutManager(activity);
            recycler.setLayoutManager(recyclerLayoutManager);
            recycler.setHasFixedSize(false);
        }

        VKRequest request = new VKApiAudios().get(new VKParameters(){{
            put(VKApiConst.EXTENDED, 1);
        }});
        switch (getArguments().getInt(ARG_LIST_TYPE)) {

            case LIST_TYPE_MY_MUSIC:
                request = new VKApiAudios().get(new VKParameters() {{
                    put(VKApiConst.EXTENDED, 1);
                }});
                break;
            case LIST_TYPE_SUGGESTED:
                request = new VKApiAudios().getRecommendations(new VKParameters() {{
                    put(VKApiConst.EXTENDED, 1);
                }});
                break;
            case LIST_TYPE_POPULAR:
                request = new VKApiAudios().getPopular(new VKParameters() {{
                    put(VKApiConst.EXTENDED, 1);
                }});
                break;
            case LIST_TYPE_ALBUMS:
                request = new VKApiAudios().getAlbums(new VKParameters() {{
                    put(VKApiConst.EXTENDED, 1);
                }});
                break;
        }

        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                adapter = new AudiosAdapter(getActivity(), (VKAudioArray) response.parsedModel);
                adapter.setHasStableIds(true);
                recycler.setAdapter(adapter);
                statusView.setVisibility(View.GONE);
            }

            @Override
            public void onError(VKError error) {
                statusView.setText(error.toString());
            }
        });

        return rootView;
    }

    public static Fragment getMyAudiosInstance() {
        AudiosListFragment fragment = new AudiosListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_LIST_TYPE, LIST_TYPE_MY_MUSIC);
        fragment.setArguments(args);
        return fragment;
    }

    public static Fragment getSuggestedInstance() {
        AudiosListFragment fragment = new AudiosListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_LIST_TYPE, LIST_TYPE_SUGGESTED);
        fragment.setArguments(args);
        return fragment;
    }

    public static Fragment getPopularInstance() {
        AudiosListFragment fragment = new AudiosListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_LIST_TYPE, LIST_TYPE_POPULAR);
        fragment.setArguments(args);
        return fragment;
    }

    public static Fragment getAlbumsInstance() {
        AudiosListFragment fragment = new AudiosListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_LIST_TYPE, LIST_TYPE_ALBUMS);
        fragment.setArguments(args);
        return fragment;
    }

    private class AudiosAdapter extends RecyclerView.Adapter<AudiosHolder> {
        private final Activity activity;
        private final VKAudioArray audios;

        public AudiosAdapter(FragmentActivity activity, VKAudioArray audios) {
            this.activity = activity;
            this.audios = audios;
        }

        @Override
        public AudiosHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            listType = getArguments().getInt(ARG_LIST_TYPE);
            if(listType == LIST_TYPE_ALBUMS){
                return new AudiosHolder(LayoutInflater.from(activity).inflate(R.layout.item_audio_album, null));
            } else {
                return new AudiosHolder(LayoutInflater.from(activity).inflate(R.layout.item_audio, null));
            }
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
            listType = getArguments().getInt(ARG_LIST_TYPE);

            if(listType == LIST_TYPE_ALBUMS) {
                //Go to album
            } else {
                playView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(activity, "Play audio", Toast.LENGTH_SHORT).show();
                        activity.startActivity(PlayerActivity.openAudio(getContext(), audio.id, audio.url, audio.title, audio.artist, audio.duration));
                    }
                });
            }


            titleView.setText(audio.title);

            if(audio.artist != null && !audio.artist.isEmpty()) {
                subtitleView.setText(audio.artist);
            } else {
                subtitleView.setVisibility(View.GONE);
            }

            if(audio.duration != 0) {
                durationView.setText(TimeUtils.formatDuration(audio.duration));
            } else {
                durationView.setVisibility(View.GONE);
            }
        }
    }
}
