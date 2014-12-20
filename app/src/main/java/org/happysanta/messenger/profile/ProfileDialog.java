package org.happysanta.messenger.profile;

import android.app.Activity;
import android.app.AlertDialog;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.vk.sdk.api.model.VKApiUserFull;

import org.happysanta.messenger.R;

/**
 * Created by Jesus Christ. Amen.
 */
public class ProfileDialog {

    private final AlertDialog userDialog;
    private View dialogView;
    private int userId;

    public ProfileDialog(Activity context) {
        dialogView = View.inflate(context, R.layout.dialog_friend, null);



        userDialog = new AlertDialog.Builder(context)
                .setView(dialogView)
                .create();
        userDialog.setCanceledOnTouchOutside(true);
    }

    public ProfileDialog(Activity activity, VKApiUserFull user) {
        this(activity);
        setUserName(user.toString());
        setUserId(user.id);
        setOnline(user.online);
        setPhoto(user.photo_50);
    }

    private void setPhoto(String photo) {
        ImageView photoView = (ImageView) dialogView.findViewById(R.id.user_photo);
        ImageLoader.getInstance().displayImage(photo,photoView);
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

    public void disableOffline() {

        TextView statusView = (TextView) dialogView.findViewById(R.id.status);
        statusView.setVisibility(View.GONE);
    }
}
