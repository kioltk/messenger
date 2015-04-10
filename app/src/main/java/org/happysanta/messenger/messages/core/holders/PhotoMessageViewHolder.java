package org.happysanta.messenger.messages.core.holders;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.vk.sdk.api.model.VKApiMessage;
import com.vk.sdk.api.model.VKApiPhoto;
import com.vk.sdk.api.model.VKApiUserFull;

import org.happysanta.messenger.R;
import org.happysanta.messenger.core.util.BitmapUtil;
import org.happysanta.messenger.core.util.ImageUtil;

/**
 * Created by Jesus Christ. Amen.
 */
public class PhotoMessageViewHolder extends MessageViewHolder {
    private final ImageView ownerView;
    private final ImageView photoView;

    public PhotoMessageViewHolder(View itemView) {
        super(itemView);
        ownerView = (ImageView) findViewById(R.id.owner);
        photoView = (ImageView) itemView.findViewById(R.id.photo);
    }

    @Override
    public void bindData(VKApiMessage message) {
        super.bindData(message);
        VKApiPhoto photoAttach = (VKApiPhoto) message.attachments.get(0);

        ImageLoader.getInstance().displayImage(photoAttach.photo_604, photoView, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                photoView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });

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
