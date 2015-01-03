package org.happysanta.messenger;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKSdkListener;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKError;

import org.happysanta.messenger.core.util.Dimen;
import org.happysanta.messenger.core.util.MapUtil;
import org.happysanta.messenger.core.util.ProfileUtil;
import org.happysanta.messenger.main.MainActivity;
import org.happysanta.messenger.messages.core.DialogUtil;
import org.happysanta.messenger.start.StartActivity;

/**
 * Created by Jesus Christ. Amen.
 */
public class MessengerApplication extends Application implements VKSdkListener {

    @Override
    public void onCreate() {
        super.onCreate();
        VKSdk.initialize(this, "4486133");
        ImageLoaderConfiguration imageLoaderConfig= new ImageLoaderConfiguration.Builder(this)
                .threadPriority(Thread.MAX_PRIORITY)
                .memoryCache(new LruMemoryCache(5 * 1024 * 1024))
                .memoryCacheSize(5 * 1024 * 1024)
                .memoryCacheSizePercentage(40)
                .diskCacheSize(100 * 1024 * 1024)
                .diskCacheFileCount(1000)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .build();
        ImageLoader.getInstance().init(imageLoaderConfig);
        Dimen.init(this);
        MapUtil.init(this);
        ProfileUtil.init(this);
        DialogUtil.init(this);
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
        ProfileUtil.setUserId(newToken.userId);

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
