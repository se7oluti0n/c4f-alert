package com.c4f.entity;

import android.graphics.Bitmap;
import com.c4f.Data.SessionData;

public class ForecastItem {

	public static String FORECAST_LAST = "http://api.wunderground.com/api/59325da480a8030d/forecast/lang:VU/q/VN/"
			+ SessionData.lastCity + ".json";

	private String dayOfWeek;
	private String tvDay;
	private String tvTempF;
	private String ivContent;
	private String temp;
	private Bitmap bm;

	public Bitmap getBm() {
		return bm;
	}

	public void setBm(Bitmap bm) {
		this.bm = bm;
	}

	public String getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public String getTemp() {
		return temp;
	}

	public void setTemp(String temp) {
		this.temp = temp;
	}

	public ForecastItem() {
		super();
	}

	public String getIvContent() {
		return ivContent;
	}

	public void setIvContent(String ivContent) {
		this.ivContent = ivContent;
	}

	public String getTvDay() {
		return tvDay;
	}

	public void setTvDay(String tvDay) {
		this.tvDay = tvDay;
	}

	public String getTvTempF() {
		return tvTempF;
	}

	public void setTvTempF(String tvTempF) {
		this.tvTempF = tvTempF;
	}

	public ForecastItem(String dayOfWeek, String tvDay, String tvTempF,
			String ivContent, String temp, Bitmap bm) {
		super();
		this.dayOfWeek = dayOfWeek;
		this.tvDay = tvDay;
		this.tvTempF = tvTempF;
		this.ivContent = ivContent;
		this.temp = temp;
		this.bm = bm;
	}

}
