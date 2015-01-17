package org.happysanta.messenger.messages.attach.holders;

import android.view.View;
import android.widget.TextView;

import com.vk.sdk.api.model.VKApiDocument;

import org.happysanta.messenger.R;
import org.happysanta.messenger.messages.attach.RemoveListener;

/**
 * Created by Jesus Christ. Amen.
 */
public class DocumentAttachViewHolder extends AttachViewHolder<VKApiDocument> {
    private final TextView titleView;
    private TextView subtitleView;
    private View removeView;

    public DocumentAttachViewHolder(View itemView, RemoveListener removeListener) {
        super(itemView, removeListener);
        titleView = (TextView) findViewById(R.id.title);
        subtitleView = (TextView) findViewById(R.id.subtitle);
        removeView = findViewById(R.id.remove);
    }

    @Override
    public void bindData(VKApiDocument attach) {
        titleView.setText(attach.title);
        subtitleView.setText(""+attach.size);
        removeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove();
            }
        });
    }

}
