package com.example.khambenh;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;

public class ResultDialog extends DialogFragment {
	String time;
	String email;
	String name;
	String makb;

	public ResultDialog() {

	}

	public ResultDialog(String time, String email, String name,String makb) {
		this.time = time;
		this.email = email;
		this.name = name;
		this.makb=makb;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		TextView myMsg = new TextView(getActivity());
		myMsg.setText("Mã khám bệnh: "+makb+"\nHọ tên: " + name + "\nThời gian hẹn: " + time
				+ "\nThông tin đăng ký đã được gửi về email " + email+"\nChúc bạn một ngày tốt lành!");
		myMsg.setGravity(Gravity.CENTER_VERTICAL);
		return new AlertDialog.Builder(getActivity())
				.setTitle("Đăng ký thành công").setMessage(myMsg.getText())

				.setPositiveButton(android.R.string.ok, new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						//bạn có muốn đặt cuộc hẹn mới???
						Intent i = new Intent(getActivity(),
								ArrangementActivity.class);
						startActivity(i);
					}
				}).create();
	}
}