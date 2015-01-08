package org.happysanta.messenger.messages.core;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Jesus Christ. Amen.
 */
public class DialogUtil {
    private static final String PREF_BODY_KEY = "body";
    private static Context context;

    public static void init(Context context){
        DialogUtil.context = context;
    }

    public static void saveBody(boolean isChat, int dialogId, String messageBody) {
        SharedPreferences prefs = context.getSharedPreferences((isChat ? "chat" : "dialog") + dialogId, Context.MODE_MULTI_PROCESS);
        prefs.edit().putString(PREF_BODY_KEY, messageBody).apply();
    }
    public static String getBody(boolean isChat, int dialogId){
        SharedPreferences prefs = context.getSharedPreferences((isChat ? "chat" : "dialog") + dialogId, Context.MODE_MULTI_PROCESS);
        return prefs.getString(PREF_BODY_KEY,null);
    }

    private final boolean isChat;
    private final int dialogId;
    public DialogUtil(boolean isChat, int dialogId) {
        this.isChat = isChat;
        this.dialogId = dialogId;
    }
    public void saveBody(String messageBody){
        saveBody(isChat, dialogId, messageBody);
    }
    public String getBody(){
        return getBody(isChat, dialogId);
    }

    public static void setChatsShowed(boolean chatsShowed) {
        context.getSharedPreferences("dialogs_list",Context.MODE_MULTI_PROCESS)
                .edit()
                .putBoolean("chats_showed",chatsShowed)
                .apply();
    }
    public static boolean isChatsShowed(){
        return context.getSharedPreferences("dialogs_list",Context.MODE_MULTI_PROCESS)
                .getBoolean("chats_showed",false);
    }
}
