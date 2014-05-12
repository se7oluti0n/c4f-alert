package com.c4f.entity;

public class Patient {
	private String account;
	private String name;
	private double lat;
	private double lon;
	private String city;
	private String message;
	private String phone;
	public Patient(String account, String name, double lat, double lon, String city, String message, String phone) {
		super();
		this.name = name;
		this.lat = lat;
		this.lon = lon;
		this.city = city;
		this.account = account;
		this.message = message;
		this.phone = phone;
	}
	
	

	public String getPhone() {
		return phone;
	}




	public void setPhone(String phone) {
		this.phone = phone;
	}




	public String getMessage() {
		return message;
	}




	public void setMessage(String message) {
		this.message = message;
	}




	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}
	
	
	
}
