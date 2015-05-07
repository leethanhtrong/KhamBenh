package com.example.khambenh;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
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
import com.loopj.android.http.RequestParams;

public class RegisterFragment extends Fragment {
	Connect con = new Connect();
	JsonParseClass jsonParse = new JsonParseClass();
	final String urlRegister = con.getUrl()
			+ "/anroidWebservice/KhamBenhService/register.php";
	private String urlFinish = con.getUrl()
			+ "/anroidWebservice/KhamBenhService/finish.php";
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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.register_fragment, container, false);
		Email = getArguments().getString("Email");
		MaBS = getArguments().getString("Doctor");
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
				Toast.makeText(getActivity().getBaseContext(),
						"Thêm mới thành công", Toast.LENGTH_SHORT).show();
				  RequestParams params = new RequestParams(); params.put("MaBS",
				  MaBS); params.put("NgayGio", Time.substring(0,3) + "-" + Time.substring(5, 6) + "-"
				  + Time.substring(8, 9) + " " + startHour + ":00:00"); params.put("Email",
				  Email); params.put("TrieuChung",
				  Symtom);
				  AsyncHttpClient client = new AsyncHttpClient();
				  client.post(con.getUrl() + "/anroidWebservice/khambenh/them",
				  params, new AsyncHttpResponseHandler() { 
				  @Override public void onSuccess(String response) {  
				  Toast.makeText(
				  getActivity().getApplicationContext(), response,
				  Toast.LENGTH_LONG).show(); tvSelectedTime.setText(response); } //
				  When the response returned by REST has Http response // code //
				  other than '200
				  
				  @Override public void onFailure(int statusCode, Throwable error,
				  String content) { 
				  if (statusCode == 404) { Toast.makeText(
				  getActivity().getApplicationContext(),
				 "Requested resource not found", Toast.LENGTH_LONG).show(); } 
				  else if (statusCode == 500) {
				  Toast.makeText( getActivity().getApplicationContext(),
				  "Something went wrong at server end", Toast.LENGTH_LONG).show();
				  } 
				  else {
				  Toast.makeText( getActivity().getApplicationContext(),
				  "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]"
				  , Toast.LENGTH_LONG).show(); } }} );
				 
			} else {
				Toast.makeText(getActivity().getBaseContext(),
						"Thêm mới thất bại", Toast.LENGTH_LONG).show();
			}
		}
	}

	public class Finish extends AsyncTask<String, String, String> {
		JSONObject jObject;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			List<NameValuePair> validateParams = new ArrayList<NameValuePair>(
					10);
			validateParams.add(new BasicNameValuePair("Email", Email));
			validateParams.add(new BasicNameValuePair("MaBS", MaBS));
			validateParams.add(new BasicNameValuePair("TrieuChung", Symtom));
			validateParams.add(new BasicNameValuePair("ThoiGian", Time));
			jObject = jsonParse.makeHttpRequest(urlFinish, "POST",
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
				Toast.makeText(getActivity().getBaseContext(),
						"Đăng ký thành công", Toast.LENGTH_SHORT).show();
				Finish finish = new Finish();
				finish.execute();
			} else {
				Toast.makeText(getActivity().getBaseContext(),
						"Đăng ký thất bại", Toast.LENGTH_LONG).show();
			}
		}
	}

}
