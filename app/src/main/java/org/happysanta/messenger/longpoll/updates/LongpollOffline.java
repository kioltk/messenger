package org.happysanta.messenger.longpoll.updates;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Jesus Christ. Amen.
 */
public class LongpollOffline {
    private final int userId;
    private final boolean timeOut;

    public LongpollOffline(JSONArray jsonUpdate) throws JSONException {
        this.userId = jsonUpdate.getInt(1)*-1;
        this.timeOut = jsonUpdate.getInt(2)==1;
    }
}
