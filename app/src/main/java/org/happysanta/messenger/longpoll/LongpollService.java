package org.happysanta.messenger.longpoll;

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
import org.happysanta.messenger.longpoll.listeners.LongpollDialogListener;
import org.happysanta.messenger.longpoll.listeners.LongpollListener;
import org.happysanta.messenger.longpoll.listeners.LongpollStatusListener;
import org.happysanta.messenger.longpoll.updates.LongpollNewMessage;
import org.happysanta.messenger.core.util.ExceptionUtil;
import org.happysanta.messenger.longpoll.updates.LongpollOffline;
import org.happysanta.messenger.longpoll.updates.LongpollOnline;
import org.happysanta.messenger.longpoll.updates.LongpollTyping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;


public class LongpollService extends Service {


    private static final String ACTION_TOGGLE = "org.happysanta.messenger.core.action.FOO";
    private static final String ACTION_DISABLE = "org.happysanta.messenger.core.action.BAZ";


    private static final String EXTRA_PARAM1 = "org.happysanta.messenger.core.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "org.happysanta.messenger.core.extra.PARAM2";
    private static HashMap<Integer,LongpollListener> listeners = new HashMap<Integer, LongpollListener>();
    private VKLongPollServer server;

    public static void toggle(Context context, String param1, String param2) {
        Intent intent = new Intent(context, LongpollService.class);
        intent.setAction(ACTION_TOGGLE);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    public static void disable(Context context, String param1, String param2) {
        Intent intent = new Intent(context, LongpollService.class);
        intent.setAction(ACTION_DISABLE);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }



    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_TOGGLE.equals(action)) {
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
        new LongpollTask(server) {
            @Override
            public void onSuccess(LongpollResponse response) {
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



    static HashMap<Integer, LongpollDialogListener> conversationListeners = new HashMap<Integer,LongpollDialogListener>();
    static HashMap<Integer, LongpollDialogListener> globalConversationListeners = new HashMap<Integer,LongpollDialogListener>();
    private LongpollDialogListener globalConversationListener = new LongpollDialogListener(0) {
        @Override
        public void onNewMessages(ArrayList<LongpollNewMessage> newMessages) {
        }

        @Override
        public void onTyping(ArrayList<LongpollTyping> typing) {
            // todo implement global conversation listener
        }
    };
    static HashMap<Integer, LongpollDialogListener> chatListeners = new HashMap<Integer,LongpollDialogListener>(); // chatid, chat listener
    static HashMap<Integer, LongpollDialogListener> globalChatListeners = new HashMap<Integer,LongpollDialogListener>();
    private LongpollDialogListener globalChatListener = new LongpollDialogListener(0) {
        @Override
        public void onNewMessages(ArrayList<LongpollNewMessage> newMessages) {
        }

        @Override
        public void onTyping(ArrayList<LongpollTyping> typing) {
            // todo implement global chat listener
        }
    };

    HashMap<Integer, LongpollStatusListener> onlinesListeners = new HashMap<Integer, LongpollStatusListener>();

    private void notifyListeners(LongpollResponse response) {

        ArrayList<LongpollNewMessage> messages = new ArrayList<LongpollNewMessage>();
        ArrayList<LongpollOnline> onlines = new ArrayList<LongpollOnline>();
        ArrayList<LongpollOffline> offlines = new ArrayList<LongpollOffline>();
        ArrayList<LongpollTyping> typings = new ArrayList<>();
        for (Object update : response.updates) {
            if (update instanceof LongpollNewMessage) {
                messages.add((LongpollNewMessage) update);
            } else {
                if (update instanceof LongpollOnline) {
                    onlines.add((LongpollOnline) update);
                } else {
                    if (update instanceof LongpollOffline) {
                        offlines.add((LongpollOffline) update);
                    } else {
                        if (update instanceof LongpollTyping) {
                            typings.add((LongpollTyping) update);
                        }
                    }
                }
            }
        }
        Collections.sort(messages, new Comparator<LongpollNewMessage>() {
            @Override
            public int compare(LongpollNewMessage lhs, LongpollNewMessage rhs) {
                return ((Long)lhs.date).compareTo(rhs.date);
            }
        });

        HashMap<Integer, ArrayList<LongpollNewMessage>> chatMessagesPacks = new HashMap<Integer, ArrayList<LongpollNewMessage>>();
        HashMap<Integer, ArrayList<LongpollNewMessage>> conversationsMessagesPacks = new HashMap<Integer, ArrayList<LongpollNewMessage>>();
        HashMap<Integer, ArrayList<LongpollTyping>> chatTypingsPacks = new HashMap<>();
        HashMap<Integer, ArrayList<LongpollTyping>> conversationTypingsPacks = new HashMap<>();


        // запаковываем
        for (LongpollNewMessage message : messages) {
            ArrayList<LongpollNewMessage> pack;
            if (message.isChat) {
                if (!chatMessagesPacks.containsKey(message.chat_id)) {
                    pack = new ArrayList<LongpollNewMessage>();
                    chatMessagesPacks.put(message.chat_id, pack);
                } else {
                    pack = chatMessagesPacks.get(message.chat_id);
                }
            } else {
                if (!conversationsMessagesPacks.containsKey(message.user_id)) {
                    pack = new ArrayList<LongpollNewMessage>();
                    conversationsMessagesPacks.put(message.user_id, pack);
                } else {
                    pack = conversationsMessagesPacks.get(message.user_id);
                }
            }
            pack.add(message);
        }

        // пакуем тайпинги
        for (LongpollTyping typing : typings) {
            ArrayList<LongpollTyping> pack;
            if (typing.isChat) {
                if (!chatTypingsPacks.containsKey(typing.dialogId)) {
                    pack = new ArrayList<>();
                    chatTypingsPacks.put(typing.dialogId, pack);
                } else {
                    pack = chatTypingsPacks.get(typing.dialogId);
                }
            } else {
                if (!conversationTypingsPacks.containsKey(typing.dialogId)) {
                    pack = new ArrayList<>();
                    conversationTypingsPacks.put(typing.dialogId, pack);
                } else {
                    pack = conversationTypingsPacks.get(typing.dialogId);
                }
            }
            pack.add(typing);
        }

        // бросаем пачки тайпингов по чатам
        for (Map.Entry<Integer, ArrayList<LongpollTyping>> chatMessagesPackEntry : chatTypingsPacks.entrySet()) {
            Integer chatid = chatMessagesPackEntry.getKey();
            ArrayList<LongpollTyping> chatMessagesPack = chatMessagesPackEntry.getValue();
            if (chatListeners.containsKey(chatid)) {
                LongpollDialogListener chatListener = chatListeners.get(chatid);
                chatListener.onTyping(chatMessagesPack);
            } else {
                globalChatListener.onTyping(chatMessagesPack);
            }
        }
        // бросаем пачки тайпингов по диалогам
        for (Map.Entry<Integer, ArrayList<LongpollTyping>> conversationPackEntry : conversationTypingsPacks.entrySet()) {
            Integer conversationId = conversationPackEntry.getKey();
            ArrayList<LongpollTyping> conversationPackMessages = conversationPackEntry.getValue();
            if (conversationListeners.containsKey(conversationId)) {
                LongpollDialogListener conversationListener = conversationListeners.get(conversationId);
                conversationListener.onTyping(conversationPackMessages);
            } else {
                globalConversationListener.onTyping(conversationPackMessages);
            }
        }

        // бросаем пачки сообщений по чатам
        for (Map.Entry<Integer, ArrayList<LongpollNewMessage>> chatPackEntry : chatMessagesPacks.entrySet()) {
            Integer chatid = chatPackEntry.getKey();
            ArrayList<LongpollNewMessage> chatPackMessages = chatPackEntry.getValue();
            if (chatListeners.containsKey(chatid)) {
                LongpollDialogListener chatListener = chatListeners.get(chatid);
                chatListener.onNewMessages(chatPackMessages);
            }
            for (LongpollDialogListener globalChatListener : globalChatListeners.values()) {
                globalChatListener.onNewMessages(chatPackMessages);
            }
        }
        // бросаем пачки сообщений по диалогам
        for (Map.Entry<Integer, ArrayList<LongpollNewMessage>> conversationPackEntry : conversationsMessagesPacks.entrySet()) {
            Integer conversationId = conversationPackEntry.getKey();
            ArrayList<LongpollNewMessage> conversationPackMessages = conversationPackEntry.getValue();
            if (conversationListeners.containsKey(conversationId)) {
                LongpollDialogListener conversationListener = conversationListeners.get(conversationId);
                conversationListener.onNewMessages(conversationPackMessages);
            }
            for (LongpollDialogListener globalConversationListener : globalConversationListeners.values()) {
                globalConversationListener.onNewMessages(conversationPackMessages);
            }
        }

        for (Object update : response.updates) {
            for (LongpollListener longpollListener : listeners.values()) {
                longpollListener.onLongPollUpdate(update);
            }
            Log.d("Longpoll update",update.toString());
        }

    }

    public static void addListener(LongpollListener listener) {
        listeners.put(listener.getId(),listener);
    }
    public static void addConversationListener(LongpollDialogListener listener){
        conversationListeners.put(listener.getId(),  listener);
    }

    public static void addGlobalConversationListener(LongpollDialogListener listener) {
        globalConversationListeners.put(listener.getId(),listener);
    }

    public static void addChatListener(LongpollDialogListener chatListener){
        chatListeners.put(chatListener.getId(),chatListener);
    }
    public static void addGlobalChatListener(LongpollDialogListener longpollDialogListener) {
        globalChatListeners.put(longpollDialogListener.getId(),longpollDialogListener);
    }
}
