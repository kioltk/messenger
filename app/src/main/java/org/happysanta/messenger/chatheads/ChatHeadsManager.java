package org.happysanta.messenger.chatheads;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.happysanta.messenger.MessengerApplication;
import org.happysanta.messenger.R;

/**
 * Created by Jesus Christ. Amen.
 */
public class ChatHeadsManager {
    // handlers
    private static MessengerApplication application;
    private static WindowManager windowManager;
    private static Handler mAnimationHandler = new Handler();

    // sizes
    private static int chatHeadSize;
    private static int chatHeadMargin;
    private static int screenWidth;
    private static ChatHeadsManager instance;
    private static RelativeLayout backgroundView;
    private static View contentView;

    public static ChatHeadsManager getInstance() {
        return instance;
    }

    public static void init(MessengerApplication messengerApp) {
        instance = new ChatHeadsManager();
        application = messengerApp;
        windowManager = (WindowManager) application.getSystemService(Activity.WINDOW_SERVICE);
        defineSizes();
    }

    private static void defineSizes() {

        chatHeadSize = (int) application.getResources().getDimension(R.dimen.chathead_size);
        chatHeadMargin = (int) application.getResources().getDimension(R.dimen.chathead_margin);
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
    }

    public static void showChatHeads(){
        backgroundView = showBackground();
        contentView = showContent();
    }

    private static View showContent() {

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 0;
        final View layoutView =  LayoutInflater.from(application).inflate(R.layout.chathead_content, null);

        windowManager.addView(layoutView, params);
        layoutView.setAlpha(0);
        layoutView.animate().alpha(1).setDuration(100).start();
        layoutView.findViewById(R.id.container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                windowManager.removeView(layoutView);
            }
        });
        return layoutView;
    }

    public static void addChatHead() {
        final ChatHeadView chatHeadView = new ChatHeadView(application);


        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 0;
        final RelativeLayout chatHeadersHolder = showBackground();
        final TextView debugView = (TextView) chatHeadersHolder.findViewById(R.id.debug);
        View contentView = chatHeadersHolder.findViewById(R.id.container);
        contentView.setFocusableInTouchMode(true);
        contentView.setFilterTouchesWhenObscured(false);
        chatHeadView.setOnTouchListener(new View.OnTouchListener() {
            public float catchedX;
            public float catchedY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        chatHeadView.animate().scaleY(0.9f).scaleX(0.9f).setDuration(200).setInterpolator(new BounceInterpolator()).start();
                        catchedY = event.getY();
                        catchedX = event.getX();
                        // chatHeadView.startDrag()
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float x = event.getRawX() - catchedX;
                        float y = event.getRawY() - catchedY;


                        //ch
                        break;
                    case MotionEvent.ACTION_UP:
                        catchedX = 0;
                        catchedY = 0;
                        chatHeadView.animate().scaleX(1f).scaleY(1f).setDuration(200).setInterpolator(new BounceInterpolator()).start();
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        break;
                }
                return true;
            }
        });
        chatHeadView.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                return false;
            }
        });
        chatHeadersHolder.addView(chatHeadView,
                new RelativeLayout.LayoutParams(chatHeadSize, chatHeadSize){{
                    topMargin = chatHeadMargin; leftMargin = chatHeadMargin; bottomMargin = chatHeadMargin;
                }}
        );
        chatHeadView.setY(chatHeadSize * 2);
        chatHeadView.setAlpha(0);
        chatHeadView.animate().alpha(1).y(0).setDuration(250).setStartDelay(100).setInterpolator(new AccelerateDecelerateInterpolator()).start();
        //windowManager.addView(chatHeadersHolder, params);

    }


    public static RelativeLayout showBackground() {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 0;
        final RelativeLayout layoutView = (RelativeLayout) LayoutInflater.from(application).inflate(R.layout.chathead_background, null);

        windowManager.addView(layoutView, params);
        layoutView.setAlpha(0);
        layoutView.animate().alpha(1).setDuration(100).start();
        layoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*layoutView.animate().alpha(0).setDuration(100).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        windowManager.removeView(layoutView);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).start();*/
                // todo collapse chatheads
                collapseChatHeads();
            }
        });
        return layoutView;
    }

    private static void collapseChatHeads() {
        windowManager.removeView(backgroundView);
        windowManager.removeView(contentView);
    }


}
