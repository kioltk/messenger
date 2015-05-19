package org.happysanta.messenger.core.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.vk.sdk.api.model.VKApiUserFull;

/**
 * Created by Jesus Christ. Amen.
 */
public class ProfileUtil {
    private static final String PREF_PROFILE_KEY = "profile_pref_key";
    private static final String PREF_PROFILE_PHOTO = "user_photo";
    private static final String PREF_PROFILE_NAME = "user_name";
    private static final String PREF_PROFILE_STATUS = "user_status";
    private static final String PREF_PROFILE_ID = "user_id";
    private static Context context;
    private static SharedPreferences manager;
    private static VKApiUserFull currentUser;

    public static void init(Context context) {
        ProfileUtil.context = context;
        manager = context.getSharedPreferences(PREF_PROFILE_KEY, Context.MODE_MULTI_PROCESS);
    }

    public static void setUserPhoto(String userPhoto){
        manager.edit().putString(PREF_PROFILE_PHOTO, userPhoto).apply();
    }
    public static String getUserPhoto() {
        return manager.getString(PREF_PROFILE_PHOTO, null);
    }

    public static void setUserName(String userName){
        manager.edit().putString(PREF_PROFILE_NAME, userName).apply();
    }
    public static String getUserName() {
        return manager.getString(PREF_PROFILE_NAME, null);
    }

    public static void setUserStatus(String userStatus){
        manager.edit().putString(PREF_PROFILE_STATUS, userStatus).apply();
    }
    public static String getUserStatus() {
        return manager.getString(PREF_PROFILE_STATUS, null);
    }

    public static void setUserId(int userId) {
        manager.edit().putInt(PREF_PROFILE_ID, userId).apply();
    }
    public static Integer getUserId() {
        return manager.getInt(PREF_PROFILE_ID, 0);
    }

    public static void setFromUser(VKApiUserFull currentUser) {
        ProfileUtil.currentUser = currentUser;
        setUserPhoto(currentUser.getPhoto());
        setUserName(currentUser.toString());
        setUserId(currentUser.id);
        setUserStatus(currentUser.activity);
        // todo listeners?
    }

    public static VKApiUserFull getUser() {
        if(currentUser==null){
            currentUser = new VKApiUserFull();
            currentUser.photo_200 = getUserPhoto();
            currentUser.id = getUserId();
            currentUser.activity = getUserStatus();
        }
        return currentUser;
    }
}
