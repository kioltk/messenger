package org.happysanta.messenger.sandbox;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;

import org.happysanta.messenger.R;
import org.happysanta.messenger.core.BaseFragment;
/**
 * Created by Jesus Christ. Amen.
 */
public class SandboxFragment extends BaseFragment {

    private SpringSystem springSystem;
    private Spring scaleAnimationSpring;
    private View layer;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_sandbox, null);

        layer = findViewById(R.id.pic);
        layer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        scaleAnimationSpring.setEndValue(2);
                        return true;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        scaleAnimationSpring.setEndValue(1);
                        return true;
                }
                return false;
            }
        });

        springSystem = SpringSystem.create();

        scaleAnimationSpring = springSystem.createSpring()
                .setSpringConfig(SpringConfig.fromBouncinessAndSpeed(15, 10))
                .addListener(new SimpleSpringListener() {
                    @Override
                    public void onSpringUpdate(Spring spring) {
                        setPopAnimationProgress((float) spring.getCurrentValue());
                    }

                });

        return rootView;
    }
    public void popAnimation(boolean on) {
        scaleAnimationSpring.setEndValue(on ? 1 : 0);
    }

    public void setPopAnimationProgress(float progress) {
        layer.setScaleX(progress);
        layer.setScaleY(progress);
    }
}


