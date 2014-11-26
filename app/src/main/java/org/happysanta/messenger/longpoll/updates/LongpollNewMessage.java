package org.happysanta.messenger.longpoll.updates;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Jesus Christ. Amen.
 */
public class LongpollNewMessage {
    private static final int FLAG_UNREAD = 1;
    private static final int FLAG_OUTBOX = 2;
    private static final int FLAG_CHAT = 16;
    private static final int FLAG_MEDIA = 512;
    public final int messageId;
    public final boolean unread;
    public final boolean out;
    public final boolean isChat;
    public final String body;
    public final Integer timestamp;
    public final int dialogId;

    public LongpollNewMessage(JSONArray jsonUpdate) throws JSONException {

        messageId = jsonUpdate.getInt(1);
        int flags = jsonUpdate.getInt(2);

        unread = (flags & FLAG_UNREAD) == FLAG_UNREAD;
        out = (flags & FLAG_OUTBOX) == FLAG_OUTBOX;
        isChat = (flags & FLAG_CHAT) == FLAG_CHAT;

        if ((flags & FLAG_MEDIA) == FLAG_MEDIA) {

        }

        dialogId = jsonUpdate.getInt(3) - (isChat ? 2000000000 : 0);
        timestamp = jsonUpdate.getInt(4);
        body = jsonUpdate.getString(6);

    }
}