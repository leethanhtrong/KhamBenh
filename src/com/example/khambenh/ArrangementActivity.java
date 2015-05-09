package com.example.khambenh;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

public class ArrangementActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_arrangement);
		ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo nf = cn.getActiveNetworkInfo();
		if (nf != null && nf.isConnected() == true) {
			DataFragment dataFragment = new DataFragment();
			FragmentManager fragmentManager = getFragmentManager();
			FragmentTransaction ft = fragmentManager.beginTransaction();
			ft.replace(R.id.content_frame, dataFragment);
			ft.commit();
		} else {
			ErrorFragment errorFragment = new ErrorFragment();
			FragmentManager fragmentManager = getFragmentManager();
			FragmentTransaction ft = fragmentManager.beginTransaction();
			ft.replace(R.id.content_frame, errorFragment);
			ft.commit();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
