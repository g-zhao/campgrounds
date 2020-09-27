package com.techelevator.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Campground {
	

	private Long campgroundId;
	private Long parkId;
	private String campgroundName;
	private String openFromMonth;
	private String openToMonth;
	private BigDecimal dailyFee;
	
	
	public Long getCampgroundId() {
		return campgroundId;
	}
	public void setCampgroundId(Long campgroundId) {
		this.campgroundId = campgroundId;
	}
	public long getParkId() {
		return parkId;
	}
	public void setParkId(long parkId) {
		this.parkId = parkId;
	}
	public String getCampgroundName() {
		return campgroundName;
	}
	public void setCampgroundName(String campgroundName) {
		this.campgroundName = campgroundName;
	}
	public String getOpenFromMonth() {
		return openFromMonth;
	}
	public void setOpenFromMonth(String openFromMonth) {
		if (openFromMonth.equals("01")) {
			this.openFromMonth = "January";
		} else if (openFromMonth.equals("05")) {
			this.openFromMonth = "May";
		}

	}
	public String getOpenToMonth() {
		return openToMonth;
	}
	public void setOpenToMonth(String openToMonth) {
		if (openToMonth.equals("09")) {
			this.openToMonth = "September";
		} else if (openToMonth.equals("10")) {
			this.openToMonth = "October";
		} else if (openToMonth.equals("11")) {
			this.openToMonth = "November";
		} else if (openToMonth.equals("12")) {
			this.openToMonth = "December";
		}
	}
	public BigDecimal getDailyFee() {
		return dailyFee;
	}
	public void setDailyFee(BigDecimal dailyFee) {
		this.dailyFee = dailyFee.setScale(2, RoundingMode.HALF_UP);
	}
	
	

	

}
