package org.happysanta.messenger.friends.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eowise.recyclerview.stickyheaders.StickyHeadersAdapter;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;

import org.happysanta.messenger.R;

/**
 * Created by alex on 07/01/15.
 */
public class LetterHeaderAdapter implements StickyHeadersAdapter<LetterHeaderAdapter.ViewHolder> {

    private VKList<VKApiUserFull> items;

    public LetterHeaderAdapter(VKList<VKApiUserFull> items) {

        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_friends, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder headerViewHolder, int position) {

        if (position < 5) {

            headerViewHolder.star   .setVisibility(View.VISIBLE);
            headerViewHolder.letter .setVisibility(View.GONE);

            return;
        }

        headerViewHolder.star   .setVisibility(View.GONE);
        headerViewHolder.letter .setVisibility(View.VISIBLE);

        headerViewHolder.letter.setText(items.get(position).toString().subSequence(0, 1));
    }

    @Override
    public long getHeaderId(int position) {

        if (position > 4) {
            return items.get(position).toString().charAt(0);
        }

        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView    letter;
        ImageView   star;

        public ViewHolder(View itemView) {
            super(itemView);

            letter  = (TextView)    itemView.findViewById(R.id.letter_header);
            star    = (ImageView)   itemView.findViewById(R.id.star_header);
        }
    }
}

