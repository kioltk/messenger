package com.vk.sdk.api.model;

import android.os.Parcel;

/**
 * Created by Jesus Christ. Amen.
 */
public class VKApiDialog extends VKApiModel implements Identifiable {

    //public int id;
    public int unread = 0;
    public String body;
    public int chat_id;
    public int user_id;
    public String title;
    public int date;
    public String photo_50;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
    }

    @Override
    public int getId() {
        if(isChat()){
            return chat_id;
        }
        return user_id;
    }
    public boolean isChat(){
        return chat_id != 0;
    }


    public String getPhoto() {
        return photo_50;
    }
}
