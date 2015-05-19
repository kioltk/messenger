package org.happysanta.messenger.chatheads;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;

import org.happysanta.messenger.MessengerApplication;
import org.happysanta.messenger.R;

/**
 * Created by Jesus Christ. Amen.
 */
public class ChatHeadsManager {
    private static final String TAG = "ChatHeads";
    // handlers
    private static MessengerApplication application;
    private static WindowManager windowManager;
    private static Handler mAnimationHandler = new Handler();
    private static SpringSystem springSystem = SpringSystem.create();

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




    public static void addChatHead() {

        final ChatHeadView chatHeadView = new ChatHeadView(application);

        final Spring xAnimationSpring = springSystem.createSpring()
                .setSpringConfig(SpringConfig.fromBouncinessAndSpeed(10, 10))
                .addListener(new SimpleSpringListener() {
                    @Override
                    public void onSpringUpdate(Spring spring) {
                        chatHeadView.setX((float) spring.getCurrentValue());
                    }

                });

        final Spring yAnimationSpring = springSystem.createSpring()
                .setSpringConfig(SpringConfig.fromBouncinessAndSpeed(10, 10))
                .addListener(new SimpleSpringListener() {
                    @Override
                    public void onSpringUpdate(Spring spring) {
                        chatHeadView.setY((float) spring.getCurrentValue());
                    }

                });

        Spring scaleAnimationSpring = springSystem.createSpring()
                .setSpringConfig(SpringConfig.fromBouncinessAndSpeed(15, 10))
                .addListener(new SimpleSpringListener() {
                    @Override
                    public void onSpringUpdate(Spring spring) {

                    }

                });


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
            public boolean moving;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        moving = true;
                        /*scaleAnimationSpring.setEndValue(2);*/
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        if(moving){
                            xAnimationSpring.setEndValue(event.getRawX());
                            yAnimationSpring.setEndValue(event.getRawY());
                            return true;
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        moving = false;
                        /*scaleAnimationSpring.setEndValue(1);*/
                        return true;
                }
                return false;
            }
        });
        /*chatHeadView.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                return false;
            }
        });*/
        chatHeadersHolder.addView(chatHeadView,
                new RelativeLayout.LayoutParams(chatHeadSize, chatHeadSize){{
                    topMargin = chatHeadMargin; leftMargin = chatHeadMargin; bottomMargin = chatHeadMargin;
                }}
        );
        chatHeadView.setY(chatHeadSize * 2);
        chatHeadView.setAlpha(0);
        chatHeadView.animate().alpha(1).y(0).setDuration(250).setStartDelay(100).setInterpolator(new AccelerateDecelerateInterpolator()).start();
        windowManager.addView(chatHeadersHolder, params);

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
        //windowManager.removeView(contentView);
    }

    static boolean showing = false;
    static float xValue;
    static float yValue;
    static ChatHeadView mainHeadView;


    private static void hideBackground() {

    }

    public static void activateMainHead() {
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                150,
                150,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        |WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 0;
        mainHeadView = new ChatHeadView(application);

        windowManager.addView(mainHeadView, params);
        mainHeadView.setAlpha(0);
        mainHeadView.animate().alpha(1).setDuration(100).start();
        mainHeadView.setOnTouchListener(new View.OnTouchListener() {

            public boolean isInside(MotionEvent event) {
                int[] location = new int[2];
                mainHeadView.getLocationOnScreen(location);
                int chatHeadX = location[0];
                int chatHeadY = location[1];
                if (event.getX() > chatHeadX && event.getX() < chatHeadX + mainHeadView.getWidth()) {
                    if (event.getY() > chatHeadY && event.getY() < chatHeadY + mainHeadView.getHeight()) {
                        return true;
                    }
                }
                return false;
            }

            public boolean moving;
            long downTime;
            long distance;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Log.d(TAG, "Touched: " + MotionEvent.actionToString(event.getAction()));
                switch (event.getAction()) {
                    /*case MotionEvent.ACTION_OUTSIDE:
                        if (isInside(event)) {
                            params.flags = WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                                    | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
                            windowManager.updateViewLayout(chatHeadView, params);
                            moving = true;
                            toggle();
                        } else{

                        }
                        return true;*/
                    case MotionEvent.ACTION_DOWN:
                        moving = true;
                        /*distance = 0;
                        downTime = System.currentTimeMillis();*/
                        //scaleAnimationSpring.setEndValue(2);
                        return false;
                    case MotionEvent.ACTION_MOVE:
                        if (moving) {
                            xAnimationSpring.setEndValue(event.getRawX());
                            yAnimationSpring.setEndValue(event.getRawY());
                            return false;
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        moving = false;
                        /*distance = (long) Math.sqrt(startX + startY);
                        if (downTime + 250 > System.currentTimeMillis() && distance < 50) {
                            mainHeadView.performClick();
                        } else {
                        }*/
                        int endPosition = mainHeadView.getHeight()/3;
                        if (event.getRawX()>screenWidth/2) {
                            endPosition = screenWidth - endPosition;
                        }
                        xAnimationSpring.setEndValue(endPosition);
                        //scaleAnimationSpring.setEndValue(1);
                        return false;
                }


                Log.d(TAG, "MainHead touch: " + MotionEvent.actionToString(event.getAction()));
                return false;
            }
        });
        // its head
        mainHeadView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "OnMainHeadClick");
                activateBackground();
            }
        });
    }

    private static void activateBackground() {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 0;
        final View backgroundView = LayoutInflater.from(application).inflate(R.layout.chathead_background, null);

        windowManager.addView(backgroundView, params);
        backgroundView.setAlpha(0);
        backgroundView.animate().alpha(1).setDuration(100).start();
        backgroundView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG, "Background touch: " + MotionEvent.actionToString(event.getAction()));
                return false;
            }
        });
        // its head
        backgroundView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "OnMainHeadClick");
                windowManager.removeViewImmediate(backgroundView);
            }
        });
    }




    static final Spring xAnimationSpring = springSystem.createSpring()
            .setSpringConfig(SpringConfig.fromBouncinessAndSpeed(12, 12))
            .addListener(new SimpleSpringListener() {
                @Override
                public void onSpringUpdate(Spring spring) {
                    xValue = (float) spring.getCurrentValue();
                    //chatHeadView.setTranslationX(xValue);
                    WindowManager.LayoutParams params = (WindowManager.LayoutParams) mainHeadView.getLayoutParams();
                    params.x = (int) xValue - mainHeadView.getWidth()/2;
                    windowManager.updateViewLayout(mainHeadView, params);
                }
            });
    static final Spring yAnimationSpring = springSystem.createSpring()
            .setSpringConfig(SpringConfig.fromBouncinessAndSpeed(12, 12))
            .addListener(new SimpleSpringListener() {
                @Override
                public void onSpringUpdate(Spring spring) {
                    yValue = (float) spring.getCurrentValue();

                    WindowManager.LayoutParams params = (WindowManager.LayoutParams) mainHeadView.getLayoutParams();
                    params.y = (int) yValue - mainHeadView.getHeight()/2;
                    windowManager.updateViewLayout(mainHeadView, params);
                }

            });
}
