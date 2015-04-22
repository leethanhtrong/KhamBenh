package com.example.khambenh;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DoctorAdapter extends BaseAdapter {
	Context context;
	ArrayList<Doctor> arrMajorData;

	public DoctorAdapter(Context context, ArrayList<Doctor> results) {
		this.context = context;
		arrMajorData = results;
	}

	@Override
	public int getCount() {
		return arrMajorData.size();
	}

	@Override
	public Object getItem(int position) {
		return arrMajorData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return arrMajorData.indexOf(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		convertView = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.single_item_doctor, null);
			TextView tvMaBS = (TextView) convertView
					.findViewById(R.id.tvDoctorMaBS);
			TextView tvHoTen = (TextView) convertView
					.findViewById(R.id.tvDoctorHoTen);
			Doctor job = arrMajorData.get(position);
			tvMaBS.setText(job.getMaBS());
			tvHoTen.setText(job.getHoTen());
		}
		return convertView;
	}
}
