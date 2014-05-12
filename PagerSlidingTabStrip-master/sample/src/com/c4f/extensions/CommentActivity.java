package com.c4f.extensions;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.astuetz.viewpager.extensions.sample.R;

import database.DatabaseHandlerNums;

public class CommentActivity extends Activity {

	private Button btnAdd;
	private EditText etName, etNum;
	private DatabaseHandlerNums dbHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_comment_reply_edit);
		btnAdd = (Button) findViewById(R.id.bt_update);
		etName = (EditText) findViewById(R.id.etname);
		etNum = (EditText) findViewById(R.id.etnum);
		dbHandler = new DatabaseHandlerNums(this);
		btnAdd.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!etName.equals("") && !etNum.equals("")) {
					dbHandler.addNum(etName.getText().toString(), etNum
							.getText().toString());
					Message msg = new Message();
					msg.obj = "reload";
					TipDisaterFragment.handler.sendMessage(msg);
					finish();
				}
			}
		});
	}

}
