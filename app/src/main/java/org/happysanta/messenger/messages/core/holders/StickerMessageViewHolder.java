package org.happysanta.messenger.messages.core.holders;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.vk.sdk.api.model.VKApiMessage;
import com.vk.sdk.api.model.VKApiUserFull;

import org.happysanta.messenger.R;
import org.happysanta.messenger.core.util.BitmapUtil;
import org.happysanta.messenger.core.util.ImageUtil;

/**
 * Created by Jesus Christ. Amen.
 */
public class StickerMessageViewHolder extends MessageViewHolder {
    private final ImageView stickerView;
    private final ImageView ownerView;

    public StickerMessageViewHolder(View itemView) {
        super(itemView);
        stickerView = (ImageView) findViewById(R.id.sticker);
        ownerView = (ImageView) findViewById(R.id.owner);
    }

    @Override
    public void bindData(VKApiMessage message) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) stickerView.getLayoutParams();
        if (message.out) {
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        } else {
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        }
        stickerView.setLayoutParams(params);
        stickerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"PAY FOR IT!",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void showOwner(VKApiUserFull owner) {
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
                ownerView.setImageBitmap(loadedImage);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
    }
}