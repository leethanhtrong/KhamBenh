//package com.example.khambenh;
//
//import android.app.AlertDialog;
//import android.app.Dialog;
//import android.content.DialogInterface;
//import android.os.Bundle;
//import android.support.v4.app.DialogFragment;
//
//public class AlertResultFragment extends DialogFragment {
//	@Override
//	public Dialog onCreateDialog(Bundle savedInstanceState) {
//		return new AlertDialog.Builder(getActivity())
//				.setIcon(R.drawable.ic_launcher)
//				.setTitle("Alert DialogFragment")
//				.setMessage("Alert DialogFragment Tutorial")
//				.setPositiveButton("Tiếp tục",
//						new DialogInterface.OnClickListener() {
//							public void onClick(DialogInterface dialog,
//									int which) {
//							}
//						})
//
//				// Negative Button
//				.setNegativeButton("Thoﾃ｡t",
//						new DialogInterface.OnClickListener() {
//							public void onClick(DialogInterface dialog,
//									int which) {
//								// Do something else
//							}
//						}).create();
//	}
//}