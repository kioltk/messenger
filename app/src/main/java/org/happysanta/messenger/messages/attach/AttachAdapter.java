package org.happysanta.messenger.messages.attach;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.vk.sdk.api.model.VKApiDocument;
import com.vk.sdk.api.model.VKAttachments;

import org.happysanta.messenger.R;
import org.happysanta.messenger.messages.attach.holders.AttachViewHolder;
import org.happysanta.messenger.messages.attach.holders.DocumentAttachViewHolder;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Jesus Christ. Amen.
 */
public class AttachAdapter extends RecyclerView.Adapter<AttachViewHolder> implements RemoveListener {
    private final ArrayList<VKAttachments.VKApiAttachment> attaches;
    private AttachCountListener countListener;

    public AttachAdapter(ArrayList<VKAttachments.VKApiAttachment> attaches) {
        this.attaches = attaches;
    }

    @Override
    public int getItemViewType(int position) {
        VKAttachments.VKApiAttachment attach = attaches.get(position);
        if(attach instanceof VKApiDocument){
            return AttachViewType.Document;
        }
        return 0;
    }

    @Override
    public AttachViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case AttachViewType.Document:
                return new DocumentAttachViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attach_document, null), this);
        }
        return new AttachViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attach_unknown,null),this);
    }

    @Override
    public void onBindViewHolder(AttachViewHolder holder, int position) {
        VKAttachments.VKApiAttachment attach = attaches.get(position);
        holder.bindData(attach);
    }

    @Override
    public int getItemCount() {
        return attaches.size();
    }

    public void onFileAttached(File... files) {
        int rangeStart = attaches.size();
        for (File file : files) {
            attaches.add(new VKApiDocument());
        }
        notifyItemRangeInserted(rangeStart, files.length);
        notifyCount();
    }

    private void notifyCount() {
        countListener.onCountChanged(attaches.size());
    }

    public void onPictureAttached(File... pictureFiles) {
        // Toast.makeText(activity, pictureFile[0].getAbsolutePath(), Toast.LENGTH_SHORT).show();
    }

    public void onVideoAttached(File... videoFiles) {
        // Toast.makeText(activity, videoFile[0].getAbsolutePath(), Toast.LENGTH_SHORT).show();
    }

    public void onAudioAttached(File... audioFiles) {
        // Toast.makeText(activity, audioFile[0].getAbsolutePath(), Toast.LENGTH_SHORT).show();
    }


    public void setCountListener(AttachCountListener attachCountListener) {
        this.countListener = attachCountListener;
    }

    @Override
    public void onRemove(int position) {
        attaches.remove(position);
        notifyItemRemoved(position);
        notifyCount();
    }
}
