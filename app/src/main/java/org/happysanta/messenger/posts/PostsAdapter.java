package org.happysanta.messenger.posts;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.vk.sdk.api.model.VKApiPost;
import com.vk.sdk.api.model.VKPostArray;

import org.happysanta.messenger.R;

/**
 * Created by insidefun on 01.01.2015.
 */
public class PostsAdapter extends RecyclerView.Adapter<PostHolder> {
    private final Activity activity;
    private VKPostArray postsList;




    public PostsAdapter(Activity activity, VKPostArray postsList) {
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
        VKApiPost post = postsList.get(position);
        holder.bind(position, post);
        if(post.from_id>0){
            holder.bindOwner(postsList.usersOwners.getById(post.from_id));
        } else {
            holder.bindOwner(postsList.groupsOwners.getById(-post.from_id));
        }
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
