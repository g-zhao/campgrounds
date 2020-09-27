package com.techelevator;

import static org.junit.Assert.assertEquals;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import com.techelevator.campground.jdbc.JDBCReservationDAO;
import com.techelevator.model.Reservation;
import com.techelevator.model.Site;

public class JDBCReservationDAOIntegrationTest {

	private static SingleConnectionDataSource dataSource;
	private JDBCReservationDAO jdbcReservationDAO;
	JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
	Reservation reservation = new Reservation();

	@BeforeClass
	public static void setupDataSource() throws Exception {
		System.out.println("Starting test suite");
		dataSource = new SingleConnectionDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		dataSource.setAutoCommit(false);
	}

	@AfterClass
	public static void closeDataSource() throws Exception {
		System.out.println("All tests are done");
		dataSource.destroy();
	}

	@Before
	public void setUp() throws Exception {
		System.out.println("Starting test");
		jdbcReservationDAO = new JDBCReservationDAO(dataSource);

	}

	@After
	public void rollback() throws SQLException {
		System.out.println("Ending test");
		dataSource.getConnection().rollback();
	}

	@Test
	public void make_Campsite_Reservation_Test() {

		List<Site> availSitesTest = new ArrayList<>();
		Site thisSite = new Site();
		thisSite.setSiteId(1L);
		thisSite.setCampgroundId(1L);
		thisSite.setSiteNumber(159);
		thisSite.setAccessible(false);
		availSitesTest.add(thisSite);


		Long expectedReservationId = jdbcReservationDAO.makeCampsiteReservation(availSitesTest, LocalDate.of(2020,2,18), LocalDate.of(2020,2,25),	BigDecimal.valueOf(100)) + 1;
		Long actualReservationId = jdbcReservationDAO.makeCampsiteReservation(availSitesTest, LocalDate.of(2020,2,18), LocalDate.of(2020,2,25), BigDecimal.valueOf(100));

		assertEquals(expectedReservationId, actualReservationId);
	}

	@Test
	public void search_reservation_test() {

		assertEquals(false, jdbcReservationDAO.searchReservation(1));

	}

}
