package com.example.khambenh;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.Toast;

public class ArrangementActivity extends Activity {
	Boolean isInternetPresent = false;
	ConnectionDetector cd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_arrangement);
		isInternetPresent = cd.isConnectingToInternet();
		if (!isInternetPresent) {
			Toast.makeText(this, "Không có kết nối!", Toast.LENGTH_LONG).show();
		} else {
			DataFragment dataFragment = new DataFragment();
			FragmentManager fragmentManager = getFragmentManager();
			FragmentTransaction ft = fragmentManager.beginTransaction();
			ft.replace(R.id.content_frame, dataFragment);
			ft.commit();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
