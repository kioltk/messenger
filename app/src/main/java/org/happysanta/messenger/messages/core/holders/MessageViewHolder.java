package org.happysanta.messenger.messages.core.holders;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.vk.sdk.api.model.VKApiMessage;
import com.vk.sdk.api.model.VKApiUserFull;

import org.happysanta.messenger.R;
import org.happysanta.messenger.core.BaseViewHolder;
import org.happysanta.messenger.core.util.BitmapUtil;
import org.happysanta.messenger.core.util.ImageUtil;

/**
 * Created by Jesus Christ. Amen.
 */
public class MessageViewHolder extends BaseViewHolder {
    public MessageViewHolder(View itemView) {
        super(itemView);
    }


    public void bindData(VKApiMessage message) {
        TextView bodyView = (TextView) findViewById(R.id.body);
        if(bodyView!=null)
            bodyView.setText(message.getBody());
    }



    public void groupTop() {

    }

    public void ungroupTop() {

    }

    public void groupBottom() {

    }

    public void ungroupBottom() {

    }

    public void showOwner(VKApiUserFull owner) {
        // todo margin right?

        final ImageView ownerView = (ImageView) findViewById(R.id.owner);
        if(ownerView!=null) {
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
    public void hideOwner(){
        final ImageView ownerView = (ImageView) findViewById(R.id.owner);
        if(ownerView!=null){
            ownerView.setVisibility(View.GONE);
        }
    }
}
