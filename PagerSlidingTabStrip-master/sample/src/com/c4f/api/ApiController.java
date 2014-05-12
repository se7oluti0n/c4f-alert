package com.c4f.api;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

public class ApiController {

	public static final String TAG = "ApiController";
	public static final int REQUEST_SUCCESS = 0;
	public static final int REQUEST_ERROR = 1;

	private Handler mHandler;

	public ApiController(Handler handler) {

		this.mHandler = handler;
	}

	public AsyncTask<String, Void, Void> getResultJSON(String path) {
		return processGetRequest(path);
	}

	public AsyncTask<String, Void, Void> notifyDisaster(String userName,
			String lat, String lon, String distance) {
		String request = "http://192.168.57.1/help_others";
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("username", userName));
		nameValuePairs.add(new BasicNameValuePair("lat", lat));
		nameValuePairs.add(new BasicNameValuePair("lon", lon));
		nameValuePairs.add(new BasicNameValuePair("distance", distance));
		return processPostRequest(request, nameValuePairs);
	}

	public AsyncTask<String, Void, Void> notify_enmergency_state(String status,
			String userName, String lat, String lon, String city, String message) {
		String request = "http://192.168.57.1/confirm_info";
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("status", status));
		nameValuePairs.add(new BasicNameValuePair("username", userName));
		nameValuePairs.add(new BasicNameValuePair("lat", lat));
		nameValuePairs.add(new BasicNameValuePair("lon", lon));
		nameValuePairs.add(new BasicNameValuePair("city", city));
		nameValuePairs.add(new BasicNameValuePair("message", message));
		return processPostRequest(request, nameValuePairs);
	}

	public AsyncTask<String, Void, Void> listen_toHelpOther(
			String userName, String lat, String lon, String distance) {
		String request = "http://192.168.57.1/help_others";
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("username", userName));
		nameValuePairs.add(new BasicNameValuePair("lat", lat));
		nameValuePairs.add(new BasicNameValuePair("lon", lon));
		nameValuePairs.add(new BasicNameValuePair("distance", distance));
		return processPostRequest(request, nameValuePairs);
	}
	
	public AsyncTask<String, Void, Void> registerAcc(
			String userName, String lat, String lon, String city) {
		String request = "http://192.168.57.1/register";
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("username", userName));
		nameValuePairs.add(new BasicNameValuePair("lat", lat));
		nameValuePairs.add(new BasicNameValuePair("lon", lon));
		nameValuePairs.add(new BasicNameValuePair("city", city));
		return processPostRequest(request, nameValuePairs);
	}
	
	public AsyncTask<String, Void, Void> alert_disaster(
			String userName, String lat, String lon) {
		String request = "http://192.168.57.1/notify";
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("username", userName));
		nameValuePairs.add(new BasicNameValuePair("lat", lat));
		nameValuePairs.add(new BasicNameValuePair("lon", lon));
		return processPostRequest(request, nameValuePairs);
	}

	public AsyncTask<String, Void, Void> processGetRequest(String path) {
		AsyncTask<String, Void, Void> task = new AsyncTask<String, Void, Void>() {
			String result = "";

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
			}

			@Override
			protected Void doInBackground(String... params) {
				// TODO Auto-generated method stub
				try {
					String path = params[0];
					URL url = new URL(path);
					HttpURLConnection http = (HttpURLConnection) url
							.openConnection();
					http.setConnectTimeout(30000);
					if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
						InputStream input = http.getInputStream();
						byte dataBytes[] = IOUtils.toByteArray(input);
						result = new String(dataBytes, "UTF8");
						input.close();
						// Log.i("home", "data:" + result);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result2) {
				// TODO Auto-generated method stub
				super.onPostExecute(result2);
				Message msg = new Message();
				if (result != null && !result.equals("")) {
					msg.what = REQUEST_SUCCESS;
					msg.obj = result;
				} else {
					msg.what = REQUEST_ERROR;
				}
				mHandler.sendMessage(msg);
			}
		};
		task.execute(path);
		return task;
	}

	public AsyncTask<String, Void, Void> processPostRequest(String path,
			final List<NameValuePair> nameValuePairs) {
		AsyncTask<String, Void, Void> task = new AsyncTask<String, Void, Void>() {
			String result = "";

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
			}

			@Override
			protected Void doInBackground(String... params) {
				// TODO Auto-generated method stub
				try {
					String path = params[0];
					HttpClient httpclient = new DefaultHttpClient();
					HttpPost httppost = new HttpPost(path);
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
							"UTF8"));

					// Execute HTTP Post Request
					HttpResponse response = httpclient.execute(httppost);
					HttpEntity entity = response.getEntity();
					InputStream input = entity.getContent();
					byte dataBytes[] = IOUtils.toByteArray(input);
					result = new String(dataBytes, "UTF8");
					// Log.i("POST", result);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result2) {
				// TODO Auto-generated method stub
				super.onPostExecute(result2);
				Message msg = new Message();
				if (result != null && !result.equals("")) {
					msg.what = REQUEST_SUCCESS;
					msg.obj = result;
				} else {
					msg.what = REQUEST_ERROR;
				}
				mHandler.sendMessage(msg);
			}
		};
		task.execute(path);
		return task;
	}

}
