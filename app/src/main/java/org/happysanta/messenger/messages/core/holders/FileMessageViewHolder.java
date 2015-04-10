package org.happysanta.messenger.messages.core.holders;

import android.view.View;
import android.widget.TextView;

import com.vk.sdk.api.model.VKApiDocument;
import com.vk.sdk.api.model.VKApiMessage;

import org.happysanta.messenger.R;

import java.text.DecimalFormat;

/**
 * Created by Jesus Christ. Amen.
 */
public class FileMessageViewHolder extends MessageViewHolder {
    private final TextView docTitleView;
    private final TextView docSubtitleView;

    public FileMessageViewHolder(View itemView) {
        super(itemView);
        docTitleView = (TextView) itemView.findViewById(R.id.file_title);
        docSubtitleView = (TextView) itemView.findViewById(R.id.file_subtitle);
    }


    @Override
    public void bindData(VKApiMessage message) {
        VKApiDocument docAttach = (VKApiDocument) message.attachments.get(0);
        docTitleView.setText(docAttach.title);
        docSubtitleView.setText(readableFileSize(docAttach.size));
    }

    public static String readableFileSize(long size) {
        if(size <= 0) return "0";
        final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}
