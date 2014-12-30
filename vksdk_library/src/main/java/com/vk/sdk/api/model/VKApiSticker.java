package com.vk.sdk.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

/**
 * Created by d_great on 25.12.14.
 */
public class VKApiSticker extends VKAttachments.VKApiAttachment  implements Parcelable, Identifiable {
    public int id;

    public int product_id;

    public int height;
    public int width;

    public String photo_64;
    public String photo_128;
    public String photo_256;

    /*
    *
    *   {
    *       "id":347,
    *       "product_id":10,
    *       "photo_64":"https:\/\/vk.com\/images\/stickers\/347\/64b.png",
    *       "photo_128":"https:\/\/vk.com\/images\/stickers\/347\/128b.png",
    *       "photo_256":"https:\/\/vk.com\/images\/stickers\/347\/256b.png",
    *       "width":256,
    *       "height":256
    *   }
    * */

    @Override
    public int getId() {
        return id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }


    public VKAttachments.VKApiAttachment parse(JSONObject from) {

        id = from.optInt("id");
        product_id = from.optInt("product_id");

        photo_64 = from.optString("photo_64");
        photo_128 = from.optString("photo_128");
        photo_256 = from.optString("photo_256");



        height = from.optInt("height");
        width = from.optInt("width");

        return this;
    }

    @Override
    public CharSequence toAttachmentString() {
        return VKAttachments.TYPE_STICKER;
    }

    @Override
    public String getType() {
        return VKAttachments.TYPE_STICKER;
    }
}
