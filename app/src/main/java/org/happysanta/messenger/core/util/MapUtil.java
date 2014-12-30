package org.happysanta.messenger.core.util;

import android.content.Context;
import android.net.Uri;

import org.happysanta.messenger.R;

import java.util.Map;

/**
 * Created by d_great on 26.12.14.
 */
public class MapUtil  {
    public static final String PARAMETER_MARKERS = "markers";

    public static final String PARAMETER_CENTER = "center";

    public static final String PARAMETER_SCALE = "scale";

    public static final String PARAMETER_SENSOR = "sensor";

    public static final String PARAMETER_SIZE = "size";

    public static final String PARAMETER_KEY = "key";

    public static final String PARAMETER_ZOOM = "zoom";

    public static final String PARAMETER_MAPTYPE = "maptype";

    private static String mKey;


    private static int mScale;


    private final static Uri BASE_URL = Uri.parse("https://maps.googleapis.com/maps/api/staticmap");

    public static Uri getMap(float latitude, float longitude, int width, int height, boolean sensor,
                      String marker) {
        final Uri.Builder b = BASE_URL.buildUpon();
        b.appendQueryParameter(PARAMETER_KEY, mKey);
        b.appendQueryParameter(PARAMETER_SIZE, width / mScale + "x" + height / mScale);
        b.appendQueryParameter(PARAMETER_SENSOR, sensor ? "true" : "false");
        b.appendQueryParameter(PARAMETER_SCALE, Integer.toString(mScale));
        final String latlon = latitude + "," + longitude;

        b.appendQueryParameter(PARAMETER_CENTER, latlon);

        if (marker != null) {
            b.appendQueryParameter(PARAMETER_MARKERS, marker + "|" + latlon);
        }
        return b.build();
    }

    public static void init(Context context){

        mKey = context.getResources().getString(R.string.google_static_map_api_key);
        mScale = 1;
    }
}
