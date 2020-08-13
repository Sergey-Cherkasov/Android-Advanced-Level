package br.svcdev.weatherapp;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Locale;

public abstract class ExternalUtils {

	public static void printDebugLog(final CharSequence className, final CharSequence text) {
		Log.d(Constants.TAG_APP, String.format(Locale.ENGLISH, "%s. %s",
				className.toString(), text.toString()));
	}

	public static String getDate(long milliseconds) {
		return new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
				.format(milliseconds * 1000);
	}

	public static String getTime(long milliseconds) {
		return new SimpleDateFormat("HH:mm", Locale.ENGLISH)
				.format(milliseconds * 1000);
	}
}
