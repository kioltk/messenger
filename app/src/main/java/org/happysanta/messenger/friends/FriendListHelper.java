package org.happysanta.messenger.friends;

import android.util.Log;

import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.methods.VKApiFriends;
import com.vk.sdk.api.methods.VKApiUsers;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by alex on 10/01/15.
 */
public class FriendListHelper {

    private static final int DATA_JSON  = 0;
    private static final int DATA_MODEL = 1;

    private static final int METHOD_GET_ONLINE  = 10;
    private static final int METHOD_USERS_GET   = 11;

    private boolean                         mOnline;

    private VKRequest.VKRequestListener     mIdGetListener;
    private VKRequest.VKRequestListener     mUserGetListener;
    private String                          mIds;

    private VKList<VKApiUserFull>           mUsersResponse;

    private IFriendListCallback             mCallback;

    public interface IFriendListCallback {

        public void onResult(ArrayList<VKApiUserFull> friends);

        public void onEmptyList(boolean online);

        public void onError(String error);
    }

    public FriendListHelper(boolean online, IFriendListCallback callback) {

        this.mOnline    = online;
        this.mCallback  = callback;
    }

    public void doRequest() {

        if (mOnline)
            mIdGetListener  = getRequestListener(DATA_JSON);

        mUserGetListener    = getRequestListener(DATA_MODEL);


        if (mOnline) {

            new VKApiFriends()
                    .getOnline(getParameters(METHOD_GET_ONLINE))
                    .executeWithListener(mIdGetListener);

        } else {

            new VKApiFriends().get().executeWithListener(mUserGetListener);
        }
    }

    private void setResult(VKList<VKApiUserFull> result) {

        if (result.isEmpty()) {

            mCallback.onEmptyList(mOnline);
            return;
        }

        sort(result);

        mCallback.onResult(result.toArrayList());
    }

    //region Getters for request
    @SuppressWarnings("unchecked")
    private VKRequest.VKRequestListener getRequestListener(int dataType) {

        VKRequest.VKRequestListener listener;

        if (dataType == DATA_MODEL) {

            listener = new VKRequest.VKRequestListener() {

                @Override
                public void onComplete(VKResponse response) {
                    super.onComplete(response);

                    mUsersResponse = (VKList<VKApiUserFull>) response.parsedModel;

                    setResult(mUsersResponse);
                }

                @Override
                public void onError(VKError error) {
                    super.onError(error);

                    mCallback.onError(error.toString());
                }
            };

            return listener;
        }

        if (dataType == DATA_JSON) {

            listener = new VKRequest.VKRequestListener() {

                @Override
                public void onComplete(VKResponse response) {
                    super.onComplete(response);

                    try {

                        mIds =  buildIdsArray(response.json);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    new VKApiUsers()
                            .get(getParameters(METHOD_USERS_GET))
                            .executeWithListener(mUserGetListener);
                }

                @Override
                public void onError(VKError error) {
                    super.onError(error);

                    mCallback.onError(error.toString());
                }
            };

            return listener;
        }

        return null;
    }

    private VKParameters getParameters(int method) {

        Map<String, Object> params = new HashMap<>();

        switch (method) {

            case METHOD_GET_ONLINE: {

                Log.d("METHOD_GET_ONLINE", "setting params");

                params.put("order", "hints");

                return new VKParameters(params);
            }

            case METHOD_USERS_GET: {

                Log.d("METHOD_USERS_GET", "setting params");
                Log.d("IDS_LIST",          mIds);

                params.put("user_ids", mIds);
                params.put("fields",  "name,last_name,age,online,photo_200");

                return new VKParameters(params);
            }
        }

        return null;
    }
    //endregion

    //region Util methods
    private String buildIdsArray(JSONObject src) throws JSONException {

        if (null == src) return null;

        StringBuilder idArrayBuilder  = new StringBuilder();
        JSONArray     idArray         = src.getJSONArray("response");

        if (null == idArray) {

            Log.d("NULL_ARRAY_IDS", "Cannot build array");
            return null;
        }

        for (int i = 0 ; i < idArray.length() ; i++) {

            if (i < idArray.length() - 1) {

                idArrayBuilder
                        .append(idArray.getInt(i))
                        .append(",");

            } else {

                idArrayBuilder.append(idArray.getInt(i));
            }
        }

        return idArrayBuilder.toString();
    }

    private void sort(List<VKApiUserFull> list) {

        if (mOnline)            return;
        if (null == list)       return;
        if (list.size() < 5)    return;

        if (list.size() > 10)   list = list.subList(5, list.size());

        Collections.sort(list, new Comparator<VKApiUserFull>() {
            @Override
            public int compare(VKApiUserFull lhs, VKApiUserFull rhs) {

                return lhs.first_name.compareTo(rhs.first_name);
            }
        });
    }
    //endregion

}
