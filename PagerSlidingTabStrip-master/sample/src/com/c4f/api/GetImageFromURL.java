package com.c4f.api;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import com.c4f.extensions.MainActivity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

public class GetImageFromURL extends AsyncTask<String, Integer, Void> {

	Handler handle;
	Bitmap bitmap;

	public GetImageFromURL(Activity mContext, Handler handler) {
		super();
		this.handle = handler;
	}

	@Override
	protected Void doInBackground(String... params) {
		// TODO Auto-generated method stub
		bitmap = download_Image(params[0]);
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		Message mess = new Message();
		if (bitmap != null) {
			mess.obj = bitmap;
			handle.sendMessage(mess);
			}
		MainActivity.setRefreshActionButtonState(false);
		super.onPostExecute(result);
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		
		super.onPreExecute();
	}

	public static Bitmap download_Image(String url) {
		// ---------------------------------------------------
		InputStream is;
		Bitmap d = null;
		try {
			is = (InputStream) new URL(url).getContent();
			d = BitmapFactory.decodeStream(is);

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return d;
		// ---------------------------------------------------
	}

}
