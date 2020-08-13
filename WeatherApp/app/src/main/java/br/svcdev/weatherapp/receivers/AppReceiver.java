package br.svcdev.weatherapp.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import br.svcdev.weatherapp.notifications.Notifications;

public class AppReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent == null) {
			return;
		}
		switch (intent.getAction()) {
			case ConnectivityManager.CONNECTIVITY_ACTION:
				ConnectivityManager connectivityManager =
						(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

				if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
					Notifications.getNotification(
							context,
							"2",
							"Type network",
							"Internet connection via Wifi"
					);
				} else if (networkInfo != null
						&& networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
					Notifications.getNotification(
							context,
							"2",
							"Type network",
							"Internet connection via mobile data"
					);
				} else {
					Notifications.getNotification(
							context,
							"2",
							"Type network",
							"There is no Internet connection"
					);
				}
				break;
			case Intent.ACTION_BATTERY_LOW:
				Notifications.getNotification(
						context,
						"2",
						"Broadcast receiver",
						"Battary is low"
				);
				break;
			case Intent.ACTION_AIRPLANE_MODE_CHANGED:
				Notifications.getNotification(
						context,
						"2",
						"Broadcast receiver",
						"Режим самолета"
				);
				break;
			default:
		}

	}
}
