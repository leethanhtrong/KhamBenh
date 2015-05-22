package com.example.khambenh;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
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
		myMsg.setText("Mã khám bệnh: "+makb+"\nHọ tên: " + name + "Thời gian hẹn: " + time
				+ "Thông tin đăng ký của bạn đã được gửi về email" + email);
		myMsg.setGravity(Gravity.CENTER_VERTICAL);
		return new AlertDialog.Builder(getActivity())
				.setTitle("Đăng ký thành công").setMessage(myMsg.getText())
				.setNegativeButton(android.R.string.no, new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// yes thì làm j
					}
				})
				.setPositiveButton(android.R.string.yes, new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// cancel thì làm j
					}
				}).create();
	}
}