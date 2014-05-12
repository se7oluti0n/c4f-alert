package com.c4f.api;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import com.c4f.entity.ForecastItem;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

public class GetImageFromURLs extends
		AsyncTask<ArrayList<ForecastItem>, Integer, Void> {

	Handler handle;

	public GetImageFromURLs(Activity mContext, Handler handler) {
		super();
		this.handle = handler;
	}

	@Override
	protected Void doInBackground(ArrayList<ForecastItem>... params) {
		// TODO Auto-generated method stub
		if (params[0] != null) {
			for (int i = 0; i < params[0].size(); i++) {
				params[0].get(i).setBm(
						download_Image(params[0].get(i).getIvContent()));
				publishProgress();
			}
		}
		return null;
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
		Message msg = new Message();
		msg.obj = "reload";
		handle.sendMessage(msg);
	}

	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
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
