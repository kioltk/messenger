package org.happysanta.messenger.longpoll.updates;

import com.vk.sdk.api.model.VKApiMessage;
import com.vk.sdk.api.model.VKApiSticker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jesus Christ. Amen.
 */
public class LongpollNewMessage extends VKApiMessage {
    private static final int FLAG_UNREAD = 1;
    private static final int FLAG_OUTBOX = 2;
    private static final int FLAG_CHAT = 8192;
    private static final int FLAG_MEDIA = 512;
    public final boolean isChat;

    /*
    *
    * attaches
    * {"attach1_product_id":"10","attach1_type":"sticker","attach1":"364"} - sticker
    * {"attach1_type":"photo","attach1":"32018303_349190109"} - photo
    * {"attach1_type":"audio","attach1":"32018303_324858579"} - audio
    * {"attach1_type":"video","attach1":"121935185_165655682"} - video
    * {"attach1_type":"doc","attach1":"32018303_347630079"} - doc
    * {"geo":"9VZT4RBV","geo_provider":"3"}
    *
    *
    * */
    public LongpollNewMessage(JSONArray jsonUpdate) throws JSONException {

        id = jsonUpdate.getInt(1);
        int flags = jsonUpdate.getInt(2);

        read_state = (flags & FLAG_UNREAD) == FLAG_UNREAD;
        out = (flags & FLAG_OUTBOX) == FLAG_OUTBOX;
        isChat = (flags & FLAG_CHAT) == FLAG_CHAT;
        JSONObject extras = jsonUpdate.getJSONObject(7);
        if ((flags & FLAG_MEDIA) == FLAG_MEDIA) {
            // todo attaches
        }
        if (isChat) {
            chat_id = jsonUpdate.getInt(3) - (2000000000);
            user_id = extras.getInt("from");
        } else {
            user_id = jsonUpdate.getInt(3);
        }

        date = jsonUpdate.getInt(4);
        emoji = extras.optInt("emoji",0)>0;

        if(extras.optString("attach1_type","").equals("sticker")) {
            sticker = new VKApiSticker();
            sticker.id = extras.optInt("attach1");
            sticker.product_id = extras.optInt("attach1_product_id");
        }else{
            body = jsonUpdate.getString(6);
        }

    }
    @Override
    public String toString() {
        return "Message " + (isChat ? "in the chat " + chat_id :"")+ " from " + user_id + ". \"" + body + "\"";
    }
}