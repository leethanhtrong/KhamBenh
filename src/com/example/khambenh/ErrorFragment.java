package com.example.khambenh;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class ErrorFragment extends Fragment {
	Button btnRetry;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.error_fragment, container, false);
		btnRetry = (Button) v.findViewById(R.id.btnRetry);
		btnRetry.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),
						ArrangementActivity.class);
				startActivity(intent);
			}
		});
		return v;
	}
}
