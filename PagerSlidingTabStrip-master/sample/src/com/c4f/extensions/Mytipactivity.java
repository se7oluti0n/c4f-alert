package com.c4f.extensions;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.astuetz.viewpager.extensions.sample.R;

import database.DatabaseHandlerTips;

public class Mytipactivity extends Activity {

	private TextView tvTitle, tvContent;
	private DatabaseHandlerTips dbHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dbHandler = new DatabaseHandlerTips(this);
		setContentView(R.layout.mytipactivity);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvContent = (TextView) findViewById(R.id.tvContent);
		if (getIntent().getStringExtra("title") != null) {
			String content = dbHandler.getTip(getIntent().getStringExtra(
					"title"));
			tvTitle.setText(getIntent().getStringExtra("title"));
			tvContent.setText(content);
		}
	}

}
