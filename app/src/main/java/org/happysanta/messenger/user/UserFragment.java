package org.happysanta.messenger.user;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.methods.VKApiUsers;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;

import org.happysanta.messenger.R;
import org.happysanta.messenger.core.BaseFragment;
import org.happysanta.messenger.core.util.BitmapUtil;
import org.happysanta.messenger.core.util.ImageUtil;

import java.util.ArrayList;

/**
 * Created by Jesus Christ. Amen.
 */
public class UserFragment extends BaseFragment {
    private View rootView;
    private ArrayList<Object> updates = new ArrayList<Object>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        final TextView usernameView = (TextView) rootView.findViewById(R.id.username);

        TextView statusView = (TextView) rootView.findViewById(R.id.status);

        final ImageView imageView = (ImageView) rootView.findViewById(R.id.user_photo);

        imageView.setImageBitmap(BitmapUtil.circle(R.drawable.user_placeholder));
        // todo это всё нужно делать заранее на экране загрузки
        new VKApiUsers().get().executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onError(VKError error) {
                super.onError(error);

                usernameView.setText(error.toString());

            }

            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                try {
                    VKList<VKApiUserFull> users = (VKList<VKApiUserFull>) response.parsedModel;
                    VKApiUserFull currentUser = users.get(0);
                    usernameView.setText(currentUser.toString());
                    ImageUtil.showFromCache(currentUser.getPhoto(), new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {

                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            imageView.setImageBitmap(BitmapUtil.circle(loadedImage));
                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {

                        }
                    });
                }catch (Exception exp){
                    usernameView.setText(exp.getMessage());
                }
            }
        });





        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_profile, menu);
    }
}
