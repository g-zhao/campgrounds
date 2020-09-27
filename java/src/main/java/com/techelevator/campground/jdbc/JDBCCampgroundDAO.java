package com.techelevator.campground.jdbc;

import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import com.techelevator.model.Campground;
import com.techelevator.model.Park;

public class JDBCCampgroundDAO {

	
	private JdbcTemplate jdbcTemplate;
	private JDBCParkDAO jdbcParkDAO;

	public JDBCCampgroundDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.jdbcParkDAO = new JDBCParkDAO(dataSource);
	}

	// This method allows us to retrieve the campgrounds by park ID (1, 2, or 3)
	public List<Campground> getCampgroundByParkId(Long parkId) {
		List<Campground> campgroundList = new ArrayList<>();
		// sql script to retreieve campground list based on parkId
		String sql = "SELECT campground_id, name, open_from_mm, open_to_mm, daily_fee FROM campground WHERE park_id = ? ORDER BY campground.campground_id";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, parkId);
		// iterates through the list of campground results returned from sql, adds
		// objects to campgroundList
		while (results.next()) {
			Campground thisCampground = mapRowToCampground(results);
			campgroundList.add(thisCampground);
		}
		return campgroundList;

	}

	public void displayCampgrounds(Long parkId) {
		List<Campground> campgroundList = getCampgroundByParkId(parkId);
		List<Park> parks = jdbcParkDAO.getAllParks();
		String ParkName = "";
		int i = 1;
		// iterate through parks object array to retrieve name based on parkId
		for (Park thisPark : parks) {
			if (thisPark.getParkId().equals(parkId)) {
				ParkName = thisPark.getParkName();
			}
		}

		// header for displaying park campgrounds at the selected park name, open,
		// closed, and fee
		System.out.println("Park Campgrounds\n" + ParkName + " National Park Campgrounds");
		System.out.printf("%-4s %-15s %-15s %-15s %-1s%n", " ", "Name", "Open", "Close", "Daily Fee");

		// iterate through campgroundlist to display the campgrounds corresponding to
		// above headers
		for (Campground thisCampground : campgroundList) {
			System.out.printf("%-4s %-15s %-15s %-15s %-1s%n", i + ".", thisCampground.getCampgroundName(),
					thisCampground.getOpenFromMonth(), thisCampground.getOpenToMonth(),
					"$" + thisCampground.getDailyFee());
			i++;
		}
	}

	public void displayCampgroundForSearch(Long parkId) {
		List<Campground> campgroundList = getCampgroundByParkId(parkId);
		// displays screen for user to select campground for reservation search
		System.out.println("Search for Campground Reservation");
		System.out.printf("%-4s %-15s %-15s %-15s %-1s%n", " ", "Name", "Open", "Close", "Daily Fee");
		int i = 1;
		for (Campground thisCampground : campgroundList) {
			System.out.printf("%-4s %-15s %-15s %-15s %-1s%n", i + ".", thisCampground.getCampgroundName(),
					thisCampground.getOpenFromMonth(), thisCampground.getOpenToMonth(),
					"$" + thisCampground.getDailyFee());
			i++;
		}
	}

	public Campground mapRowToCampground(SqlRowSet results) {
		Campground campground = new Campground();
		campground.setCampgroundId(results.getLong("campground_id"));
		campground.setCampgroundName(results.getString("name"));
		campground.setOpenFromMonth(results.getString("open_from_mm"));
		campground.setOpenToMonth(results.getString("open_to_mm"));
		campground.setDailyFee(results.getBigDecimal("daily_fee"));

		
		return campground;
	}
}

