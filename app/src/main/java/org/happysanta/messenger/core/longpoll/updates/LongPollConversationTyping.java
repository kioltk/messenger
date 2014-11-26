package org.happysanta.messenger.core.longpoll.updates;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Jesus Christ. Amen.
 */
public class LongPollConversationTyping {
    private final int userId;

    public LongPollConversationTyping(JSONArray jsonUpdate) throws JSONException {
        this.userId = jsonUpdate.getInt(1);
    }
}
