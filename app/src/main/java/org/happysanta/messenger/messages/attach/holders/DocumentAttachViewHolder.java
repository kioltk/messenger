package org.happysanta.messenger.messages.attach.holders;

import android.view.View;
import android.widget.TextView;

import com.vk.sdk.api.model.VKApiDocument;

import org.happysanta.messenger.R;

/**
 * Created by Jesus Christ. Amen.
 */
public class DocumentAttachViewHolder extends AttachViewHolder<VKApiDocument> {
    private final TextView titleView;
    private TextView subtitleView;

    public DocumentAttachViewHolder(View itemView) {
        super(itemView);
        titleView = (TextView) findViewById(R.id.title);
        subtitleView = (TextView) findViewById(R.id.subtitle);
    }

    @Override
    public void bindData(VKApiDocument attach) {
        titleView.setText(attach.title);
        subtitleView.setText(""+attach.size);

    }
}
