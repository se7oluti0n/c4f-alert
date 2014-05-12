package com.c4f.entity;

import com.astuetz.viewpager.extensions.sample.R;

import android.app.Notification.Style;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.renderscript.Type;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;

public class CustomImageViewWind extends ImageView{
	
	public float wind_kmp;
	public float win_rotate;
	public CustomImageViewWind(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		invalidate();
	}

	public CustomImageViewWind(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		invalidate();
	}

	public CustomImageViewWind(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		invalidate();
	}
	
	public int dpToPx(int dp) {
	    DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
	    int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));       
	    return px;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		Paint paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);
		Log.d("width",""+getWidth());
		
		Bitmap bmp_circle = BitmapFactory.decodeResource(getResources(), R.drawable.circle);
		Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.arrow_rect);
		bmp = Bitmap.createScaledBitmap(bmp, dpToPx(10), dpToPx(15), false);
		canvas.rotate(win_rotate, getWidth()/2, getHeight()/2);
		canvas.drawBitmap(bmp_circle, null, new Rect(0, 0, getWidth(), getHeight()), paint);
		canvas.drawBitmap(bmp, getWidth()/2-bmp.getWidth()/2, 0, paint);
		
		canvas.save();
		canvas.rotate(-win_rotate, getWidth()/2, getHeight()/2);
		canvas.drawText("N", getWidth()/2-dpToPx(5), getHeight()/3 , paint);
		Typeface type = Typeface.defaultFromStyle(Typeface.BOLD);
		paint.setTypeface(type);
		canvas.drawText(""+wind_kmp, getWidth()/2-dpToPx(10), getHeight()/2 + dpToPx(5), paint);
		
		
		canvas.restore();
		super.onDraw(canvas);
	}
	
}
