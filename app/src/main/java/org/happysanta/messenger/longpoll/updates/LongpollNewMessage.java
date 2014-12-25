package org.happysanta.messenger.longpoll.updates;

import com.vk.sdk.api.model.VKApiMessage;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Jesus Christ. Amen.
 */
public class LongpollNewMessage extends VKApiMessage {
    private static final int FLAG_UNREAD = 1;
    private static final int FLAG_OUTBOX = 2;
    private static final int FLAG_CHAT = 8192;
    private static final int FLAG_MEDIA = 512;
    public final boolean isChat;

    public LongpollNewMessage(JSONArray jsonUpdate) throws JSONException {

        id = jsonUpdate.getInt(1);
        int flags = jsonUpdate.getInt(2);

        read_state = (flags & FLAG_UNREAD) == FLAG_UNREAD;
        out = (flags & FLAG_OUTBOX) == FLAG_OUTBOX;
        isChat = (flags & FLAG_CHAT) == FLAG_CHAT;

        if ((flags & FLAG_MEDIA) == FLAG_MEDIA) {
            // todo attaches
        }
        if (isChat) {
            chat_id = jsonUpdate.getInt(3) - (2000000000);
            user_id = jsonUpdate.getJSONObject(7).getInt("from");
        } else {
            user_id = jsonUpdate.getInt(3);
        }


        date = jsonUpdate.getInt(4);
        body = jsonUpdate.getString(6);

    }
    @Override
    public String toString() {
        return "Message " + (isChat ? "in the chat " + chat_id :"")+ " from " + user_id + ". \"" + body + "\"";
    }
}