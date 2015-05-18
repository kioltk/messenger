package org.happysanta.messenger.core;

import com.vk.sdk.api.model.VKApiMessage;

import org.happysanta.messenger.longpoll.LongpollService;
import org.happysanta.messenger.longpoll.listeners.LongpollListener;
import org.happysanta.messenger.longpoll.updates.LongpollNewMessage;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Jesus Christ. Amen.
 */
public class MessengerCore {
    private static MessengerCore core;


    public static void init() {
        new MessengerCore();
    }
    public MessengerCore(){
        MessengerCore.core = this;
        LongpollService.addListener(new LongpollListener() {
            @Override
            public int getId() {
                return LongpollService.MESSENGER_CORE_LISTENER_ID;
            }

            @Override
            public void onLongPollUpdate(Object update) {

            }
        });
    }
    public static final MessengerCore messenger(){
        return core;
    }

    public HashMap<Integer, ArrayList<VKApiMessage>> data = new HashMap<>();
    public HashMap<Integer, ArrayList<VKApiMessage>> sendingQueues = new HashMap<>();
    public HashMap<Integer, ArrayList<VKApiMessage>> errorQueues = new HashMap<>();

    public ArrayList<VKApiMessage> getMessages(int peerId){
        if(!data.containsKey(peerId)){
            data.put(peerId, new ArrayList<VKApiMessage>());
        }
        return data.get(peerId);
    }
    public ArrayList<VKApiMessage> getSendQueue(int peerId){
        if(!sendingQueues.containsKey(peerId)){
            sendingQueues.put(peerId, new ArrayList<VKApiMessage>());
        }
        return sendingQueues.get(peerId);
    }
    public ArrayList<VKApiMessage> getErrorQueue(int peerId){
        if(!errorQueues.containsKey(peerId)){
            errorQueues.put(peerId, new ArrayList<VKApiMessage>());
        }
        return errorQueues.get(peerId);
    }

    public void send(VKApiMessage message){
        ArrayList<VKApiMessage> sendingQueue = getSendQueue(message.getPeerId());
        ArrayList<VKApiMessage> convData = getMessages(message.getPeerId());
        convData.add(message);
        sendingQueue.add(message);
    }

    public void newMessages(ArrayList<LongpollNewMessage> newMessages) {
        ArrayList<LongpollNewMessage> filteredMessages = new ArrayList<>();
        for (LongpollNewMessage newMessage: newMessages) {

            if (!newMessage.out) {
                onInMessage(newMessage);
            } else{
                /*messages.add(newMessage);// todo add by sending query
                notifyItemInserted(messages.indexOf(newMessage)+1);*/
            }
            // update to full?
        }
    }

    public void onInMessage(LongpollNewMessage newMessage){
        ArrayList<VKApiMessage> messages = getMessages(newMessage.getPeerId());
        int pasteIndex = 0;
        for (int i = 0; i < messages.size(); i++) {
            VKApiMessage message = messages.get(i);
            if(message.id<newMessage.id){
                pasteIndex=i+1;
            }
        }
        messages.add(pasteIndex, newMessage);
    }

}