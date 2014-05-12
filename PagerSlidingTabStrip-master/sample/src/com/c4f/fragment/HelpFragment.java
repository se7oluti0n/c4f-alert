package com.c4f.fragment;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.astuetz.viewpager.extensions.sample.GoogleMapActivity;
import com.astuetz.viewpager.extensions.sample.R;
import com.c4f.Data.SessionData;
import com.c4f.api.ApiController;
import com.c4f.entity.MyListViewAdapter;
import com.c4f.entity.Patient;
import com.c4f.extensions.MainActivity;
import com.google.android.gms.maps.GoogleMap;

public class HelpFragment extends BaseFragment {

	public static ArrayList<Patient> arr = new ArrayList<Patient>();
	public static MyListViewAdapter adapter;
	public static ListView listView;
	Button btnOverView;
	public static double lat,lon,lat_des,lon_des;

	public static Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
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
						arr.clear();
						try {
							JSONObject json = new JSONObject(data);

							JSONArray arr_ = json.getJSONArray("helper_array");

							for (int i = 0; i < arr_.length(); i++) {
								JSONObject jsonOb = arr_.getJSONObject(i);
								String account = jsonOb.getString("username");
								double lat = jsonOb.getDouble("lat");
								double lon = jsonOb.getDouble("lon");
								String message = jsonOb.getString("message");
								String name = jsonOb.getString("name");
								String phone = jsonOb.getString("phone");
								

								arr.add(new Patient(account, name, lat, lon, "", message, phone));

							}
							adapter.notifyDataSetChanged();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						break;
					}
				}
			});
			apiController.listen_toHelpOther(SessionData.account,
					String.valueOf(SessionData.latitude),
					String.valueOf(SessionData.longitude), "5");
		}
	};

	static String data;

	public static HelpFragment newInstance(int position) {
		HelpFragment f = new HelpFragment();
		Bundle b = new Bundle();
		b.putInt(ARG_POSITION, position);
		f.setArguments(b);
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View v = null;
		v = inflater.inflate(R.layout.help_fragment, null);

		listView = (ListView) v.findViewById(R.id.lv);
		adapter = new MyListViewAdapter(getActivity(),
				R.layout.custom_listview, arr);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), GoogleMapActivity.class);
				intent.putExtra("type", 1);
				lat = SessionData.latitude;
				lon = SessionData.longitude;
				lat_des = arr.get(arg2).getLat();
				lon_des = arr.get(arg2).getLon();
				startActivity(intent);
			}
		});
		btnOverView = (Button) v.findViewById(R.id.btn_overView);
		btnOverView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), GoogleMapActivity.class);
				intent.putExtra("type", 0);
				startActivity(intent);
			}
		});

		return v;
	}

}