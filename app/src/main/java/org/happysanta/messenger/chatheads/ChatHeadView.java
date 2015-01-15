package org.happysanta.messenger.chatheads;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;

import org.happysanta.messenger.R;


/**
 * Created by Jesus Christ. Amen.
 */
public class ChatHeadView extends View /*implements View.OnClickListener, View.OnTouchListener*/ {


    private ChatHeadsManager.ChatHeadMovementTask movementTask;
    private int currentPositionX;
    private int currentPositionY;
    private boolean dragging = false;
    private int dragPositionX;
    private int dragPositionY;
    private WindowManager.LayoutParams params;

    public ChatHeadView(Context context) {
        super(context);
        init(null);
    }


    public ChatHeadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ChatHeadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attributeSet) {
        setBackgroundResource(R.drawable.chathead_default);
    }


   /*

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(movementTask!=null){
            return false;
        }
        final int action = event.getActionMasked();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                // Filter and redirect the events to dragTray()
                dragChatHead(action, (int) event.getRawX(), (int) event.getRawY());
                break;
            default:
                return false;
        }
        return true;
    }

    private void dragChatHead(int action, int x, int y) {
        switch (action) {
            case MotionEvent.ACTION_DOWN:

                if (movementTask!=null){
                    cancelMovementTask();
                }

                // Store the start points
                currentPositionX = params.x;
                currentPositionY = params.y;
                dragPositionX = params.x;
                dragPositionY = params.y;
                break;

            case MotionEvent.ACTION_MOVE:
                float deltaX = x - dragPositionX;
                float deltaY = y - dragPositionY;
                // Calculate position of the whole tray according to the drag, and update layout.
                if(dragging) {
                    params.x += deltaX - params.width / 2;
                    params.y += deltaY - params.height;
                    dragPositionX = params.x;
                    dragPositionY = params.y;
                    //animateButtons();
                    ChatHeadsManager.updateViewLayout(this, params);
                }else{
                    if(Math.abs(deltaX) < params.height/3 || Math.abs(deltaY) < params.height/3){
                        dragging = true;
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if(dragging) {
                    ChatHeadsManager.restore(this, dragPositionX, dragPositionY);
                }else{
                    performClick();

                }
                dragging = false;
                break;
        }
    }

    public void setMovementTask(ChatHeadsManager.ChatHeadMovementTask movementTask) {
        this.movementTask = movementTask;
    }
    public void cancelMovementTask(){
        if(movementTask!=null){
            movementTask.cancel();
            movementTask = null;
        }
    }*/
}