package org.happysanta.messenger.longpoll.updates;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Jesus Christ. Amen.
 */
public class LongpollOnline {
    private final int userId;
    private final Object platformId;

    public LongpollOnline(JSONArray jsonUpdate) throws JSONException {
        this.userId = jsonUpdate.getInt(1)*-1;
        this.platformId = jsonUpdate.getInt(2)>0;
    }
}
