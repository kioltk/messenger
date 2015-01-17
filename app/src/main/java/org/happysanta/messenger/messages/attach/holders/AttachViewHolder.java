package org.happysanta.messenger.messages.attach.holders;

import android.view.View;
import android.widget.TextView;

import com.vk.sdk.api.model.VKAttachments;

import org.happysanta.messenger.R;
import org.happysanta.messenger.core.BaseViewHolder;
import org.happysanta.messenger.messages.attach.RemoveListener;

/**
 * Created by Jesus Christ. Amen.
 */
public class AttachViewHolder<AttachType extends VKAttachments.VKApiAttachment> extends BaseViewHolder {
    private final RemoveListener removeListener;

    public AttachViewHolder(View itemView, RemoveListener removeListener) {
        super(itemView);
        this.removeListener = removeListener;
    }

    public void bindData(AttachType attach) {
        TextView bodyView = (TextView) findViewById(R.id.body);
        if (bodyView != null) {
            bodyView.setText(attach.toAttachmentString());
        }
    }


    protected void remove() {
        removeListener.onRemove(getPosition());
    }
}
