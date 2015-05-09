package com.example.khambenh;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class FirstActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_first);
		Thread background = new Thread() {
			public void run() {
				try {
					// Thread will sleep for 5 seconds
					sleep(5 * 1000);
					// After 5 seconds redirect to another intent
					Intent i = new Intent(getBaseContext(),
							ArrangementActivity.class);
					startActivity(i);
					// Remove activity
					finish();
				} catch (Exception e) {

				}
			}
		};
		// start thread
		background.start();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
