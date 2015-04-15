package org.happysanta.messenger.core;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Jesus Christ. Amen.
 */
public class BaseViewHolder extends RecyclerView.ViewHolder {
    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    protected View findViewById(int layoutId){
        return itemView.findViewById(layoutId);
    }
    protected Context getContext(){
        return itemView.getContext();
    }

    protected Resources getResources(){
        return getContext().getResources();
    }

    protected int getColor(int colorId) {
        return getResources().getColor(colorId);
    }

    public void bind(int position, Object item){}
}
