package org.happysanta.messenger.longpoll.updates;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Jesus Christ. Amen.
 */
public class LongpollTyping {
    public final int dialogId;
    public final boolean isChat;
    public final int userId;

    public LongpollTyping(int dialogId) {
        this.dialogId = dialogId;
        this.userId = dialogId;
        this.isChat = false;
    }

    public LongpollTyping(int userId, int dialogId) {
        this.dialogId = dialogId;
        this.userId = userId;
        this.isChat = true;
    }

    @Override
    public String toString() {
        return "u"+userId+" typed" + (isChat?" in the chat " + dialogId:"");
    }
}
