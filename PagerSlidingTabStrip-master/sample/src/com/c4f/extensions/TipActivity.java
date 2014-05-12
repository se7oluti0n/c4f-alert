package com.c4f.extensions;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.astuetz.viewpager.extensions.sample.R;

public class TipActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		switch (getIntent().getIntExtra("key", 0)) {
		case 0:
			setContentView(R.layout.tipbaoactivity);
			break;
		case 1:
			setContentView(R.layout.tipluactivity);
			break;
		case 2:
			setContentView(R.layout.tipdongdatactivity);
			break;
		}
	}

}
