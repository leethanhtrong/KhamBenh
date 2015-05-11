package com.example.khambenh;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AlarmService extends Service {

	private NotificationManager mManager;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@SuppressWarnings("static-access")
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		mManager = (NotificationManager) this.getApplicationContext()
				.getSystemService(
						this.getApplicationContext().NOTIFICATION_SERVICE);
		Intent in = new Intent(this.getApplicationContext(),
				ArrangementActivity.class);
		Notification notification = new Notification(R.drawable.ic_launcher,
				"This is a test message!", System.currentTimeMillis());
		in.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);

		PendingIntent pendingNotificationIntent = PendingIntent.getActivity(
				this.getApplicationContext(), 0, in,
				PendingIntent.FLAG_UPDATE_CURRENT);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.setLatestEventInfo(this.getApplicationContext(),
				"Thông báo", "This is a test message!",
				pendingNotificationIntent);
		mManager.notify(0, notification);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
