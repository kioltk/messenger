package org.happysanta.messenger.core.util;

import org.apache.http.HttpException;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

/**
 * Created by Jesus Christ. Amen.
 */
public class ExceptionUtil {
    public static boolean isConnectionException(Exception exception) {
        return exception instanceof SocketException ||
                exception instanceof HttpException ||
                exception instanceof TimeoutException ||
                exception instanceof UnknownHostException;
    }
}
