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
public class AttachAdapter extends RecyclerView.Adapter<AttachViewHolder> implements AttachListener {
    private final ArrayList<VKAttachments.VKApiAttachment> attaches;

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
                return new DocumentAttachViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attach_document, null));
        }
        return new AttachViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attach_unknown,null));
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

    @Override
    public void onFileAttached(File... file) {
        attaches.add(new VKApiDocument());
        notifyItemInserted(attaches.size()-1);
    }
    @Override
    public void onPictureAttached(File... pictureFile) {
        // Toast.makeText(activity, pictureFile[0].getAbsolutePath(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onVideoAttached(File... videoFile) {
        // Toast.makeText(activity, videoFile[0].getAbsolutePath(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAudioAttached(File... audioFile) {
        // Toast.makeText(activity, audioFile[0].getAbsolutePath(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGeoAttached(AttachGeo geo) {
        // Toast.makeText(activity, geo.title, Toast.LENGTH_SHORT).show();
    }
}
