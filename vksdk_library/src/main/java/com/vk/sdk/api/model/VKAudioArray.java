package com.vk.sdk.api.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by insidefun on 09.05.2015
 */
public class VKAudioArray extends VKList<VKApiAudio> {
    @Override
    public VKApiModel parse(JSONObject response) throws JSONException {
        fill(response, VKApiAudio.class);
        return this;
    }
}
