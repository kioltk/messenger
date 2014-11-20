package com.vk.sdk.api.model;

import android.os.Parcel;

/**
 * Created by Jesus Christ. Amen.
 */
public class VKApiDialog extends VKApiModel implements Identifiable {

    public VKApiMessage message;
    public int id;
    public int unread = 0;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeInt(this.id);
        dest.writeInt(this.unread);
    }

    @Override
    public int getId() {
        return message.user_id;
    }


}
