package com.c4f.extensions;

import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.astuetz.viewpager.extensions.sample.R;
import com.c4f.api.ApiController;
import com.c4f.api.GetImageFromURLs;
import com.c4f.entity.ForecastItem;
import com.c4f.entity.ForecastItemDetail;

public class DetailForecastFragment extends DialogFragment {

	private ListView lvForecast;
	private ListViewAdapter adapter;
	private ArrayList<ForecastItem> arrItem;
	private ArrayList<ForecastItemDetail> arrItemDetail;
	private ApiController apiController;
	private String data = "";
	private ForecastItem item = new ForecastItem();
	private TextView tvTitle, tvTime;

	public static DetailForecastFragment newInstance() {
		DetailForecastFragment f = new DetailForecastFragment();
		return f;
	}

	private void init(View v) {
		arrItem = new ArrayList<ForecastItem>();
		arrItemDetail = new ArrayList<ForecastItemDetail>();
		lvForecast = (ListView) v.findViewById(R.id.lvForecast);
		adapter = new ListViewAdapter(getActivity(), R.layout.row_item, arrItem);
		lvForecast.setAdapter(adapter);
		tvTitle = (TextView) v.findViewById(R.id.tvLocation);
		tvTime = (TextView) v.findViewById(R.id.tvUpdate);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {

		if (getDialog() != null) {
			getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
			getDialog().getWindow().setBackgroundDrawableResource(
					android.R.color.transparent);
		}

		View root = inflater.inflate(R.layout.fragment_quick_contact,
				container, false);

		RelativeLayout ivrl = (RelativeLayout) root.findViewById(R.id.ivrl);

		Calendar c = Calendar.getInstance();
		int hours = c.get(Calendar.HOUR_OF_DAY);
		if (hours <= 9) {
			ivrl.setBackgroundResource(R.drawable.morning);
		}
		if (hours > 9 && hours <= 12) {
			ivrl.setBackgroundResource(R.drawable.lunch);
		}
		if (hours > 12) {
			ivrl.setBackgroundResource(R.drawable.evening);
		}

		init(root);

		apiController = new ApiController(new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case ApiController.REQUEST_ERROR:

					break;
				case ApiController.REQUEST_SUCCESS:
					data = (String) msg.obj;
					if (data != null && arrItem != null) {
						decodeAndUpdateUIWithType1(data);
						new GetImageFromURLs(getActivity(), new Handler() {

							@Override
							public void handleMessage(Message msg) {
								super.handleMessage(msg);
								if (msg.obj == "reload") {
									adapter.notifyDataSetChanged();
								}
							}
						}).execute(arrItem);
					}
					break;
				}
			}
		});
		apiController.getResultJSON(item.FORECAST_LAST);
		tvTitle.setText(item.FORECAST_LAST.substring(
				item.FORECAST_LAST.length() - 10,
				item.FORECAST_LAST.length() - 5));

		lvForecast.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {
				LinearLayout layoutDetail = (LinearLayout) view
						.findViewById(R.id.layoutDetail);
				if (layoutDetail.getVisibility() == View.VISIBLE) {
					layoutDetail.removeAllViews();
					layoutDetail.setVisibility(View.GONE);
				} else {
					layoutDetail.setVisibility(View.VISIBLE);
					View v = inflater.inflate(R.layout.detaiweather, null);
					View v1 = inflater.inflate(R.layout.detaiweather, null);
					ForecastItemDetail item = new ForecastItemDetail();
					ForecastItemDetail item1 = new ForecastItemDetail();
					if (position == 0) {
						item = arrItemDetail.get(0);
						item1 = arrItemDetail.get(1);
					}
					if (position == 1) {
						item = arrItemDetail.get(2);
						item1 = arrItemDetail.get(3);
					}
					if (position == 2) {
						item = arrItemDetail.get(4);
						item1 = arrItemDetail.get(5);
					}
					if (position == 3) {
						item = arrItemDetail.get(6);
						item1 = arrItemDetail.get(7);
					}
					ImageView ivCon = (ImageView) v.findViewById(R.id.ivCon);
					TextView tvTime = (TextView) v.findViewById(R.id.tvTime);
					TextView tvText = (TextView) v.findViewById(R.id.tvText);
					TextView tvText_metric = (TextView) v
							.findViewById(R.id.tvText_metric);
					ImageView ivCon1 = (ImageView) v1.findViewById(R.id.ivCon);
					TextView tvTime1 = (TextView) v1.findViewById(R.id.tvTime);
					TextView tvText1 = (TextView) v1.findViewById(R.id.tvText);
					TextView tvText_metric1 = (TextView) v1
							.findViewById(R.id.tvText_metric);
					tvTime.setText("+" + item.getTitle());
					tvText.setText("-" + item.getFcttext());
					tvText_metric.setText("-" + item.getFcttext_metric());
					tvTime1.setText("+" + item1.getTitle());
					tvText1.setText("-" + item1.getFcttext());
					tvText_metric1.setText("-" + item1.getFcttext_metric());
					layoutDetail.addView(v);
					layoutDetail.addView(v1);
				}
			}
		});

		return root;
	}

	public void decodeAndUpdateUIWithType1(String data) {
		try {
			JSONObject jsonData = new JSONObject(data);

			JSONArray forecastday = jsonData.getJSONObject("forecast")
					.getJSONObject("simpleforecast")
					.getJSONArray("forecastday");
			String update = jsonData.getJSONObject("forecast")
					.getJSONObject("txt_forecast").getString("date");
			tvTime.setText(update);
			for (int i = 0; i < forecastday.length(); i++) {
				item = new ForecastItem();
				JSONObject job = forecastday.getJSONObject(i);
				JSONObject date = job.getJSONObject("date");
				JSONObject high = job.getJSONObject("high");
				JSONObject low = job.getJSONObject("low");
				String hc = high.getString("celsius");
				String hf = high.getString("fahrenheit");
				String lf = low.getString("fahrenheit");
				String lc = low.getString("celsius");
				String thu = date.getString("weekday_short");
				String ngay = date.getString("day") + " "
						+ date.getString("monthname_short");
				String c = hc + "/" + lc;
				String f = hf + "/" + lf;
				item.setDayOfWeek(thu);
				item.setTvDay(ngay);
				item.setTemp(c);
				item.setTvTempF(f);
				item.setIvContent(job.getString("icon_url"));
				arrItem.add(item);
			}

			JSONArray temp = jsonData.getJSONObject("forecast")
					.getJSONObject("txt_forecast").getJSONArray("forecastday");
			for (int i = 0; i < temp.length(); i++) {
				ForecastItemDetail itemDetail = new ForecastItemDetail();
				JSONObject job = temp.getJSONObject(i);
				itemDetail.setPeriod(job.getString("period"));
				itemDetail.setIcon(job.getString("icon"));
				itemDetail.setIcon_url(job.getString("icon_url"));
				itemDetail.setTitle(job.getString("title"));
				itemDetail.setFcttext(job.getString("fcttext"));
				itemDetail.setFcttext_metric(job.getString("fcttext_metric"));
				Log.i("asdasd", job.getString("fcttext_metric"));
				itemDetail.setPop(job.getString("pop"));
				arrItemDetail.add(itemDetail);
			}
			// UpdateUI

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.d("Err", "Canot parse to JSON");
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onStart() {
		super.onStart();

		// change dialog width
		if (getDialog() != null) {

			int fullWidth = getDialog().getWindow().getAttributes().width;

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
				Display display = getActivity().getWindowManager()
						.getDefaultDisplay();
				Point size = new Point();
				display.getSize(size);
				fullWidth = size.x;
			} else {
				Display display = getActivity().getWindowManager()
						.getDefaultDisplay();
				fullWidth = display.getWidth();
			}

			final int padding = (int) TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
							.getDisplayMetrics());

			int w = fullWidth - padding;
			int h = getDialog().getWindow().getAttributes().height;

			getDialog().getWindow().setLayout(w, 350);
		}
	}

	private class ListViewAdapter extends ArrayAdapter<ForecastItem> {

		private ArrayList<ForecastItem> arrItem;
		private Activity context;
		private int id;

		public ListViewAdapter(Activity context, int id,
				ArrayList<ForecastItem> arrItem) {
			super(context, id, arrItem);
			this.context = context;
			this.id = id;
			this.arrItem = arrItem;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolderItem viewHolder;
			if (convertView == null) {
				convertView = context.getLayoutInflater().inflate(id, null);
				viewHolder = new ViewHolderItem();

				viewHolder.tvDayOfWeek = (TextView) convertView
						.findViewById(R.id.tvDayOfWeek);
				viewHolder.tvTemp = (TextView) convertView
						.findViewById(R.id.tvTemp);
				viewHolder.ivIconWeather = (ImageView) convertView
						.findViewById(R.id.ivIconWeather);
				viewHolder.tvDay = (TextView) convertView
						.findViewById(R.id.tvDay);
				viewHolder.tvTempF = (TextView) convertView
						.findViewById(R.id.tvTempF);
				convertView.setTag(viewHolder);

			} else {
				viewHolder = (ViewHolderItem) convertView.getTag();
			}

			ForecastItem item = arrItem.get(position);
			if (item != null) {
				viewHolder.tvDayOfWeek.setText(item.getDayOfWeek());
				viewHolder.tvTemp.setText(item.getTemp());
				viewHolder.tvDay.setText(item.getTvDay());
				viewHolder.tvTempF.setText(item.getTvTempF());
				if (item.getBm() != null) {
					viewHolder.ivIconWeather.setImageBitmap(item.getBm());
				}
			}

			return convertView;
		}
	}

	static class ViewHolderItem {
		TextView tvDayOfWeek;
		TextView tvDay;
		TextView tvTemp;
		TextView tvTempF;
		ImageView ivIconWeather;
	}

}
