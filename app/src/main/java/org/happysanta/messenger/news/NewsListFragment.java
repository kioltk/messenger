package org.happysanta.messenger.news;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.happysanta.messenger.R;

/**
 * Created by Jesus Christ. Amen.
 */
public class NewsListFragment extends Fragment {
    private View cardView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        cardView = inflater.inflate(R.layout.item_news, container, false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
