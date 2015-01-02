package org.happysanta.messenger.core.util;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.File;

/**
 * Created by Jesus Christ. Amen.
 */
public class ImageUtil {

    public static void showFromCache(final ImageLoadingListener imageLoadingListener, String imageUrl) {
        if(imageUrl==null){
            return;
        }
        if (ImageLoader.getInstance().getMemoryCache().keys().contains(imageUrl)) {
            Bitmap bitmap = ImageLoader.getInstance().getMemoryCache().get(imageUrl);
            imageLoadingListener.onLoadingComplete(imageUrl, null, bitmap);
        } else {

            File imageFile = ImageLoader.getInstance().getDiskCache().get(imageUrl);
            if (imageFile!=null) {
                ImageLoader.getInstance().loadImage("file://" + imageFile.getPath(), imageLoadingListener);
            } else {
                DisplayImageOptions options = new DisplayImageOptions.Builder()
                        .cacheInMemory(true)
                        .cacheOnDisk(true)
                        .build();
                ImageLoader.getInstance().loadImage(imageUrl, options, imageLoadingListener);
            }
        }
    }
}