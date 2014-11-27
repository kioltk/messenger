package org.happysanta.messenger.longpoll.listeners;

import org.happysanta.messenger.longpoll.updates.LongpollOffline;
import org.happysanta.messenger.longpoll.updates.LongpollOnline;

/**
 * Created by Jesus Christ. Amen.
 */
public abstract class LongpollStatusListener {

    public abstract void onOnline(LongpollOnline[] onlines);
    public abstract void onOffline(LongpollOffline[] offlines);

}
