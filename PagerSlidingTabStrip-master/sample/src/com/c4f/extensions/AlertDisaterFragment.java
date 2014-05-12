package com.c4f.extensions;

import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.astuetz.viewpager.extensions.sample.R;

public class AlertDisaterFragment extends DialogFragment {

	private ListView lvTip;
	private String[] temp = new String[] { "Kinh nghiệm chống bão",
			"Cách đối phó với lũ", "Ứng phó với động đất" };
	private ArrayAdapter<String> adapter;

	public static AlertDisaterFragment newInstance() {
		AlertDisaterFragment f = new AlertDisaterFragment();
		return f;
	}

	@Override
	public View onCreateView(final LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {

		if (getDialog() != null) {
			getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
			getDialog().getWindow().setBackgroundDrawableResource(
					android.R.color.transparent);
		}

		View root = inflater.inflate(R.layout.fragment_quick_tip, container,
				false);

		lvTip = (ListView) root.findViewById(R.id.lvTip);
		adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, temp);
		lvTip.setAdapter(adapter);

		lvTip.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent i = new Intent(getActivity(), TipActivity.class);
				i.putExtra("key", position);
				startActivity(i);
			}
		});

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
}
