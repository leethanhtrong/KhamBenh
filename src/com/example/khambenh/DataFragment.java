package com.example.khambenh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
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
import android.content.Intent;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class DataFragment extends Fragment implements
		WeekView.MonthChangeListener, WeekView.EventClickListener,
		WeekView.EventLongPressListener {
	Connect con = new Connect();
	private static final int TYPE_DAY_VIEW = 1;
	private static final int TYPE_THREE_DAY_VIEW = 2;
	private static final int TYPE_WEEK_VIEW = 3;
	private int mWeekViewType = TYPE_THREE_DAY_VIEW;
	private WeekView mWeekView;
	private String jsonResult;
	private String urlMajor = con.getUrl()
			+ "/anroidWebservice/KhamBenhService/major.php";
	private String urlDoctor = con.getUrl()
			+ "/anroidWebservice/KhamBenhService/doctor.php";
	private Spinner spMajor;
	private Spinner spDoctor;
	private Button btnSubmit;
	private EditText etSymptom;
	private TextView tvSelectedTime;
	String MaCK;
	String MaBS;
	String Time;
	String Symptom;
	MajorAdapter Madapter;
	View parentContainer;
	DataFragment dataFragment;
	private int countClear = 0;
	DoctorAdapter Dadapter;
	List<String> cuocHen = new ArrayList<String>();
	List<String> day = new ArrayList<String>();
	List<String> start = new ArrayList<String>();
	List<String> end = new ArrayList<String>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.data_fragment, container, false);
		parentContainer = inflater.inflate(R.layout.data_fragment, container,
				false);
		dataFragment = this;
		accessWebServiceMajor();
		etSymptom = (EditText) v.findViewById(R.id.etSymptom);
		tvSelectedTime = (TextView) v.findViewById(R.id.tvSelectedTime);
		//tvSelectedTime.clearFocus();
		spMajor = (Spinner) v.findViewById(R.id.spMajor);
		spMajor.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View v,
					int position, long id) {
				MaCK = ((TextView) v.findViewById(R.id.tvMajorMaCK)).getText()
						.toString();
				accessWebServiceDoctor();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
			}
		});
		spDoctor = (Spinner) v.findViewById(R.id.spDoctor);
		spDoctor.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View v,
					int position, long id) {

				MaBS = ((TextView) v.findViewById(R.id.tvDoctorMaBS)).getText()
						.toString();
				invokeWS(MaBS);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
			}

		});
		btnSubmit = (Button) v.findViewById(R.id.btnContinute);
		btnSubmit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Time = tvSelectedTime.getText().toString();
				if (Time.trim().length() == 0) {
					Toast.makeText(getActivity(),
							"Vui lòng chọn thời gian hẹn bác sĩ",
							Toast.LENGTH_SHORT).show();
				} else {
					Symptom = etSymptom.getText().toString();
					if (Symptom.trim().length() == 0) {
						Toast.makeText(getActivity(),
								"Vui lòng nhập triệu chứng bạn mắc phải",
								Toast.LENGTH_SHORT).show();
					} else {
						Bundle bun = new Bundle();
						bun.putString("MaBS", MaBS);
						bun.putString("Time", Time);
						bun.putString("Symtom", Symptom);
						Intent intent = new Intent(getActivity(),
								InfoActivity.class);
						intent.putExtras(bun);
						startActivity(intent);
						getActivity().overridePendingTransition(
								R.anim.slide_in_right, R.anim.slide_out_left);
					}
				}
			}
		});
		mWeekView = (WeekView) v.findViewById(R.id.weekView);
		mWeekView.setOnEventClickListener(this);
		mWeekView.setMonthChangeListener(this);
		mWeekView.setEventLongPressListener(this);
		return v;
	}

	private class JsonReadTaskPostMajor extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(params[0]);
			try {
				HttpResponse response = httpclient.execute(httppost);
				jsonResult = inputStreamToStringMajor(
						response.getEntity().getContent()).toString();
			}

			catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		private StringBuilder inputStreamToStringMajor(InputStream is) {
			String rLine = "";
			StringBuilder answer = new StringBuilder();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));

			try {
				while ((rLine = rd.readLine()) != null) {
					answer.append(rLine);
				}
			}

			catch (IOException e) {
				Toast.makeText(getActivity().getBaseContext(),
						"Error..." + e.toString(), Toast.LENGTH_LONG).show();
			}
			return answer;
		}

		@Override
		protected void onPostExecute(String result) {
			getMajor();
		}
	}

	public void accessWebServiceMajor() {
		JsonReadTaskPostMajor taskMajor = new JsonReadTaskPostMajor();
		taskMajor.execute(new String[] { urlMajor });
	}

	public void getMajor() {
		ArrayList<Major> results = new ArrayList<Major>();
		try {
			
			JSONObject jsonResponse = new JSONObject(jsonResult);
			JSONArray jsonMainNode = jsonResponse.optJSONArray("chuyenkhoa");
			Major major;
			for (int i = 0; i < jsonMainNode.length(); i++) {
				major = new Major();
				JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
				major.setMaCK(jsonChildNode.optString("MaCK"));
				major.setTenCK(jsonChildNode.optString("TenCK"));
				results.add(major);
			}
		} catch (JSONException e) {
			Toast.makeText(getActivity().getBaseContext(),
					"Error" + e.toString(), Toast.LENGTH_SHORT).show();
		}

		Madapter = new MajorAdapter(getActivity(), results);
		spMajor.setAdapter(Madapter);
	}

	private class JsonReadTaskPostDoctor extends
			AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(params[0]);
			List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);
			nameValuePair.add(new BasicNameValuePair("MaCK", MaCK));
			try {
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePair));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			try {
				HttpResponse response = httpclient.execute(httppost);
				jsonResult = inputStreamToStringDoctor(
						response.getEntity().getContent()).toString();
			}

			catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		private StringBuilder inputStreamToStringDoctor(InputStream is) {
			String rLine = "";
			StringBuilder answer = new StringBuilder();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));

			try {
				while ((rLine = rd.readLine()) != null) {
					answer.append(rLine);
				}
			}

			catch (IOException e) {
				Toast.makeText(getActivity().getBaseContext(),
						"Error..." + e.toString(), Toast.LENGTH_LONG).show();
			}
			return answer;
		}

		@Override
		protected void onPostExecute(String result) {
			getDoctor();
		}
	}

	public void accessWebServiceDoctor() {
		JsonReadTaskPostDoctor taskDoctor = new JsonReadTaskPostDoctor();
		taskDoctor.execute(new String[] { urlDoctor });
	}

	public void getDoctor() {
		ArrayList<Doctor> results = new ArrayList<Doctor>();
		try {
			JSONObject jsonResponse = new JSONObject(jsonResult);
			JSONArray jsonMainNode = jsonResponse.optJSONArray("bacsi");
			Doctor doctor;
			for (int i = 0; i < jsonMainNode.length(); i++) {
				doctor = new Doctor();
				JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
				doctor.setMaBS(jsonChildNode.optString("MaBS"));
				doctor.setHoTen(jsonChildNode.optString("HoTen"));
				results.add(doctor);
			}
		} catch (JSONException e) {
			Toast.makeText(getActivity().getBaseContext(),
					"Error" + e.toString(), Toast.LENGTH_SHORT).show();
		}

		Dadapter = new DoctorAdapter(getActivity(), results);
		spDoctor.setAdapter(Dadapter);
	}

	@Override
	public void onEventLongPress(WeekViewEvent event, RectF eventRect) {
		Toast.makeText(getActivity(), "Long pressed event: " + event.getName(),
				Toast.LENGTH_SHORT).show();
	}

	public void invokeWS(String x) {
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(con.getUrl() + "/anroidWebservice/bacsi/llv/" + x,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String response) {
						try {

							JSONArray array = new JSONArray(response);
							JSONArray llv = array.getJSONArray(0);
							JSONArray ngayDat = array.getJSONArray(1);
							for (int i = 0; i < llv.length(); i++) {
								JSONObject mJsonObj = llv.getJSONObject(i);
								day.add(mJsonObj.getString("Thu"));
								String time = mJsonObj.getString("Gio");
								start.add(time.substring(0,
										Math.min(time.length(), 2)));
								end.add(time.substring(Math.max(
										time.length() - 2, 0)));
							}

							for (int j = 0; j < ngayDat.length(); j++) {
								JSONObject mJsonObj = ngayDat.getJSONObject(j);
								cuocHen.add(mJsonObj.getString("NgayGio"));
							}

							mWeekView.notifyDatasetChanged();
							// mWeekView.goToToday();
						} catch (JSONException e) {
							Toast.makeText(
									getActivity().getBaseContext(),
									"Error Occured [Server's JSON response might be invalid]!",
									Toast.LENGTH_LONG).show();
							e.printStackTrace();

						}
					}

					@Override
					public void onFailure(int statusCode, Throwable error,
							String content) {
						if (statusCode == 404) {
							Toast.makeText(getActivity().getBaseContext(),
									"Requested resource not found",
									Toast.LENGTH_LONG).show();
						} else if (statusCode == 500) {
							Toast.makeText(getActivity().getBaseContext(),
									"Something went wrong at server end",
									Toast.LENGTH_LONG).show();
						} else {
							Toast.makeText(
									getActivity().getBaseContext(),
									"Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]",
									Toast.LENGTH_LONG).show();
						}
					}
				});
	}

	@Override
	public void onEventClick(WeekViewEvent event, RectF eventRect) {
		if (event.getColor() == getResources().getColor(R.color.event_color_02)) {
			Toast.makeText(getActivity(), "Full", Toast.LENGTH_SHORT).show();
		} else {
			String dayAdded0="",monthAdded0="";
			int startHour = event.getStartTime().get(Calendar.HOUR_OF_DAY) + 7;
			// int endHour=event.getEndTime().get(Calendar.HOUR_OF_DAY)+7;
			int theMonth = event.getStartTime().get(Calendar.MONTH) + 1;
			
			int theDay = event.getStartTime().get(Calendar.DAY_OF_MONTH);
			int theYear = event.getStartTime().get(Calendar.YEAR);
			if(theDay<10){
				dayAdded0="0";
			}
			if(theMonth<10){
				monthAdded0="0";
			}
			String selectedDate = dayAdded0+theDay + "-" + monthAdded0+theMonth + "-" + theYear + " "
					+ startHour + ":00";
			tvSelectedTime.setText(selectedDate);
		}

	}

	@Override
	public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {
		List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();
		int dayOfWeek = 0, startHours = 0, endHours = 0;
		// Loop 3 time for previous week, this week and next week
		if (day.size() > 0) {
			++countClear;
		}
		for (int i = 1; i < day.size(); i++) {
			for (int count = 0; count < 4; count++) {
				Calendar startTime = Calendar.getInstance();
				dayOfWeek = Integer.parseInt(day.get(i));
				startHours = Integer.parseInt(start.get(i));
				endHours = Integer.parseInt(end.get(i));
				if (count != 0) {
					startTime.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
					int daysInMonth = startTime
							.getActualMaximum(Calendar.DAY_OF_MONTH);
					if ((count * 7) > daysInMonth)
						break;
					startTime.add(Calendar.DATE, count * 7);
				}
				startTime.set(Calendar.DAY_OF_WEEK, dayOfWeek);
				startTime.set(Calendar.HOUR_OF_DAY, startHours);
				startTime.set(Calendar.MINUTE, 0);
				startTime.set(Calendar.MONTH, newMonth - 1);
				startTime.set(Calendar.YEAR, newYear);
				Calendar endTime = (Calendar) startTime.clone();
				endTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(end.get(i)));
				endTime.set(Calendar.MINUTE, 0);
				endTime.set(Calendar.MONTH, newMonth - 1);
				WeekViewEvent event = new WeekViewEvent(1,
						getEventTitle(startTime), startTime, endTime);
				event.setColor(getResources().getColor(R.color.event_color_01));
				events.add(event);
			}
		}
		for (int j = 0; j < cuocHen.size(); j++) {
			String x = cuocHen.get(j);
			String timePart = x.substring(x.lastIndexOf(' ') + 1);
			Date date = Date.valueOf(x.substring(0, x.indexOf(" ")));
			int hour = Integer.parseInt(timePart.substring(0, 2));
			Calendar calStart = new GregorianCalendar();
			calStart.setTime(date);
			calStart.set(Calendar.HOUR_OF_DAY, hour);
			calStart.set(Calendar.MINUTE, 0);
			Calendar calEnd = (Calendar) calStart.clone();
			calEnd.set(Calendar.HOUR_OF_DAY, hour + 1);
			WeekViewEvent event = new WeekViewEvent(11,
					getEventTitle(calStart), calStart, calEnd);
			event.setColor(getResources().getColor(R.color.event_color_02));
			for (int k = 0; k < events.size(); k++) {
				int hour1 = events.get(k).getStartTime()
						.get(Calendar.HOUR_OF_DAY) + 7;
				int day1 = events.get(k).getStartTime().get(Calendar.DATE);
				int month1 = events.get(k).getStartTime().get(Calendar.MONTH);
				int year1 = events.get(k).getStartTime().get(Calendar.YEAR);
				if (hour1 == hour && day1 == calStart.get(Calendar.DATE)
						&& month1 == calStart.get(Calendar.MONTH)
						&& year1 == calStart.get(Calendar.YEAR)) {
					events.get(k).setColor(
							getResources().getColor(R.color.event_color_02));
				}
			}
		}
		// clear day array for new doctor selection
		if (day.size() > 0 && countClear >= 3) {
			day.clear();
			cuocHen.clear();
			start.clear();
			end.clear();
			countClear = 0;
		}
		return events;
	}

	private String getEventTitle(Calendar time) {
		return String.format("Cuộc hẹn từ %02d:%02d đến %02d:%02d",
				time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE),
				time.get(Calendar.HOUR_OF_DAY) + 1, time.get(Calendar.MINUTE));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		switch (id) {
		case R.id.action_today:
			mWeekView.goToToday();
			return true;
		case R.id.action_day_view:
			if (mWeekViewType != TYPE_DAY_VIEW) {
				item.setChecked(!item.isChecked());
				mWeekViewType = TYPE_DAY_VIEW;
				mWeekView.setNumberOfVisibleDays(1);
				mWeekView.setColumnGap((int) TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_DIP, 8, getResources()
								.getDisplayMetrics()));
				mWeekView.setTextSize((int) TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_SP, 12, getResources()
								.getDisplayMetrics()));
				mWeekView.setEventTextSize((int) TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_SP, 12, getResources()
								.getDisplayMetrics()));
			}
			return true;
		case R.id.action_three_day_view:
			if (mWeekViewType != TYPE_THREE_DAY_VIEW) {
				item.setChecked(!item.isChecked());
				mWeekViewType = TYPE_THREE_DAY_VIEW;
				mWeekView.setNumberOfVisibleDays(3);
				mWeekView.setColumnGap((int) TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_DIP, 8, getResources()
								.getDisplayMetrics()));
				mWeekView.setTextSize((int) TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_SP, 12, getResources()
								.getDisplayMetrics()));
				mWeekView.setEventTextSize((int) TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_SP, 12, getResources()
								.getDisplayMetrics()));
			}
			return true;
		case R.id.action_week_view:
			if (mWeekViewType != TYPE_WEEK_VIEW) {
				item.setChecked(!item.isChecked());
				mWeekViewType = TYPE_WEEK_VIEW;
				mWeekView.setNumberOfVisibleDays(7);
				mWeekView.setColumnGap((int) TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_DIP, 2, getResources()
								.getDisplayMetrics()));
				mWeekView.setTextSize((int) TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_SP, 10, getResources()
								.getDisplayMetrics()));
				mWeekView.setEventTextSize((int) TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_SP, 10, getResources()
								.getDisplayMetrics()));
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
