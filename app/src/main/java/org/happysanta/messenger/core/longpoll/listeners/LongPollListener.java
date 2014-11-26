package org.happysanta.messenger.core.longpoll.listeners;

/**
 * Created by Jesus Christ. Amen.
 */
public interface LongPollListener {
    int getId();
    void onLongPollUpdate(Object update);
}
