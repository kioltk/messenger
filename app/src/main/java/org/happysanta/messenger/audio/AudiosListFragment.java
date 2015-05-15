package org.happysanta.messenger.audio;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.methods.VKApiAudios;
import com.vk.sdk.api.model.VKApiAudio;
import com.vk.sdk.api.model.VKAudioArray;

import org.happysanta.messenger.R;
import org.happysanta.messenger.core.BaseFragment;
import org.happysanta.messenger.core.BaseViewHolder;
import org.happysanta.messenger.core.util.TimeUtils;
import org.happysanta.messenger.core.views.TintImageView;

/**
 * Created by Jesus Christ. Amen.
 */
public class AudiosListFragment extends BaseFragment {
    private static final String ARG_LIST_TYPE = "list_type";
    private static final int LIST_TYPE_MY_MUSIC = 0;
    private static final int LIST_TYPE_SUGGESTED = 1;
    /*
    private static final int LIST_TYPE_POPULAR = 2;
    private static final int LIST_TYPE_ALBUMS = 3;
    private int listType;
    listType = getArguments().getInt(ARG_LIST_TYPE);
    */

    private int audioId;
    int ownerId;
    //Edit audio strings
    private String editTitleString;
    private String editArtistString;

    private RecyclerView recycler;
    private TextView statusView;
    private AudioListAdapter adapter;

