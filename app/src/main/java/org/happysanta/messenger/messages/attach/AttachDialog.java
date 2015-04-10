package org.happysanta.messenger.messages.attach;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.droidkit.pickers.Intents;

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

    private final Activity activity;


    private ArrayList<View> mNames = new ArrayList<>();
    private ArrayList<View> mActions = new ArrayList<>();

    private boolean mAnimationComplete = false;

    public AttachDialog(Activity activity) {
        super(activity, R.style.dialog);
        this.activity = activity;
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

        View cancelButton = findViewById(R.id.cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO implement animated close for incomplete state
                if (!mAnimationComplete) {

                    cancel();
                    return;
                }

                int j = 0;

                for (int i = mActions.size() - 1 ; i >= 0 ; i--, j++) {

                    setAnimator(mActions.get(i), j, true);
                }
            }
        });

        cancelButton.setRotation(90);
        cancelButton.setAlpha(0);
        cancelButton.animate().rotation(0).setDuration(350).alpha(1).start();


        mActions = new ArrayList<View>() {{

            add(findViewById(R.id.attach_photo));
            add(findViewById(R.id.attach_audio));
            add(findViewById(R.id.attach_gallery));
            add(findViewById(R.id.attach_video));
            add(findViewById(R.id.attach_map));
            add(findViewById(R.id.attach_file));

        }};

        mNames = new ArrayList<View>() {{

            add(findViewById(R.id.attach_photo_name));
            add(findViewById(R.id.attach_audio_name));
            add(findViewById(R.id.attach_gallery_name));
            add(findViewById(R.id.attach_video_name));
            add(findViewById(R.id.attach_map_name));
            add(findViewById(R.id.attach_file_name));

        }};

        for (int i = 0; i < mActions.size(); i++) {

            setAnimator(mActions.get(i), i, false);
        }

    }


    private void setAnimator(final View button, final int counter, final boolean close) {

        if (!close) {

            button.setScaleX(0);
            button.setScaleY(0);
        }

        int xPos = 0;
        int yPos = 0;

        switch (counter) {

            case PHOTO: {

                xPos = 0;
                yPos = 0;
                break;
            }

            case AUDIO: {

                xPos = 0;
                yPos = Dimen.get(R.dimen.picker_coordY_audio);
                break;
            }

            case GALLERY: {

                xPos = Dimen.get(R.dimen.picker_coordX_gallery);
                yPos = Dimen.get(R.dimen.picker_coordY_gallery);
                break;
            }
            case VIDEO: {

                xPos = Dimen.get(R.dimen.picker_coordX_video);
                yPos = Dimen.get(R.dimen.picker_coordY_video);
                break;
            }
            case MAP: {

                xPos = Dimen.get(R.dimen.picker_coordX_map);
                yPos = Dimen.get(R.dimen.picker_coordY_map);
                break;
            }
            case FILE: {

                xPos = Dimen.get(R.dimen.picker_coordX_file);
                yPos = Dimen.get(R.dimen.picker_coordY_file);
            }
        }

        final ObjectAnimator scaleX = ObjectAnimator.ofFloat(button, "scaleX", close ? 0 : 1);
        final ObjectAnimator scaleY = ObjectAnimator.ofFloat(button, "scaleY", close ? 0 : 1);

        final ObjectAnimator translationY = ObjectAnimator.ofFloat(
                button,
                "translationY",
                close ? 0 : yPos
        );

        final ObjectAnimator translationX = ObjectAnimator.ofFloat(
                button,
                "translationX",
                close ? 0 : xPos
        );

        AnimatorSet set = new AnimatorSet();

        set.setStartDelay(67 * counter);
        set.setDuration(100);
        set.setInterpolator(new DecelerateInterpolator());
        set.playTogether(new HashSet<Animator>() {{

            add(scaleX);
            add(scaleY);
            add(translationY);
            add(translationX);

        }});

        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                if (!close) {

                    if (counter > 4) mAnimationComplete = true;

                    button.setClickable(true);
                    button.setOnClickListener(AttachDialog.this);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                        StateListAnimator onClickAnimator = AnimatorInflater
                                .loadStateListAnimator(
                                        getContext(),
                                        R.anim.attach_button_state_animator
                                );

                        button.setStateListAnimator(onClickAnimator);
                    }

                    setTextAnimator(mNames.get(counter), button, counter, false);

                } else {

                    button.setVisibility(View.GONE);

                    if (counter == 5) cancel();

                }
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);

                if (close) {

                    int revCounter = mActions.indexOf(button);

                    setTextAnimator(mNames.get(revCounter), button, counter, true);
                }
            }
        });

        set.start();
    }

    private void setTextAnimator(final View text, View button, final int counter,
                                 final boolean close) {

        if (!close) {

            text.setVisibility(View.VISIBLE);

            float centerX = Dimen.get(R.dimen.picker_icon_size)/2;
            float xPos = button.getX() + (centerX - text.getWidth()/2);
            float yPos = button.getY() + Dimen.get(R.dimen.picker_icon_size)
                    + text.getHeight()/3;

            text.setScaleX(0);
            text.setScaleY(0);
            text.setAlpha(0);

            text.setX(xPos);
            text.setY(yPos);
        }

        final ObjectAnimator scaleX = ObjectAnimator.ofFloat(text, "scaleX", close ? 0 : 1);
        final ObjectAnimator scaleY = ObjectAnimator.ofFloat(text, "scaleY", close ? 0 : 1);
        final ObjectAnimator alpha  = ObjectAnimator.ofFloat(text, "alpha",  close ? 0 : 0.7F);

        AnimatorSet set = new AnimatorSet();

        if (close) set.setStartDelay(53 * counter);
        set.setDuration(100);
        set.setInterpolator(new DecelerateInterpolator());
        set.playTogether(new HashSet<Animator>() {{

            add(scaleX);
            add(scaleY);
            add(alpha);

        }});

        set.start();
    }

    @Override
    public void onClick(View v) {

        // stub

        switch (v.getId()) {

            case R.id.attach_photo: {

                Toast.makeText(getContext(), "Photo", Toast.LENGTH_SHORT).show();
                /*
                activity.startActivityForResult(Intents.pickPhoto(getContext()), AttachRequestCode.PHOTO);
                dismiss();
                */
                break;
            }
            case R.id.attach_audio: {

                Toast.makeText(getContext(), "Audio", Toast.LENGTH_SHORT).show();
                /*
                activity.startActivityForResult(Intents.pickAudio(getContext()), AttachRequestCode.AUDIO);
                dismiss();
                */
                break;
            }
            case R.id.attach_gallery: {

                Toast.makeText(getContext(), "Gallery", Toast.LENGTH_SHORT).show();
                /*
                activity.startActivityForResult(Intents.pickGallery(getContext()), AttachRequestCode.GALLERY);
                dismiss();
                */
                break;
            }
            case R.id.attach_map: {
                activity.startActivityForResult(Intents.pickLocation(getContext()), AttachRequestCode.MAP);
                dismiss();
                break;
            }
            case R.id.attach_video: {

                Toast.makeText(getContext(), "Video", Toast.LENGTH_SHORT).show();
                /*
                activity.startActivityForResult(Intents.pickVideo(getContext()), AttachRequestCode.VIDEO);
                dismiss();
                */
                break;
            }
            case R.id.attach_file: {
                activity.startActivityForResult(Intents.pickFile(getContext()), AttachRequestCode.FILE);
                dismiss();
                break;
            }
        }

    }

    @Override
    public void onBackPressed() {

        if (!mAnimationComplete) {

            super.onBackPressed();
            return;
        }

        int j = 0;

        for (int i = mActions.size() - 1 ; i >= 0 ; i--, j++) {

            setAnimator(mActions.get(i), j, true);
        }
    }



}