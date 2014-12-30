package com.vk.sdk.api.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by d_great on 26.12.14.
 */
public class VKApiGeo {

    public String type;
    public float lat;
    public float lon;

    public VKApiGeoPlace place = new VKApiGeoPlace();

    public VKApiGeo parse(JSONObject geo) throws JSONException {
        if(geo==null){
            return null;
        }
        String coordinates = geo.getString("coordinates");
        String[] coordinatesArray = coordinates.split(" ");
        lat = Float.parseFloat(coordinatesArray[0]);
        lon = Float.parseFloat(coordinatesArray[1]);
        JSONObject placeJson = geo.getJSONObject("place");
        place.title = placeJson.getString("title");
        place.city = placeJson.getString("city");
        place.country = placeJson.getString("country");
        return this;
    }


    public class VKApiGeoPlace{
        public String title;
        public String country;
        public String city;
    }

}
