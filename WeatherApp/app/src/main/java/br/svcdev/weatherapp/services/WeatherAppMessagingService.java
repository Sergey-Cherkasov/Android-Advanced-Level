package br.svcdev.weatherapp.services;

import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import br.svcdev.weatherapp.R;

public class WeatherAppMessagingService extends FirebaseMessagingService {

	private int messageId = 0;

	@Override
	public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
		String title = remoteMessage.getNotification().getTitle();
		if (title == null) {
			title = "Push Message";
		}
		String text = remoteMessage.getNotification().getBody();
		NotificationCompat.Builder builder =
				new NotificationCompat.Builder(this, "2")
						.setSmallIcon(R.mipmap.ic_launcher)
						.setContentTitle(title)
						.setContentText(text);
		NotificationManager notificationManager =
				(NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(messageId++, builder.build());
	}

	@Override
	public void onNewToken(@NonNull String token) {
		Log.d("PushMessage", "Token " + token);
		sendRegistrationToServer(token);
	}

	private void sendRegistrationToServer(String token) {

	}

}
