package com.example.khambenh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlarmManager;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class SubmitFragment extends Fragment {
	Connect con = new Connect();
	String successTag = null;
	private String jsonResult;
	JsonParseClass jsonParse = new JsonParseClass();
	private String urlSubmit = con.getUrl()
			+ "/anroidWebservice/KhamBenhService/submit.php";
	TextView tvName;
	TextView tvPhone;
	TextView tvEmail;
	TextView tvBirthDay;
	TextView tvGender;
	TextView tvAddress;
	Button btnSubmit;
	String Email, MaBS, Symtom, Time;
	private PendingIntent pendingIntent;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.submit_fragment, container, false);
		Email = getArguments().getString("Email");
		MaBS = getArguments().getString("MaBS");
		Symtom = getArguments().getString("Symtom");
		Time = getArguments().getString("Time");
		tvName = (TextView) v.findViewById(R.id.tvSubmitName);
		tvPhone = (TextView) v.findViewById(R.id.tvSubmitPhone);
		tvEmail = (TextView) v.findViewById(R.id.tvSubmitEmail);
		tvEmail.setText(Email);
		tvAddress = (TextView) v.findViewById(R.id.tvSubmitAddress);
		tvGender = (TextView) v.findViewById(R.id.tvSubmitGender);
		tvBirthDay = (TextView) v.findViewById(R.id.tvSubmitBirthDay);
		btnSubmit = (Button) v.findViewById(R.id.btnSubmit);
		btnSubmit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/*RequestParams params = new RequestParams();
				params.put("MaBS", MaBS);
				params.put("NgayGio", Time);
				params.put("Email", Email);
				params.put("TrieuChung", Symtom);
				AsyncHttpClient client = new AsyncHttpClient();
				client.post(
						"http://minhhunglaw.com/anroidWebservice/khambenh/them",
						params, new AsyncHttpResponseHandler() {
							@Override
							public void onSuccess(String response) {
								// Hide Progress Dialog
								Toast.makeText(getActivity().getBaseContext(),
										"Thêm cuộc hẹn hành công " + response,
										Toast.LENGTH_LONG).show();
							}

							// When the response returned by REST has Http
							// response code
							// other than '200'
							@Override
							public void onFailure(int statusCode,
									Throwable error, String content) {
								// Hide Progress Dialog

								// When Http response code is '404'
								if (statusCode == 404) {
									Toast.makeText(
											getActivity().getBaseContext(),
											"Requested resource not found",
											Toast.LENGTH_SHORT).show();
								}
								// When Http response code is '500'
								else if (statusCode == 500) {
									Toast.makeText(
											getActivity().getBaseContext(),
											"Something went wrong at server end",
											Toast.LENGTH_SHORT).show();
								}
								// When Http response code other than 404, 500
								else {
									Toast.makeText(
											getActivity().getBaseContext(),
											"Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]",
											Toast.LENGTH_SHORT).show();
								}
							}
						});*/
				Calendar calendar = Calendar.getInstance();
				
				calendar.set(Calendar.MONTH,
						Integer.parseInt(Time.substring(3, 5)));
				calendar.set(Calendar.YEAR,
						Integer.parseInt(Time.substring(6, 10)));
				//if(Integer.parseInt(Time.substring(3, 5)==calendar.get())
				calendar.set(Calendar.DAY_OF_MONTH,
						Integer.parseInt(Time.substring(0, 2))-1);
				calendar.set(Calendar.HOUR,
						7);
				
				/*if(Time.substring(12, 13).equalsIgnoreCase(":")){
					calendar.set(Calendar.HOUR,
							7);
				}else{
					calendar.set(Calendar.HOUR,
							Integer.parseInt(Time.substring(11, 13)));
				}*/
				calendar.set(Calendar.MINUTE,
						0);
				
				/*Toast.makeText(
						getActivity().getBaseContext(),
						Long.toString(when),
						Toast.LENGTH_LONG).show();*/
				
				//Calendar now = Calendar.getInstance();
/*				Integer dayNow=now.get(Calendar.DAY_OF_MONTH);
				Integer monthNow=now.get(Calendar.MONTH);
				Integer yearNow=now.get(Calendar.YEAR);*/
				//long seconds=calendar.getTimeInMillis()-now.getTimeInMillis();
				
				/*Date today1 = new Date(calendar.getTimeInMillis()); 
				Calendar test = Calendar.getInstance();  
				test.setTime(today1);
				Toast.makeText(
				getActivity().getBaseContext(),
				Integer.toString(test.get(calendar.DAY_OF_MONTH)),
				Toast.LENGTH_LONG).show();*/
				
				Intent myIntent = new Intent(getActivity(),
						KhamBenhReceiver.class);
				pendingIntent = PendingIntent.getBroadcast(getActivity(), 0,
						myIntent, 0);
				AlarmManager alarmManager = (AlarmManager) (getActivity()
						.getSystemService(getActivity().ALARM_SERVICE));
				alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(),
						pendingIntent);
			};
		});
		accessWebService();
		return v;
	}

	private class JsonReadTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... params) {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(params[0]);
			List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);
			nameValuePair.add(new BasicNameValuePair("email", Email));
			try {
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePair));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			try {
				HttpResponse response = httpclient.execute(httppost);
				jsonResult = inputStreamToString(
						response.getEntity().getContent()).toString();
			}

			catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		private StringBuilder inputStreamToString(InputStream is) {
			String rLine = "";
			StringBuilder answer = new StringBuilder();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));

			try {
				while ((rLine = rd.readLine()) != null) {
					answer.append(rLine);
				}
			}

			catch (IOException e) {
			}
			return answer;
		}

		@Override
		protected void onPostExecute(String result) {
			getData();
		}
	}

	public void accessWebService() {
		JsonReadTask task = new JsonReadTask();
		task.execute(new String[] { urlSubmit });
	}

	private void getData() {
		try {
			JSONObject jsonResponse = new JSONObject(jsonResult);
			JSONArray jsonMainNode = jsonResponse.optJSONArray("benhnhan");
			for (int i = 0; i < jsonMainNode.length(); i++) {
				JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
				tvName.setText(jsonChildNode.optString("HoTen"));
				tvAddress.setText(jsonChildNode.optString("DChi"));
				tvPhone.setText(jsonChildNode.optString("DienThoai"));
				tvBirthDay.setText(jsonChildNode.optString("NgaySinh"));
				tvGender.setText(jsonChildNode.optString("GioiTinh"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
