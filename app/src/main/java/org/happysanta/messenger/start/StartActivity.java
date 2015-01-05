package org.happysanta.messenger.start;

import android.app.MediaRouteButton;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKOpenAuthActivity;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.methods.VKApiUsers;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;
import com.vk.sdk.util.VKUtil;

import org.happysanta.messenger.MessengerApplication;
import org.happysanta.messenger.R;
import org.happysanta.messenger.core.util.ProfileUtil;
import org.happysanta.messenger.main.MainActivity;

import java.util.Arrays;


public class StartActivity extends ActionBarActivity {

    private View splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VKUIHelper.onCreate(this);
        setContentView(R.layout.activity_start);

        splash = findViewById(R.id.splash);

        if(VKSdk.wakeUpSession(this)){
            startMainActivity();
            return;
        }


        final Button button = (Button) findViewById(R.id.start);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
            }
        });

        String[] fingerprints = VKUtil.getCertificateFingerprint(this, this.getPackageName());
        Log.d("fingerprint", Arrays.toString(fingerprints));

        button.postDelayed(new Runnable() {
            @Override
            public void run() {
                button.setVisibility(View.VISIBLE);
                //splash.setVisibility(View.GONE);
            }
        }, 1500);
    }

    private void startMainActivity() {
        new VKApiUsers().get().executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onError(VKError error) {
                super.onError(error);
            }

            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                try {
                    VKList<VKApiUserFull> users = (VKList<VKApiUserFull>) response.parsedModel;
                    VKApiUserFull currentUser = users.get(0);
                    ProfileUtil.setFromUser(currentUser);
                }catch (Exception exp){

                }
            }
        });
        splash.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        },1500);
    }

    private void start() {
        VKSdk.authorize( new String[]{ VKScope.MESSAGES, VKScope.FRIENDS }, false, true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        VKUIHelper.onResume(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VKUIHelper.onDestroy(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        VKUIHelper.onActivityResult(this, requestCode, resultCode, data);
        switch (requestCode) {
            case VKSdk.VK_SDK_REQUEST_CODE: {
                switch (resultCode){
                    case RESULT_CANCELED:
                        break;
                    case RESULT_OK:
                        startMainActivity();
                        break;
                }
            }
            break;
        }


    }
}
