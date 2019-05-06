package com.project.agu.steamstalk;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Locale;

public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {
    Context context;


    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);


        Preference idPref = findPreference("profile");
        Preference gameCount = findPreference("gameCount");
        Preference language = findPreference("language");

        idPref.setOnPreferenceChangeListener(this);
        gameCount.setOnPreferenceChangeListener(this);
        language.setOnPreferenceChangeListener(this);

        SharedPreferences id_pref = PreferenceManager
                .getDefaultSharedPreferences(this.getActivity().getApplicationContext());
        onPreferenceChange(idPref,id_pref.getString(idPref.getKey(),""));

        SharedPreferences game_count = PreferenceManager
                .getDefaultSharedPreferences(this.getActivity().getApplicationContext());
        onPreferenceChange(gameCount,game_count.getString(gameCount.getKey(),""));

        SharedPreferences langua = PreferenceManager
                .getDefaultSharedPreferences(this.getActivity().getApplicationContext());
        onPreferenceChange(language,langua.getString(language.getKey(),""));




    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String value = newValue.toString();
        preference.setSummary("");


        if(preference instanceof ListPreference)
        {
            Log.v("list",value);
            Locale locale = new Locale(value);
            locale.setDefault(locale);
            Configuration  config  = new Configuration();
            config.locale = locale;
            this.getActivity().getBaseContext().getResources().updateConfiguration(config,this.getActivity().getBaseContext().getResources().getDisplayMetrics());




        }




        return true;
    }
}
