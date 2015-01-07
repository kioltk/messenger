package org.happysanta.messenger.friends;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.eowise.recyclerview.stickyheaders.StickyHeadersBuilder;
import com.eowise.recyclerview.stickyheaders.StickyHeadersItemDecoration;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.methods.VKApiFriends;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKUsersArray;

import org.happysanta.messenger.R;
import org.happysanta.messenger.user.UserDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Jesus Christ. Amen.
 */
public class FriendsFragment extends Fragment implements FriendsAdapter.IViewHolderCallback{

    private VKUsersArray                    mFriends;
    private ArrayList<VKApiUserFull>        mFriendsList;

    private RecyclerView                    mFriendsView;
    private FriendsAdapter mFriendsListAdapter;
    private RecyclerView.LayoutManager      mLayoutManager;
    private StickyHeadersItemDecoration     mLetterStickyHeader;

    private TextView                        mStatusView;
    private ProgressBar                     mProgressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_friends, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mFriendsView    = (RecyclerView)    view.findViewById(R.id.friends_list);
        mStatusView     = (TextView)        view.findViewById(R.id.status);
        mProgressBar    = (ProgressBar)     view.findViewById(R.id.progress);

        mLayoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);

        mFriendsView.setLayoutManager(mLayoutManager);

        new VKApiFriends().get().executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {

                mFriends = (VKUsersArray) response.parsedModel;

                if (mFriends.isEmpty()) {

                    mStatusView.setVisibility(View.VISIBLE);

                } else if (mFriends.size() < 10) {

                    sort(mFriends);

                    mFriendsList        = mFriends.toArrayList();

                    mFriendsListAdapter = new FriendsAdapter(mFriendsList, FriendsFragment.this);
                    mFriendsView.setAdapter(mFriendsListAdapter);

                } else {

                    sort(mFriends.subList(5, mFriends.size()));

                    mFriendsList        = mFriends.toArrayList();

                    mFriendsListAdapter = new FriendsAdapter(mFriendsList, FriendsFragment.this);

                    mLetterStickyHeader = new StickyHeadersBuilder()
                            .setAdapter(mFriendsListAdapter)
                            .setRecyclerView(mFriendsView)
                            .setStickyHeadersAdapter(new LetterHeaderAdapter(mFriendsList), true)
                            .build();


                    mFriendsView.setAdapter(mFriendsListAdapter);
                    mFriendsView.addItemDecoration(mLetterStickyHeader);

                }

                mProgressBar.setVisibility(View.GONE);

            }

            @Override
            public void onError(VKError error) {
                super.onError(error);

                mStatusView .setText        (error.toString());
                mProgressBar.setVisibility  (View.GONE);

            }
        });
    }

    @Override
    public void onItemClick(int position) {
        new UserDialog(getActivity(), mFriends.get(position)).show();
    }

    private void sort(List<VKApiUserFull> list) {

        Collections.sort(list, new Comparator<VKApiUserFull>() {
            @Override
            public int compare(VKApiUserFull lhs, VKApiUserFull rhs) {

                return lhs.first_name.compareTo(rhs.first_name);
            }
        });
    }
}
