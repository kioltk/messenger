package org.happysanta.messenger.settings.customElements;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import org.happysanta.messenger.R;

/**
 * Created by alex on 05/01/15.
 */
public class PopupPreference extends DialogPreference {

    private SharedPreferences mPreferences;

    public PopupPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public PopupPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PopupPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PopupPreference(Context context) {
        super(context);
    }


    @Override
    protected View onCreateDialogView() {

        mPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        Log.d("onCreate", "key = " + getKey()
                + " val = " + mPreferences.getBoolean(getKey(), false));

        if (mPreferences.getBoolean(getKey(), false)) {

            setDialogMessage(getContext().getString(R.string.pref_msg_stop_sync));

        } else {

            setDialogMessage(getContext().getString(R.string.pref_msg_start_sync));
        }

        return super.onCreateDialogView();
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        Log.d("onClose", "key = " + getKey() + " result = " + positiveResult);

        mPreferences    = PreferenceManager.getDefaultSharedPreferences(getContext());

        boolean isSync  = mPreferences.getBoolean(getKey(), false);

        SharedPreferences.Editor editor = mPreferences.edit();

        if (positiveResult) {

            editor.putBoolean(getKey(), !isSync);
            editor.apply();

        } else return;

        // updating summary

        setSummary(!isSync ? "On" : "Off");
    }
}
