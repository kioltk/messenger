package com.vk.sdk.api.methods;

import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.model.VKAudioArray;
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

    public VKRequest get(final VKParameters params) {
        return prepareRequest("get", params, VKRequest.HttpMethod.GET, VKAudioArray.class);
    }

    public VKRequest getById(VKParameters params) {
        return prepareRequest("getById", params, VKRequest.HttpMethod.GET, VKAudioArray.class);
    }

    public VKRequest getLyrics(VKParameters params) {
        return prepareRequest("getLyrics", params, VKRequest.HttpMethod.GET, VKAudioArray.class);
    }

    public VKRequest search(VKParameters params) {
        return prepareRequest("search", params, VKRequest.HttpMethod.GET, VKAudioArray.class);
    }

    public VKRequest getUploadServer(VKParameters params) {
        return prepareRequest("getUploadServer", params, VKRequest.HttpMethod.GET, VKAudioArray.class);
    }

    /**
     * Actions with audio
     */
    public VKRequest save(VKParameters params) {
        return prepareRequest("save", params, VKRequest.HttpMethod.GET, VKAudioArray.class);
    }

    public VKRequest add(VKParameters params) {
        return prepareRequest("add", params, VKRequest.HttpMethod.GET, VKAudioArray.class);
    }

    public VKRequest delete(VKParameters params) {
        return prepareRequest("delete", params, VKRequest.HttpMethod.GET, VKAudioArray.class);
    }

    public VKRequest edit(VKParameters params) {
        return prepareRequest("edit", params, VKRequest.HttpMethod.GET, VKAudioArray.class);
    }

    public VKRequest reorder(VKParameters params) {
        return prepareRequest("reorder", params, VKRequest.HttpMethod.GET, VKAudioArray.class);
    }

    public VKRequest restore(VKParameters params) {
        return prepareRequest("restore", params, VKRequest.HttpMethod.GET, VKAudioArray.class);
    }

    /**
     * Albums
     */
    public VKRequest getAlbums(VKParameters params) {
        return prepareRequest("getAlbums", params, VKRequest.HttpMethod.GET, VKAudioArray.class);
    }

    public VKRequest addAlbum(VKParameters params) {
        return prepareRequest("addAlbum", params, VKRequest.HttpMethod.GET, VKAudioArray.class);
    }

    public VKRequest editAlbum(VKParameters params) {
        return prepareRequest("editAlbum", params, VKRequest.HttpMethod.GET, VKAudioArray.class);
    }

    public VKRequest deleteAlbum(VKParameters params) {
        return prepareRequest("deleteAlbum", params, VKRequest.HttpMethod.GET, VKAudioArray.class);
    }

    public VKRequest moveToAlbum(VKParameters params) {
        return prepareRequest("moveToAlbum", params, VKRequest.HttpMethod.GET, VKAudioArray.class);
    }

    public VKRequest setBroadcast(VKParameters params) {
        return prepareRequest("setBroadcast", params, VKRequest.HttpMethod.GET, VKAudioArray.class);
    }

    public VKRequest getRecommendations(VKParameters params) {
        return prepareRequest("getRecommendations", params, VKRequest.HttpMethod.GET, VKAudioArray.class);
    }

    public VKRequest getPopular(VKParameters params) {
        return prepareRequest("getPopular", params, VKRequest.HttpMethod.GET, VKAudioArray.class);
    }

    public VKRequest getCount(VKParameters params) {
        return prepareRequest("getCount", params, VKRequest.HttpMethod.GET, VKAudioArray.class);
    }

}
