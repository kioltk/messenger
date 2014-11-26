package org.happysanta.messenger.core.longpoll.updates;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Jesus Christ. Amen.
 */
public class LongPollOnline {
    private final int userId;
    private final Object platformId;

    public LongPollOnline(JSONArray jsonUpdate) throws JSONException {
        this.userId = jsonUpdate.getInt(1)*-1;
        this.platformId = jsonUpdate.getInt(2)>0;
    }
}
