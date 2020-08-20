package br.svcdev.weatherapp;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;

import br.svcdev.weatherapp.fragments.MapsActivity;

public class AppbarHandler {

	public static void onClickItemMenuSearchLocation(Context context) {
		runActivity(context, SearchLocationActivity.class);
	}

	public static void onClickItemMenuMap(Context context) {
		runActivity(context, MapsActivity.class);
	}

	public static void onClickItemMenuSettings(Context context) {
		runActivity(context, SettingsActivity.class);
	}

	private static void runActivity(Context context, Class<?> nameClass) {
		context.startActivity(new Intent(context, nameClass));
	}

}
