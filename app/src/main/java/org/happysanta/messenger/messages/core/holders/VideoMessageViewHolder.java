package org.happysanta.messenger.messages.core.holders;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.vk.sdk.api.model.VKApiMessage;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKApiVideo;

import org.happysanta.messenger.R;
import org.happysanta.messenger.core.util.BitmapUtil;
import org.happysanta.messenger.core.util.ImageUtil;
import org.happysanta.messenger.core.util.TimeUtils;

/**
 * Created by Jesus Christ. Amen.
 */
public class VideoMessageViewHolder extends MessageViewHolder {
    private final ImageView ownerView;
    private final TextView videoTitleView;
    private final TextView videoDurationView;

    public VideoMessageViewHolder(View itemView) {
        super(itemView);
        ownerView = (ImageView) findViewById(R.id.owner);
        videoTitleView = (TextView) itemView.findViewById(R.id.video_title);
        videoDurationView = (TextView) itemView.findViewById(R.id.video_duration);
    }

    @Override
    public void bindData(VKApiMessage message) {
        VKApiVideo videoAttach = (VKApiVideo) message.attachments.get(0);
        videoTitleView.setText(videoAttach.title);
        videoDurationView.setText(getDurationString(videoAttach.duration));
    }

    private String getDurationString(int seconds) {

        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;

        return TimeUtils.formatDuration(hours) + ":" + TimeUtils.formatDuration(minutes) + ":" + TimeUtils.formatDuration(seconds);
    }


    public void showOwner(VKApiUserFull owner) {
        ownerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        ownerView.setImageBitmap(BitmapUtil.circle(R.drawable.user_placeholder));
        ImageUtil.showFromCache(owner.getPhoto(), new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if (loadedImage != null)
                    ownerView.setImageBitmap(loadedImage);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });

    }
}
