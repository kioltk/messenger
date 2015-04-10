package org.happysanta.messenger.messages.core.holders;

import android.view.View;
import android.widget.TextView;

import com.vk.sdk.api.model.VKApiAudio;
import com.vk.sdk.api.model.VKApiMessage;

import org.happysanta.messenger.R;

/**
 * Created by Jesus Christ. Amen.
 */
public class AudioMessageViewHolder extends MessageViewHolder {
    private final TextView audioTitleView;
    private final TextView audioSubtitleView;

    public AudioMessageViewHolder(View itemView) {
        super(itemView);
        audioTitleView = (TextView) itemView.findViewById(R.id.audio_title);
        audioSubtitleView = (TextView) itemView.findViewById(R.id.audio_subtitle);
    }

    @Override
    public void bindData(VKApiMessage message) {
        VKApiAudio attach = (VKApiAudio) message.attachments.get(0);
        audioTitleView.setText(attach.artist);
        audioSubtitleView.setText(attach.title);
    }
}