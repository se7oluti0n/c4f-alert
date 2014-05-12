package com.c4f.fragment;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.viewpager.extensions.sample.R;
import com.c4f.Data.SessionData;
import com.c4f.api.ApiController;
import com.c4f.entity.DisaterEntity;
import com.c4f.extensions.AlertDisaterFragment;
import com.c4f.extensions.MainActivity;
import com.c4f.extensions.TipDisaterFragment;

public class AlertFragment extends BaseFragment {

	private String data = "";
	public static Handler handler;
	private DisaterEntity entity;
	private TextView tvLocation, tvUpdate, tvDes;
	private ImageView ivWeather;

	public static AlertFragment newInstance(int position) {
		AlertFragment f = new AlertFragment();
		Bundle b = new Bundle();
		b.putInt(ARG_POSITION, position);
		f.setArguments(b);
		return f;
	}

	private void notifyDisaster() {
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
					Log.i("date", data);
					if (data != null && !data.equals("")) {
						try {
							parseData(data);
							if (entity != null) {
								tvLocation.setText( entity.getLat() + " - "
										+ entity.getLon());
								tvUpdate.setText(entity.getDescription());
								tvDes.setText(SessionData.getTypeDisater(entity
										.getDtype()));
								SessionData.setImage(ivWeather,
										entity.getDtype());
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					break;
				}
			}
		});
		apiController.alert_disaster(SessionData.account,
				String.valueOf(SessionData.latitude),
				String.valueOf(SessionData.longitude));
	}

	private void notify_enmergency_state(String status, String message) {
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
					Toast.makeText(getActivity(), "Notify success", Toast.LENGTH_LONG).show();
					break;
				}
			}
		});
		apiController.notify_enmergency_state(status, SessionData.account,
				String.valueOf(SessionData.latitude),
				String.valueOf(SessionData.longitude), SessionData.lastCity, message);
	}

	private Button btNoOK;
	private Button btOk;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.alert_fragment, null);
		btNoOK = (Button) v.findViewById(R.id.btNoOK);
		tvLocation = (TextView) v.findViewById(R.id.tvLocation);
		tvUpdate = (TextView) v.findViewById(R.id.tvUpdate);
		tvDes = (TextView) v.findViewById(R.id.tvDes);
		ivWeather = (ImageView) v.findViewById(R.id.ivWeather);

		handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (msg.obj == "loaddata") {
					notifyDisaster();
				}
			}
		};
		btNoOK.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
				View inflater = getActivity().getLayoutInflater().inflate(
						R.layout.custom_alert, null);
				final EditText edt = (EditText) inflater.findViewById(R.id.edt_alert);
				alert.setPositiveButton("Send", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						notify_enmergency_state("1",edt.getText().toString());
						
					}
				});
				alert.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						});
				alert.setView(inflater);
				alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
					
					@Override
					public void onCancel(DialogInterface arg0) {
						// TODO Auto-generated method stub
						notify_enmergency_state("1",edt.getText().toString());
					}
				});
				alert.create().show();
			}
		});

		btOk = (Button) v.findViewById(R.id.btOk);
		btOk.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				notify_enmergency_state("0", "");
				SessionData.disaster_id = Integer.parseInt(entity.getId());
				SharedPreferences pref = getActivity().getSharedPreferences("C4F",
						getActivity().MODE_PRIVATE);
				Editor edit = pref.edit();
				edit.putInt("DATA_IS_DISASTER_ID", SessionData.disaster_id);
				edit.commit();
			}
		});
		Button btTip = (Button) v.findViewById(R.id.btTip);
		btTip.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDisaterFragment dialog = new AlertDisaterFragment();
				dialog.show(getActivity().getSupportFragmentManager(),
						"QuickTipsFragment");
			}
		});
		Button btMyPlan = (Button) v.findViewById(R.id.btMyPlan);
		btMyPlan.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				TipDisaterFragment dialog = new TipDisaterFragment();
				dialog.show(getActivity().getSupportFragmentManager(),
						"MyTipsFragment");
			}
		});
		return v;
	}

	private void parseData(String data) throws JSONException {
		JSONObject job = new JSONObject(data);
		String id = job.getString("id");
		String dtype = job.getString("dtype");
		String lat = job.getString("lat");
		String lon = job.getString("lon");
		String radius = job.getString("radius");
		String time = job.getString("time");
		String description = job.getString("description");
		String last_time = job.getString("last_time");
		String distance = job.getString("distance");
		entity = new DisaterEntity(id, dtype, lat, lon, radius, time,
				description, last_time, distance);
		entity.toString();
	}
}