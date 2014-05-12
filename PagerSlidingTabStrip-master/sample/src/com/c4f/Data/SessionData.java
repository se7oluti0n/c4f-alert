package com.c4f.Data;

import com.astuetz.viewpager.extensions.sample.R;

import android.widget.ImageView;

public class SessionData {

	public static String DATA_LAST_CITY = "DATA_LAST_CITY";
	public static String DATA_LATITUDE = "DATA_LATITUDE";
	public static String DATA_LONGTITUDE = "DATA_LONGTITUDE";
	public static String DATA_TYPE_ALARM = "DATA_TYPE_ALARM";
	public static String DATA_ACCOUNT = "DATA_ACCOUNT";
	public static String DATA_IS_NOTIFY = "DATA_IS_NOTIFY";

	public static String lastCity = "HaNoi";
	public static double latitude = 102;
	public static double longitude = 105;
	public static int type_alarm = 0;
	public static int disaster_id = -1;
	public static String DATA_IS_DISASTER_ID = "DATA_IS_DISASTER_ID";

	public static String account = "";

	public static String CHAY = "0";
	public static String LU = "1";
	public static String BAO = "2";
	public static String DONGDAT = "3";
	public static boolean is_notify = true;

	public static String getTypeDisater(String dtype) {
		if (dtype.equals(CHAY)) {
			return "CHÁY RỪNG";
		}
		if (dtype.equals(LU)) {
			return "LŨ LỤT";
		}
		if (dtype.equals("BAO")) {
			return "BÃO";
		}
		if (dtype.equals(DONGDAT)) {
			return "ĐỘNG ĐẤT";
		}
		return "";
	}

	public static void setImage(ImageView v, String dtype) {
		if (dtype.equals(CHAY)) {
			v.setImageResource(R.drawable.chayrung);
		}
		if (dtype.equals(LU)) {
			v.setImageResource(R.drawable.lu);
		}
		if (dtype.equals("BAO")) {
			v.setImageResource(R.drawable.loc);
		}
		if (dtype.equals(DONGDAT)) {
			v.setImageResource(R.drawable.earthquake);
		}
	}
}
