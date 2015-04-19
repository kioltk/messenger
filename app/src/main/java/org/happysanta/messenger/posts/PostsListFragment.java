package org.happysanta.messenger.posts;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
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

    // core
    private VKList<VKApiPost> newsList = new VKList<>();

    // ui
    private RecyclerView recyclerView;
    private PostsAdapter postsAdapter;

    public PostsListFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_news, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        new VKApiWall().get(new VKParameters(){{ put(VKApiWall.EXTENDED,1); }}).executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                postsAdapter = new PostsAdapter(getActivity(), (VKPostArray) response.parsedModel);
                recyclerView.setAdapter(postsAdapter);
            }
        });


        return rootView;
    }
}