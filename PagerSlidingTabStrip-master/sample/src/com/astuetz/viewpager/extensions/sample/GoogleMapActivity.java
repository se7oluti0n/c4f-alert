package com.astuetz.viewpager.extensions.sample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.c4f.Data.SessionData;
import com.c4f.entity.GMapV2Direction;
import com.c4f.entity.GetDirectionsAsyncTask;
import com.c4f.fragment.HelpFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class GoogleMapActivity extends FragmentActivity {

	GoogleMap mMap;
	LatLng latlng = new LatLng(21.009642, 105.788684);
	LatLng latlng_Des = new LatLng(22.008888, 105.788666);
	String personName1, personName2;
	int type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_google_map);
		
		type = getIntent().getIntExtra("type", 0);
		mMap = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 10));
		if (type == 0) {
			Log.d("arr.size",""+HelpFragment.arr.size());
			for	(int i = 0; i< HelpFragment.arr.size(); i++) {
				if (HelpFragment.arr.get(i).getAccount().equalsIgnoreCase(SessionData.account)) {
					
					showMarker(new LatLng(HelpFragment.arr.get(i).getLat(), HelpFragment.arr.get(i).getLon()), HelpFragment.arr.get(i).getName());
				}
				else {
					showEnmergencyMarker(new LatLng(HelpFragment.arr.get(i).getLat(), HelpFragment.arr.get(i).getLon()), HelpFragment.arr.get(i).getName(), HelpFragment.arr.get(i).getPhone());
				}
			}
				showMarker(new LatLng(SessionData.latitude, SessionData.longitude), SessionData.account);
			
			
		} else {
			latlng = new LatLng(HelpFragment.lat, HelpFragment.lon);
			latlng_Des = new LatLng(HelpFragment.lat_des, HelpFragment.lon_des);
			findDirections(latlng.latitude, latlng.longitude,
					latlng_Des.latitude, latlng_Des.longitude,
					GMapV2Direction.MODE_WALKING);
		}

	}
	
	void showEnmergencyMarker(LatLng ll, String name, String phoneNum) {
		mMap.addMarker(new MarkerOptions().position(ll).title(name+"\n"+phoneNum));
	}

	void showMarker(LatLng ll, String name) {
		BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory
				.defaultMarker(BitmapDescriptorFactory.HUE_AZURE);

		mMap.addMarker(new MarkerOptions().position(ll).icon(bitmapDescriptor));
	}

	public void handleGetDirectionsResult(ArrayList<LatLng> directionPoints) {
		Polyline newPolyline;
		GoogleMap mMap = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();
		PolylineOptions rectLine = new PolylineOptions().width(1).color(
				Color.RED);

		for (int i = 0; i < directionPoints.size(); i++) {
			if (i == 0)
				showMarker(latlng, personName1);
			if (i == directionPoints.size() - 1)
				showEnmergencyMarker(latlng_Des, personName2, "");
			rectLine.add(directionPoints.get(i));
		}
		newPolyline = mMap.addPolyline(rectLine);
	}

	public void findDirections(double fromPositionDoubleLat,
			double fromPositionDoubleLong, double toPositionDoubleLat,
			double toPositionDoubleLong, String mode) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(GetDirectionsAsyncTask.USER_CURRENT_LAT,
				String.valueOf(fromPositionDoubleLat));
		map.put(GetDirectionsAsyncTask.USER_CURRENT_LONG,
				String.valueOf(fromPositionDoubleLong));
		map.put(GetDirectionsAsyncTask.DESTINATION_LAT,
				String.valueOf(toPositionDoubleLat));
		map.put(GetDirectionsAsyncTask.DESTINATION_LONG,
				String.valueOf(toPositionDoubleLong));
		map.put(GetDirectionsAsyncTask.DIRECTIONS_MODE, mode);

		GetDirectionsAsyncTask asyncTask = new GetDirectionsAsyncTask(this);
		asyncTask.execute(map);
	}

}
