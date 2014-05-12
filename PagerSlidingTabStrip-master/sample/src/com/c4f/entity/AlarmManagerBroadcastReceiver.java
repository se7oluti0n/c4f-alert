package com.c4f.entity;

import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.astuetz.viewpager.extensions.sample.R;
import com.c4f.Data.SessionData;
import com.c4f.api.ApiController;
import com.c4f.extensions.MainActivity;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;

public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {

	final public static String ONE_TIME = "onetime";
	private static final int NOTIFY_ME_ID1 = 1337;
	private static final int NOTIFY_ME_ID2 = 1338;
	private int hour, minute;
	private SharedPreferences pref;
	String lastCity;
	float latitude;
	float longitude;
	int alarm_type;
	int disaster_id;
	boolean isNofity;
	private Notification note;
	PowerManager.WakeLock wl;
	NotificationManager mgr;

	public AlarmManagerBroadcastReceiver() {
		super();
	}

	public void getDataSharePreferent(Context context) {
		pref = context.getSharedPreferences("C4F", context.MODE_PRIVATE);
		lastCity = pref.getString("DATA_LAST_CITY", SessionData.lastCity);
		latitude = pref.getFloat("DATA_LATITUDE", (float) SessionData.latitude);
		longitude = pref.getFloat("DATA_LONGTITUDE",
				(float) SessionData.longitude);
		isNofity = pref.getBoolean("DATA_IS_NOTIFY", SessionData.is_notify);
		alarm_type = pref.getInt("DATA_TYPE_ALARM", SessionData.type_alarm);
		disaster_id = pref.getInt("DATA_IS_DISASTER_ID", SessionData.disaster_id);
		Log.d("longtitude in receiver", "" + longitude);
	}

	public void savePreferent(Context context) {
		SharedPreferences pref = context.getSharedPreferences("C4F",
				context.MODE_PRIVATE);
		Editor edit = pref.edit();
		edit.putString("DATA_LAST_CITY", lastCity);
		edit.putFloat("DATA_LONGTITUDE", (float) longitude);
		edit.putFloat("DATA_LATITUDE", (float) latitude);
		edit.putInt("DATA_TYPE_ALARM", alarm_type);
		edit.putInt("DATA_IS_DISASTER_ID", disaster_id);
		edit.commit();
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		getDataSharePreferent(context);
		Log.d("On receiving", "Receive");
		PowerManager pm = (PowerManager) context
				.getSystemService(Context.POWER_SERVICE);
		wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG");
		// Acquire the lock
		wl.acquire();

		// Build notification
		mgr = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		note = new Notification(R.drawable.ic_launcher, context.getResources()
				.getString(R.string.app_name), System.currentTimeMillis());
		// Load data to check Disaster
		// Load data to check HelpPeople

		alert_disaster(context);
		// After uncomment this line you will see number of notification arrived
		// note.number=2;

	}

	public void SetAlarm(Context context) {
		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
		intent.putExtra(ONE_TIME, Boolean.FALSE);
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
		Calendar calendar = Calendar.getInstance();
		hour = calendar.get(Calendar.HOUR_OF_DAY);
		minute = calendar.get(Calendar.MINUTE);
		Log.d("hour", "" + hour);
		Log.d("minute", "" + minute + 1);
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 00);

		am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
				6000, pi);
	}

	private void listen_toHelpOther(final Context context) {
		ApiController apiController = new ApiController(new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case ApiController.REQUEST_ERROR:

					break;
				case ApiController.REQUEST_SUCCESS:
					String data = (String) msg.obj;
					try {
						Log.d("Data Notify", data);
						// This pending intent will open after notification
						// click
						
						JSONObject jsonObj = new JSONObject(data);

						if (isNofity) {
							Intent intent = new Intent(context, MainActivity.class);
							PendingIntent i = PendingIntent.getActivity(context, 0,
									intent, 0);
							note.setLatestEventInfo(context, context.getResources()
									.getString(R.string.app_name),
									"Co nguoi can giup do~", i);
							mgr.notify(NOTIFY_ME_ID1, note);
							if (alarm_type != 1) {
								alarm_type = 2;
								savePreferent(context);
							}

							// Release the lock
							wl.release();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					break;
				}
			}
		});
		apiController.listen_toHelpOther(SessionData.account, String.valueOf(latitude),
				String.valueOf(longitude), "5");
	}

	private void alert_disaster(final Context context) {
		ApiController apiController = new ApiController(new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case ApiController.REQUEST_ERROR:

					break;
				case ApiController.REQUEST_SUCCESS:
					String data = (String) msg.obj;
					try {
						Log.d("Data Notify", data);
						// This pending intent will open after notification
						// click

						JSONObject jsonObj = new JSONObject(data);
						int id = jsonObj.getInt("id");
						Log.d("dids",""+disaster_id);
						if (disaster_id != id) {
							

						Intent intent = new Intent(context, MainActivity.class);
						PendingIntent i = PendingIntent.getActivity(context, 0,
								intent, 0);
						note.setLatestEventInfo(context, context.getResources()
								.getString(R.string.app_name),
								"Co tham hoa, quay len anh em oi", i);
						mgr.notify(NOTIFY_ME_ID2, note);
						alarm_type = 1;
						savePreferent(context);
						}
						listen_toHelpOther(context);
						// Release the lock

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						listen_toHelpOther(context);
						e.printStackTrace();
					}

					break;
				}
			}
		});
		Log.d("Account",""+SessionData.account);
		Log.d("Latitude",""+latitude);
		apiController.alert_disaster(SessionData.account, String.valueOf(latitude),
				String.valueOf(longitude));
	}

	public void CancelAlarm(Context context) {
		Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
		PendingIntent sender = PendingIntent
				.getBroadcast(context, 0, intent, 0);
		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(sender);
	}

	public void setOnetimeTimer(Context context) {
		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
		intent.putExtra(ONE_TIME, Boolean.TRUE);
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
		am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pi);
	}

}