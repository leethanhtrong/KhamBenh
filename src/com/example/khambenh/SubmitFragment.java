package com.example.khambenh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.submit_fragment, container, false);
		Email = getArguments().getString("Email");
		MaBS = getArguments().getString("Doctor");
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
				// thêm cuộc hẹn tại đây
				Toast.makeText(getActivity().getBaseContext(),
						"Nè dog", Toast.LENGTH_SHORT).show();
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
