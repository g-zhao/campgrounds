package com.techelevator.model;

import java.time.LocalDate;

import org.apache.commons.lang3.text.WordUtils;


@SuppressWarnings("deprecation")

public class Park {

	private Long parkId;
	private String parkName;
	private String location;
	private LocalDate establishDate;
	private int area;
	private int visitors;
	private String description;
	
	
	public Long getParkId() {
		return parkId;
	}
	public void setParkId(Long parkId) {
		this.parkId = parkId;
	}
	public String getParkName() {
		return parkName;
	}
	public void setParkName(String parkName) {
		this.parkName = parkName;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public LocalDate getEstablishDate() {
		return establishDate;
	}
	public void setEstablishDate(LocalDate establishDate) {
		this.establishDate = establishDate;
	}
	public int getArea() {
		return area;
	}
	public void setArea(int area) {
		this.area = area;
	}
	public int getVisitors() {
		return visitors;
	}
	public void setVisitors(int visitors) {
		this.visitors = visitors;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public String toString() {
		WordUtils wrap = new WordUtils();
		String info = parkName + " National Park \nLocation: \t\t" + location + "\nEstablished: \t\t"
				+ establishDate + "\nArea: \t\t\t" + area + "\nAnnual Visitors: \t" + visitors + "\n";
		String descriptionStr = WordUtils.wrap(description, 60);
		return info + descriptionStr;
	}
	
}

