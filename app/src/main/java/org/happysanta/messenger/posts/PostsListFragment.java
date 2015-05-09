package org.happysanta.messenger.posts;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.methods.VKApiFeed;
import com.vk.sdk.api.methods.VKApiWall;
import com.vk.sdk.api.model.VKApiPost;
import com.vk.sdk.api.model.VKList;
import com.vk.sdk.api.model.VKPostArray;

import org.happysanta.messenger.R;
import org.happysanta.messenger.core.BaseFragment;

/**
 * Created by Jesus Christ. Amen.
 */
public class PostsListFragment extends BaseFragment {

    private static final String ARG_LIST_TYPE = "list_type";
    private static final int LIST_TYPE_NEWS = 0;
    private static final int LIST_TYPE_SUGGESTED = 1;
    private static final int LIST_TYPE_FRIENDS = 2;
    private static final int LIST_TYPE_COMMUNITIES = 3;
    private static final int LIST_TYPE_PHOTOS = 4;

    // core
    private VKList<VKApiPost> newsList = new VKList<>();

    // ui
    private RecyclerView recyclerView;
    private PostsAdapter postsAdapter;
    private ProgressBar progressView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_news, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        progressView = (ProgressBar) rootView.findViewById(R.id.progress_bar);

        VKRequest request = new VKApiWall().get(new VKParameters() {{
            put(VKApiWall.EXTENDED, 1);
        }});
        switch (getArguments().getInt(ARG_LIST_TYPE)) {
            case LIST_TYPE_NEWS:
                request = new VKApiFeed().get(new VKParameters() {{
                    put(VKApiWall.EXTENDED, 1);
                    put("filters", "post");
                    fetchComments();
                }});
                break;
            /*case LIST_TYPE_FRIENDS:
                request = new VKApiFeed().get(new VKParameters() {{
                    put(VKApiWall.EXTENDED, 1);
                    put("filters", "friends");
                }});
                break;*/
            case LIST_TYPE_SUGGESTED:
                request = new VKApiFeed().getRecommended(new VKParameters() {{
                    put(VKApiWall.EXTENDED, 1);
                    put("filters", "post");
                    fetchComments();
                }});
                break;

        }

        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                postsAdapter = new PostsAdapter(getActivity(), (VKPostArray) response.parsedModel);
                recyclerView.setAdapter(postsAdapter);
                progressView.setVisibility(View.GONE);
            }
        });


        return rootView;
    }

    private void fetchComments() {
        new VKApiFeed().getComments(new VKParameters() {{
            put(VKApiWall.EXTENDED, 1);
            put("need_likes", 1);
        }});
    }

    public static Fragment getNewsInstance() {
        PostsListFragment fragment = new PostsListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_LIST_TYPE, LIST_TYPE_NEWS);
        fragment.setArguments(args);
        return fragment;
    }

    public static Fragment getSuggestedInstance() {
        PostsListFragment fragment = new PostsListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_LIST_TYPE, LIST_TYPE_SUGGESTED);
        fragment.setArguments(args);
        return fragment;
    }
    public static Fragment getFriendsInstance() {
        PostsListFragment fragment = new PostsListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_LIST_TYPE, LIST_TYPE_FRIENDS);
        fragment.setArguments(args);
        return fragment;
    }
    public static Fragment getCommunitiesInstance() {
        PostsListFragment fragment = new PostsListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_LIST_TYPE, LIST_TYPE_COMMUNITIES);
        fragment.setArguments(args);
        return fragment;
    }
    public static Fragment getPhotosInstance() {
        PostsListFragment fragment = new PostsListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_LIST_TYPE, LIST_TYPE_PHOTOS);
        fragment.setArguments(args);
        return fragment;
    }

}