package br.svcdev.weatherapp;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public abstract class WeatherAppPreferenceUtils {

    private static volatile SharedPreferences sPreferencesInstance;

    public static SharedPreferences getPreferencesInstance(Context context){
        if (sPreferencesInstance == null) {
            synchronized (WeatherAppPreferenceUtils.class) {
                sPreferencesInstance = PreferenceManager.getDefaultSharedPreferences(context);
            }
        }
        return sPreferencesInstance;
    }



}
