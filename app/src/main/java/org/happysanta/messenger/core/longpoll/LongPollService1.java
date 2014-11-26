package org.happysanta.messenger.core.longpoll;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.content.Context;
import android.os.IBinder;
import android.util.Log;

import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.methods.VKApiMessages;
import com.vk.sdk.api.model.VKLongPollServer;

import org.happysanta.messenger.core.NetworkStateReceiver;
import org.happysanta.messenger.core.longpoll.listeners.LongPollListener;
import org.happysanta.messenger.core.longpoll.updates.LongPollNewMessage;
import org.happysanta.messenger.core.util.ExceptionUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class LongPollService1 extends Service {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_ENABLE = "org.happysanta.messenger.core.action.FOO";
    private static final String ACTION_DISABLE = "org.happysanta.messenger.core.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "org.happysanta.messenger.core.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "org.happysanta.messenger.core.extra.PARAM2";
    private static HashMap<Integer,LongPollListener> listeners = new HashMap<Integer, LongPollListener>();
    private VKLongPollServer server;

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void enable(Context context, String param1, String param2) {
        Intent intent = new Intent(context, LongPollService1.class);
        intent.setAction(ACTION_ENABLE);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void disable(Context context, String param1, String param2) {
        Intent intent = new Intent(context, LongPollService1.class);
        intent.setAction(ACTION_DISABLE);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }



    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_ENABLE.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                startLongPoll();
            } else if (ACTION_DISABLE.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionDisable(param1, param2);
            }
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        onHandleIntent(intent);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void startLongPoll() {
        VKRequest request = new VKApiMessages().getLongPollService();
        request.executeWithListener(new VKRequest.VKRequestListener() {


            @Override
            public void onComplete(VKResponse response) {
                server = (VKLongPollServer) response.parsedModel;
                connect();
            }

            @Override
            public void onError(VKError error) {
                if (ExceptionUtil.isConnectionException(error.httpError)) {
                    new NetworkStateReceiver(NetworkStateReceiver.LONGPOLL_SERVICE_NETWORK_LISTENER) {
                        @Override
                        public void onConnected() {
                        }

                        @Override
                        public void onLost() {
                        }
                    };
                }
                Log.e("AGCY SPY LONGPOLLSERVICE", "refreshing error: " + error);
            }

        });
    }

    private void connect() {
        new LongPollTask1(server) {
            @Override
            public void onSuccess(LongPollResponse response) {
                server.ts = response.ts;
                notifyListeners(response);
                connect();
            }

            @Override
            public void onError(Exception exp) {
                startLongPoll();
            }
        }.start();
    }


    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionDisable(String param1, String param2) {


    }

    public static void addListener(LongPollListener longPollListener) {
        listeners.put(longPollListener.getId(), longPollListener);
    }


    private void notifyListeners(LongPollResponse response) {

        ArrayList<LongPollNewMessage> messages = new ArrayList<LongPollNewMessage>();
        for (Object update : response.updates) {
            if(update instanceof LongPollNewMessage){
                messages.add((LongPollNewMessage) update);
            }
        }
        Collections.sort(messages, new Comparator<LongPollNewMessage>() {
            @Override
            public int compare(LongPollNewMessage lhs, LongPollNewMessage rhs) {
                return lhs.timestamp.compareTo(rhs.timestamp);
            }
        });

        // todo create listeners

        for (LongPollListener longPollListener : listeners.values()) {
            for (Object update : response.updates) {
                longPollListener.onLongPollUpdate(update);
            }
        }




    }
}
