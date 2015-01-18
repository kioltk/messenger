package com.droidkit.pickers.map;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;

import com.droidkit.pickers.map.util.Timer;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by kioltk on 10/30/14.
 */
public class AddressTask extends AsyncTask<Void, Void, Address> {
    private final LatLng position;
    private final Context context;

    public AddressTask(Context context, LatLng position){
        this.context = context;
        this.position = position;
    }

    @Override
    protected Address doInBackground(Void... params) {
        Timer.start();

        try {


            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(position.latitude, position.longitude, 1);
            if (addresses.isEmpty()){
                return null;
            }
            Address address = addresses.get(0);

            Log.v("IGA", "Address " + address.toString());

            Timer.finish("Address was taken");
            return address;

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Timer.finish("Address was taken");
        return null;
    }
}
