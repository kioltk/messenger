package org.happysanta.messenger.core.longpoll;

import android.os.AsyncTask;
import android.util.Log;

import com.vk.sdk.api.model.VKLongPollServer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.happysanta.messenger.core.longpoll.updates.LongPollChatTyping;
import org.happysanta.messenger.core.longpoll.updates.LongPollConversationTyping;
import org.happysanta.messenger.core.longpoll.updates.LongPollNewMessage;
import org.happysanta.messenger.core.longpoll.updates.LongPollOffline;
import org.happysanta.messenger.core.longpoll.updates.LongPollOnline;
import org.happysanta.messenger.core.util.ExceptionUtil;
import org.json.JSONArray;
import org.json.JSONObject;


public abstract class LongPollTask1 extends AsyncTask<Void, Void, Object> {
    private final String server;
    private final String key;
    private final int ts;
    String url = "http://%s?act=a_check&key=%s&ts=%s&wait=25&mode=98";
    private Exception exception;
    private boolean finished = false;
    public int duration = 0;

    public LongPollTask1(String server, String key, int ts){
        this.server = server;
        this.key = key;
        this.ts = ts;
    }

    public LongPollTask1(VKLongPollServer server) {
        this.server = server.server;
        this.key = server.key;
        this.ts = server.ts;
    }

    @Override
    protected Object doInBackground(Void... params) {
        try {
            String response = "";
            String finalUrl = String.format(url,server,key,ts);
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet get = new HttpGet(finalUrl);

            HttpResponse httpResponse = httpClient.execute(get);
            HttpEntity httpEntity = httpResponse.getEntity();
            response = EntityUtils.toString(httpEntity);
            Log.i("AGCY SPY","longpoll responses:" + response);
            if (isCancelled())
                return null;
            return parseResult(response);
        }catch (Exception exp){
            this.exception = exp;
        }
        return exception;
    }

    private Object parseResult(String result) throws Exception{

        if(result!=null)
            try {
                String jsonResponse =  result;
                JSONObject response = new JSONObject(jsonResponse);
                if (response.has("failed")) {
                    throw new ServerException();
                }


                LongPollResponse longPollResponse = new LongPollResponse();
                longPollResponse.ts = response.optInt("ts");
                JSONArray updates = response.optJSONArray("updates");
                for (int i = 0; i < updates.length(); i++) {
                    longPollResponse.updates.add(optUpdate(updates.optJSONArray(i)));
                }
                return longPollResponse;
            } catch (Exception e) {
                this.exception = e;
                return exception;
            }
        else {

        }

        return null;
    }

    private Object optUpdate(JSONArray jsonUpdate) throws Exception{
        int type = jsonUpdate.getInt(0);
        switch (type) {
            case 4:
                return new LongPollNewMessage(jsonUpdate);
                //break;
            case 6:
                //todo read messages
                break;
            case 7:
                // 6,$peer_id,$local_id — прочтение всех входящих сообщений с $peer_id вплоть до $local_id включительно
                // 7,$peer_id,$local_id — прочтение всех исходящих сообщений с $peer_id вплоть до $local_id включительно
                break;
            case 8:
                return new LongPollOnline(jsonUpdate);
            case 9:
                return new LongPollOffline(jsonUpdate);
            case 61:
                return new LongPollConversationTyping(jsonUpdate);
            case 62:
                return new LongPollChatTyping(jsonUpdate);
        }
        return "unparsed update: " + jsonUpdate;
    }

    @Override
    protected void onPostExecute(Object result) {
        if(result==null) return;
        if(result instanceof Exception){
            if(ExceptionUtil.isConnectionException(exception)) {
                onError(new ConnectionException(exception));
            } else {
                onError(exception);
            }
        } else {
            onSuccess((LongPollResponse) result);
        }





    }


    public abstract void onSuccess(LongPollResponse response);
    public abstract void onError(Exception exp);

    public void start() {
        execute();
    }


    public static class ServerException extends Exception {
    }

    private class ConnectionException extends Exception {
        private final Exception exp;

        public ConnectionException(Exception exception) {
            this.exp = exception;
        }

        @Override
        public String getLocalizedMessage() {
            return exp.getLocalizedMessage();
        }

        @Override
        public String getMessage() {
            return exp.getMessage();
        }

        @Override
        public String toString() {
            return exp.toString();
        }
    }
}
