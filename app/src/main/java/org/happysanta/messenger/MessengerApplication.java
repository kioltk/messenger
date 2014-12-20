package org.happysanta.messenger;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKSdkListener;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKError;

import org.happysanta.messenger.main.MainActivity;
import org.happysanta.messenger.start.StartActivity;

/**
 * Created by Jesus Christ. Amen.
 */
public class MessengerApplication extends Application implements VKSdkListener {

    @Override
    public void onCreate() {
        super.onCreate();
        VKSdk.initialize(this, "4486133");
        ImageLoader.getInstance().init(new ImageLoaderConfiguration.Builder(this).build());
    }


    @Override
    public void onCaptchaError(VKError captchaError) {

    }

    @Override
    public void onTokenExpired(VKAccessToken expiredToken) {

    }

    @Override
    public void onAccessDenied(VKError authorizationError) {

    }

    @Override
    public void onReceiveNewToken(VKAccessToken newToken) {
        Log.d("VKSDK", "ReceiveNewToken");
        newToken.saveTokenToSharedPreferences(getApplicationContext(),VKSdk.VK_SDK_ACCESS_TOKEN_PREF_KEY);

    }

    @Override
    public void onAcceptUserToken(VKAccessToken token) {
        Log.d("VKSDK", "AcceptUserToken");
        token.saveTokenToSharedPreferences(getApplicationContext(),VKAccessToken.PREFERENCES_KEY_TOKEN);
    }

    @Override
    public void onRenewAccessToken(VKAccessToken token) {
        Log.d("VKSDK", "RenewAccessToken");
        token.saveTokenToSharedPreferences(getApplicationContext(),VKAccessToken.PREFERENCES_KEY_TOKEN);

    }
}
