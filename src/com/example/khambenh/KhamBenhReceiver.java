package com.example.khambenh;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class KhamBenhReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		Toast.makeText(context, "Có cuộc hẹn ", Toast.LENGTH_LONG).show();
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				new Intent(context, KhamBenhReceiver.class), 0);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				context).setSmallIcon(0).setContentTitle("Thông báo")
				.setContentText("Bạn có một cuộc hẹn khám bệnh");
		mBuilder.setContentIntent(contentIntent);
		mBuilder.setDefaults(Notification.DEFAULT_SOUND);
		mBuilder.setAutoCancel(true);
		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(1, mBuilder.build());
	}
}
