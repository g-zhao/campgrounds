package com.techelevator;

import static org.junit.Assert.*;

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
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campground.jdbc.JDBCCampgroundDAO;
import com.techelevator.campground.jdbc.JDBCReservationDAO;
import com.techelevator.campground.jdbc.JDBCSiteDAO;
import com.techelevator.model.Campground;
import com.techelevator.model.Site;

public class JDBCSiteDAOIntegrationTest {

	private static SingleConnectionDataSource dataSource;
	private JDBCSiteDAO jdbcSiteDao;
	private JDBCCampgroundDAO jdbcCampgroundDao;
	private JDBCReservationDAO jdbcReservationDao;

	JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

	
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

		jdbcCampgroundDao = new JDBCCampgroundDAO(dataSource);
		jdbcSiteDao = new JDBCSiteDAO(dataSource);
		jdbcReservationDao = new JDBCReservationDAO(dataSource);
	}

	@After
	public void tearDown() throws Exception {
		System.out.println("Ending test");
		dataSource.getConnection().rollback();

	}

	@Test
	public void searchSiteAvailTest() {

		List<Site> searchSiteAvailList = jdbcSiteDao.searchSiteAvail("1", LocalDate.of(2020,9,05), LocalDate.of(2020,8,20));

		Long expectedSiteId = 3L;
		int expectedSiteNumber = 3;
		int expectedMaxOccupancy = 6;
		boolean expectedAccessible = false;
		int expectedMaxRvLength = 0;
		boolean expectedUtilities = false;
		assertNotNull(searchSiteAvailList);
		assertEquals(expectedSiteId, searchSiteAvailList.get(0).getSiteId());
		assertEquals(expectedSiteNumber, searchSiteAvailList.get(0).getSiteNumber());
		assertEquals(expectedMaxOccupancy, searchSiteAvailList.get(0).getMaxOccupancy());
		assertEquals(expectedAccessible, searchSiteAvailList.get(0).getAccessible());
		assertEquals(expectedMaxRvLength, searchSiteAvailList.get(0).getMaxRvLength());
		assertEquals(expectedUtilities, searchSiteAvailList.get(0).getUtilities());

	}

}
