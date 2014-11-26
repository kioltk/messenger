package org.happysanta.messenger.longpoll.listeners;

/**
 * Created by Jesus Christ. Amen.
 */
public class LongpollMessageListener implements LongpollListener {


    private final boolean isChat;
    private int dialogId;

    public LongpollMessageListener(int conversationId) {
        this.dialogId = conversationId;
        this.isChat = false;
    }

    public LongpollMessageListener(int dialogId, boolean isChat) {
        this.dialogId = dialogId;
        this.isChat = isChat;
    }

    @Override
    public int getId() {
        return dialogId;
    }

    public boolean isChat() {
        return isChat;
    }

    @Override
    public void onLongPollUpdate(Object update) {

    }

}