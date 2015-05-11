package com.example.khambenh;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class KhamBenhReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent service1 = new Intent(context, AlarmService.class);
		context.startService(service1);

	}
}
