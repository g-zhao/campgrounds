package com.techelevator.campground;

import java.util.Scanner;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

import com.techelevator.campground.jdbc.JDBCCampgroundDAO;
import com.techelevator.campground.jdbc.JDBCParkDAO;
import com.techelevator.campground.jdbc.JDBCReservationDAO;
import com.techelevator.campground.jdbc.JDBCSiteDAO;
import com.techelevator.view.Menu;

public class CampgroundCLI {

	Scanner userInput = new Scanner(System.in);

	// main menu options: park list
	private String[] MAIN_MENU_OPTION_PARKS ;

	// view campground menu options
	private static final String VIEW_ALL_CAMPGROUNDS = "View Campgrounds";
	private static final String SEARCH_FOR_RESERVATION = "Search for Reservation";
	private static final String RETURN_TO_PREVIOUS_SCREEN = "Return to previous screen";
	private static final String[] COMMAND_MENU_OPTIONS = new String[] { VIEW_ALL_CAMPGROUNDS, SEARCH_FOR_RESERVATION,
			RETURN_TO_PREVIOUS_SCREEN };

	// park campgrounds menu options
	private static final String SEARCH_FOR_AVAIL_RESERVATION = "Search for Available Reservation";
	private static final String[] COMMAND_MENU_OPTIONS_SHORT = new String[] { SEARCH_FOR_AVAIL_RESERVATION,
			RETURN_TO_PREVIOUS_SCREEN };

	private Menu menu;

	// JDBC objects
	private JDBCParkDAO jdbcParkDAO;
	private JDBCCampgroundDAO jdbcCampgroundDAO;
	private JDBCSiteDAO jdbcSiteDAO;
	private JDBCReservationDAO jdbcReservationDAO;

	public static void main(String[] args) throws Exception {
		//basic datasource set up, pointing to campground sql server
		Menu menu = new Menu(System.in, System.out);
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");

		CampgroundCLI application = new CampgroundCLI(dataSource, menu);
		application.run();
	}

	public CampgroundCLI(DataSource dataSource, Menu menu) {
		this.menu = menu;
		
		//passing datasource to jdbc objects
		jdbcCampgroundDAO = new JDBCCampgroundDAO(dataSource);
		jdbcParkDAO = new JDBCParkDAO(dataSource);
		jdbcReservationDAO = new JDBCReservationDAO(dataSource);
		jdbcSiteDAO = new JDBCSiteDAO(dataSource);

	}


	public void run() throws Exception {

		while (true) {
			//initial screen welcoming to user to select a park for more details
			System.out.println("Select A Park For Additional Details");
			/* string array which is populated with the park names
			 * and last option as string quit to be used by menu options
			 */
			String[] parksAndQuit = jdbcParkDAO.parkNamesAndQuit(); 
			MAIN_MENU_OPTION_PARKS = parksAndQuit; //assignment of array into the menu option
			String choice = (String) menu.getChoiceFromOptions(MAIN_MENU_OPTION_PARKS);
			//if user chooses the number corresponding to quit, application will exit
			if (choice.equals("Quit")) {
				System.out.println("Thank you for using Tech Elevator Camp Reservations! \nHave a Great Day!");
				System.exit(0);
			//if user didn't quit, display of park information and calls the view campground method passing in parkId
			} else {
				System.out.println(jdbcParkDAO.getParkInfoByName(choice));
				viewCampgrounds(jdbcParkDAO.getParkIdByName(choice));
			}

		}
	}
	
	//method to get all the campgrounds from the respective park
	public void viewCampgrounds(int parkId) throws Exception {
		String commandMenuChoiceOptions = "";
		
		while (!commandMenuChoiceOptions.equals(RETURN_TO_PREVIOUS_SCREEN)) {
			String commandMenuChoice = (String) menu.getChoiceFromOptions(COMMAND_MENU_OPTIONS);
			//if user chooses to select "view all campgrounds" then method call to display campgrounds
			if (commandMenuChoice.equals(VIEW_ALL_CAMPGROUNDS)) {
				jdbcCampgroundDAO.displayCampgrounds((long) parkId);
				String commandMenuChoiceShort = (String) menu.getChoiceFromOptions(COMMAND_MENU_OPTIONS_SHORT);
				/*user selected to view all campgrounds, which displays campgrounds
				 * then offers a shorter menu to search for reservation at the selected park */
				if (commandMenuChoiceShort.equals(SEARCH_FOR_AVAIL_RESERVATION)) {
					searchReservationScreen(parkId);
				}
			//calls searchreservation if the user skips the view campgrounds and searches for reservation directly
			} else if (commandMenuChoice.equals(SEARCH_FOR_RESERVATION)) {
				searchReservationScreen(parkId);
			} else if (commandMenuChoice.equals(RETURN_TO_PREVIOUS_SCREEN)) {
				run();
			}

		}
	}

	public void searchReservationScreen(int parkId) throws Exception {
		/*method which displays again the campgrounds at the park, allowing the user to select which campground
		 * they would like to search for a reservation at */
		String commandMenuChoiceOptions = "";
		boolean exitReservation = true;
		while (!commandMenuChoiceOptions.equals(RETURN_TO_PREVIOUS_SCREEN) || exitReservation) {
			jdbcCampgroundDAO.displayCampgroundForSearch((long) parkId);
			// call search reservation method.
			if (jdbcReservationDAO.searchReservation(parkId) == false) {
				exitReservation = false;
				run();
			}

		}
	}

}
