package com.c4f.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.c4f.api.ApiController;

public class BaseFragment extends Fragment {

	protected static final String ARG_POSITION = "position";

	protected int position;

	protected static ApiController apiController;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		position = getArguments().getInt(ARG_POSITION);
	}
}
