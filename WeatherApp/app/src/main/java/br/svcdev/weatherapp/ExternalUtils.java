package br.svcdev.weatherapp;

import android.util.Log;

public abstract class ExternalUtils {

    public static void printDebugLog(String string){
        Log.d(Constants.TAG_APP, string);
    }

}
