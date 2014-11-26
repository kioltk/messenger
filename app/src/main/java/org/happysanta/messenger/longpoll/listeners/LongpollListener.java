package org.happysanta.messenger.longpoll.listeners;

/**
 * Created by Jesus Christ. Amen.
 */
public interface LongpollListener {
    int getId();
    void onLongPollUpdate(Object update);
}
