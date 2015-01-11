package com.vk.sdk.api.model;

import android.os.Parcel;

/**
 * Created by Jesus Christ. Amen.
 */
public class VKApiDialog extends VKApiModel implements Identifiable {

    private final boolean isChat;
    private final VKApiUserFull dialogOwner;
    private final VKList<VKApiUserFull> participants;
    public int usersCount = 1;
    //public int id;
    public int unread = 0;
    public int dialogId;// chat id or user id
    private String title;
    public String photo_200;

    public VKApiMessage lastMessage;

    public VKApiDialog(VKApiMessage dialogMessage, VKApiUserFull dialogOwner) {
        this.isChat = false;
        this.title = dialogOwner.toString();
        this.dialogId = dialogOwner.id;
        this.photo_200 = dialogOwner.photo_200;
        this.lastMessage = dialogMessage;
        this.dialogOwner = dialogOwner;
        participants = null;
    }

    public VKApiDialog(VKApiMessage dialogMessage, VKApiUserFull chatOwner, VKList<VKApiUserFull> chatUsers) {
        this.isChat = true;
        this.dialogId = dialogMessage.chat_id;
        this.title = dialogMessage.title;
        this.photo_200 = dialogMessage.photo_200;
        this.usersCount = chatUsers.size();
        this.lastMessage = dialogMessage;
        this.dialogOwner = chatOwner;
        this.participants = chatUsers;
    }

    public VKApiUserFull getParticipant(int userId){
        return participants.getById(userId);
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

    public String getBody(){
        return lastMessage.getBody();
    }

    public String getPhoto() {
        return photo_200;
    }

    public long getDate() {
        return lastMessage.date;
    }

    public void setLastMessage(VKApiMessage lastMessage) {
        this.lastMessage = lastMessage;
    }

    public boolean isOnline(){
        return dialogOwner.online;
    }

    public String getTitle() {
        if (!isChat) {
            return dialogOwner.toString();
        }
        return title;
    }

    public String getSubtitle(){
        if (!isChat) {
            return dialogOwner.online ? (dialogOwner.online_mobile?"online":"mobile"):(""+dialogOwner.last_seen);
        }
        return participants.size()+" users";
    }

}
