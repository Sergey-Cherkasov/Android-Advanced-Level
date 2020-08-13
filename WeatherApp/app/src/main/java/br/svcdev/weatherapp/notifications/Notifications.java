package br.svcdev.weatherapp.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

import androidx.core.app.NotificationCompat;

import br.svcdev.weatherapp.R;

public class Notifications {

	private static int messageId = 1000;

	/**
	 * Метод инициализации канала уведомлений
	 *
	 * @param context Контекст активити
	 */
	public static void initNotificationChannel(Context context) {
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
			NotificationManager manager =
					(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			int importance = NotificationManager.IMPORTANCE_LOW;
			NotificationChannel channel = new NotificationChannel("2", "name", importance);
			manager.createNotificationChannel(channel);
		}
	}

	/**
	 * Метод получения уведомления
	 */
	public static void getNotification(Context context, String channelId, String contentTitle,
									   String contentText) {
		NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
				.setSmallIcon(R.drawable.ic_settings)
				.setContentTitle(contentTitle)
				.setContentText(contentText);
		NotificationManager notificationManager =
				(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(messageId++, builder.build());
	}
}
