package com.kryptkode.cyberman.eritsmartdisplay;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.util.Log;


public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener{

    public static final String TAG = SettingsFragment.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);

        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        PreferenceScreen preferenceScreen = getPreferenceScreen();


            Preference pref = preferenceScreen.getPreference(1);
            if(pref instanceof PreferenceCategory){
                
                if(((PreferenceCategory) pref).getPreference(0) instanceof  ListPreference){
                    ListPreference listPreference = (ListPreference) ((PreferenceCategory) pref).getPreference(0);
                    String value = sharedPreferences.getString(listPreference.getKey(), getString(R.string.sort_default));
                    setPrefernceSummary(listPreference, value);
                    Log.i(TAG, "onCreatePreferences: List" +  value + pref.getKey());
                }

                Log.i(TAG, "onCreatePreferences: ");

            }


    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference pref = findPreference(key);
        if (pref != null) {
            if(pref instanceof ListPreference){
                String value = sharedPreferences.getString(pref.getKey(), getString(R.string.sort_default));
                setPrefernceSummary(pref, value);
            }
        }

    }



    private void setPrefernceSummary(Preference preference, String value){
        if(preference instanceof ListPreference){
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(value);
            if(prefIndex >=0){
                listPreference.setSummary(listPreference.getEntries()[prefIndex]);
            }

        }
    }
}
