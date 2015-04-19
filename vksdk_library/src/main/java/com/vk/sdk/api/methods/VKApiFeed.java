package com.vk.sdk.api.methods;

import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.model.VKPostArray;

/**
 * Created by Jesus Christ. Amen.
 */
public class VKApiFeed extends VKApiBase {

    public VKApiFeed(){
        super("newsfeed");
    }

    public VKRequest get(VKParameters params) {
        return prepareRequest("get", params, VKRequest.HttpMethod.GET, VKPostArray.class);
    }
}
