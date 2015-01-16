package org.happysanta.messenger.messages.core;

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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.happysanta.messenger.R;
import org.happysanta.messenger.core.util.Dimen;

import java.util.ArrayList;
import java.util.HashSet;


/**
 * Created by Jesus Christ. Amen.
 */
public class AttachDialog extends Dialog implements View.OnClickListener {

    private static final String TAG = "ATTACH_FRAG";

    private static final int PHOTO      = 0;
    private static final int AUDIO      = 1;
    private static final int GALLERY    = 2;
    private static final int VIDEO      = 3;
    private static final int MAP        = 4;
    private static final int FILE       = 5;

    private AttachListener attachListener;

    private ImageView mAttachPhoto, mAttachAudio, mAttachGall,
            mAttachVideo, mAttachMap, mAttachFile;

    private TextView mTestText;

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

        FrameLayout rootView = (FrameLayout) findViewById(R.id.picker_dialog);

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });

        mTestText = (TextView) findViewById(R.id.picker_test_text);

        mAttachPhoto = (ImageView) findViewById(R.id.attach_photo);
        mAttachAudio = (ImageView) findViewById(R.id.attach_audio);
        mAttachGall = (ImageView) findViewById(R.id.attach_gallery);
        mAttachVideo = (ImageView) findViewById(R.id.attach_video);
        mAttachMap = (ImageView) findViewById(R.id.attach_map);
        mAttachFile = (ImageView) findViewById(R.id.attach_file);

        ArrayList<View> circles = new ArrayList<View>() {{

            add(mAttachPhoto);
            add(mAttachAudio);
            add(mAttachGall);
            add(mAttachVideo);
            add(mAttachMap);
            add(mAttachFile);

        }};

        for (int i = 0; i < circles.size(); i++) {

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

            case PHOTO: {

                argX = 0;
                argY = 0;
                break;
            }

            case AUDIO: {

                argX = 0;
                argY = Dimen.get(R.dimen.picker_coordY_audio);
                break;
            }

            case GALLERY: {

                argX = Dimen.get(R.dimen.picker_coordX_gallery);
                argY = Dimen.get(R.dimen.picker_coordY_gallery);
                break;
            }
            case VIDEO: {

                argX = Dimen.get(R.dimen.picker_coordX_video);
                argY = Dimen.get(R.dimen.picker_coordY_video);
                break;
            }
            case MAP: {

                argX = Dimen.get(R.dimen.picker_coordX_map);
                argY = Dimen.get(R.dimen.picker_coordY_map);
                break;
            }
            case FILE: {

                argX = Dimen.get(R.dimen.picker_coordX_file);
                argY = Dimen.get(R.dimen.picker_coordY_file);
            }
        }

        final ObjectAnimator scaleX = ObjectAnimator.ofFloat(v, "scaleX", 1);
        final ObjectAnimator scaleY = ObjectAnimator.ofFloat(v, "scaleY", 1);

        final ObjectAnimator translationY = ObjectAnimator.ofFloat(v, "translationY", argY);
        final ObjectAnimator translationX = ObjectAnimator.ofFloat(v, "translationX", argX);

        AnimatorSet set = new AnimatorSet();

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

    private void setTextAnimator(View text, View button, int counter) {

        // TODO implement this

//        ObjectAnimator animator = ObjectAnimator.ofInt(button, "bottom", 10, 20);

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