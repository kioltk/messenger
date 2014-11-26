package org.happysanta.messenger.core.longpoll;

import org.happysanta.messenger.core.longpoll.updates.LongPollNewMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Jesus Christ. Amen.
 */
public class LongPollResponse {
    public int ts;
    public ArrayList updates = new ArrayList();

}
