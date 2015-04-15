package org.happysanta.messenger.posts;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.vk.sdk.api.model.VKApiPost;
import com.vk.sdk.api.model.VKList;

import org.happysanta.messenger.R;

/**
 * Created by insidefun on 01.01.2015.
 */
public class PostsAdapter extends RecyclerView.Adapter<PostHolder> {
    private final Activity activity;
    private VKList<VKApiPost> postsList;




    public PostsAdapter(Activity activity, VKList<VKApiPost> postsList) {
        this.activity = activity;
        this.postsList = postsList;
    }

    public VKApiPost getItem(int position) { return postsList.get(position); }

    @Override
    public PostHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PostHolder(LayoutInflater.from(activity).inflate(R.layout.item_post, null));
    }

    @Override
    public void onBindViewHolder(PostHolder holder, int position) {
        holder.bind(position, getItem(position));
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }


}
