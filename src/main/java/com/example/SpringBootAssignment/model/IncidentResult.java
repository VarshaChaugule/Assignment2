package com.example.SpringBootAssignment.model;

public class IncidentResult {
	private String assetName;
	private Integer totalIncident;
	private Double totalUptime;
	private Integer rating;

	

	public IncidentResult(String assetName, Integer totalIncident, Double totalUptime, Integer rating) {
		super();
		this.assetName = assetName;
		this.totalIncident = totalIncident;
		this.totalUptime = totalUptime;
		this.rating = rating;
	}

	public String getAssetName() {
		return assetName;
	}

	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}

	public Integer getTotalIncident() {
		return totalIncident;
	}

	public void setTotalIncident(Integer totalIncident) {
		this.totalIncident = totalIncident;
	}

	public Double getTotalUptime() {
		return totalUptime;
	}

	public void setTotalUptime(Double totalUptime) {
		this.totalUptime = totalUptime;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

}
