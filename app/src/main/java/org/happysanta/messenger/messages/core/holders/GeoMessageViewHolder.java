package org.happysanta.messenger.messages.core.holders;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.vk.sdk.api.model.VKApiGeo;
import com.vk.sdk.api.model.VKApiMessage;
import com.vk.sdk.api.model.VKApiUserFull;

import org.happysanta.messenger.R;
import org.happysanta.messenger.core.util.BitmapUtil;
import org.happysanta.messenger.core.util.ImageUtil;
import org.happysanta.messenger.core.util.MapUtil;

/**
 * Created by Jesus Christ. Amen.
 */
public class GeoMessageViewHolder extends MessageViewHolder {
    private final TextView titleView;
    private final ImageView mapView;
    private final ImageView ownerView;

    public GeoMessageViewHolder(View itemView) {
        super(itemView);
        mapView = (ImageView) findViewById(R.id.map);
        titleView = (TextView) findViewById(R.id.title);
        ownerView = (ImageView) findViewById(R.id.owner);
    }

    @Override
    public void bindData(VKApiMessage message) {

        final VKApiGeo geo = message.geo;
        mapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setTitle(R.string.map_show_title)
                        .setMessage(R.string.map_show_message)
                        .setPositiveButton(R.string.map_show_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                getContext().startActivity(
                                        new Intent(Intent.ACTION_VIEW, Uri.parse("geo:" + geo.lat + "," + geo.lon
                                                + "?q=" + geo.lat + "," + geo.lon + "&z=" + 10)));
                            }
                        })
                        .setNegativeButton(R.string.map_show_cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setCancelable(true).show();
            }
        });
        titleView.setText(geo.place.title);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ownerView.getLayoutParams();
        if (message.out) {
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT,0);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        } else {
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,0);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        }
        RelativeLayout.LayoutParams mapLayoutParams = (RelativeLayout.LayoutParams) mapView.getLayoutParams();
        ImageLoader.getInstance().displayImage(MapUtil.getMap(geo.lat, geo.lon, mapLayoutParams.width, mapLayoutParams.height, true, "").toString(), mapView, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                mapView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
    }
    public void showOwner(VKApiUserFull owner) {
        // todo margin right?
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
                if(loadedImage!=null)
                    ownerView.setImageBitmap(BitmapUtil.circle(loadedImage));
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });

    }
}
