package org.happysanta.messenger.core.longpoll.updates;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Jesus Christ. Amen.
 */
public class LongPollOffline {
    private final int userId;
    private final boolean timeOut;

    public LongPollOffline(JSONArray jsonUpdate) throws JSONException {
        this.userId = jsonUpdate.getInt(1)*-1;
        this.timeOut = jsonUpdate.getInt(2)==1;
    }
}
