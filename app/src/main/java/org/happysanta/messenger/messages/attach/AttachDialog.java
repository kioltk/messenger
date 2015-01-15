package org.happysanta.messenger.messages.attach;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import org.happysanta.messenger.R;

import java.util.ArrayList;
import java.util.HashSet;


/**
 * Created by Jesus Christ. Amen.
 */
public class AttachDialog extends Dialog implements View.OnClickListener {

    private static final String TAG = "ATTACH_FRAG";

    private AttachListener attachListener;

    public AttachDialog(Context context) {
        super(context, R.style.dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_attach);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(params);
        /*setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });*/
        final ImageView attachPhoto = (ImageView) findViewById(R.id.attach_photo);
        final ImageView attachAudio = (ImageView) findViewById(R.id.attach_audio);
        final ImageView attachGall = (ImageView) findViewById(R.id.attach_gallery);
        final ImageView attachMap = (ImageView) findViewById(R.id.attach_map);
        final ImageView attachVideo = (ImageView) findViewById(R.id.attach_video);
        final ImageView attachFile = (ImageView) findViewById(R.id.attach_file);

        ArrayList<View> circles = new ArrayList<View>() {{

            add(attachPhoto);
            add(attachAudio);
            add(attachGall);
            add(attachMap);
            add(attachVideo);
            add(attachFile);

        }};

        for (int i = circles.size() - 1; i >= 0; i--) {

            setAnimator(circles.get(i), i);
        }

    }

    public void setAttachListener(AttachListener attachListener) {
        this.attachListener = attachListener;
    }

    private void setAnimator(final View v, final int counter) {

        v.setScaleX(0);
        v.setScaleY(0);

        int argX = 0;
        int argY = 0;

        switch (counter) {

            case 0: {

                argX = 0;
                argY = 0;
                break;
            }

            case 1: {

                argX = 0;
                argY = -335;
                break;
            }

            case 2: {

                argX = 300;
                argY = -125;
                break;
            }
            case 3: {

                argX = 200;
                argY = 250;
                break;
            }
            case 4: {

                argX = -200;
                argY = 250;
                break;
            }
            case 5: {

                argX = -300;
                argY = -125;
            }
        }

        final ObjectAnimator scaleX = ObjectAnimator.ofFloat(v, "scaleX", 1);
        final ObjectAnimator scaleY = ObjectAnimator.ofFloat(v, "scaleY", 1);

        final ObjectAnimator translationY = ObjectAnimator.ofFloat(v, "translationY", argY);
        final ObjectAnimator translationX = ObjectAnimator.ofFloat(v, "translationX", argX);

        AnimatorSet set = new AnimatorSet();

        set.setTarget(v);
        set.setStartDelay(50 * counter);
        set.setDuration(150);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.playTogether(new HashSet<Animator>() {{

            add(scaleX);
            add(scaleY);
            add(translationY);
            add(translationX);

        }});

        set.start();

        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                v.setClickable(true);
                v.setOnClickListener(AttachDialog.this);
            }
        });
    }

    @Override
    public void onClick(View v) {

        // stub

        switch (v.getId()) {

            case R.id.attach_photo: {

                Toast.makeText(getContext(), "Photo", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.attach_audio: {

                Toast.makeText(getContext(), "Audio", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.attach_gallery: {

                Toast.makeText(getContext(), "Gallery", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.attach_map: {

                Toast.makeText(getContext(), "Map", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.attach_video: {

                Toast.makeText(getContext(), "Video", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.attach_file: {

                Toast.makeText(getContext(), "File", Toast.LENGTH_SHORT).show();
                break;
            }
        }

    }
}