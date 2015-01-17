package org.happysanta.messenger.messages.attach.holders;

import android.view.View;
import android.widget.TextView;

import com.vk.sdk.api.model.VKAttachments;

import org.happysanta.messenger.R;
import org.happysanta.messenger.core.BaseViewHolder;

/**
 * Created by Jesus Christ. Amen.
 */
public class AttachViewHolder<AttachType extends VKAttachments.VKApiAttachment> extends BaseViewHolder {
    public AttachViewHolder(View itemView) {
        super(itemView);
    }

    public void bindData(AttachType attach) {
        TextView bodyView = (TextView) findViewById(R.id.body);
        if (bodyView != null) {
            bodyView.setText(attach.toAttachmentString());
        }
    }
}
