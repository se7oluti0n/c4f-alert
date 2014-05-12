package com.c4f.entity;

public class DisaterEntity {
	private String id;
	private String dtype;
	private String lat;
	private String lon;
	private String radius;
	private String time;
	private String description;
	private String last_time;
	private String distance;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDtype() {
		return dtype;
	}

	public void setDtype(String dtype) {
		this.dtype = dtype;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	public String getRadius() {
		return radius;
	}

	public void setRadius(String radius) {
		this.radius = radius;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLast_time() {
		return last_time;
	}

	public void setLast_time(String last_time) {
		this.last_time = last_time;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public DisaterEntity() {
		super();
	}

	public DisaterEntity(String id, String dtype, String lat, String lon,
			String radius, String time, String description, String last_time,
			String distance) {
		super();
		this.id = id;
		this.dtype = dtype;
		this.lat = lat;
		this.lon = lon;
		this.radius = radius;
		this.time = time;
		this.description = description;
		this.last_time = last_time;
		this.distance = distance;
	}

	@Override
	public String toString() {
		return "DisaterEntity [id=" + id + ", dtype=" + dtype + ", lat=" + lat
				+ ", lon=" + lon + ", radius=" + radius + ", time=" + time
				+ ", description=" + description + ", last_time=" + last_time
				+ ", distance=" + distance + "]";
	}

}
