package org.happysanta.messenger.user;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.methods.VKApiUsers;
import com.vk.sdk.api.model.VKApiCity;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;

import org.happysanta.messenger.R;
import org.happysanta.messenger.core.BaseActivity;
import org.happysanta.messenger.core.util.BitmapUtil;
import org.happysanta.messenger.core.util.ImageUtil;

/**
 * Created by Jesus Christ. Amen.
 */
public class UserDialog {

    private final AlertDialog userDialog;
    private View dialogView;
    private int userId;

    public UserDialog(Activity context) {
        dialogView = View.inflate(context, R.layout.dialog_friend, null);



        userDialog = new AlertDialog.Builder(context)
                .setView(dialogView)
                .create();
        userDialog.setCanceledOnTouchOutside(true);
    }

    public UserDialog(Activity activity, VKApiUserFull user) {
        this(activity);
        showUser(user);
    }

    public UserDialog(Activity activity, final int userId) {
        this(activity);
        dialogView.findViewById(R.id.loading);
        dialogView.findViewById(R.id.content_holder).setVisibility(View.GONE);
        new VKApiUsers().get(new VKParameters(){{
            put("user_ids", userId);
            put("fields","photo_200,city,activity,last_seen,counters,bdate");
        }}).executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {

                VKList<VKApiUserFull> users = (VKList<VKApiUserFull>) response.parsedModel;
                VKApiUserFull currentUser = users.get(0);

                showUser(currentUser);
            }
        });
    }

    private void showUser(VKApiUserFull user) {
        setUserName(user.toString());
        setUserId(user.id);
        setOnline(user.online);
        setPhoto(user.getPhoto());
        setCounters(user.counters);
        setCity(user.city);
        dialogView.findViewById(R.id.loading).setVisibility(View.GONE);
        dialogView.findViewById(R.id.content_holder).setVisibility(View.VISIBLE);
    }

    private void setCity(VKApiCity city) {
        if(city==null){
            findViewById(R.id.city_holder).setVisibility(View.GONE);
        }else{
            ((TextView)findViewById(R.id.city)).setText(city.title);
        }
    }

    private void setCounters(VKApiUserFull.Counters counters) {
        View view = findViewById(R.id.friends_list);
    }


    private void setPhoto(String photo) {
        final ImageView photoView = (ImageView) dialogView.findViewById(R.id.user_photo);
        ImageUtil.showFromCache(new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                photoView.setImageBitmap(BitmapUtil.circle(loadedImage));
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        }, photo);
    }

    public void setUserName(String userName) {
        TextView nameView = (TextView) dialogView.findViewById(R.id.text_name);
        nameView.setText(userName);
    }


    public void setUserId(int id) {
        this.userId = id;
    }

    public void setOnline(boolean online) {
        TextView statusView = (TextView) dialogView.findViewById(R.id.status);
        String onlineText;
        if(online){
            onlineText = "online";
        }else{
            onlineText = "offline";
        }
        statusView.setText(onlineText);
    }

    public void show() {
        userDialog.show();
    }
    private View findViewById(int resId) {
        return dialogView.findViewById(resId);
    }
    public void setLastSeen(long time) {

        TextView statusView = (TextView) dialogView.findViewById(R.id.status);
        statusView.setVisibility(View.GONE);
    }
}
