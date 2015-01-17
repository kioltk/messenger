package org.happysanta.messenger.messages.core.holders;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.vk.sdk.api.model.VKApiUserFull;

import org.happysanta.messenger.R;
import org.happysanta.messenger.core.util.BitmapUtil;
import org.happysanta.messenger.core.util.ImageUtil;

/**
 * Created by Jesus Christ. Amen.
 */
public class PhotoMessageViewHolder extends MessageViewHolder {
    private final ImageView ownerView;

    public PhotoMessageViewHolder(View itemView) {
        super(itemView);
        ownerView = (ImageView) findViewById(R.id.owner);
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
