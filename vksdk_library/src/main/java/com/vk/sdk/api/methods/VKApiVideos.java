package com.vk.sdk.api.methods;

import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKParser;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.model.VKApiVideo;
import com.vk.sdk.api.model.VKList;

import org.json.JSONObject;

/**
 * Created by Jesus Christ. Amen.
 */
public class VKApiVideos extends VKApiBase {

    public VKApiVideos(){
        super("video");
    }

    public VKRequest get(VKParameters params) {
        return prepareRequest("get", params, VKRequest.HttpMethod.GET, new VKParser() {
            @Override
            public Object createModel(JSONObject object) {
                return new VKList<VKApiVideo>(object, VKApiVideo.class);
            }
        });
    }
}
