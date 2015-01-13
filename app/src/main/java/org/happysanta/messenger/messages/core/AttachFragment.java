package org.happysanta.messenger.messages.core;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import org.happysanta.messenger.R;
import org.happysanta.messenger.core.BaseFragment;

import java.util.ArrayList;
import java.util.HashSet;


/**
 * Created by Jesus Christ. Amen.
 */
public class AttachFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "ATTACH_FRAG";

    private AttachListener attachListener;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_attach, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final ImageView attachPhoto  = (ImageView) view.findViewById(R.id.attach_photo);
        final ImageView attachAudio  = (ImageView) view.findViewById(R.id.attach_audio);
        final ImageView attachGall   = (ImageView) view.findViewById(R.id.attach_gallery);
        final ImageView attachMap    = (ImageView) view.findViewById(R.id.attach_map);
        final ImageView attachVideo  = (ImageView) view.findViewById(R.id.attach_video);
        final ImageView attachFile   = (ImageView) view.findViewById(R.id.attach_file);

        ArrayList<View> circles = new ArrayList<View>() {{

            add(attachPhoto);
            add(attachAudio);
            add(attachGall);
            add(attachMap);
            add(attachVideo);
            add(attachFile);

        }};

        for (int i = circles.size()-1 ; i >= 0 ; i--) {

            setAnimator(circles.get(i), i);
        }

    }

    // attaches here

    public static AttachFragment getInstance() {
        AttachFragment fragment = new AttachFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
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
                v.setOnClickListener(AttachFragment.this);
            }
        });
    }

    @Override
    public void onClick(View v) {

        // stub

        switch (v.getId()) {

            case R.id.attach_photo: {

                Toast.makeText(getActivity(), "Photo", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.attach_audio: {

                Toast.makeText(getActivity(), "Audio", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.attach_gallery: {

                Toast.makeText(getActivity(), "Gallery", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.attach_map: {

                Toast.makeText(getActivity(), "Map", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.attach_video: {

                Toast.makeText(getActivity(), "Video", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.attach_file: {

                Toast.makeText(getActivity(), "File", Toast.LENGTH_SHORT).show();
                break;
            }
        }

    }
}