    private String TAG = "AudioListFragment";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_audio_list, container, false);
        recycler = (RecyclerView) rootView.findViewById(R.id.recycler);
        statusView = (TextView) rootView.findViewById(R.id.status);

        LinearLayoutManager recyclerLayoutManager = new LinearLayoutManager(activity);
        recycler.setLayoutManager(recyclerLayoutManager);
        recycler.setHasFixedSize(false);

        VKRequest request = new VKApiAudios().get(new VKParameters(){{
            put(VKApiConst.EXTENDED, 1);
        }});
        switch (getArguments().getInt(ARG_LIST_TYPE)) {

            case LIST_TYPE_MY_MUSIC:
                request = new VKApiAudios().get(new VKParameters() {{
                    put(VKApiConst.EXTENDED, 1);
                    put("count", 1000);
                }});
                break;
            case LIST_TYPE_SUGGESTED:
                request = new VKApiAudios().getRecommendations(new VKParameters() {{
                    put(VKApiConst.EXTENDED, 1);
                    put("count", 100);
                }});
                break;

            /*
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
                */
        }

        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                adapter = new AudioListAdapter(getActivity(), (VKAudioArray) response.parsedModel);
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

    private void editAudio(){

        new VKApiAudios().edit(new VKParameters(){{
            put("owner_id", ownerId);
            put("audio_id", audioId);
            put("title", editTitleString);
            put("artist", editArtistString);
        }}).executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
            }

            @Override
            public void onError(VKError error) { statusView.setText(error.toString()); }
        });
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

    /*
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
    */

    private class AudioListAdapter extends RecyclerView.Adapter<AudioViewHolder> {
        private final Activity activity;
        private final VKAudioArray audios;

        public AudioListAdapter(FragmentActivity activity, VKAudioArray audios) {
            this.activity = activity;
            this.audios = audios;
        }

        @Override
        public AudioViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new AudioViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_audio, null));
        }

        @Override
        public void onBindViewHolder(AudioViewHolder holder, int position) {
            holder.bind(audios.get(position));
        }

        @Override
        public int getItemCount() { return audios.size(); }
    }

    private class AudioViewHolder extends BaseViewHolder {
        private ImageView playView;
        private TextView titleView;
        private TextView subtitleView;
        private final TextView durationView;

        private final EditText editTitleView;
        private final EditText editArtistView;
        private final View titleLayout;
        private final View editLayout;
        private final View durationLayout;
        private final View buttonsLayout;
        private final TintImageView btnConfirm;
        private final TintImageView btnCancel;

        public AudioViewHolder(View itemView) {
            super(itemView);
            playView = (ImageView) findViewById(R.id.play);
            titleView = (TextView) findViewById(R.id.title);
            subtitleView = (TextView) findViewById(R.id.artist);
            durationView = (TextView) findViewById(R.id.duration);
            durationLayout = findViewById(R.id.duration_layout);
            buttonsLayout = findViewById(R.id.buttons_layout);

            //Audio edit
            editTitleView = (EditText) findViewById(R.id.edit_title);
            editArtistView = (EditText) findViewById(R.id.edit_artist);
            titleLayout = findViewById(R.id.title_layout);
            editLayout = findViewById(R.id.edit_layout);
            btnConfirm = (TintImageView) findViewById(R.id.confirm);
            btnCancel = (TintImageView) findViewById(R.id.cancel);
        }
        public void bind(final VKApiAudio audio){
            ownerId = audio.owner_id;
            audioId = audio.getId();

                titleLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(activity, "Play audio", Toast.LENGTH_SHORT).show();
                        activity.startActivity(PlayerActivity.openAudio(getContext(), audio.id, audio.url, audio.title, audio.artist, audio.duration));
                    }
                });

                playView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(activity, "Play audio", Toast.LENGTH_SHORT).show();
                        activity.startActivity(PlayerActivity.openAudio(getContext(), audio.id, audio.url, audio.title, audio.artist, audio.duration));
                    }
                });

                //Fill views
                titleView.setText(audio.title);
                subtitleView.setText(audio.artist);
                if(audio.duration != 0) {
                    durationView.setText(TimeUtils.formatDuration(audio.duration));
                } else {
                    durationView.setVisibility(View.GONE);
                }

                //Edit audio on long click
                titleLayout.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        titleLayout.setVisibility(View.GONE);
                        durationLayout.setVisibility(View.GONE);
                        editLayout.setVisibility(View.VISIBLE);
                        buttonsLayout.setVisibility(View.VISIBLE);

                        //Confirm renaming
                        btnConfirm.setOnClickListener(new View.OnClickListener() {
                            String newTitleName;
                            String newArtistName;

                            @Override
                            public void onClick(View v) {
                                editTitleString = editTitleView.getText().toString();
                                editArtistString = editArtistView.getText().toString();
                                if (!editTitleString.isEmpty() && !editTitleString.equals("") && editTitleString != null) {
                                    newTitleName = editTitleString;
                                    editAudio();

                                    Toast.makeText(activity, "new Title is: " + editTitleString, Toast.LENGTH_SHORT).show();
                                    Log.v(TAG, "new Title is " + editTitleString);
                                } else {
                                    editTitleString = audio.title;
                                    Log.v(TAG, "new Title is empty, current: " + audio.title);
                                }

                                if (!editArtistString.isEmpty() && !editArtistString.equals("") && editArtistString != null) {
                                    newArtistName = editArtistString;
                                    editAudio();

                                    Toast.makeText(activity, "new Artist is: " + editArtistString, Toast.LENGTH_SHORT).show();
                                    Log.v(TAG, "new Artist is " + editArtistString);
                                } else {
                                    editArtistString = audio.artist;
                                    Log.v(TAG, "new Artist is empty, current: " + audio.artist);
                                }

                                titleLayout.setVisibility(View.VISIBLE);
                                durationLayout.setVisibility(View.VISIBLE);
                                editLayout.setVisibility(View.GONE);
                                buttonsLayout.setVisibility(View.GONE);
                            }
                        });

                        //Cancel renaming
                        btnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                editTitleString = audio.title;
                                editArtistString = audio.artist;

                                Log.v(TAG, "Cancel");
                                titleLayout.setVisibility(View.VISIBLE);
                                durationLayout.setVisibility(View.VISIBLE);
                                editLayout.setVisibility(View.GONE);
                                buttonsLayout.setVisibility(View.GONE);
                            }
                        });


                        return true;
                    }
                });

        }
    }
}
