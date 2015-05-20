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
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction ft = fragmentManager.beginTransaction();
		//if (nf != null && nf.isConnected() == true) {
			DataFragment dataFragment = new DataFragment();
			ft.replace(R.id.content_frame, dataFragment);
			ft.commit();
		/*} else {
			ErrorFragment errorFragment = new ErrorFragment();
			ft.replace(R.id.content_frame, errorFragment);
			ft.commit();
		}*/
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
