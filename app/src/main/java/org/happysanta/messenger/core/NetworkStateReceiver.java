package org.happysanta.messenger.core;

/**
 * Created by Jesus Christ. Amen.
 */
public abstract class NetworkStateReceiver {
    public final static String LONGPOLL_SERVICE_NETWORK_LISTENER = "LONGPOLL_SERVICE";

    public NetworkStateReceiver(String listenerKey) {
    }

    public abstract void onConnected();

    public abstract void onLost();
}
