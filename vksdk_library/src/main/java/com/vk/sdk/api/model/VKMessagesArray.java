package com.vk.sdk.api.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jesus Christ. Amen.
 */
public class VKMessagesArray extends VKList<VKApiMessage> {
    @Override
    public VKApiModel parse(JSONObject response) throws JSONException {
        fill(response, VKApiMessage.class);
        return this;
    }
}
