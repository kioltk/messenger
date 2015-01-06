package org.happysanta.messenger.news;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.model.VKApiPost;
import com.vk.sdk.api.model.VKList;

import org.happysanta.messenger.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jesus Christ. Amen.
 */
public class NewsListFragment extends Fragment {

    // core
    private VKList<VKApiPost> newsList;

    // ui
    private View cardView;
    private ListView listView;
    private NewsAdapter newsAdapter;

    public NewsListFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        cardView = inflater.inflate(R.layout.item_news, container, false);
        listView = (ListView) getActivity().findViewById(R.id.list_news);

        newsAdapter = new NewsAdapter(getActivity(), newsList);
        //listView.setAdapter(newsAdapter); | NullPointerException

        return cardView;
    }
}
