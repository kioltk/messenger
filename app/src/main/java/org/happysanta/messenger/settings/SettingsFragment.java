package org.happysanta.messenger.settings;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.TextUtils;
import android.util.Log;

import org.happysanta.messenger.R;
import org.happysanta.messenger.settings.views.LogoutPreference;
import org.happysanta.messenger.settings.views.PopupPreference;

/**
 * Created by Jesus Christ. Amen.
 */

public class SettingsFragment extends PreferenceFragment {

    public static final String NOTIFY_ENABLE_KEY        = "pref_notifications_enable";
    public static final String NOTIFY_VIBRATION_KEY     = "pref_vibration";
    public static final String NOTIFY_LED_BLINK_KEY     = "pref_led_blink";
    public static final String NOTIFY_POPUP_KEY         = "pref_popup_notify";
    public static final String NOTIFY_RINGTONE_KEY      = "pref_notify_ringtone";

    public static final String CONTACT_SYNC_PREF_KEY    = "pref_contact_sync";

    public static final String CH_ENABLE_KEY            = "pref_chat_heads";
    public static final String CH_HIDE_ON_FULLSCREEN    = "pref_chat_heads_hide_on_fullscreen";
    public static final String CH_START_FROM_TRAY_KEY   = "pref_chat_heads_start_from_tray";
    private static final CharSequence PREF_LOGOUT = "pref_logout";

    private SharedPreferences mSharedPreferences;

    @Override
    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);

        addPreferencesFromResource(R.xml.settings);

        // TODO fix RingtonePreference
        // listener below still does not work
        bindPreferenceSummaryToValue(findPreference(NOTIFY_RINGTONE_KEY));

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        initSummaries(findPreference(CONTACT_SYNC_PREF_KEY));

        LogoutPreference logoutPref = (LogoutPreference) findPreference(PREF_LOGOUT);
        logoutPref.setOnLogoutListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getActivity().finish();
            }
        });
        /*setOnKeyListener.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                Toast.makeText(getActivity(),"keycode " + keyCode,Toast.LENGTH_SHORT).show();
                return false;
            }
        });*/
    }

    private void initSummaries(Preference preference) {

        if (preference instanceof PopupPreference) {

            boolean isSync = mSharedPreferences.getBoolean(preference.getKey(), false);

            preference.setSummary(isSync ? "On" : "Off");

        }
    }

    //region bindPreferenceSummaryToValue
    private static void bindPreferenceSummaryToValue(Preference preference) {

        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener =
            new Preference.OnPreferenceChangeListener() {

        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {

            String stringValue = value.toString();


            Log.d("PrefChange", "value: " + stringValue);


            if (preference instanceof ListPreference) {

                ListPreference listPreference = (ListPreference) preference;
                int            index          = listPreference.findIndexOfValue(stringValue);

                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);

            } else if (preference instanceof RingtonePreference) {

                if (TextUtils.isEmpty(stringValue)) {

                    preference.setSummary(R.string.pref_summary_silent);

                } else {

                    Ringtone ringtone = RingtoneManager.getRingtone(
                            preference.getContext(), Uri.parse(stringValue));

                    if (ringtone == null) {

                        preference.setSummary(null);

                    } else {

                        String name = ringtone.getTitle(preference.getContext());
                        preference.setSummary(name);
                    }
                }

            } else {

                preference.setSummary(stringValue);
            }

            return true;
        }
    };
    //endregion

}
