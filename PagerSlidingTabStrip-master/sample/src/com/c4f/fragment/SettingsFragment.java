package com.c4f.fragment;

import java.util.Calendar;

import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import com.astuetz.viewpager.extensions.sample.R;
import com.c4f.Data.SessionData;
import com.c4f.entity.AlarmManagerBroadcastReceiver;

public class SettingsFragment extends BaseFragment {

	private SharedPreferences pref;
	private ToggleButton tbNotification;
	private RelativeLayout rlNotification;
	private TextView tvTime;

	public static SettingsFragment newInstance(int position) {
		SettingsFragment f = new SettingsFragment();
		Bundle b = new Bundle();
		b.putInt(ARG_POSITION, position);
		f.setArguments(b);
		return f;
	}

	public void init(View v) {
		tbNotification = (ToggleButton) v.findViewById(R.id.tbNotification);
		rlNotification = (RelativeLayout) v.findViewById(R.id.rlNotification);
		tvTime = (TextView) v.findViewById(R.id.tvTime);
	}

	public void getDataSharePreferent() {
		pref = getActivity().getSharedPreferences("C4F",
				getActivity().MODE_PRIVATE);
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
		tbNotification.setChecked(SessionData.is_notify);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.setting_fragment, null);
		init(v);
		getDataSharePreferent();
		tbNotification
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						try {
							if (isChecked) {
								// Bật cập nhật thông báo
								SessionData.is_notify = true;
								pref.edit().putBoolean(SessionData.DATA_IS_NOTIFY, true).commit();
								
							} else {
								// Tắt cập nhật thông báo
								SessionData.is_notify = false;
								pref.edit().putBoolean(SessionData.DATA_IS_NOTIFY, false).commit();
							}
						} catch (Exception ex) {

						}
					}
				});
		rlNotification.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				int hour = 0, minute = 0;
				if (tvTime.getText() != null && !tvTime.getText().equals("")) {
					String currentSetTime = tvTime.getText().toString();
					String times[] = currentSetTime.split(":");
					hour = Integer.parseInt(times[0]);
					minute = Integer.parseInt(times[1]);
				} else {
					Calendar c = Calendar.getInstance();
					hour = c.get(Calendar.HOUR_OF_DAY);
					minute = c.get(Calendar.MINUTE);
				}
				TimePickerDialog mTimePicker = new TimePickerDialog(
						getActivity(),
						new TimePickerDialog.OnTimeSetListener() {
							@Override
							public void onTimeSet(TimePicker timePicker,
									int selectedHour, int selectedMinute) {
								if (selectedMinute < 9) {
									tvTime.setText(selectedHour + ":0"
											+ selectedMinute);
								} else {
									tvTime.setText(selectedHour + ":"
											+ selectedMinute);
								}
								//Thực hiện set giờ thông báo ở dòng này
							}
						}, hour, minute, true);// Yes 24 hour time
				mTimePicker.setTitle(getResources().getString(
						R.string.selecttime));
				mTimePicker.show();
			}
		});
		return v;
	}

}