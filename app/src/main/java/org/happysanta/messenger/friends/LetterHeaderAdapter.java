package org.happysanta.messenger.friends;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eowise.recyclerview.stickyheaders.StickyHeadersAdapter;
import com.vk.sdk.api.model.VKApiUserFull;

import org.happysanta.messenger.R;

import java.util.ArrayList;

/**
 * Created by alex on 07/01/15.
 */
public class LetterHeaderAdapter implements StickyHeadersAdapter<LetterHeaderAdapter.ViewHolder> {

    private ArrayList<VKApiUserFull> items;

    public LetterHeaderAdapter(ArrayList<VKApiUserFull> items) {

        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_friends, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder headerViewHolder, int position) {
        headerViewHolder.letter.setText(items.get(position).toString().subSequence(0, 1));
    }

    @Override
    public long getHeaderId(int position) {
        return items.get(position).toString().charAt(0);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView letter;

        public ViewHolder(View itemView) {
            super(itemView);
            letter = (TextView) itemView.findViewById(R.id.letter_header);
        }
    }
}

