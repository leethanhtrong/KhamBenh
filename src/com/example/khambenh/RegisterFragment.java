package com.example.khambenh;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class RegisterFragment extends Fragment {
	Connect con = new Connect();
	JsonParseClass jsonParse = new JsonParseClass();
	final String urlRegister = con.getUrl()
			+ "/anroidWebservice/KhamBenhService/register.php";
	String successTag = null;
	private static final String TAG_SUCCESS = "success";
	EditText etName;
	EditText etPhone;
	TextView tvEmail;
	EditText etBirthDay;
	Spinner spGender;
	EditText etAddress;
	Button btnRegister;
	String Name, Email, Phone, Address, Gender, BirthDay, MaBS, Symtom, Time;
	private PendingIntent pendingIntent;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.register_fragment, container, false);
		Email = getArguments().getString("Email");
		MaBS = getArguments().getString("MaBS");
		Symtom = getArguments().getString("Symtom");
		Time = getArguments().getString("Time");
		etName = (EditText) v.findViewById(R.id.etRegisterName);
		etPhone = (EditText) v.findViewById(R.id.etRegisterPhone);
		tvEmail = (TextView) v.findViewById(R.id.tvRegisterEmail);
		tvEmail.setText(Email);
		etAddress = (EditText) v.findViewById(R.id.etRegisterAddress);
		spGender = (Spinner) v.findViewById(R.id.spRegisterGender);
		spGender.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View view,
					int arg2, long arg3) {
				Gender = spGender.getSelectedItem().toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		etBirthDay = (EditText) v.findViewById(R.id.etRegisterBirthDay);
		etBirthDay.setInputType(InputType.TYPE_NULL);
		etBirthDay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DialogFragment dialogFragment = new SelectDateFragment();
				dialogFragment.show(getFragmentManager(),
						"Chọn ngày tháng năm sinh");
			}
		});
		btnRegister = (Button) v.findViewById(R.id.btnRegister);
		btnRegister.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Name = etName.getText().toString().trim();
				Phone = etPhone.getText().toString().trim();
				Email = tvEmail.getText().toString().trim();
				Address = etAddress.getText().toString().trim();
				if (Name == null) {
					etName.setError("Vui lòng nhập họ tên");
				} else {
					if (Address == null) {
						etAddress.setError("Vui lòng nhập địa chỉ");
					} else {
						if (Phone == null) {
							etPhone.setError("Vui lòng nhập số điện thoại");
						} else {
							Register register = new Register();
							register.execute();
						}
					}
				}
			}
		});

		return v;
	}

	public class SelectDateFragment extends DialogFragment implements
			DatePickerDialog.OnDateSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			final Calendar calendar = Calendar.getInstance();
			int yy = calendar.get(Calendar.YEAR);
			int mm = calendar.get(Calendar.MONTH);
			int dd = calendar.get(Calendar.DAY_OF_MONTH);
			return new DatePickerDialog(getActivity(), this, yy, mm, dd);
		}

		public void onDateSet(DatePicker view, int yy, int mm, int dd) {
			populateSetDate(yy, mm + 1, dd);
		}

		public void populateSetDate(int year, int month, int day) {
			etBirthDay.setText(day + "-" + month + "-" + year);
		}

	}

	public class Register extends AsyncTask<String, String, String> {
		JSONObject jObject;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			Name = etName.getText().toString().trim();
			Address = etAddress.getText().toString().trim();
			Phone = etPhone.getText().toString().trim();
			Email = tvEmail.getText().toString().trim();
			BirthDay = etBirthDay.getText().toString().trim();
			Gender = spGender.getSelectedItem().toString();
			List<NameValuePair> validateParams = new ArrayList<NameValuePair>(6);
			validateParams.add(new BasicNameValuePair("email", Email));
			validateParams.add(new BasicNameValuePair("name", Name));
			validateParams.add(new BasicNameValuePair("address", Address));
			validateParams.add(new BasicNameValuePair("phone", Phone));
			validateParams.add(new BasicNameValuePair("birthday", BirthDay));
			validateParams.add(new BasicNameValuePair("gender", Gender));
			jObject = jsonParse.makeHttpRequest(urlRegister, "POST",
					validateParams);
			try {
				successTag = jObject.getString(TAG_SUCCESS);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return successTag;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (successTag.equalsIgnoreCase("1")) {
				String timeHour="";
				if(Time.substring(12, 13).equalsIgnoreCase(":")){
					timeHour=Time.substring(11, 12);
				}else{
					timeHour=Time.substring(11, 13);
				}
				
				String timeMonth=Time.substring(3, 5);
				String timeYear=Time.substring(6, 10);
				String timeDay=Time.substring(0, 2);
						
				RequestParams params = new RequestParams();
				params.put("MaBS", MaBS);
				params.put("NgayGio", timeYear+"-"+timeMonth+"-"+timeDay+" "+timeHour+":00:00");
				params.put("Email", Email);
				params.put("TrieuChung", Symtom);
				AsyncHttpClient client = new AsyncHttpClient();
				client.post(
						"http://minhhunglaw.com/anroidWebservice/khambenh/them",
						params, new AsyncHttpResponseHandler() {
							@Override
							public void onSuccess(String response) {
								FragmentTransaction ft = getFragmentManager()
										.beginTransaction();
								ResultDialog newFragment = new ResultDialog(Time, Email, etName
										.getText().toString(),response);
								newFragment.show(ft, "dialog");
							}

							@Override
							public void onFailure(int statusCode,
									Throwable error, String content) {
								if (statusCode == 404) {
									Toast.makeText(
											getActivity().getBaseContext(),
											"Requested resource not found",
											Toast.LENGTH_SHORT).show();
								}
								else if (statusCode == 500) {
									Toast.makeText(
											getActivity().getBaseContext(),
											"Something went wrong at server end",
											Toast.LENGTH_SHORT).show();
								}
								else {
									Toast.makeText(
											getActivity().getBaseContext(),
											"Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]",
											Toast.LENGTH_SHORT).show();
								}
							}
						});
				Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.MONTH,
						Integer.parseInt(timeMonth));
				calendar.set(Calendar.YEAR,
						Integer.parseInt(timeYear));
				calendar.set(Calendar.DAY_OF_MONTH,
						Integer.parseInt(timeDay) - 1);
				calendar.set(Calendar.HOUR, 7);
				calendar.set(Calendar.MINUTE, 0);
				
				Calendar compare = Calendar.getInstance();
				if(compare.get(Calendar.DAY_OF_MONTH)==Integer.parseInt(timeDay)&&compare.get(Calendar.MONTH)==calendar.get(Calendar.MONTH)&&compare.get(Calendar.YEAR)==calendar.get(Calendar.YEAR)){
					/*Toast.makeText(
							getActivity().getBaseContext(),
							"oh shit",
							Toast.LENGTH_SHORT).show();*/
				}else{
					Toast.makeText(
							getActivity().getBaseContext(),
							"oh shit",
							Toast.LENGTH_SHORT).show();
					long when = calendar.getTimeInMillis();
					Intent intent = new Intent(getActivity(),
							KhamBenhReceiver.class);
					PendingIntent pendingIntent = PendingIntent.getBroadcast(
							getActivity().getBaseContext(), 234324243, intent, 0);
					AlarmManager alarmManager = (AlarmManager) getActivity()
							.getSystemService(Context.ALARM_SERVICE);
					alarmManager.set(AlarmManager.RTC_WAKEUP,
							System.currentTimeMillis() + when, pendingIntent);
					
					
//					Intent myIntent = new Intent(getActivity(),
//					KhamBenhReceiver.class);
//			pendingIntent = PendingIntent.getBroadcast(getActivity(), 0,
//					myIntent, 0);
//			AlarmManager alarmManager = (AlarmManager) (getActivity()
//					.getSystemService(getActivity().ALARM_SERVICE));
//			alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(),
//					pendingIntent);
				}
			}
		}
	}
}
