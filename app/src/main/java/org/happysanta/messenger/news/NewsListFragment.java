package org.happysanta.messenger.news;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.methods.VKApiWall;
import com.vk.sdk.api.model.VKApiPost;
import com.vk.sdk.api.model.VKList;

import org.happysanta.messenger.R;
import org.happysanta.messenger.core.BaseFragment;

/**
 * Created by Jesus Christ. Amen.
 */
public class NewsListFragment extends BaseFragment {

    // core
    private VKList<VKApiPost> newsList = new VKList<>();

    // ui
    private ListView listView;
    private NewsAdapter newsAdapter;

    public NewsListFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_news, container, false);
        listView = (ListView) rootView.findViewById(R.id.list);

        new VKApiWall().get(new VKParameters(){{ put(VKApiWall.EXTENDED,1); }}).executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                newsAdapter = new NewsAdapter(getActivity(), (VKList<VKApiPost>) response.parsedModel);
                listView.setAdapter(newsAdapter);
            }
        });


        return rootView;
    }
}
