package com.example.khambenh;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MajorAdapter extends BaseAdapter {
	Context context;
	ArrayList<Major> arrMajorData;

	public MajorAdapter(Context context, ArrayList<Major> results) {
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
			convertView = mInflater.inflate(R.layout.single_item_major, null);
			TextView tvMaCK = (TextView) convertView
					.findViewById(R.id.tvMajorMaCK);
			TextView tvTenCK = (TextView) convertView
					.findViewById(R.id.tvMajorTenCK);
			Major job = arrMajorData.get(position);
			tvMaCK.setText(job.getMaCK());
			tvTenCK.setText(job.getTenCK());
		}
		return convertView;
	}
}
