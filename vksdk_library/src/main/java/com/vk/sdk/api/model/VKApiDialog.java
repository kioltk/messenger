package com.vk.sdk.api.model;

import android.os.Parcel;

/**
 * Created by Jesus Christ. Amen.
 */
public class VKApiDialog extends VKApiModel implements Identifiable {

    private final boolean isChat;
    public int usersCount = 1;
    //public int id;
    public int unread = 0;
    public String body;
    public int dialogId;
    public String title;
    public long date;
    public String photo_200;

    public VKApiDialog(VKApiMessage dialogMessage, VKApiUserFull dialogOwner) {
        isChat = false;
        this.body = dialogMessage.body;
        this.title = dialogOwner.toString();
        this.dialogId = dialogOwner.id;
        this.photo_200 = dialogOwner.photo_200;
        this.date = dialogMessage.date;
    }

    public VKApiDialog(VKApiMessage dialogMessage, VKApiUserFull chatOwner, VKList<VKApiUserFull> chatUsers) {
        isChat = true;
        this.body = "CHAT";
        this.dialogId = dialogMessage.chat_id;
        this.title = dialogMessage.title;
        this.photo_200 = dialogMessage.photo_200;
        this.date = dialogMessage.date;
        this.usersCount = chatUsers.size();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
    }

    @Override
    public int getId() {
        return dialogId;
    }
    public boolean isChat(){
        return isChat;
    }


    public String getPhoto() {
        return photo_200;
    }
}
