package com.c4f.entity;

public class ForecastItemDetail {
	private String period;
	private String icon;
	private String icon_url;
	private String title;
	private String fcttext;
	private String fcttext_metric;
	private String pop;

	public ForecastItemDetail(String period, String icon, String icon_url,
			String title, String fcttext, String fcttext_metric, String pop) {
		super();
		this.period = period;
		this.icon = icon;
		this.icon_url = icon_url;
		this.title = title;
		this.fcttext = fcttext;
		this.fcttext_metric = fcttext_metric;
		this.pop = pop;
	}

	public ForecastItemDetail() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getIcon_url() {
		return icon_url;
	}

	public void setIcon_url(String icon_url) {
		this.icon_url = icon_url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFcttext() {
		return fcttext;
	}

	public void setFcttext(String fcttext) {
		this.fcttext = fcttext;
	}

	public String getFcttext_metric() {
		return fcttext_metric;
	}

	public void setFcttext_metric(String fcttext_metric) {
		this.fcttext_metric = fcttext_metric;
	}

	public String getPop() {
		return pop;
	}

	public void setPop(String pop) {
		this.pop = pop;
	}

}
