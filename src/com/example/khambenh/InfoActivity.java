package com.example.khambenh;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class InfoActivity extends Activity {
	Connect con = new Connect();
	private String url = con.getUrl()
			+ "/anroidWebservice/KhamBenhService/checkmail.php";
	private EditText etEmail;
	String successTag = null;
	JsonParseClass jsonParse = new JsonParseClass();
	private static final String TAG_SUCCESS = "success";
	private Button btnCheckMail;
	String Email, MaBS, Symtom, Time;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);
		Bundle bun = getIntent().getExtras();
		MaBS = bun.getString("Doctor");
		Symtom = bun.getString("Symtom");
		Time = bun.getString("Time");
		etEmail = (EditText) findViewById(R.id.etEmail);
		btnCheckMail = (Button) findViewById(R.id.btnCheck);
		btnCheckMail.setOnClickListener(new OnClickListener() {
			private boolean isValidEmail(String email) {
				String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
						+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

				Pattern pattern = Pattern.compile(EMAIL_PATTERN);
				Matcher matcher = pattern.matcher(email);
				return matcher.matches();
			}

			public void onClick(View v) {
				if (!isValidEmail(etEmail.getText().toString().trim())) {
					etEmail.setError("Email không hợp lệ");
				} else {
					CheckMail chkMail = new CheckMail();
					chkMail.execute();
				}
			}
		});
	}

	public class CheckMail extends AsyncTask<String, String, String> {
		JSONObject jObject;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			Email = etEmail.getText().toString().trim();
			List<NameValuePair> validateParams = new ArrayList<NameValuePair>();
			validateParams.add(new BasicNameValuePair("email", Email));
			jObject = jsonParse.makeHttpRequest(url, "POST", validateParams);
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
			Bundle bundle = new Bundle();
			bundle.putString("Email", Email);
			bundle.putString("MaBS", MaBS);
			bundle.putString("Symtom", Symtom);
			bundle.putString("Time", Time);
			if (successTag.equalsIgnoreCase("1")) {
				SubmitFragment submitFragment = new SubmitFragment();
				submitFragment.setArguments(bundle);
				FragmentManager fragmentManager = getFragmentManager();
				FragmentTransaction ft = fragmentManager.beginTransaction();
				ft.replace(R.id.content_frame, submitFragment);
				ft.commit();
			} else {
				RegisterFragment registerFragment = new RegisterFragment();
				registerFragment.setArguments(bundle);
				FragmentManager fragmentManager = getFragmentManager();
				FragmentTransaction ft = fragmentManager.beginTransaction();
				ft.replace(R.id.content_frame, registerFragment);
				ft.commit();
			}
		}
	}
}
