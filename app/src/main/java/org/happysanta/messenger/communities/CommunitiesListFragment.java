package org.happysanta.messenger.communities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.methods.VKApiGroups;
import com.vk.sdk.api.model.VKApiCommunity;
import com.vk.sdk.api.model.VKApiCommunityArray;
import com.vk.sdk.api.model.VKApiCommunityFull;
import com.vk.sdk.api.model.VKList;

import org.happysanta.messenger.R;
import org.happysanta.messenger.core.BaseFragment;
import org.happysanta.messenger.core.BaseViewHolder;

/**
 * Created by insidefun on 08.05.2015
 */
public class CommunitiesListFragment extends BaseFragment {

    private static final String ARG_LIST_TYPE = "list_type";
    private static final int LIST_TYPE_COMMUNITIES = 0;
    private static final int LIST_TYPE_MANAGE = 1;
    private static final int LIST_TYPE_EVENTS = 2;

    private RecyclerView recycler;
    private ProgressBar progress;
    private VKList<VKApiCommunity> communities = new VKList<>();
    private CommunitiesAdapter adapter;

    public CommunitiesListFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView =  inflater.inflate(R.layout.fragment_communities_list, container, false);
        recycler = (RecyclerView) rootView.findViewById(R.id.recycler);
        progress = (ProgressBar) findViewById(R.id.progress_bar);

        //Recycler
        GridLayoutManager recyclerLayoutManager = new GridLayoutManager(activity, 4);
        recycler.setLayoutManager(recyclerLayoutManager);
        recycler.setHasFixedSize(false);

        VKRequest request = new VKApiGroups().get(new VKParameters() {{
            put(VKApiConst.EXTENDED, 1);
        }});
        switch (getArguments().getInt(ARG_LIST_TYPE)) {

            case LIST_TYPE_COMMUNITIES:
                request = new VKApiGroups().get(new VKParameters() {{
                    put(VKApiConst.EXTENDED, 1);
                    put("filter", "groups, publics");
                }});
                break;
            case LIST_TYPE_MANAGE:
                request = new VKApiGroups().get(new VKParameters() {{
                    put(VKApiConst.EXTENDED, 1);
                    put("filter", "moder");
                }});
                break;
            case LIST_TYPE_EVENTS:
                request = new VKApiGroups().get(new VKParameters() {{
                    put(VKApiConst.EXTENDED, 1);
                    put("filter", "events");
                }});
                break;
        }
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                adapter = new CommunitiesAdapter(getActivity(), (VKApiCommunityArray) response.parsedModel);
                recycler.setAdapter(adapter);
                progress.setVisibility(View.GONE);
            }
        });

        return rootView;
    }

    public static Fragment getCommunitiesInstance() {
        CommunitiesListFragment fragment = new CommunitiesListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_LIST_TYPE, LIST_TYPE_COMMUNITIES);
        fragment.setArguments(args);
        return fragment;
    }

    public static Fragment getCommunitiesManageInstance() {
        CommunitiesListFragment fragment = new CommunitiesListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_LIST_TYPE, LIST_TYPE_MANAGE);
        fragment.setArguments(args);
        return fragment;
    }

    public static Fragment getEventInstance() {
        CommunitiesListFragment fragment = new CommunitiesListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_LIST_TYPE, LIST_TYPE_EVENTS);
        fragment.setArguments(args);
        return fragment;
    }


    private class CommunitiesAdapter extends RecyclerView.Adapter<CommunitiesHolder> {
        private final VKApiCommunityArray communities;
        private final Activity activity;

        public CommunitiesAdapter(FragmentActivity activity, VKApiCommunityArray communities) {
            this.activity = activity;
            this.communities = communities;
        }

        @Override
        public CommunitiesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CommunitiesHolder(LayoutInflater.from(activity).inflate(R.layout.item_community, null));
        }

        @Override
        public void onBindViewHolder(CommunitiesHolder holder, int position) {
            holder.bind(0, communities.get(position));
        }

        @Override
        public int getItemCount() { return communities.size(); }
    }

    private class CommunitiesHolder extends BaseViewHolder{
        private final ImageView photoView;
        private final TextView nameView;
        private final TextView communityTypeView;
        private final TextView followersCountView;

        public CommunitiesHolder(View itemView) {
            super(itemView);
            photoView = (ImageView) itemView.findViewById(R.id.photo);
            nameView = (TextView) itemView.findViewById(R.id.title);
            communityTypeView = (TextView) itemView.findViewById(R.id.community_type);
            followersCountView = (TextView) itemView.findViewById(R.id.followers_count);

        }

        public void bind(final int position, final VKApiCommunityFull community){

            ImageLoader.getInstance().displayImage(community.photo_100, photoView, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {

                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    Log.d("onLoadingFailed", "Community: " + community.toString());
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    photoView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {

                }
            });
            nameView.setText(community.name);
            nameView.setMaxLines(2);
            if (community.type == 0){
                communityTypeView.setText("Open Community");
            } else if (community.type == 1) {
                communityTypeView.setText("Public page");
            } else {
                communityTypeView.setText("Event");
            }
            followersCountView.setText(community.members_count + " followers");
        }
    }
}
