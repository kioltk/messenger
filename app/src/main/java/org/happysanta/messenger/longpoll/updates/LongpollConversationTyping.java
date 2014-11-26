package org.happysanta.messenger.longpoll.updates;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Jesus Christ. Amen.
 */
public class LongpollConversationTyping {
    private final int userId;

    public LongpollConversationTyping(JSONArray jsonUpdate) throws JSONException {
        this.userId = jsonUpdate.getInt(1);
    }
}
