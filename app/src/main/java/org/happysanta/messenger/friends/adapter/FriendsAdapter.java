package org.happysanta.messenger.friends.adapter;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.vk.sdk.api.model.VKApiUserFull;

import org.happysanta.messenger.R;
import org.happysanta.messenger.core.util.BitmapUtil;
import org.happysanta.messenger.core.util.ImageUtil;

import java.util.ArrayList;

/**
 * Created by alex on 06/01/15.
 */
public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder> {

    private final ArrayList<VKApiUserFull> mFriends;

    private IViewHolderCallback            mListener;

    public FriendsAdapter(ArrayList<VKApiUserFull> friends,
                          IViewHolderCallback viewHolderListener) {

        this.mFriends   = friends;
        this.mListener  = viewHolderListener;

        setHasStableIds(true);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        public TextView            mNameView;
        public ImageView           mPhotoView;
        public IViewHolderCallback mHolderCallback;

        public ViewHolder(View itemView, IViewHolderCallback listener) {
            super(itemView);

            this.mNameView          = (TextView)  itemView.findViewById(R.id.user_name);
            this.mPhotoView         = (ImageView) itemView.findViewById(R.id.user_photo);

            this.mHolderCallback    = listener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            this.mHolderCallback.onItemClick(getPosition());
        }
    }


    public interface IViewHolderCallback {

        public void onItemClick(int position);
    }


    @Override
    public FriendsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_friend, parent, false);

        return new ViewHolder(itemView, mListener);
    }


    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {

        final VKApiUserFull user = mFriends.get(position);

        viewHolder.mNameView.setText(user.toString());
        viewHolder.mPhotoView.setImageBitmap(BitmapUtil.circle(R.drawable.user_placeholder));

        ImageUtil.showFromCache(user.getPhoto(), new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                Log.d("onLoadingFailed", "User name: " + user.toString());
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                viewHolder.mPhotoView.setImageBitmap(BitmapUtil.circle(loadedImage));
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mFriends.size();
    }

    @Override
    public long getItemId(int position) {
        return mFriends.get(position).hashCode();
    }
}
