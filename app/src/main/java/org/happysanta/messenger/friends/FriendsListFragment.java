package org.happysanta.messenger.friends;

import android.os.Bundle;
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
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.methods.VKApiFriends;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;

import org.happysanta.messenger.R;
import org.happysanta.messenger.core.BaseFragment;
import org.happysanta.messenger.friends.adapter.FriendsAdapter;
import org.happysanta.messenger.friends.adapter.LetterHeaderAdapter;
import org.happysanta.messenger.user.UserDialog;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Jesus Christ. Amen.
 */
public class FriendsListFragment extends BaseFragment implements
        FriendsAdapter.IViewHolderCallback {

    private static final String ARG_ONLINE_ONLY = "online_only";
    private VKList<VKApiUserFull>        mFriendsList;
    private FriendsAdapter                  mFriendsListAdapter;
    private IFragmentTitleCallback          mCountListener;
    private boolean mOnlinesOnly;

    private RecyclerView.LayoutManager      mLayoutManager;
    private StickyHeadersItemDecoration     mLetterStickyHeader;

    private TextView                        mStatusView;
    private ProgressBar                     mProgressBar;
    private RecyclerView                    mFriendsView;

    public static FriendsListFragment newInstance(boolean onlineOnly,
                                                  IFragmentTitleCallback listener) {

        FriendsListFragment newFragment     = new FriendsListFragment();
        Bundle              bundle          = new Bundle();


        bundle      .putBoolean(ARG_ONLINE_ONLY, onlineOnly);

        newFragment .setArguments(bundle);
        newFragment .setListener (listener);

        return newFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_friends_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mStatusView     = (TextView)        view.findViewById(R.id.status);
        mProgressBar    = (ProgressBar)     view.findViewById(R.id.progress);
        mFriendsView    = (RecyclerView)    view.findViewById(R.id.friends_list);

        mLayoutManager  = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);

        mFriendsView.setLayoutManager(mLayoutManager);

        mOnlinesOnly = getArguments().getBoolean(ARG_ONLINE_ONLY);

        // FriendListHelper helper = new FriendListHelper(mOnlinesOnly, this);
        // helper.doRequest();
        VKParameters params = new VKParameters() {{
            put("fields", "name,last_name,age,photo_200");
            put("order", "hints");
        }};
        VKRequest request = mOnlinesOnly ? new VKApiFriends().getOnline(params) : new VKApiFriends().get(params);
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                mFriendsList = (VKList<VKApiUserFull>) response.parsedModel;

                if(mFriendsList.isEmpty()){

                    if (mOnlinesOnly) {
                        mStatusView.setText(R.string.friends_onlines_none);
                    } else {
                        mStatusView.setText(R.string.friends_empty);
                    }
                    mStatusView .setVisibility(View.VISIBLE);
                    mProgressBar.setVisibility(View.GONE);

                }else {
                    sort(mFriendsList);
                    if (mCountListener != null)
                        mCountListener.setFriendsCountTitle(mFriendsList.size(), (mOnlinesOnly ? 1 : 0));

                    mFriendsListAdapter = new FriendsAdapter(mFriendsList,
                            FriendsListFragment.this);

                    if (mFriendsList.size() > 10) {

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
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);


                mStatusView .setText        (error.errorMessage);
                mProgressBar.setVisibility  (View.GONE);
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        new UserDialog(getActivity(), mFriendsList.get(position).id).show();
    }

    private void setListener(IFragmentTitleCallback countListener) {

        mCountListener = countListener;
    }

    void sort(List<VKApiUserFull> list) {
        if (null == list)       return;
        if (list.size() <= 10)    return;
        if (list.size() > 10)   list = list.subList(5, list.size());
        Collections.sort(list, new Comparator<VKApiUserFull>() {
            @Override
            public int compare(VKApiUserFull lhs, VKApiUserFull rhs) {
                return lhs.first_name.compareTo(rhs.first_name);
            }
        });
    }
    //endregion

}
