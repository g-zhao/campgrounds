package com.techelevator.model;

public class Site {
	
	private Long siteId;
	private Long campgroundId;
	private int siteNumber;
	private int maxOccupancy;
	private boolean accessible;
	private int maxRvLength;
	private boolean utilities;
	
	
	
	public Long getSiteId() {
		return siteId;
	}
	public void setSiteId(Long siteId) {
		this.siteId = siteId;
	}
	public Long getCampgroundId() {
		return campgroundId;
	}
	public void setCampgroundId(Long campgroundId) {
		this.campgroundId = campgroundId;
	}
	public int getSiteNumber() {
		return siteNumber;
	}
	public void setSiteNumber(int siteNumber) {
		this.siteNumber = siteNumber;
	}
	public int getMaxOccupancy() {
		return maxOccupancy;
	}
	public void setMaxOccupancy(int maxOccupancy) {
		this.maxOccupancy = maxOccupancy;
	}
	public boolean getAccessible() {
		return accessible;
	}
	public void setAccessible(boolean accessaible) {
		this.accessible = accessaible;
	}
	public int getMaxRvLength() {
		return maxRvLength;
	}
	public void setMaxRvLength(int maxRvLength) {
		this.maxRvLength = maxRvLength;
	}
	public boolean getUtilities() {
		return utilities;
	}
	public void setUtilities(boolean utilities) {
		this.utilities = utilities;
	}
	
	
	

}
