package org.happysanta.messenger.core.longpoll.listeners;

/**
 * Created by Jesus Christ. Amen.
 */
public class LongPollMessageListener implements LongPollListener {


    private final boolean isChat;
    private int dialogId;

    public LongPollMessageListener(int conversationId) {
        this.dialogId = conversationId;
        this.isChat = false;
    }

    public LongPollMessageListener(int dialogId, boolean isChat) {
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