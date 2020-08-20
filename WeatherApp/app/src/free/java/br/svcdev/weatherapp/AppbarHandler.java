package br.svcdev.weatherapp;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;

public class AppbarHandler {

	public static void onClickItemMenuSearchLocation(Context context) {
		runActivity(context, SearchLocationActivity.class);
	}

	public static void onClickItemMenuMap(Context context){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Attention").setMessage("Available in the paid version.")
				.setCancelable(false).setPositiveButton(R.string.menu_ok, (dialog, id) -> {
		});
		AlertDialog alertDialog = builder.create();
		alertDialog.show();
	}

	public static void onClickItemMenuSettings(Context context) {
		runActivity(context, SettingsActivity.class);
	}

	private static void runActivity(Context context, Class<?> nameClass) {
		context.startActivity(new Intent(context, nameClass));
	}

}
