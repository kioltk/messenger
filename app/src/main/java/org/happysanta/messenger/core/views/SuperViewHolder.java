package org.happysanta.messenger.core.views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by admin on 27.03.2015.
 */
public class SuperViewHolder extends RecyclerView.ViewHolder {
    public SuperViewHolder(View itemView) {
        super(itemView);
    }

    public Context getContext(){
        return itemView.getContext();
    }
}
