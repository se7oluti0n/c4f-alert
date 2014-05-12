package com.c4f.extensions;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.astuetz.viewpager.extensions.sample.R;

import database.DatabaseHandlerNums;
import database.DatabaseHandlerTips;

public class TipDisaterFragment extends DialogFragment implements
		OnClickListener {

	private ListView lvTip;
	private ArrayAdapter<String> adapter;
	private DatabaseHandlerTips dbHandlerTip;
	private DatabaseHandlerNums dbHandlerNum;
	private ArrayList<String> arrDataTip;
	private ArrayList<String> arrDataNum;
	public static Handler handler;
	private Button btnTabTip, btnTabAdd, btnTabNum;
	public static boolean isNum = true;
	private TextView tvBG;

	public static TipDisaterFragment newInstance() {
		TipDisaterFragment f = new TipDisaterFragment();
		return f;
	}

	public void init(View root) {
		lvTip = (ListView) root.findViewById(R.id.lvTips);
		btnTabNum = (Button) root.findViewById(R.id.btnTabNum);
		btnTabTip = (Button) root.findViewById(R.id.btnTabTip);
		btnTabAdd = (Button) root.findViewById(R.id.btnTabAdd);
		tvBG = (TextView) root.findViewById(R.id.tvBG);
		arrDataTip = new ArrayList<String>();
		arrDataNum = new ArrayList<String>();
		dbHandlerTip = new DatabaseHandlerTips(getActivity());
		dbHandlerNum = new DatabaseHandlerNums(getActivity());
	}

	@Override
	public View onCreateView(final LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {

		if (getDialog() != null) {
			getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
			getDialog().getWindow().setBackgroundDrawableResource(
					android.R.color.transparent);
		}

		View root = inflater
				.inflate(R.layout.fragment_mytips, container, false);
		init(root);

		arrDataNum = dbHandlerNum.getAllNumbers();
		arrDataTip = dbHandlerTip.getAllTips();

		btnTabNum.setOnClickListener(this);
		btnTabTip.setOnClickListener(this);
		btnTabAdd.setOnClickListener(this);

		onClick(btnTabNum);

		lvTip.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (isNum) {
					Intent callIntent = new Intent(Intent.ACTION_DIAL);
					callIntent.setData(Uri.parse("tel:"
							+ Uri.encode(arrDataNum.get(position).trim()
									.split("-")[1])));
					callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(callIntent);
				} else {
					Intent i = new Intent(getActivity(), Mytipactivity.class);
					i.putExtra("title", arrDataTip.get(position));
					startActivity(i);
				}
			}
		});

		lvTip.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (isNum) {
					dbHandlerNum.deleteImage(arrDataNum.get(position).trim()
							.split("-")[1].trim());
				} else {
					dbHandlerTip.deleteTip(arrDataTip.get(position));
				}
				Message msg = new Message();
				msg.obj = "reload";
				handler.sendMessage(msg);
				return false;
			}
		});

		handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (msg.obj == "reload") {
					arrDataNum = dbHandlerNum.getAllNumbers();
					arrDataTip = dbHandlerTip.getAllTips();
					onClick(btnTabNum);
				}
			}

		};

		return root;
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnTabNum:
			adapter = new ArrayAdapter<String>(getActivity(),
					android.R.layout.simple_list_item_1, arrDataNum);
			lvTip.setAdapter(adapter);
			isNum = true;
			setBackgroundButton(isNum, v);
			break;
		case R.id.btnTabTip:
			adapter = new ArrayAdapter<String>(getActivity(),
					android.R.layout.simple_list_item_1, arrDataTip);
			lvTip.setAdapter(adapter);
			isNum = false;
			setBackgroundButton(isNum, v);
			break;
		case R.id.btnTabAdd:
			if (isNum) {
				Intent i = new Intent(getActivity(), CommentActivity.class);
				startActivity(i);
			} else {
				Intent i = new Intent(getActivity(), CommentActivityTip.class);
				startActivity(i);
			}
			break;
		}
	}

	private void setBackgroundButton(boolean isNum, View v) {
		if (isNum) {
			tvBG.setBackgroundColor(Color.rgb(221, 128, 161));
			v.setBackgroundResource(R.drawable.tablikeselected);
			btnTabTip.setBackgroundResource(R.drawable.tabunlike);
		} else {
			tvBG.setBackgroundColor(Color.rgb(139, 204, 202));
			v.setBackgroundResource(R.drawable.tabunlikeselected);
			btnTabNum.setBackgroundResource(R.drawable.tablike);
		}
	}
}
