package org.happysanta.messenger.longpoll.updates;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Jesus Christ. Amen.
 */
public class LongpollChatTyping {
    private final int chatId;

    public LongpollChatTyping(JSONArray jsonUpdate) throws JSONException {
        this.chatId = jsonUpdate.getInt(1);
    }
}
