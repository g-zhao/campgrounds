package com.techelevator.campground.jdbc;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import com.techelevator.model.Campground;
import com.techelevator.model.Reservation;
import com.techelevator.model.Site;

public class JDBCReservationDAO {

	private JdbcTemplate jdbcTemplate;
	Reservation reservation = null;
	JDBCSiteDAO jdbcSiteDAO;
	JDBCCampgroundDAO jdbcCampgroundDAO;
	Scanner userInput = new Scanner(System.in);

	public JDBCReservationDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.jdbcCampgroundDAO = new JDBCCampgroundDAO(dataSource);
		this.jdbcSiteDAO = new JDBCSiteDAO(dataSource);
	}

	// This inserts the reservation into the table and returns the reservation ID
	// We need the reservation ID for "The reservation has been made and the
	// confirmation id is {Reservation_id}"
	public Long makeCampsiteReservation(List<Site> availSites, LocalDate from_date, LocalDate to_date,
			BigDecimal dailyFee) {

		boolean validSiteSelection = false;
		boolean invalidSelection = false;
		String str = "";
		Long siteId = 0L;
		String name = "";

		// while loop to get site_id from user until selection matches with one of the
		// site options provided
		while (!validSiteSelection) {
			System.out.println("Which site should be reserved? (enter 0 to cancel) __");
			str = userInput.nextLine();
			siteId = Long.parseLong(str);
			if (str.equals("0")) {
				validSiteSelection = true;
				return 0L;
			} else {
				// for each loop to iterate through the user input of site ID to the sites
				// offered in display. if equals then they can proceed.
				for (Site thisSite : availSites) {
					if (thisSite.getSiteId().equals(siteId)) {
						validSiteSelection = true;
						invalidSelection = true;
						break;
					}
				}
				// tells the user the site selection is invalid, displays the sites again,
				// repeats the loop until the selection is valid or 0 to exit
				if (!invalidSelection) {
					System.out.println("The site you entered was invalid, please try again.");
					System.out.println("\nResults Matching Your Search Criteria");
					System.out.printf("%-10s %-10s %-15s %-15s %-15s %-1s%n", "Site Id.", "Max Occup.", "Accessible?",
							"Max RV Length", "Utility", "Cost");

					for (Site thisSite : availSites) {
						System.out.printf("%-10s %-10s %-15s %-15s %-15s %-1s%n", thisSite.getSiteId(),
								thisSite.getMaxOccupancy(), thisSite.getAccessible(), thisSite.getMaxRvLength(),
								thisSite.getUtilities(), dailyFee.setScale(2, RoundingMode.HALF_UP));
					}
				}
			}
		}
		// user input of name for reservation
		System.out.println("What name should the reservation be made under? __");
		name = userInput.nextLine();

		// sql statement to make reservation and returns reservation id made as a
		// confirmation
		String sql = "INSERT INTO reservation (site_id, name, from_date, to_date, create_date) VALUES (?, ?, ?, ?, CURRENT_DATE) RETURNING reservation_id";
		SqlRowSet result = jdbcTemplate.queryForRowSet(sql, siteId, name, from_date, to_date);
		long reserveId = (long) 0;

		if (result.next()) {
			reserveId = result.getLong("reservation_id");
		}

		System.out.println(
				"The reservation has been made for " + name + " and the confirmation ID is " + reserveId + ".\n");
		return reserveId;

	}

	// searches available camp sites for reservation and returns it to the customer
	public boolean searchReservation(int parkId) {
		System.out.println("Which campground (enter 0 to cancel)? __");
		String campgroundId = userInput.nextLine();
		Long cgId = Long.parseLong(campgroundId);
		BigDecimal dailyFee = new BigDecimal(0);
		String arrivalDate = "";
		String departureDate = "";
		LocalDate arrDate = null;
		LocalDate depDate = null;
		boolean arrivalCorrect = false;
		boolean departCorrect = false;

		List<Site> searchSiteAvail = new ArrayList<Site>();
		List<Campground> campgroundList = new ArrayList<>();
		campgroundList = jdbcCampgroundDAO.getCampgroundByParkId((long) parkId);
		for (Campground campground : campgroundList) {
			if (campground.getCampgroundId().equals(cgId)) {
				dailyFee = campground.getDailyFee();
				break;
			}
		}
		// 0 quits and returns to previous method, otherwise asks for date to determine
		// to and from
		if (campgroundId.equals("0")) {
			return false;
		} else {
			// while loop to get user input of arrival date, checks formatting
			try {
				System.out.println("What is the arrival date? YYYY-MM-DD");
				arrDate = getDate();
			} catch (DateTimeParseException e){
				System.out.println("Incorrect date format, please try again.");
			} 
			// while loop to get user input of departure date, checks formatting
			try {
				System.out.println("What is the departure date? YYYY-MM-DD");
				depDate = getDate();
			} catch (DateTimeParseException e){
				System.out.println("Incorrect date format, please try again.");
			}
			// calls searchSiteAvail method to return the sites available with the search
			// criteria
			searchSiteAvail = jdbcSiteDAO.searchSiteAvail(campgroundId, arrDate, depDate);
			System.out.println("\nResults Matching Your Search Criteria");
			System.out.printf("%-10s %-10s %-15s %-15s %-15s %-1s%n", "Site Id.", "Max Occup.", "Accessible?",
					"Max RV Length", "Utility", "Cost");
			// prints out the available sites
			for (Site thisSite : searchSiteAvail) {
				System.out.printf("%-10s %-10s %-15s %-15s %-15s %-1s%n", thisSite.getSiteId(),
						thisSite.getMaxOccupancy(), thisSite.getAccessible(), thisSite.getMaxRvLength(),
						thisSite.getUtilities(), dailyFee.setScale(2, RoundingMode.HALF_UP));
			}

		}

	// call makeCampsiteReservation to complete reservation.
	Long id = makeCampsiteReservation(searchSiteAvail, arrDate, depDate, dailyFee);return false;

}
	
	public LocalDate getDate() {
		String dateInput = userInput.nextLine();
		LocalDate date = LocalDate.parse(dateInput);
		return date;
	}

}
