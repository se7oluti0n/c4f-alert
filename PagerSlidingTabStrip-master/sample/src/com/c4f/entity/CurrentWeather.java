package com.c4f.entity;

public class CurrentWeather {
	public String full;
	public String icon_url;
	public String temp_c;
	public String wind_dir;
	public String wind_kph;
	public String weather;
	public String feelslike_c;
	public String last_update_time;
	public String wind_degrees;
	
	//
	public String percentIlluminated;
	public String phaseofMoon;
	public String sunrise_hour;
	public String sunrise_minute;
	public String sunset_hour;
	public String sunset_minute;
	
	//
	public String maxtempm;

	public String getFull() {
		return full;
	}

	public void setFull(String full) {
		this.full = full;
	}

	public String getIcon_url() {
		return icon_url;
	}

	public void setIcon_url(String icon_url) {
		this.icon_url = icon_url;
	}

	public String getTemp_c() {
		return temp_c;
	}

	public void setTemp_c(String temp_c) {
		this.temp_c = temp_c;
	}

	public String getWind_dir() {
		return wind_dir;
	}

	public void setWind_dir(String wind_dir) {
		this.wind_dir = wind_dir;
	}

	public String getWind_kph() {
		return wind_kph;
	}

	public void setWind_kph(String wind_kph) {
		this.wind_kph = wind_kph;
	}

	public String getWeather() {
		return weather;
	}

	public void setWeather(String weather) {
		this.weather = weather;
	}

	public String getFeelslike_c() {
		return feelslike_c;
	}

	public void setFeelslike_c(String feelslike_c) {
		this.feelslike_c = feelslike_c;
	}

	public String getAgeOfMoon() {
		return percentIlluminated;
	}

	public void setAgeOfMoon(String ageOfMoon) {
		this.percentIlluminated = ageOfMoon;
	}

	public String getPhaseofMoon() {
		return phaseofMoon;
	}

	public void setPhaseofMoon(String phaseofMoon) {
		this.phaseofMoon = phaseofMoon;
	}

	public String getSunrise_hour() {
		return sunrise_hour;
	}

	public void setSunrise_hour(String sunrise_hour) {
		this.sunrise_hour = sunrise_hour;
	}

	public String getSunrise_minute() {
		return sunrise_minute;
	}

	public void setSunrise_minute(String sunrise_minute) {
		this.sunrise_minute = sunrise_minute;
	}

	public String getSunset_hour() {
		return sunset_hour;
	}

	public void setSunset_hour(String sunset_hour) {
		this.sunset_hour = sunset_hour;
	}

	public String getSunset_minute() {
		return sunset_minute;
	}

	public void setSunset_minute(String sunset_minute) {
		this.sunset_minute = sunset_minute;
	}

	public String getTempm() {
		return maxtempm;
	}

	public void setTempm(String maxtempm) {
		this.maxtempm = maxtempm;
	}

	public CurrentWeather(String full, String icon_url, String temp_c,
			String wind_dir, String wind_kph, String weather,
			String feelslike_c, String ageOfMoon, String phaseofMoon,
			String sunrise_hour, String sunrise_minute, String sunset_hour,
			String sunset_minute, String tempm) {
		super();
		this.full = full;
		this.icon_url = icon_url;
		this.temp_c = temp_c;
		this.wind_dir = wind_dir;
		this.wind_kph = wind_kph;
		this.weather = weather;
		this.feelslike_c = feelslike_c;
		this.percentIlluminated = ageOfMoon;
		this.phaseofMoon = phaseofMoon;
		this.sunrise_hour = sunrise_hour;
		this.sunrise_minute = sunrise_minute;
		this.sunset_hour = sunset_hour;
		this.sunset_minute = sunset_minute;
		this.maxtempm = tempm;
	}

	public CurrentWeather() {
		super();
		this.full = "";
		this.icon_url = "";
		this.temp_c = "";
		this.wind_dir = "";
		this.wind_kph = "";
		this.weather = "";
		this.feelslike_c = "";
		this.percentIlluminated = "";
		this.phaseofMoon = "";
		this.sunrise_hour = "";
		this.sunrise_minute = "";
		this.sunset_hour = "";
		this.sunset_minute = "";
		this.maxtempm = "";
	}
	
	
	
}
