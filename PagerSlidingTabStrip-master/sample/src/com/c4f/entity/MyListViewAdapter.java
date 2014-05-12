package com.c4f.entity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import com.astuetz.viewpager.extensions.sample.R;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MyListViewAdapter extends ArrayAdapter<Patient> {

	private Activity context;
	private ArrayList<Patient> arr;
	private int resID;

	public MyListViewAdapter(Activity context, int resource,
			ArrayList<Patient> objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.resID = resource;
		this.arr = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		convertView = context.getLayoutInflater().inflate(resID, null);

		TextView name = (TextView) convertView.findViewById(R.id.name);
		TextView location = (TextView) convertView.findViewById(R.id.location);

		name.setText(arr.get(position).getName());
//		location.setText(arr.get(position).getLat() + " and "
//				+ arr.get(position).getLon());
		if (!arr.get(position).getMessage().equalsIgnoreCase("null"))
		location.setText(arr.get(position).getMessage());
		return convertView;
	}

}
