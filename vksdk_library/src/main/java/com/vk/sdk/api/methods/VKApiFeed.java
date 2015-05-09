package com.vk.sdk.api.methods;

import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.model.VKCommentArray;
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

    public VKRequest getRecommended(VKParameters params) {
        return prepareRequest("getRecommended", params, VKRequest.HttpMethod.GET, VKPostArray.class);
    }

    public VKRequest getComments(VKParameters params) {
        return prepareRequest("getComments", params, VKRequest.HttpMethod.GET, VKCommentArray.class);
    }
}
