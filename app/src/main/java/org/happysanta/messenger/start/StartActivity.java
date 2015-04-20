package org.happysanta.messenger.start;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.methods.VKApiUsers;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.util.VKUtil;

import org.happysanta.messenger.R;
import org.happysanta.messenger.core.util.ProfileUtil;
import org.happysanta.messenger.main.MainActivity;

import java.util.Arrays;


public class StartActivity extends ActionBarActivity {

    private View splash;
    private Button startButton;
    private View startFictive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VKUIHelper.onCreate(this);
        setContentView(R.layout.activity_start);

        splash = findViewById(R.id.splash);
        startButton = (Button) findViewById(R.id.start);
        startFictive = findViewById(R.id.start_fictive);

        if(VKSdk.wakeUpSession(this)){
            startMainActivity(false);
            return;
        }


        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
            }
        });
        startFictive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMainActivity(true);
            }
        });

        String[] fingerprints = VKUtil.getCertificateFingerprint(this, this.getPackageName());
        Log.d("fingerprint", Arrays.toString(fingerprints));

        startButton.postDelayed(new Runnable() {
            @Override
            public void run() {
                startButton.setVisibility(View.VISIBLE);
                //splash.setVisibility(View.GONE);
            }
        }, 1500);
    }

    private void startMainActivity(boolean animateButtons) {
        if(animateButtons) {
            startButton.animate().alpha(0).translationY(100).setDuration(250).setStartDelay(100).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    startButton.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            }).start();
            startFictive.animate().alpha(0).translationY(100).setDuration(250).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    startFictive.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            }).start();
        } else {
            startButton.setVisibility(View.GONE);
            startFictive.setVisibility(View.GONE);
        }
        new VKApiUsers().access().executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onError(VKError error) {
                super.onError(error);
            }

            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                try {
                    VKApiUserFull currentUser = (VKApiUserFull) response.parsedModel;
                    if(currentUser!=null)
                        ProfileUtil.setFromUser(currentUser);
                } catch (Exception exp) {

                }
            }
        });
        splash.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        }, 500);
    }

    private void start() {
        VKSdk.authorize( new String[]{  VKScope.WALL, VKScope.STATUS, VKScope.STATS, VKScope.MESSAGES, VKScope.FRIENDS , VKScope.AUDIO, VKScope.VIDEO, VKScope.PHOTOS, VKScope.PAGES, VKScope.GROUPS }, false, true);
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
                        startMainActivity(true);
                        break;
                }
            }
            break;
        }


    }
}
