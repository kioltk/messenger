package org.happysanta.messenger.longpoll.listeners;

import org.happysanta.messenger.longpoll.updates.LongpollNewMessage;
import org.happysanta.messenger.longpoll.updates.LongpollTyping;

import java.util.ArrayList;

/**
 * Created by Jesus Christ. Amen.
 */
public abstract class LongpollDialogListener implements LongpollListener {


    private int dialogId;

    public LongpollDialogListener(int conversationId) {
        this.dialogId = conversationId;
    }


    @Override
    public int getId() {
        return dialogId;
    }

    @Override
    public void onLongPollUpdate(Object update) {

    }

    public abstract void onNewMessages(ArrayList<LongpollNewMessage> messages);
    public abstract void onTyping(ArrayList<LongpollTyping> typing);

}