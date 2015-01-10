package org.happysanta.messenger.friends;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.eowise.recyclerview.stickyheaders.StickyHeadersBuilder;
import com.eowise.recyclerview.stickyheaders.StickyHeadersItemDecoration;
import com.vk.sdk.api.model.VKApiUserFull;

import org.happysanta.messenger.R;
import org.happysanta.messenger.core.BaseFragment;
import org.happysanta.messenger.friends.adapter.FriendsAdapter;
import org.happysanta.messenger.friends.adapter.LetterHeaderAdapter;
import org.happysanta.messenger.user.UserDialog;

import java.util.ArrayList;

/**
 * Created by Jesus Christ. Amen.
 */
public class FriendsListFragment extends BaseFragment implements
        FriendsAdapter.IViewHolderCallback,
        FriendListHelper.IFriendListCallback {

    private ArrayList<VKApiUserFull>        mFriendsList;
    private FriendsAdapter                  mFriendsListAdapter;
    private IFragmentTitleCallback          mCountListener;
    private boolean                         mOnline;

    private RecyclerView.LayoutManager      mLayoutManager;
    private StickyHeadersItemDecoration     mLetterStickyHeader;

    private TextView                        mStatusView;
    private ProgressBar                     mProgressBar;
    private RecyclerView                    mFriendsView;

    public static FriendsListFragment newInstance(boolean onlineOnly,
                                                  IFragmentTitleCallback listener) {

        FriendsListFragment newFragment     = new FriendsListFragment();
        Bundle              bundle          = new Bundle();


        bundle      .putBoolean("OnlineOnly", onlineOnly);

        newFragment .setArguments(bundle);
        newFragment .setListener (listener);

        return newFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_friends_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mStatusView     = (TextView)        view.findViewById(R.id.status);
        mProgressBar    = (ProgressBar)     view.findViewById(R.id.progress);
        mFriendsView    = (RecyclerView)    view.findViewById(R.id.friends_list);

        mLayoutManager  = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);

        mFriendsView.setLayoutManager(mLayoutManager);

        mOnline = getArguments().getBoolean("OnlineOnly");

        FriendListHelper helper = new FriendListHelper(mOnline, this);

        helper.doRequest();
    }

    @Override
    public void onItemClick(int position) {
        new UserDialog(getActivity(), mFriendsList.get(position)).show();
    }

    private void setListener(IFragmentTitleCallback countListener) {

        mCountListener = countListener;
    }

    //region Helper callback
    @Override
    public void onResult(ArrayList<VKApiUserFull> friends) {

        mFriendsList = friends;

        if (mCountListener != null)
            mCountListener.setFriendsCountTitle(mFriendsList.size(), (mOnline ? 1 : 0));

        mFriendsListAdapter = new FriendsAdapter(mFriendsList,
                FriendsListFragment.this);

        if (mFriendsList.size() > 10 && !mOnline) {

            mLetterStickyHeader = new StickyHeadersBuilder()
                    .setAdapter(mFriendsListAdapter)
                    .setRecyclerView(mFriendsView)
                    .setStickyHeadersAdapter(
                            new LetterHeaderAdapter(mFriendsList), true)
                    .build();

            mFriendsView.addItemDecoration(mLetterStickyHeader);
        }

        mFriendsView.setAdapter(mFriendsListAdapter);

        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onEmptyList(boolean online) {

        mStatusView .setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onError(String error) {

        mStatusView .setText        (error);
        mProgressBar.setVisibility  (View.GONE);
    }
    //endregion

}
