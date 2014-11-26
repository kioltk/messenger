package org.happysanta.messenger.core.longpoll.updates;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Jesus Christ. Amen.
 */
public class LongPollChatTyping {
    private final int chatId;

    public LongPollChatTyping(JSONArray jsonUpdate) throws JSONException {
        this.chatId = jsonUpdate.getInt(1);
    }
}
