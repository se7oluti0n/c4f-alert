package com.c4f.extensions;

import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.astuetz.PagerSlidingTabStrip;
import com.astuetz.viewpager.extensions.sample.R;
import com.c4f.Data.SessionData;
import com.c4f.api.ApiController;
import com.c4f.entity.AlarmManagerBroadcastReceiver;
import com.c4f.fragment.AlertFragment;
import com.c4f.fragment.HelpFragment;
import com.c4f.fragment.HomeFragment;
import com.c4f.fragment.SettingsFragment;
import com.c4f.utils.GPSTracker;

public class MainActivity extends FragmentActivity {

	private final Handler handler = new Handler();

	private PagerSlidingTabStrip tabs;
	private ViewPager pager;
	private MyPagerAdapter adapter;

	private Drawable oldBackground = null;
	private int currentColor = 0;
	private String[] colors = new String[] { "#FF96AA39", "#FFC74B46",
			"#FFF4842D", "#FF3F9FE0", "#FF5161BC" };

	private static Menu optionsMenu;
	public static GPSTracker gps;
	public SharedPreferences pref;
	AlarmManagerBroadcastReceiver alarm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		gps = new GPSTracker(this);

		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		pager = (ViewPager) findViewById(R.id.pager);
		adapter = new MyPagerAdapter(getSupportFragmentManager());

		pager.setAdapter(adapter);

		final int pageMargin = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
						.getDisplayMetrics());
		pager.setPageMargin(pageMargin);

		tabs.setViewPager(pager);

		changeColor(Color.parseColor("#FF96AA39"));
		tabs.setTextSize(11);

		tabs.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				int color = Color.parseColor(colors[arg0]);
				changeColor(color);
				if (AlertFragment.handler != null) {
					if (arg0 == 1) {
						Message msg = new Message();
						msg.obj = "loaddata";
						Log.i("dau ma", "adsda");
						AlertFragment.handler.sendMessage(msg);
						
					}
				}
				if (arg0 == 0) {
					Message msg = new Message();
					msg.obj = "getCache";
					HomeFragment.handler.sendMessage(msg);
				}
				if (HelpFragment.handler != null) {
					if (arg0 == 2) {
						HelpFragment.handler.sendMessage(HelpFragment.handler.obtainMessage());
					}
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});


		alarm = new AlarmManagerBroadcastReceiver();
//		alarm.CancelAlarm(getApplicationContext());
		alarm.SetAlarm(getApplicationContext());

	}

	

	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		this.optionsMenu = menu;
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case R.id.action_contact:
			DetailForecastFragment dialog = new DetailForecastFragment();
			dialog.show(getSupportFragmentManager(), "QuickContactFragment");
			return true;
		case R.id.airport_menuRefresh:
			// reload
			// setRefreshActionButtonState(true);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void changeColor(int newColor) {

		tabs.setIndicatorColor(newColor);

		// change ActionBar color just if an ActionBar is available
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

			Drawable colorDrawable = new ColorDrawable(newColor);
			Drawable bottomDrawable = getResources().getDrawable(
					R.drawable.actionbar_bottom);
			LayerDrawable ld = new LayerDrawable(new Drawable[] {
					colorDrawable, bottomDrawable });

			if (oldBackground == null) {

				if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
					ld.setCallback(drawableCallback);
				} else {
					getActionBar().setBackgroundDrawable(ld);
				}

			} else {

				TransitionDrawable td = new TransitionDrawable(new Drawable[] {
						oldBackground, ld });

				// workaround for broken ActionBarContainer drawable handling on
				// pre-API 17 builds
				// https://github.com/android/platform_frameworks_base/commit/a7cc06d82e45918c37429a59b14545c6a57db4e4
				if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
					td.setCallback(drawableCallback);
				} else {
					getActionBar().setBackgroundDrawable(td);
				}

				td.startTransition(200);

			}

			oldBackground = ld;

			// http://stackoverflow.com/questions/11002691/actionbar-setbackgrounddrawable-nulling-background-from-thread-handler
			getActionBar().setDisplayShowTitleEnabled(false);
			getActionBar().setDisplayShowTitleEnabled(true);
			
			getActionBar().setTitle(SessionData.account);

		}

		currentColor = newColor;

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		try {
			super.onSaveInstanceState(outState);
			outState.putInt("currentColor", currentColor);
		} catch (Exception ex) {

		}
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		currentColor = savedInstanceState.getInt("currentColor");
		changeColor(currentColor);
	}

	private Drawable.Callback drawableCallback = new Drawable.Callback() {
		@Override
		public void invalidateDrawable(Drawable who) {
			getActionBar().setBackgroundDrawable(who);
		}

		@Override
		public void scheduleDrawable(Drawable who, Runnable what, long when) {
			handler.postAtTime(what, when);
		}

		@Override
		public void unscheduleDrawable(Drawable who, Runnable what) {
			handler.removeCallbacks(what);
		}
	};

	public class MyPagerAdapter extends FragmentPagerAdapter {

		private final String[] TITLES = { "Home", "Alert", "Help", "Settings" };

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return TITLES[position];
		}

		@Override
		public int getCount() {
			return TITLES.length;
		}

		@Override
		public Fragment getItem(int position) {
			// if (SessionData.type_alarm == 2) {
			// SessionData.type_alarm = 0;
			// return HelpFragment.newInstance(position);
			// }
			switch (position) {
			case 0:
				return HomeFragment.newInstance(position);
			case 1:
				return AlertFragment.newInstance(position);
			case 2:
				return HelpFragment.newInstance(position);
			case 3:
				return SettingsFragment.newInstance(position);
			}
			return null;
		}

	}

	public static void setRefreshActionButtonState(final boolean refreshing) {
		if (optionsMenu != null) {
			final MenuItem refreshItem = optionsMenu
					.findItem(R.id.airport_menuRefresh);
			if (refreshItem != null) {
				if (refreshing && refreshItem.getActionView() == null) {
					refreshItem
							.setActionView(R.layout.actionbar_indeterminate_progress);
				} else {
					refreshItem.setActionView(null);
				}
			}
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		pref = getApplicationContext().getSharedPreferences("C4F", getApplicationContext().MODE_PRIVATE);
		Editor edit = pref.edit();
		edit.putString(SessionData.lastCity, SessionData.lastCity);
		edit.putFloat(SessionData.DATA_LONGTITUDE,
				(float) SessionData.longitude);
		edit.putFloat(SessionData.DATA_LATITUDE, (float) SessionData.latitude);
		edit.putInt(SessionData.DATA_TYPE_ALARM, SessionData.type_alarm);
		edit.putString(SessionData.DATA_ACCOUNT, SessionData.account);
		edit.commit();
		super.onDestroy();
	}

}