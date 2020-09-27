package com.techelevator.campground.jdbc;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import com.techelevator.model.Park;


public class JDBCParkDAO {

	private JdbcTemplate jdbcTemplate;
	private List<Park> theParkInfo;
	private String pattern = "MM/dd/yyyy";
	NumberFormat commaFormat = NumberFormat.getInstance();
	SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);

	public JDBCParkDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	

	//method to get all parks from sql database and returns the List of Park objects
	public List<Park> getAllParks() {
		List<Park> allParks = new ArrayList<Park>();
		SqlRowSet sql = jdbcTemplate.queryForRowSet("SELECT * FROM park ORDER BY park_id");

		while (sql.next()) {
			Park thisPark = mapRowToPark(sql);
			allParks.add(thisPark);
		}
		return allParks;
		
	}
	
	//returns string array of park names and "quit" in the last index
	public String[] parkNamesAndQuit() {

		List<Park> allParks = getAllParks();
		String[] parkNames = new String[allParks.size() + 1];
		int i = 0;
		for (Park thisPark : allParks) {
			parkNames[i] = thisPark.getParkName();
			i++;
		}
		parkNames[i] = "Quit";
		return parkNames;
	}
	
	//method which returns the toString of the park based on user input of the park name
	public String getParkInfoByName(String name) {
		List<Park> parks = getAllParks();
		String parkInfo = "Invalid Info";
		for (Park thisPark : parks) {
			if (thisPark.getParkName().equals(name)) {
				parkInfo = thisPark.toString();
			}
		}
		return parkInfo;
	}
	//method used to return the parkId based on the name of the park passed
	public int getParkIdByName(String name) {
		int parkId = 0;
		List<Park> parks = getAllParks();
		for (Park thisPark : parks) {
			if (thisPark.getParkName().equals(name)) {
				parkId = thisPark.getParkId().intValue();
			}
		}
		return parkId;
	}
	
	private String printParkInformation() {
		String descriptionFormat = "";
		String parkInfo = "";
		for (Park description : theParkInfo) {
			String[] descriptionSplit = description.getDescription().split(" ");
			for (int i = 0; i < descriptionSplit.length; i++) {
				if (i % 10 == 0) {
					descriptionFormat += "\n" + descriptionSplit[i] + " ";
				} else {
					descriptionFormat += descriptionSplit[i] + " ";
				}
			}
		}
		for (Park info : theParkInfo) {
			parkInfo = String.format("\n%15s\n %-15s\n %-15s\n %-15s\n %-15s\n %-15s\n", info.getParkName() + " National Park",
					"Location: " + info.getLocation(), "Established: " + dateFormat.format(info.getEstablishDate()),
					"Area: " + commaFormat.format(info.getArea()) + "sq km",
					"Annual Visitors: " + commaFormat.format(info.getVisitors()), descriptionFormat);
		}
		return parkInfo;
	}
	
	public List<Park> getParkInformation(String name) {
		theParkInfo = new ArrayList<>();
		String sqlGetParkInformation = "Select * FROM park WHERE name = ?";
		SqlRowSet result = jdbcTemplate.queryForRowSet(sqlGetParkInformation, name);
		while (result.next()) {
			Park thePark = mapRowToPark(result);
			theParkInfo.add(thePark);
		}
		
	
	//	printParkInformation();
		return theParkInfo;
	}

	public Park mapRowToPark(SqlRowSet sql) {
		Park thisPark = new Park();
		thisPark.setParkId(sql.getLong("park_id"));
		thisPark.setParkName(sql.getString("name"));
		thisPark.setLocation(sql.getString("location"));
		thisPark.setEstablishDate(sql.getDate("establish_date").toLocalDate());
		thisPark.setArea(sql.getInt("area"));
		thisPark.setVisitors(sql.getInt("visitors"));
		thisPark.setDescription(sql.getString("description"));
		return thisPark;
	}

	
	
}
