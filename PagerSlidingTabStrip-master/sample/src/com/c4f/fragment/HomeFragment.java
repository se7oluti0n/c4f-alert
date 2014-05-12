package com.c4f.fragment;

import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.astuetz.viewpager.extensions.sample.R;
import com.c4f.Data.SessionData;
import com.c4f.api.ApiController;
import com.c4f.api.GetImageFromURL;
import com.c4f.entity.CurrentWeather;
import com.c4f.entity.CustomImageViewWind;
import com.c4f.entity.StringJSON;
import com.c4f.extensions.MainActivity;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeFragment extends BaseFragment {

	private StringJSON item;
	private String CURRENT_DAY_DATA = "";
	private String CURRENT_DAY_SUNSET_DATA = "";
	private String CURRENT_DAY_YESTERDAY_DATA = "";
	private String CURRENT_LOCATION_DATA = "";
	private String query = "";
	private String data = "";
	private CurrentWeather currentWeather;
	private SharedPreferences preferences;

	public static HomeFragment newInstance(int position) {
		HomeFragment f = new HomeFragment();
		Bundle b = new Bundle();
		b.putInt(ARG_POSITION, position);
		f.setArguments(b);
		return f;
	}

	private boolean isEnableGPS() {
		MainActivity.gps.getLocation();
		if (MainActivity.gps.canGetLocation()) {
			SessionData.latitude = MainActivity.gps.getLatitude();
			SessionData.longitude = MainActivity.gps.getLongitude();
			return true;
		} else {
			Toast.makeText(getActivity(),
					"You need turn on GPS for better performance",
					Toast.LENGTH_SHORT).show();
			return false;
		}
	}
	private SharedPreferences pref;
	
	public void getDataSharePreferent() {
		pref = getActivity().getSharedPreferences("C4F", getActivity().MODE_PRIVATE);
		SessionData.lastCity = pref.getString(SessionData.DATA_LAST_CITY,
				SessionData.lastCity);
		SessionData.latitude = pref.getFloat(SessionData.DATA_LATITUDE,
				(float) SessionData.latitude);
		SessionData.longitude = pref.getFloat(SessionData.DATA_LONGTITUDE,
				(float) SessionData.longitude);
		SessionData.type_alarm = pref.getInt(SessionData.DATA_TYPE_ALARM,
				SessionData.type_alarm);
		SessionData.account = pref.getString(SessionData.DATA_ACCOUNT,
				SessionData.account);
		SessionData.is_notify = pref.getBoolean(SessionData.DATA_IS_NOTIFY, SessionData.is_notify);
	}
	
	public void checkAndAutoRegis() {
		if (SessionData.account.equalsIgnoreCase("")) {
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

							String descrip = jsonObj.getString("description");
							String userName = jsonObj.getString("username");

							if (descrip.equalsIgnoreCase("username exists!")) {
								Toast.makeText(getActivity(),
										"" + descrip, Toast.LENGTH_SHORT)
										.show();
							} else {
								Toast.makeText(getActivity(),
										"" + descrip, Toast.LENGTH_SHORT)
										.show();
								SessionData.account = userName;
								
								Editor edit = pref.edit();
								edit.putString(SessionData.DATA_ACCOUNT,
										SessionData.account);
								edit.commit();
							}

							// Release the lock

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						break;
					}
				}
			});
			// Create random UserName
			String randS = "";
			final String alphabet = "0123456789ABCDE";
			final int N = alphabet.length();

			Random r = new Random();

			for (int i = 0; i < 16; i++) {
				randS += (alphabet.charAt(r.nextInt(N)));
			}
			apiController.registerAcc(randS, String.valueOf(SessionData.latitude), String.valueOf(SessionData.longitude), SessionData.lastCity);
		}
	}

	private void loadData(String path, final int type) {
		MainActivity.setRefreshActionButtonState(true);
		apiController = new ApiController(new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case ApiController.REQUEST_ERROR:

					break;
				case ApiController.REQUEST_SUCCESS:
					data = (String) msg.obj;
					handleTypeUrl(type, data);

					if (type == 4) {
						// load data type 1, 2, 3
						Log.d("Load data", "Load data 1,2,3 after load 4");
						CURRENT_DAY_DATA = item.CURRENT_DAY + "HaNoi.json";
						Log.i("path1", CURRENT_DAY_DATA);
						loadData(CURRENT_DAY_DATA, 1);

						CURRENT_DAY_SUNSET_DATA = item.CURRENT_DAY_SUNSET
								+ "HaNoi.json";
						Log.i("path2", CURRENT_DAY_SUNSET_DATA);
						loadData(CURRENT_DAY_SUNSET_DATA, 2);

						CURRENT_DAY_YESTERDAY_DATA = item.CURRENT_DAY_YESTERDAY
								+ "HaNoi.json";
						Log.i("path3", CURRENT_DAY_YESTERDAY_DATA);
						loadData(CURRENT_DAY_YESTERDAY_DATA, 3);
					}

					break;
				}
			}
		});
		apiController.getResultJSON(path);
	}

	private void handleTypeUrl(int type, String data) {
		Log.d("Data return", data);
		switch (type) {
		case 1:
			decodeAndUpdateUIWithType1(data);
			break;
		case 2:
			decodeAndUpdateUIWithType2(data);
			break;
		case 3:
			decodeAndUpdateUIWithType3(data);
			break;
		case 4:
			decodeAndUpdateUIWithType4(data);
			checkAndAutoRegis();
			break;

		default:
			break;
		}
	}

	public void decodeAndUpdateUIWithType1(String data) {
		try {
			JSONObject jsonData = new JSONObject(data);

			JSONObject current_observation = jsonData
					.getJSONObject("current_observation");
			JSONObject display_location = current_observation
					.getJSONObject("display_location");
			currentWeather.full = display_location.getString("full");
			currentWeather.icon_url = current_observation.getString("icon_url");
			currentWeather.temp_c = current_observation.getString("temp_c");
			currentWeather.wind_dir = current_observation.getString("wind_dir");
			currentWeather.wind_kph = current_observation.getString("wind_kph");
			currentWeather.weather = current_observation.getString("weather");
			currentWeather.feelslike_c = current_observation
					.getString("feelslike_c");
			currentWeather.last_update_time = current_observation
					.getString("observation_time");
			currentWeather.wind_degrees = current_observation
					.getString("wind_degrees");
			currentWeather.last_update_time = currentWeather.last_update_time
					.replace("ICT", "");
			// UpdateUI

			full.setText(currentWeather.full);
			preferences.edit().putString("full", currentWeather.full).commit();
			new GetImageFromURL(getActivity(), new Handler() {

				@Override
				public void handleMessage(Message msg) {
					// TODO Auto-generated method stub
					if (msg.obj != null) {
						icon.setImageBitmap((Bitmap) msg.obj);
					}
					super.handleMessage(msg);

				}
			}).execute(currentWeather.icon_url);
			temp_c.setText(currentWeather.temp_c);
			preferences.edit().putString("temp_c", currentWeather.temp_c).commit();
			tvInfo.setText(currentWeather.feelslike_c + " °C");
			preferences.edit().putString("tvInfo",
					currentWeather.feelslike_c + " °C").commit();
			weather.setText(currentWeather.weather);
			preferences.edit().putString("weather", currentWeather.weather).commit();
			update_time.setText(currentWeather.last_update_time);
			preferences.edit().putString("update_time",
					currentWeather.last_update_time).commit();
			tv_wind.setText(Html.fromHtml("Wind from <B>"
					+ currentWeather.wind_dir + "</B>"));
			preferences.edit().putString(
					"tv_wind",
					Html.fromHtml(
							"Wind from <B>" + currentWeather.wind_dir + "</B>")
							.toString()).commit();
			custom_wind.wind_kmp = Float.parseFloat(currentWeather.wind_kph);
			custom_wind.win_rotate = Float
					.parseFloat(currentWeather.wind_degrees);
			custom_wind.invalidate();
			preferences.edit().commit();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.d("Err 1", "Canot parse to JSON");
			e.printStackTrace();
		}

	}

	public void decodeAndUpdateUIWithType2(String data) {
		try {
			JSONObject jsonData = new JSONObject(data);

			JSONObject moon_phase = jsonData.getJSONObject("moon_phase");
			currentWeather.percentIlluminated = moon_phase
					.getString("percentIlluminated");
			currentWeather.phaseofMoon = moon_phase.getString("phaseofMoon");
			JSONObject sunrise = moon_phase.getJSONObject("sunrise");
			currentWeather.sunrise_hour = sunrise.getString("hour");
			currentWeather.sunrise_minute = sunrise.getString("minute");
			JSONObject sunset = moon_phase.getJSONObject("sunset");
			currentWeather.sunset_hour = sunset.getString("hour");
			currentWeather.sunset_minute = sunset.getString("minute");
			// UpdateUI
			tv_sunRise.setText(currentWeather.sunrise_hour + ":"
					+ currentWeather.sunrise_minute);
			preferences
					.edit()
					.putString(
							"tv_sunRise",
							currentWeather.sunrise_hour + ":"
									+ currentWeather.sunrise_minute).commit();
			tv_sunSet.setText(currentWeather.sunset_hour + ":"
					+ currentWeather.sunset_minute);
			preferences.edit().putString(
					"tv_sunSet",
					currentWeather.sunset_hour + ":"
							+ currentWeather.sunset_minute).commit();

			tvMoonAge.setText(currentWeather.phaseofMoon);
			preferences.edit().putString("tvMoonAge",
					currentWeather.phaseofMoon).commit();
			currentWeather.percentIlluminated = currentWeather.percentIlluminated
					.trim();
			String phase = currentWeather.phaseofMoon.replace(" ", "");
			if (phase.equalsIgnoreCase("FULLMOON")) {
				ivMoon.setBackgroundResource(R.drawable.image09);
			} else if (phase.equalsIgnoreCase("WANINGGIBBOUS")) {
				ivMoon.setBackgroundResource(R.drawable.image08);
			} else if (phase.equalsIgnoreCase("LASTQUARTER")) {
				ivMoon.setBackgroundResource(R.drawable.image03);
			} else if (phase.equalsIgnoreCase("WANINGCRESCENT")) {
				ivMoon.setBackgroundResource(R.drawable.image04);
			} else if (phase.equalsIgnoreCase("NEWMOON")) {
				ivMoon.setBackgroundResource(R.drawable.image05);
			} else if (phase.equalsIgnoreCase("WAXINGCRESCENT")) {
				ivMoon.setBackgroundResource(R.drawable.image06);
			} else if (phase.equalsIgnoreCase("FIRSTQUARTER")) {
				ivMoon.setBackgroundResource(R.drawable.image07);
			} else {
				ivMoon.setBackgroundResource(R.drawable.image10);
			}
			preferences.edit().commit();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.d("Err 2", "Canot parse to JSON");
			e.printStackTrace();
		}

	}

	public void decodeAndUpdateUIWithType3(String data) {
		try {
			JSONObject jsonData = new JSONObject(data);

			JSONObject history = jsonData.getJSONObject("history");
			JSONArray dailysummary = history.getJSONArray("dailysummary");
			currentWeather.maxtempm = dailysummary.getJSONObject(0).getString(
					"maxtempm");
			Log.d("maxTempm", "" + currentWeather.maxtempm);
			// UpdateUI
			currentWeather.maxtempm = currentWeather.maxtempm.trim();
			currentWeather.temp_c = currentWeather.temp_c.trim();
			if (currentWeather.maxtempm.equalsIgnoreCase(""))
				return;
			if (currentWeather.temp_c.equalsIgnoreCase(""))
				return;
			if (Integer.parseInt(currentWeather.maxtempm) > Integer
					.parseInt(currentWeather.temp_c)) {
				String s = "Today is forecast to be <i><b><font color='#fd884d'>COLDER</font></b></i> than yesterday.";
				tvPredicion.setText(Html.fromHtml(s));
				preferences.edit().putString("tvPredicion", s).commit();
			} else {
				String s = "Today is forecast to be <i><b><font color='#fd884d'>WARMER</font></b></i> than yesterday.";
				tvPredicion.setText(Html.fromHtml(s));
				preferences.edit().putString("tvPredicion", s).commit();
			}
			preferences.edit().commit();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.d("Err 3", "Canot parse to JSON");
			e.printStackTrace();
		}

	}

	public void decodeAndUpdateUIWithType4(String data) {
		try {
			JSONObject jsonData = new JSONObject(data);
			Log.i("data", data);
			JSONObject location = jsonData.getJSONObject("location");
			String city = location.getString("city");
			if (city.equalsIgnoreCase("Ha Noi")) {
				city = city.replace(" ", "");
			} else {
				city = city.trim();
				city.replace(" ", "_");
			}

			SessionData.lastCity = city;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.d("Err 4", "Canot parse to JSON");
			e.printStackTrace();
		}

	}

	private void init() {
		if (item == null) {
			item = new StringJSON();
		}
		if (isEnableGPS()) {
			MainActivity.gps.getLocation();
			SessionData.latitude = MainActivity.gps.getLatitude();
			SessionData.longitude = MainActivity.gps.getLongitude();
			savePreferent();
			query = SessionData.latitude + "," + SessionData.longitude
					+ ".json";
			CURRENT_LOCATION_DATA = item.CURRENT_LOCATION + query;
			Log.d("path4", CURRENT_LOCATION_DATA);
			loadData(CURRENT_LOCATION_DATA, 4);
		} else {
			CURRENT_DAY_DATA = item.CURRENT_DAY + "HaNoi.json";
			Log.i("path1", CURRENT_DAY_DATA);
			loadData(CURRENT_DAY_DATA, 1);

			CURRENT_DAY_SUNSET_DATA = item.CURRENT_DAY_SUNSET + "HaNoi.json";
			Log.i("path2", CURRENT_DAY_SUNSET_DATA);
			loadData(CURRENT_DAY_SUNSET_DATA, 2);

			CURRENT_DAY_YESTERDAY_DATA = item.CURRENT_DAY_YESTERDAY
					+ "HaNoi.json";
			Log.i("path3", CURRENT_DAY_YESTERDAY_DATA);
			loadData(CURRENT_DAY_YESTERDAY_DATA, 3);

		}

	}

	public void savePreferent() {
		SharedPreferences pref = getActivity().getSharedPreferences("C4F",
				getActivity().MODE_PRIVATE);
		Editor edit = pref.edit();
		edit.putString(SessionData.lastCity, SessionData.lastCity);
		edit.putFloat(SessionData.DATA_LONGTITUDE,
				(float) SessionData.longitude);
		edit.putFloat(SessionData.DATA_LATITUDE, (float) SessionData.latitude);
		edit.putInt(SessionData.DATA_TYPE_ALARM, SessionData.type_alarm);
		edit.putInt("DATA_IS_DISASTER_ID", SessionData.disaster_id);
		edit.commit();
	}

	private TextView full;
	private ImageView icon;
	private TextView temp_c;
	private CustomImageViewWind custom_wind;
	private TextView tv_wind;
	private TextView tv_sunRise;
	private TextView tv_sunSet;
	private ImageView ivMoon;
	private TextView tvMoonAge;
	private TextView tvPredicion;
	private TextView tvInfo;
	private TextView weather;
	private TextView update_time;
	public static Handler handler;

	private void initLayout(View v) {
		full = (TextView) v.findViewById(R.id.title_current_weather);
		icon = (ImageView) v.findViewById(R.id.ivIcon);
		temp_c = (TextView) v.findViewById(R.id.tvTemp);
		custom_wind = (CustomImageViewWind) v.findViewById(R.id.imv_wind);
		tv_wind = (TextView) v.findViewById(R.id.tv_wind);
		tv_sunRise = (TextView) v.findViewById(R.id.tvSun);
		tv_sunSet = (TextView) v.findViewById(R.id.tvSuns);
		ivMoon = (ImageView) v.findViewById(R.id.ivMoon);
		tvMoonAge = (TextView) v.findViewById(R.id.tvMoonAge);
		tvPredicion = (TextView) v.findViewById(R.id.tvPredicion);
		tvInfo = (TextView) v.findViewById(R.id.tvInfo);
		weather = (TextView) v.findViewById(R.id.weather);
		update_time = (TextView) v.findViewById(R.id.update_time);
	}

	public void getCache() throws Exception {
		preferences = getActivity().getSharedPreferences("Cache",
				getActivity().MODE_PRIVATE);
		full.setText(preferences.getString("full", ""));
		update_time.setText(preferences.getString("update_time", ""));
		weather.setText(preferences.getString("weather", ""));
		temp_c.setText(preferences.getString("temp_c", ""));
		tvInfo.setText(preferences.getString("tvInfo", ""));
		tv_sunRise.setText(preferences.getString("tv_sunRise", ""));
		tv_sunSet.setText(preferences.getString("tv_sunSet", ""));
		tv_wind.setText(preferences.getString("tv_wind", ""));
		tvMoonAge.setText(preferences.getString("tvMoonAge", ""));
		tvPredicion.setText(Html.fromHtml(preferences.getString("tvPredicion", "")));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.home_fragment, null);
		currentWeather = new CurrentWeather();
		initLayout(v);
		getDataSharePreferent();
		try {
			getCache();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (msg.obj == "getCache") {
					try {
						getCache();
						Log.d("getCache","get Cache");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

		};
		init();
		
		return v;
	}

}
