package com.example.khambenh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;

public class JsonParseClass {
	DefaultHttpClient httpClient;
	HttpPost httpPost;
	HttpResponse httpResponse;
	HttpEntity httpEntity;
	static InputStream inputStream = null;
	static JSONObject jsonObject = null;
	static String jsonString = "";

	public JSONObject makeHttpRequest(String url, String method,
			List<NameValuePair> params) {
		try {
			if (method == "POST") {
				httpClient = new DefaultHttpClient();
				httpPost = new HttpPost(url);
				httpPost.setEntity(new UrlEncodedFormEntity(params,"UTF-8"));
				httpResponse = httpClient.execute(httpPost);
				httpEntity = httpResponse.getEntity();
				inputStream = httpEntity.getContent();
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			System.out.println(e.toString());
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			System.out.println(e.toString());
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e.toString());
		}
		try {
			BufferedReader bufferReader = new BufferedReader(
					new InputStreamReader(inputStream, "UTF-8"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = bufferReader.readLine()) != null) {
				sb.append(line + "\n");
			}
			inputStream.close();
			jsonString = sb.toString();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			Log.e("Buffer Error", "Error converting result " + e.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			jsonObject = new JSONObject(jsonString);
		} catch (JSONException e) {
			e.printStackTrace();
			Log.e("JSON Parser", "Error parsing data [" + e.getMessage() + "] "
					+ jsonString);
		}
		return jsonObject;
	}
}