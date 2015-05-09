package com.vk.sdk.api.methods;

import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKParser;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.model.VKApiAudio;
import com.vk.sdk.api.model.VKList;

import org.json.JSONObject;

/**
 * Created by Jesus Christ. Amen.
 */
public class VKApiAudios extends VKApiBase {
    /**
     * https://vk.com/dev/audio.get
     *
     * @param params use parameters from description with VKApiConst class
     * @return Request for load
     */

    public VKApiAudios(){
        super("audio");
    }

    public VKRequest get(VKParameters params) {
        return prepareRequest("get", params, VKRequest.HttpMethod.GET, new VKParser() {
            @Override
            public Object createModel(JSONObject object) {
                return new VKList<VKApiAudio>(object, VKApiAudio.class);
            }
        });
    }

    public VKRequest getById(VKParameters params) {
        return prepareRequest("getById", params, VKRequest.HttpMethod.GET, new VKParser() {
            @Override
            public Object createModel(JSONObject object) {
                return new VKList<VKApiAudio>(object, VKApiAudio.class);
            }
        });
    }

    public VKRequest getRecommendations(VKParameters params) {
        return prepareRequest("getRecommendations", params, VKRequest.HttpMethod.GET, new VKParser() {
            @Override
            public Object createModel(JSONObject object) {
                return new VKList<VKApiAudio>(object, VKApiAudio.class);
            }
        });
    }

    public VKRequest getPopular(VKParameters params) {
        return prepareRequest("getPopular", params, VKRequest.HttpMethod.GET, new VKParser() {
            @Override
            public Object createModel(JSONObject object) {
                return new VKList<VKApiAudio>(object, VKApiAudio.class);
            }
        });
    }

    public VKRequest getAlbums(VKParameters params) {
        return prepareRequest("getAlbums", params, VKRequest.HttpMethod.GET, new VKParser() {
            @Override
            public Object createModel(JSONObject object) {
                return new VKList<VKApiAudio>(object, VKApiAudio.class);
            }
        });
    }
}
